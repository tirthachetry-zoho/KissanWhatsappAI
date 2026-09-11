package com.kishan.service;

import com.kishan.config.AppConfig;
import com.kishan.entity.*;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Shared inbound-message pipeline used by both WhatsApp channels:
 * <ul>
 *   <li>Meta Cloud API webhook ({@code /webhook})</li>
 *   <li>OpenWA self-hosted gateway webhook ({@code /webhook/openwa})</li>
 * </ul>
 *
 * <p>Pipeline: upsert farmer → find/create active conversation → persist the
 * incoming message → generate an AI reply → persist the outgoing message →
 * (optionally) send the reply over the configured WhatsApp channel.</p>
 */
@ApplicationScoped
public class WhatsAppMessageProcessor {

    @Inject
    AppConfig appConfig;

    @Inject
    AIService aiService;

    @Inject
    WhatsAppMessagingService messaging;

    /**
     * Handle one inbound WhatsApp message from a farmer.
     *
     * @param from       phone number of the sender (digits only, no suffix)
     * @param senderName display name when the gateway provides one (may be null)
     * @param type       message type (text, image, …)
     * @param content    text content or media caption/reference (may be null)
     */
    @Transactional
    public String handleIncomingMessage(String from, String senderName, MessageType type, String content) {
        if (from == null || from.isBlank()) {
            return null;
        }
        String phone = stripJid(from);

        Farmer farmer = Farmer.find("phoneNumber", phone).firstResult();
        if (farmer == null) {
            farmer = new Farmer();
            farmer.setPhoneNumber(phone);
            farmer.setLanguage(appConfig.defaultLanguage());
            farmer.setLocation(appConfig.defaultState());
            farmer.setName(senderName);
            farmer.persist();
            Log.infof("Created farmer %s (language=%s)", phone, farmer.getLanguage());
        } else if (senderName != null && !senderName.isBlank() && farmer.getName() == null) {
            farmer.setName(senderName);
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
        incoming.setMessageType(type == null ? MessageType.TEXT : type);
        incoming.setContent(content);
        incoming.persist();

        String responseText = aiService.generateResponse(farmer, conversation, incoming);

        Message outgoing = new Message();
        outgoing.setConversation(conversation);
        outgoing.setDirection(MessageDirection.OUTGOING);
        outgoing.setMessageType(MessageType.TEXT);
        outgoing.setContent(responseText);
        outgoing.persist();

        Log.infof("WhatsApp message from %s handled; response: %s", phone, responseText);

        // First message in a conversation is a good moment to (re)connect the session.
        messaging.ensureSessionReady();
        messaging.sendText(phone, responseText);

        return responseText;
    }

    /** Strip a WhatsApp JID suffix ("@c.us", "@g.us", …) leaving the plain number. */
    public static String stripJid(String jid) {
        if (jid == null) {
            return null;
        }
        int at = jid.indexOf('@');
        return at >= 0 ? jid.substring(0, at) : jid;
    }

    /**
     * Map an engine-neutral type string (as delivered by OpenWA: "text",
     * "image", "audio", "video", "document", …) to a {@link MessageType}.
     */
    public static MessageType mapType(String type) {
        if (type == null) {
            return MessageType.TEXT;
        }
        return switch (type.toLowerCase()) {
            case "image" -> MessageType.IMAGE;
            case "audio", "voice" -> MessageType.AUDIO;
            case "video" -> MessageType.VIDEO;
            case "document" -> MessageType.DOCUMENT;
            default -> MessageType.TEXT;
        };
    }
}