package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;

public class HealthCheckFormMapper {
    public static HealthCheckForm toEntity(HealthCheckFormDTO dto, DonationRegistration reg) {
        return HealthCheckForm.builder()
                .registration(reg)
                .bodyTemperature(dto.getBodyTemperature())
                .heartRate(dto.getHeartRate())
                .bloodPressureSys(dto.getBloodPressureSys())
                .bloodPressureDia(dto.getBloodPressureDia())
                .heightCm(dto.getHeightCm())
                .weightKg(dto.getWeightKg())
                .hasFever(dto.getHasFever())
                .tookAntibioticsRecently(dto.getTookAntibioticsRecently())
                .hasChronicIllness(dto.getHasChronicIllness())
                .isPregnantOrBreastfeeding(dto.getIsPregnantOrBreastfeeding())
                .hadRecentTattooOrSurgery(dto.getHadRecentTattooOrSurgery())
                .hasRiskySexualBehavior(dto.getHasRiskySexualBehavior())
                .isEligible(dto.getIsEligible())
                .notesByStaff(dto.getNotesByStaff())
                .build();
    }

    public static HealthCheckFormDTO toDTO(HealthCheckForm entity) {
        HealthCheckFormDTO dto = new HealthCheckFormDTO();
        dto.setId(entity.getId());
        dto.setRegistrationId(entity.getRegistration().getRegistrationId());
        dto.setBodyTemperature(entity.getBodyTemperature());
        dto.setHeartRate(entity.getHeartRate());
        dto.setBloodPressureSys(entity.getBloodPressureSys());
        dto.setBloodPressureDia(entity.getBloodPressureDia());
        dto.setHeightCm(entity.getHeightCm());
        dto.setWeightKg(entity.getWeightKg());
        dto.setHasFever(entity.getHasFever());
        dto.setTookAntibioticsRecently(entity.getTookAntibioticsRecently());
        dto.setHasChronicIllness(entity.getHasChronicIllness());
        dto.setIsPregnantOrBreastfeeding(entity.getIsPregnantOrBreastfeeding());
        dto.setHadRecentTattooOrSurgery(entity.getHadRecentTattooOrSurgery());
        dto.setHasRiskySexualBehavior(entity.getHasRiskySexualBehavior());
        dto.setIsEligible(entity.getIsEligible());
        dto.setNotesByStaff(entity.getNotesByStaff());
        return dto;
    }

}
