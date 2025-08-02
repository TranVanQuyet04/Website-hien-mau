package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.Data;

@Data
public class DonorReadinessRequestDTO {
    private DonorReadinessLevel readinessLevel;
}
