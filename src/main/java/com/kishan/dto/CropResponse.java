package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Crop;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CropResponse {

    private Long id;
    private Long farmId;
    private String crop;
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
}
