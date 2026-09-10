package com.kishan.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
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
}
