package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckFailureLog;

public class HealthCheckFailureLogMapper {

    public static HealthCheckFailureLogDTO toDTO(HealthCheckFailureLog entity) {
        return HealthCheckFailureLogDTO.builder()
                .logId(entity.getLogId())
                .registrationId(entity.getRegistration().getRegistrationId())
                .reason(entity.getReason())
                .staffNote(entity.getStaffNote())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static HealthCheckFailureLog toEntity(HealthCheckFailureLogDTO dto, DonationRegistration reg) {
        return HealthCheckFailureLog.builder()
                .registration(reg)
                .reason(dto.getReason())
                .staffNote(dto.getStaffNote())
                .build();
    }
}
