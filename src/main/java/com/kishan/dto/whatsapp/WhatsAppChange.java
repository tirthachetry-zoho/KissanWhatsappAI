package com.kishan.dto.whatsapp;


import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "WhatsAppChange", description = "One change inside a webhook entry; message flow uses field 'messages'")
public class WhatsAppChange {
    @Schema(description = "Change field; 'messages' for inbound message notifications",
            example = "messages")
    private String field;
    @Schema(description = "Payload for the change")
    private WhatsAppValue value;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public WhatsAppValue getValue() {
        return value;
    }

    public void setValue(WhatsAppValue value) {
        this.value = value;
    }
}