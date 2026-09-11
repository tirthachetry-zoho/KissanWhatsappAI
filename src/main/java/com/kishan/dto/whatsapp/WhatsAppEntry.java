package com.kishan.dto.whatsapp;


import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "WhatsAppEntry", description = "One notification entry: WhatsApp Business Account id plus its changes")
public class WhatsAppEntry {
    @Schema(description = "WhatsApp Business Account id", example = "WABA_ID")
    private String id;
    @Schema(description = "Changes; message notifications use field 'messages'")
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