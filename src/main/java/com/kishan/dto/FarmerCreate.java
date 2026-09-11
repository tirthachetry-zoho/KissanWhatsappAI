package com.kishan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "FarmerCreate", description = "Payload to create or update a farmer profile")
public class FarmerCreate {

    @NotBlank
    @Size(max = 20)
    @Schema(description = "WhatsApp phone number in international format, must be unique",
            example = "919000000001", required = true, maxLength = 20)
    private String phoneNumber;

    @Size(max = 100)
    @Schema(description = "Farmer display name (auto-filled from WhatsApp profile on webhook ingest)",
            example = "Ravi Kumar", maxLength = 100)
    private String name;

    @Size(max = 10)
    @Schema(description = "Preferred language code (en, kn, hi)", example = "en",
            defaultValue = "en", maxLength = 10)
    private String language = "en";

    @Size(max = 200)
    @Schema(description = "Home location; defaults to the launch region when omitted",
            example = "Madhya Pradesh", maxLength = 200)
    private String location;

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
