package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryKPIDTO {
    private long totalDonors;
    private long emergencyDonations;
    private long regularDonations;
    private double rejectionRate;
    private int averageRecoveryDays;
    private int totalSuccessfulTransfusions;
    private int hospitalCount;
}

