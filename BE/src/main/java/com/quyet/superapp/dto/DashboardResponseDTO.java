package com.quyet.superapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {

    private long donorsToday;
    private long totalUnits;
    private long urgentRequestsPending;
    private long urgentRequestsApproved;
    private long urgentRequestsRejected;
    private List<GroupStat> groupStats;
}
