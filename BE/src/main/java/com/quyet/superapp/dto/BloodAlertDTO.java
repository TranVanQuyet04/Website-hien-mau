package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodAlertDTO {
    private String bloodType;
    private String component;
    private int quantity;
    private int minThreshold;
    private int criticalThreshold;
    private String status; // NORMAL / WARNING / CRITICAL
}
