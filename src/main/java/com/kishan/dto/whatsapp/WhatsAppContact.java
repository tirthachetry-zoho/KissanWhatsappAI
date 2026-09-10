package com.kishan.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WhatsAppContact {

    @JsonProperty("wa_id")
    private String waId;

    private WhatsAppProfile profile;
}