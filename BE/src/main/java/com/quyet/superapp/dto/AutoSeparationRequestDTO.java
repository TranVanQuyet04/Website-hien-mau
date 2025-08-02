package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoSeparationRequestDTO {
    private Long donationId;
    private String method;
    private boolean leukoreduced;
    private Long staffId;
}
