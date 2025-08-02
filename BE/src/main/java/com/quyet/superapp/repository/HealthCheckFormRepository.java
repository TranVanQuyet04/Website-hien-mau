package com.quyet.superapp.repository;

import com.quyet.superapp.entity.HealthCheckForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCheckFormRepository extends JpaRepository<HealthCheckForm, Long> {
    boolean existsByRegistration_RegistrationId(Long registrationId);
    HealthCheckForm findByRegistration_RegistrationId(Long registrationId);
}
