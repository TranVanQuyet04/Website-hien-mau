package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodInventoryAlertDTO {
    private Long id;
    private String bloodType;
    private String component;
    private Integer quantityMl;
    private Integer minThresholdMl;
    private Integer criticalThresholdMl;
    private String alertLevel; // NORMAL, WARNING, CRITICAL
}
