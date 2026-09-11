package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.FarmEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FarmEventResponse {

    private Long id;
    private Long farmId;
    private String eventType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String quantity;
    private Double cost;
    private String notes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static FarmEventResponse from(FarmEvent event) {
        FarmEventResponse dto = new FarmEventResponse();
        dto.setId(event.id);
        dto.setFarmId(event.getFarm() != null ? event.getFarm().id : null);
        dto.setEventType(event.getEventType());
        dto.setDate(event.getDate());
        dto.setQuantity(event.getQuantity());
        dto.setCost(event.getCost());
        dto.setNotes(event.getNotes());
        dto.setCreatedAt(event.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}