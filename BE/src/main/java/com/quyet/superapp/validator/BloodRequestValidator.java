package com.quyet.superapp.validator;

import com.quyet.superapp.dto.BloodRequestWithNewPatientDTO;
import com.quyet.superapp.entity.Patient;
import com.quyet.superapp.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BloodRequestValidator {

    private final PatientRepository patientRepository;

    public void validateAndSetSuspectedPatient(BloodRequestWithNewPatientDTO dto) {
        if (dto.getPatientId() == null && dto.getCitizenId() != null) {
            Optional<Patient> patient = patientRepository.findByCitizenId(dto.getCitizenId());
            patient.ifPresent(p -> dto.setPatientId(p.getId()));
        }
    }
}
