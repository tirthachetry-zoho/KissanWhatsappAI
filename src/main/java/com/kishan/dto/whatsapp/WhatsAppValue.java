package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WhatsAppValue {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    private Map<String, Object> metadata;

    private List<WhatsAppContact> contacts;

    private List<WhatsAppMessage> messages;
}