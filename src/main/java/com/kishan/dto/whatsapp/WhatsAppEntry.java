package com.kishan.dto.whatsapp;

import lombok.Data;

import java.util.List;

@Data
public class WhatsAppEntry {
    private String id;
    private List<WhatsAppChange> changes;
}