package com.kishan.service;

/**
 * Abstraction over the WhatsApp sending channel.
 * <ul>
 *   <li>{@link #isConfigured()} – an upstream gateway (OpenWA) is available.</li>
 *   <li>{@link #isActive()}    – real replies should be sent to farmers.</li>
 *   <li>{@link #sendText(String, String)} – send a text reply to a phone number.</li>
 *   <li>{@link #ensureSessionReady()} – make sure the WhatsApp session is running.</li>
 * </ul>
 */
public interface WhatsAppMessagingService {

    boolean isConfigured();

    boolean isActive();

    void sendText(String phoneNumber, String text);

    void ensureSessionReady();
}