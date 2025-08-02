package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadinessStackedDTO {
    private String bloodGroup;
    private Long emergencyNow;
    private Long emergencyFlexible;
    private Long regular;
}
