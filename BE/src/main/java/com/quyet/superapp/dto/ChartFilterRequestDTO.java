package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChartFilterRequestDTO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long bloodTypeId;
    private Long componentId;
    private String readinessLevel; // ENUM dưới dạng String
}
