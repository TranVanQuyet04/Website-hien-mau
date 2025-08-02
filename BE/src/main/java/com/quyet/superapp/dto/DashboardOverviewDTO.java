package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardOverviewDTO {
    private long donorsToday;
    private long bloodUnitsAvailable;
    private long urgentRequestsCount;
    private List<BloodGroupDistributionDTO> bloodDistribution;
}
