package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kishan.entity.Farm;
import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}
