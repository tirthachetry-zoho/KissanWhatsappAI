package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "FarmEventCreate", description = "Payload to record a dated farm activity")
public class FarmEventCreate {

    @NotNull
    @Schema(description = "Farm id (must exist)", example = "1", required = true)
    private Long farmId;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Activity type, e.g. sowing, irrigation, fertilizer, harvest",
            example = "irrigation", required = true, maxLength = 50)
    private String eventType;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Activity date", example = "2024-07-01", format = "date", required = true)
    private LocalDate date;

    @Size(max = 50)
    @Schema(description = "Quantity involved, e.g. fertilizer bags, water hours",
            example = "2 bags", maxLength = 50)
    private String quantity;

    @Schema(description = "Cost in INR", example = "1500.0")
    private Double cost;

    @Schema(description = "Free-form notes", example = "Drip irrigation for tomato plot")
    private String notes;

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}