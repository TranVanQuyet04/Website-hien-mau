package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApheresisMachineDTO {
    private Long id;
    private String serialNumber;
    private String manufacturer;
    private String model;
    private boolean isActive;
    private LocalDate lastMaintenance;
    private String note;
}
