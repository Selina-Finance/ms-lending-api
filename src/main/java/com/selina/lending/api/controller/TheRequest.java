package com.selina.lending.api.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class TheRequest {

    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
