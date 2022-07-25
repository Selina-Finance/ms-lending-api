package com.selina.lending.api.controller;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TheRequest {

    @NotNull
    private Integer id;
    @NotBlank
    private String name;
}
