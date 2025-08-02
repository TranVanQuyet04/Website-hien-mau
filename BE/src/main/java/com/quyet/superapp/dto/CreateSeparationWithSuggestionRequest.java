package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeparationWithSuggestionRequest {
    private Long bloodBagId;
    private Long operatorId;
    private Long machineId;
    private SeparationMethod type;
    private String note;

    private String gender;
    private Double weight;
    private boolean leukoreduced;
}
