package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrgentDonorRegistrationDTO {
    private Long bloodTypeId;
    private Double latitude;
    private Double longitude;
    private DonorReadinessLevel readinessLevel;
}
