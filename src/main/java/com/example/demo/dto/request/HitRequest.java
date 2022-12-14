package com.example.demo.dto.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class HitRequest {
    @Max(5)
    @Min(-3)
    @NotNull
    private Double x;
    @Max(5)
    @Min(-3)
    private Double y;
    @Max(5)
    @Min(-3)
    private Double r;
}
