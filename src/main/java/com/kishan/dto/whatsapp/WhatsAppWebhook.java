package com.kishan.dto.whatsapp;


import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "WhatsAppWebhook", description = "Meta WhatsApp Business API inbound notification envelope (object + entry list)")
public class WhatsAppWebhook {
    @Schema(description = "Always 'whatsapp_business_account' for message notifications",
            example = "whatsapp_business_account")
    private String object;
    @Schema(description = "Notification entries; each carries a changes array with field 'messages'")
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