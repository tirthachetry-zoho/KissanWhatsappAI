package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.FarmEvent;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
}