package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodRequestWithNewPatientDTO;
import com.quyet.superapp.dto.CreateBloodRequestDTO;
import com.quyet.superapp.entity.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PatientMapper {

    // Mapping từ DTO: BloodRequestWithNewPatientDTO → Patient
    public Patient toEntity(BloodRequestWithNewPatientDTO dto) {
        return Patient.builder()
                .fullName(dto.getPatientName())
                .phone(dto.getPatientPhone())
                .gender(dto.getPatientGender())
                .age(dto.getPatientAge())
                .weight(dto.getPatientWeight())
                .bloodGroup(dto.getPatientBloodGroup())
                .citizenId(dto.getCitizenId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Mapping từ DTO: CreateBloodRequestDTO → Patient (dành cho luồng thường)
    public Patient toEntity(CreateBloodRequestDTO dto) {
        return Patient.builder()
                .fullName(dto.getPatientName())
                .phone(dto.getPatientPhone())
                .gender(dto.getPatientGender())
                .age(dto.getPatientAge())
                .weight(dto.getPatientWeight())
                .bloodGroup(dto.getPatientBloodGroup())
                .citizenId(dto.getCitizenId()) // ⚠️ Đảm bảo field này có trong DTO
                .createdAt(LocalDateTime.now())
                .build();
    }
}
