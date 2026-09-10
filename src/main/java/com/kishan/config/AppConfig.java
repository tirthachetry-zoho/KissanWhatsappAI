package com.kishan.config;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    String defaultLanguage();

    List<String> supportedLanguages();
}