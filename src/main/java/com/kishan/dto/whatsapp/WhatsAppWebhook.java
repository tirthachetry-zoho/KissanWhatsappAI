package com.kishan.dto.whatsapp;


import java.util.List;

public class WhatsAppWebhook {
    private String object;
    private List<WhatsAppEntry> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<WhatsAppEntry> getEntry() {
        return entry;
    }

    public void setEntry(List<WhatsAppEntry> entry) {
        this.entry = entry;
    }
}