package com.kishan.dto.whatsapp;

import lombok.Data;

import java.util.List;

@Data
public class WhatsAppWebhook {
    private String object;
    private List<WhatsAppEntry> entry;
}