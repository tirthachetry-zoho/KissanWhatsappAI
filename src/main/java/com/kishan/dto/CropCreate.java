package com.kishan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
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
}
