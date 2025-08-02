package com.quyet.superapp.repository;

import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.entity.HealthCheckFailureLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthCheckFailureLogRepository extends JpaRepository<HealthCheckFailureLog, Long> {
    List<HealthCheckFailureLog> findByRegistration_RegistrationId(Long registrationId);

}
