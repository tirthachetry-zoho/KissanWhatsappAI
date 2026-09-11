package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Farmer;

import java.time.LocalDateTime;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
