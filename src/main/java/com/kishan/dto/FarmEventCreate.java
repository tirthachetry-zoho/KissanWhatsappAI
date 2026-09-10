package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FarmEventCreate {

    @NotNull
    private Long farmId;

    @NotBlank
    @Size(max = 50)
    private String eventType;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Size(max = 50)
    private String quantity;

    private Double cost;

    private String notes;
}