package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CropCreate {

    @NotNull
    private Long farmId;

    @NotBlank
    @Size(max = 100)
    private String crop;

    @Size(max = 100)
    private String variety;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate plantingDate;

    @Size(max = 50)
    private String area;

    @Size(max = 50)
    private String growthStage;

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
}
