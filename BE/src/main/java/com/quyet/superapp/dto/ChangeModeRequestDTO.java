package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.Data;

@Data
public class ChangeModeRequestDTO {
    private DonorReadinessLevel newMode;
}

