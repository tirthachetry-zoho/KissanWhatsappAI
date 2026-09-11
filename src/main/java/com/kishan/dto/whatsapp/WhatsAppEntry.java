package com.kishan.dto.whatsapp;


import java.util.List;

public class WhatsAppEntry {
    private String id;
    private List<WhatsAppChange> changes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<WhatsAppChange> getChanges() {
        return changes;
    }

    public void setChanges(List<WhatsAppChange> changes) {
        this.changes = changes;
    }
}