package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.enums.SeparationPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeparationOrderRequest {
    private Long bloodBagId;
    private Long operatorId;
    private Long machineId; // optional nếu phương pháp là MANUAL
    private SeparationMethod method;
    private SeparationPattern pattern;
    private String note;
}
