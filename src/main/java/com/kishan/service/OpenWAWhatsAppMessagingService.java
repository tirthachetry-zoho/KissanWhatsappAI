package com.kishan.service;

import com.kishan.config.OpenWAConfig;
import com.kishan.openwa.OpenWAClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * {@link WhatsAppMessagingService} backed by the OpenWA self-hosted WhatsApp
 * API gateway (https://github.com/rmyndharis/OpenWA).
 *
 * <p>Free to run – no Meta Business Platform charges, no template restrictions.
 * The WhatsApp number is linked by scanning a QR code / phone-number pairing
 * code from the OpenWA dashboard.</p>
 */
@ApplicationScoped
public class OpenWAWhatsAppMessagingService implements WhatsAppMessagingService {

    @Inject
    OpenWAClient openwaClient;

    @Inject
    OpenWAConfig config;

    @Override
    public boolean isConfigured() {
        return openwaClient.isConfigured();
    }

    @Override
    public boolean isActive() {
        return isConfigured() && config.sendReplies();
    }

    /**
     * Send a text reply to a farmer. No-op when OpenWA is not configured or
     * reply sending is disabled (e.g. in automated tests).
     */
    @Override
    public void sendText(String phoneNumber, String text) {
        if (!isActive()) {
            Log.debug("OpenWA reply sending inactive – skipping send to " + phoneNumber);
            return;
        }
        try {
            openwaClient.sendText(phoneNumber, text);
        } catch (RuntimeException e) {
            // Never fail the inbound webhook because the gateway is unreachable.
            Log.errorf("Failed to send OpenWA reply to %s: %s", phoneNumber, e.getMessage());
        }
    }

    @Override
    public void ensureSessionReady() {
        if (!isConfigured()) {
            return;
        }
        try {
            openwaClient.startSessionIfNotReady();
        } catch (RuntimeException e) {
            Log.errorf("Failed to ensure OpenWA session is running: %s", e.getMessage());
        }
    }
}