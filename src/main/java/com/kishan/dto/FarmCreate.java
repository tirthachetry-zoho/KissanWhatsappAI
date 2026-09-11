package com.kishan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FarmCreate {

    @NotNull
    private Long farmerId;

    @Size(max = 200)
    private String location;

    private Double area;

    @Size(max = 20)
    private String areaUnit = "acres";

    @Size(max = 100)
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
