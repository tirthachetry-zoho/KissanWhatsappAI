package com.kishan.openwa;

import com.kishan.config.OpenWAConfig;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * High-level client for the OpenWA gateway. Resolves/creates the WhatsApp
 * session, sends text messages and (optionally) registers the webhook that
 * delivers inbound messages to this backend.
 */
@ApplicationScoped
public class OpenWAClient {

    @Inject
    OpenWAConfig config;

    @Inject
    @RestClient
    OpenWARestClient restClient;

    private final AtomicReference<String> sessionId = new AtomicReference<>();

    private volatile boolean webhookRegisterAttempted;

    /** True only when an API key is configured, so the gateway is considered usable. */
    public boolean isConfigured() {
        return config.apiKey().isPresent() && !config.apiKey().get().isBlank();
    }

    public String sessionName() {
        return config.sessionName();
    }

    /** Resolve the session UUID from config or by name; create it when missing. */
    public String resolveSessionId() {
        String cached = sessionId.get();
        if (cached != null) {
            return cached;
        }
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "OpenWA is not configured. Set OPENWA_API_KEY (and OPENWA_SESSION_ID or OPENWA_SESSION_NAME).");
        }

        String resolved = config.sessionId().filter(s -> !s.isBlank()).orElse(null);
        if (resolved == null) {
            try {
                List<OpenWASession> sessions = restClient.listSessions();
                resolved = sessions.stream()
                        .filter(s -> config.sessionName().equalsIgnoreCase(s.getName()))
                        .map(OpenWASession::getId)
                        .findFirst()
                        .orElse(null);
            } catch (WebApplicationException e) {
                throw new IllegalStateException("Cannot reach OpenWA at " + config.apiBaseUrl() + ": " + e.getResponse().getStatus(), e);
            }
        }

        if (resolved == null) {
            Log.infof("OpenWA session '%s' not found – creating it. Scan the QR code / use the pairing code in the OpenWA dashboard to link your WhatsApp number.", config.sessionName());
            OpenWASession created = restClient.createSession(new OpenWACreateSessionRequest(config.sessionName()));
            resolved = created.getId();
        }

        sessionId.set(resolved);
        return resolved;
    }

    /** Start the session (if it is not already running). Best-effort; logs on failure. */
    public void startSessionIfNotReady() {
        if (!isConfigured()) {
            return;
        }
        try {
            restClient.startSession(resolveSessionId());
        } catch (WebApplicationException e) {
            Log.debugf("OpenWA start session returned HTTP %s (expected if already running)", e.getResponse().getStatus());
        }
    }

    /** Send a text message to a phone number (converted to the @c.us JID). */
    public void sendText(String phoneNumber, String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        String chatId = phoneNumber.endsWith("@c.us")
                ? phoneNumber
                : phoneNumber + "@c.us";
        registerWebhookIfNeeded();
        OpenWASendTextResponse response = restClient.sendText(resolveSessionId(), new OpenWASendTextRequest(chatId, text));
        Log.debugf("Sent OpenWA message id=%s to %s", response.getMessageId(), chatId);
    }

    /**
     * Register (once) the OpenWA webhook that delivers inbound messages to
     * this backend when {@code openwa.auto-register-webhook=true} and a public
     * webhook URL is configured.
     */
    public void registerWebhookIfNeeded() {
        if (webhookRegisterAttempted) {
            return;
        }
        if (!config.autoRegisterWebhook() || config.webhookUrl().filter(u -> !u.isBlank()).isEmpty()) {
            webhookRegisterAttempted = true;
            return;
        }
        synchronized (this) {
            if (webhookRegisterAttempted) {
                return;
            }
            String url = config.webhookUrl().get().replaceAll("/+$", "") + "/webhook/openwa";
            String sid = resolveSessionId();
            try {
                List<OpenWAWebhook> existing = restClient.listWebhooks(sid);
                boolean registered = existing.stream().anyMatch(w -> url.equals(w.getUrl()));
                if (!registered) {
                    OpenWAWebhookCreateRequest req = new OpenWAWebhookCreateRequest();
                    req.setUrl(url);
                    req.setEvents(List.of("message.received", "session.status"));
                    req.setActive(true);
                    config.webhookSecret().filter(s -> !s.isBlank()).ifPresent(req::setSecret);
                    OpenWAWebhook webhook = restClient.createWebhook(sid, req);
                    Log.infof("Registered OpenWA webhook id=%s → %s", webhook.getId(), url);
                }
            } catch (RuntimeException e) {
                Log.warnf("Failed to register OpenWA webhook at %s: %s", url, e.getMessage());
            } finally {
                webhookRegisterAttempted = true;
            }
        }
    }
}