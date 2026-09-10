package com.kishan.config;

import io.smallrye.config.ConfigMapping;

import java.util.Optional;

@ConfigMapping(prefix = "whatsapp")
public interface WhatsAppConfig {

    String apiUrl();

    Optional<String> phoneNumberId();

    Optional<String> accessToken();

    String verifyToken();
}