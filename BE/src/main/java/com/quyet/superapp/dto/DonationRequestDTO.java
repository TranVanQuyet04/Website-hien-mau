package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRequestDTO {
    private Long donationId;
    private Long userId;
    private Long registrationId;
    private Long bloodTypeId;
    private Long componentId;
    private LocalDate donationDate;
    private Integer volumeMl;
    private String location;
    private String notes;
    private Double weight;
    private DonationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


