package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Farm;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "FarmResponse", description = "Persisted farm plot")
public class FarmResponse {

    @Schema(description = "Database id", example = "1", readOnly = true)
    private Long id;
    @Schema(description = "Owner farmer id", example = "1")
    private Long farmerId;
    @Schema(description = "Plot location / village", example = "Sehore, MP")
    private String location;
    @Schema(description = "Plot size", example = "2.5")
    private Double area;
    @Schema(description = "Area unit", example = "acres")
    private String areaUnit;
    @Schema(description = "Soil type", example = "Black cotton soil")
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
