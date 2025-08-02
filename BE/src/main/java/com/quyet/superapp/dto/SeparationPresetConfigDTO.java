package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationPresetConfigDTO {
    private Long id;
    private String gender;
    private int minWeight;
    private String method;
    private boolean leukoreduced;
    private double rbcRatio;
    private double plasmaRatio;
    private Integer plateletsFixed;
}
