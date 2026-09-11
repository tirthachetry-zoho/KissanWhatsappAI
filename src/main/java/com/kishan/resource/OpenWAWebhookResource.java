package com.kishan.resource;

import com.kishan.entity.MessageType;
import com.kishan.openwa.OpenWAWebhookEvent;
import com.kishan.service.WhatsAppMessageProcessor;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

/**
 * Webhook endpoint for OpenWA (self-hosted WhatsApp gateway).
 * Receives inbound messages from OpenWA and processes them through the
 * same conversation/AI pipeline as the official WhatsApp Business API.
 *
 * <p>OpenWA delivers events with the envelope
 * {@code { event, timestamp, sessionId, idempotencyKey, deliveryId, data }}.
 * Inbound farmer messages arrive as {@code message.received} events whose
 * {@code data} holds the message object:
 * {@code { id, from, to, body, type, timestamp, isGroup, contact{...} }}.</p>
 */
@Path("/webhook/openwa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OpenWAWebhookResource {

    @Inject
    WhatsAppMessageProcessor processor;

    /**
     * Receives OpenWA webhook events and always answers 200 so the gateway
     * does not retry non-applicable events.
     */
    @POST
    public Response receive(OpenWAWebhookEvent event) {
        if (event == null || event.getData() == null) {
            return Response.ok().build();
        }

        Log.debugf("Received OpenWA webhook event: %s", event.getEvent());

        if ("message.received".equals(event.getEvent())) {
            try {
                processMessage(event.getData());
            } catch (RuntimeException e) {
                // Never bubble up: OpenWA would retry, and the reply is best-effort.
                Log.errorf(e, "Error processing OpenWA message.received event");
            }
        }

        return Response.ok().build();
    }

    private void processMessage(Map<String, Object> data) {
        String from = asString(data.get("from"));
        if (from == null || from.isBlank()) {
            Log.warn("OpenWA event missing sender (data.from)");
            return;
        }

        // Only 1:1 chats: skip groups, status broadcasts and non-user JIDs.
        String jid = from.toLowerCase();
        if (jid.endsWith("@g.us") || jid.endsWith("@broadcast") || jid.endsWith("@newsletter")) {
            Log.debugf("Skipping non-individual OpenWA chat: %s", from);
            return;
        }

        String body = asString(data.get("body"));
        MessageType type = WhatsAppMessageProcessor.mapType(asString(data.get("type")));

        String senderName = null;
        Object contact = data.get("contact");
        if (contact instanceof Map<?, ?> contactMap) {
            Object pushName = contactMap.get("pushName");
            Object name = contactMap.get("name");
            senderName = pushName != null ? pushName.toString()
                    : name != null ? name.toString() : null;
        }

        String phone = WhatsAppMessageProcessor.stripJid(from);
        Log.infof("Processing OpenWA message from %s: %s", phone, body);
        processor.handleIncomingMessage(phone, senderName, type, body);
    }

    private static String asString(Object value) {
        return value == null ? null : value.toString();
    }
}
