package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Crop;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(name = "CropResponse", description = "Persisted crop record")
public class CropResponse {

    @Schema(description = "Database id", example = "1", readOnly = true)
    private Long id;
    @Schema(description = "Farm id", example = "1")
    private Long farmId;
    @Schema(description = "Crop name", example = "Tomato")
    private String crop;
    @Schema(description = "Variety / cultivar", example = "Arka Rakshak")
    private String variety;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plantingDate;

    private String area;
    private String growthStage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public static CropResponse from(Crop crop) {
        CropResponse dto = new CropResponse();
        dto.setId(crop.id);
        dto.setFarmId(crop.getFarm() != null ? crop.getFarm().id : null);
        dto.setCrop(crop.getCrop());
        dto.setVariety(crop.getVariety());
        dto.setPlantingDate(crop.getPlantingDate());
        dto.setArea(crop.getArea());
        dto.setGrowthStage(crop.getGrowthStage());
        dto.setCreatedAt(crop.getCreatedAt());
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

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public LocalDate getPlantingDate() {
        return plantingDate;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.plantingDate = plantingDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
