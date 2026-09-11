package com.kishan.resource;

import com.kishan.config.AppConfig;
import com.kishan.config.WhatsAppConfig;
import com.kishan.dto.whatsapp.*;
import com.kishan.entity.*;
import com.kishan.service.AIService;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/webhook")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WhatsAppWebhookResource {

    @Inject
    WhatsAppConfig whatsappConfig;

    @Inject
    AppConfig appConfig;

    @Inject
    AIService aiService;

    /**
     * WhatsApp webhook verification (GET). Echoes hub.challenge when the
     * verify token matches the configured token.
     */
    @GET
    public Response verify(@QueryParam("hub.mode") String mode,
                           @QueryParam("hub.verify_token") String token,
                           @QueryParam("hub.challenge") String challenge) {
        if ("subscribe".equals(mode) && whatsappConfig.verifyToken().equals(token)) {
            return Response.ok(challenge).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     * Receives WhatsApp messages. Upserts the farmer, finds/creates an active
     * conversation, stores the incoming message, generates a response via the
     * AI service and stores the outgoing message.
     */
    @POST
    @Transactional
    public Response receive(WhatsAppWebhook webhook) {
        if (webhook == null || webhook.getEntry() == null) {
            return Response.ok().build();
        }
        for (WhatsAppEntry entry : webhook.getEntry()) {
            if (entry.getChanges() == null) {
                continue;
            }
            for (WhatsAppChange change : entry.getChanges()) {
                WhatsAppValue value = change.getValue();
                if (value == null || value.getMessages() == null) {
                    continue;
                }
                for (WhatsAppMessage msg : value.getMessages()) {
                    handleMessage(value, msg);
                }
            }
        }
        return Response.ok().build();
    }

    private void handleMessage(WhatsAppValue value, WhatsAppMessage msg) {
        if (msg.getFrom() == null || msg.getFrom().isBlank()) {
            return;
        }
        Farmer farmer = Farmer.find("phoneNumber", msg.getFrom()).firstResult();
        if (farmer == null) {
            farmer = new Farmer();
            farmer.setPhoneNumber(msg.getFrom());
            farmer.setLanguage(appConfig.defaultLanguage());
            farmer.setLocation(appConfig.defaultState());
            if (value.getContacts() != null && !value.getContacts().isEmpty()
                    && value.getContacts().get(0).getProfile() != null) {
                farmer.setName(value.getContacts().get(0).getProfile().getName());
            }
            farmer.persist();
        }

        Conversation conversation = Conversation
                .find("farmer.id = ?1 and status = ?2", farmer.id, ConversationStatus.ACTIVE)
                .firstResult();
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setFarmer(farmer);
            conversation.setStatus(ConversationStatus.ACTIVE);
            conversation.persist();
        }

        Message incoming = new Message();
        incoming.setConversation(conversation);
        incoming.setDirection(MessageDirection.INCOMING);
        incoming.setMessageType(resolveType(msg));
        incoming.setContent(resolveContent(msg));
        incoming.persist();

        String responseText = aiService.generateResponse(farmer, conversation, incoming);

        Message outgoing = new Message();
        outgoing.setConversation(conversation);
        outgoing.setDirection(MessageDirection.OUTGOING);
        outgoing.setMessageType(MessageType.TEXT);
        outgoing.setContent(responseText);
        outgoing.persist();

        Log.infof("WhatsApp message from %s handled; response: %s", msg.getFrom(), responseText);
    }

    private MessageType resolveType(WhatsAppMessage msg) {
        if (msg.getImage() != null) {
            return MessageType.IMAGE;
        }
        if (msg.getAudio() != null) {
            return MessageType.AUDIO;
        }
        if (msg.getVideo() != null) {
            return MessageType.VIDEO;
        }
        if (msg.getDocument() != null) {
            return MessageType.DOCUMENT;
        }
        return MessageType.TEXT;
    }

    private String resolveContent(WhatsAppMessage msg) {
        if (msg.getText() != null) {
            Object body = msg.getText().get("body");
            return body == null ? null : body.toString();
        }
        if (msg.getImage() != null) {
            Object caption = msg.getImage().get("caption");
            if (caption != null) {
                return caption.toString();
            }
            Object url = msg.getImage().get("url");
            return url == null ? "[image]" : "[image] " + url;
        }
        if (msg.getAudio() != null) {
            Object url = msg.getAudio().get("url");
            return url == null ? "[audio]" : "[audio] " + url;
        }
        if (msg.getVideo() != null) {
            Object url = msg.getVideo().get("url");
            return url == null ? "[video]" : "[video] " + url;
        }
        if (msg.getDocument() != null) {
            Object url = msg.getDocument().get("url");
            return url == null ? "[document]" : "[document] " + url;
        }
        return null;
    }
}