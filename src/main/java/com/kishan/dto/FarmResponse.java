package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Farm;

import java.time.LocalDateTime;

public class FarmResponse {

    private Long id;
    private Long farmerId;
    private String location;
    private Double area;
    private String areaUnit;
    private String soilType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static FarmResponse from(Farm farm) {
        FarmResponse dto = new FarmResponse();
        dto.setId(farm.id);
        dto.setFarmerId(farm.getFarmer() != null ? farm.getFarmer().id : null);
        dto.setLocation(farm.getLocation());
        dto.setArea(farm.getArea());
        dto.setAreaUnit(farm.getAreaUnit());
        dto.setSoilType(farm.getSoilType());
        dto.setCreatedAt(farm.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilType) {
        this.soilType = soilType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
