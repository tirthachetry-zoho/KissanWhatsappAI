package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Farmer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FarmerResponse {

    private Long id;
    private String phoneNumber;
    private String name;
    private String language;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public static FarmerResponse from(Farmer farmer) {
        FarmerResponse dto = new FarmerResponse();
        dto.setId(farmer.id);
        dto.setPhoneNumber(farmer.getPhoneNumber());
        dto.setName(farmer.getName());
        dto.setLanguage(farmer.getLanguage());
        dto.setLocation(farmer.getLocation());
        dto.setCreatedAt(farmer.getCreatedAt());
        dto.setUpdatedAt(farmer.getUpdatedAt());
        return dto;
    }
}
