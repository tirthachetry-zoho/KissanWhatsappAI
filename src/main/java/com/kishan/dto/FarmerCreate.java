package com.kishan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FarmerCreate {

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @Size(max = 100)
    private String name;

    @Size(max = 10)
    private String language = "en";

    @Size(max = 200)
    private String location;
}
