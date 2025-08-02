package com.quyet.superapp.dto;

import com.quyet.superapp.entity.DonationRegistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthCheckFailureLogDTO {

    private Long logId;
    private Long registrationId;
    private String reason;
    private String staffNote;
    private LocalDateTime createdAt;

}
