package com.kishan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "FarmCreate", description = "Payload to register a farm plot for a farmer")
public class FarmCreate {

    @NotNull
    @Schema(description = "Owner farmer id (must exist)", example = "1", required = true)
    private Long farmerId;

    @Size(max = 200)
    @Schema(description = "Plot location / village", example = "Sehore, MP", maxLength = 200)
    private String location;

    @Schema(description = "Plot size", example = "2.5")
    private Double area;

    @Size(max = 20)
    @Schema(description = "Area unit", example = "acres", defaultValue = "acres", maxLength = 20)
    private String areaUnit = "acres";

    @Size(max = 100)
    @Schema(description = "Soil type", example = "Black cotton soil", maxLength = 100)
    private String soilType;

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
}
