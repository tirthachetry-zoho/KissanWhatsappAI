package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhatsAppContact {

    @JsonProperty("wa_id")
    private String waId;

    private WhatsAppProfile profile;

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }

    public WhatsAppProfile getProfile() {
        return profile;
    }

    public void setProfile(WhatsAppProfile profile) {
        this.profile = profile;
    }
}