package com.kishan.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    String defaultLanguage();

    List<String> supportedLanguages();

    /**
     * State assumed for new farmer profiles when none is provided.
     * The MVP launches Madhya Pradesh-first.
     */
    @WithDefault("Madhya Pradesh")
    String defaultState();
}