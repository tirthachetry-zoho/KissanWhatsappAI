package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "CropCreate", description = "Payload to record a crop planted on a farm")
public class CropCreate {

    @NotNull
    @Schema(description = "Farm id (must exist)", example = "1", required = true)
    private Long farmId;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Crop name", example = "Tomato", required = true, maxLength = 100)
    private String crop;

    @Size(max = 100)
    @Schema(description = "Variety / cultivar", example = "Arka Rakshak", maxLength = 100)
    private String variety;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Planting date", example = "2024-06-15", format = "date")
    private LocalDate plantingDate;

    @Size(max = 50)
    @Schema(description = "Area under this crop", example = "1 acre", maxLength = 50)
    private String area;

    @Size(max = 50)
    @Schema(description = "Current growth stage", example = "Flowering", maxLength = 50)
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
