package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreDonationTestDTO {
    private Long preDonationTestId;
    private Long healthCheckId;
    private Boolean hivResult;
    private Boolean hbvResult;
    private Boolean hcvResult;
    private Boolean syphilisResult;
    private Double hbLevel;
    private Long bloodTypeId;
    private LocalDate testDate;
}
