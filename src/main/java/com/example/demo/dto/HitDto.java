package com.example.demo.dto;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class HitDto {
    private long id;
    private double xValue;
    private double yValue;
    private double rValue;
    private boolean result;
    private long executionTime;
    private LocalDateTime dateTime;

    public HitDto(double xValue, double yValue, double rValue) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.rValue = rValue;
    }

    public HitDto(double xValue, double yValue, double rValue, boolean result, long executionTime, LocalDateTime dateTime) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.rValue = rValue;
        this.result = result;
        this.executionTime = executionTime;
        this.dateTime = dateTime;
    }
}
