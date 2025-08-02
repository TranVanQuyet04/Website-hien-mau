package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.PreDonationTestDTO;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.entity.PreDonationTest;
import org.springframework.stereotype.Component;


@Component
public class PreDonationTestMapper {

    public PreDonationTestDTO toDTO(PreDonationTest entity) {
        PreDonationTestDTO dto = new PreDonationTestDTO();
        dto.setPreDonationTestId(entity.getPreDonationTestId());
        dto.setHealthCheckId(entity.getHealthCheckForm().getId());
        dto.setHivResult(entity.getHivResult());
        dto.setHbvResult(entity.getHbvResult());
        dto.setHcvResult(entity.getHcvResult());
        dto.setSyphilisResult(entity.getSyphilisResult());
        dto.setHbLevel(entity.getHbLevel());
        dto.setTestDate(entity.getTestDate());
        //dto.setDonationId(entity.getDonation().getDonationId());
        dto.setBloodTypeId(entity.getBloodType().getBloodTypeId());
        return dto;
    }

    public PreDonationTest toEntity(PreDonationTestDTO dto, HealthCheckForm form, BloodType bloodType) {
        PreDonationTest entity = new PreDonationTest();
        entity.setPreDonationTestId(dto.getPreDonationTestId()); // nếu là update
        entity.setHealthCheckForm(form);                         // ✅ Gắn phiếu khám
        entity.setHivResult(dto.getHivResult());
        entity.setHbvResult(dto.getHbvResult());
        entity.setHcvResult(dto.getHcvResult());
        entity.setSyphilisResult(dto.getSyphilisResult());
        entity.setHbLevel(dto.getHbLevel());
        entity.setTestDate(dto.getTestDate());
        entity.setBloodType(bloodType);
        return entity;
    }

    public void updateEntity(PreDonationTest entity, PreDonationTestDTO dto, HealthCheckForm form, BloodType bloodType) {
        entity.setHealthCheckForm(form);
        entity.setHivResult(dto.getHivResult());
        entity.setHbvResult(dto.getHbvResult());
        entity.setHcvResult(dto.getHcvResult());
        entity.setSyphilisResult(dto.getSyphilisResult());
        entity.setHbLevel(dto.getHbLevel());
        entity.setTestDate(dto.getTestDate());
        entity.setBloodType(bloodType);
    }


}
