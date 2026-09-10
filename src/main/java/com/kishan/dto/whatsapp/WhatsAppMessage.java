package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class WhatsAppMessage {

    @JsonProperty("from")
    private String from;

    private String id;

    private String timestamp;

    private String type;

    private Map<String, Object> text;

    private Map<String, Object> image;

    private Map<String, Object> audio;

    private Map<String, Object> video;

    private Map<String, Object> document;
}