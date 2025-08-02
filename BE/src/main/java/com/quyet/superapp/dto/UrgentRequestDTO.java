package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodRequestStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UrgentRequestDTO {
    private Long urgentRequestId;
    private String hospitalName;
    private String bloodType;
    private int units;
    private LocalDate requestDate;
    private BloodRequestStatus status;
    private Long requesterId;
}

