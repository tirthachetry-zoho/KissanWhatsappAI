package com.kishan.config;

import io.smallrye.config.ConfigMapping;

import java.util.Optional;

@ConfigMapping(prefix = "openai")
public interface OpenAIConfig {

    Optional<String> apiKey();

    String model();
}