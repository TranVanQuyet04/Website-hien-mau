package com.quyet.superapp.service;

import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckFailureLog;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.mapper.HealthCheckFailureLogMapper;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.HealthCheckFailureLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckFailureLogService {
    private final HealthCheckFailureLogRepository logRepository;
    private final DonationRegistrationRepository registrationRepository;

    public HealthCheckFailureLogDTO saveLog(Long registrationId, String reason, String staffNote) {
        DonationRegistration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new MemberException("NOT_FOUND", "Không tìm thấy đơn đăng ký"));

        HealthCheckFailureLog log = HealthCheckFailureLog.builder()
                .registration(reg)
                .reason(reason)
                .staffNote(staffNote)
                .build();

        logRepository.save(log);
        return HealthCheckFailureLogMapper.toDTO(log);
    }

    public List<HealthCheckFailureLogDTO> getLogsByRegistrationId(Long regId) {
        return logRepository.findByRegistration_RegistrationId(regId)
                .stream()
                .map(HealthCheckFailureLogMapper::toDTO)
                .collect(Collectors.toList());
    }

}
