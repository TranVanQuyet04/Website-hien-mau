package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCitizenId(String citizenId);
    boolean existsByCitizenId(String citizenId);
    Optional<Patient> findByPhone(String phone);

    boolean existsByInsuranceCardNumber(String cardNumber);



}
