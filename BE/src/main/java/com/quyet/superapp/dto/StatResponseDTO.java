package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatResponseDTO {
    private int totalUsers;
    private int totalBloodUnits;
    private int pendingUrgentRequests;
    private int totalDonations;
    private int activeBlogs;
    private int successfulPayments;
}
