package com.kishan.config;

import io.smallrye.config.ConfigMapping;

import java.util.Optional;

/**
 * Configuration for the OpenWA self-hosted WhatsApp API gateway
 * (https://github.com/rmyndharis/OpenWA).
 */
@ConfigMapping(prefix = "openwa")
public interface OpenWAConfig {

    /** Base URL of the OpenWA instance, e.g. http://localhost:2785 */
    String apiBaseUrl();

    /** OpenWA API key (X-API-Key header). Empty disables the integration. */
    Optional<String> apiKey();

    /** Session name this backend should use/create in OpenWA. */
    String sessionName();

    /** Optional – if already created, the OpenWA session UUID. */
    Optional<String> sessionId();

    /** Public URL this backend is reachable at; used by OpenWA to POST webhooks. */
    Optional<String> webhookUrl();

    /** Auto-register the "/webhook/openwa" endpoint as an OpenWA webhook for the session. */
    boolean autoRegisterWebhook();

    /** Optional HMAC secret OpenWA uses to sign webhook payloads (X-OpenWA-Signature). */
    Optional<String> webhookSecret();

    /** Activate real sending of assistant replies over OpenWA. */
    boolean sendReplies();
}