package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonorDetailDTO {
    private String fullName;
    private String bloodGroup;
    private String component;
    private String readinessLevel;
    private Long totalDonations;               // COUNT trả về Long
    private LocalDateTime lastDonationDate;    // MAX trả về LocalDateTime
    private String status;
    private Double distanceKm;


    public DonorDetailDTO(String fullName, String bloodGroup, String component, String readinessLevel,
                          Long totalDonations, LocalDateTime lastDonationDate) {
        this.fullName = fullName;
        this.bloodGroup = bloodGroup;
        this.component = component;
        this.readinessLevel = readinessLevel;
        this.totalDonations = totalDonations;
        this.lastDonationDate = lastDonationDate;
    }
}
