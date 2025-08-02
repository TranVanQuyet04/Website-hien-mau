package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.DonationRegistration;

public class BloodBagMapper {

    public static BloodBagDTO toDTO(BloodBag entity) {
        return new BloodBagDTO(
                entity.getBloodBagId(),
                entity.getBagCode(),
                entity.getBloodType() != null ? String.valueOf(entity.getBloodType().getBloodTypeId()) : null,
                entity.getRh(),
                entity.getVolume(),
                entity.getHematocrit(),
                entity.getCollectedAt(),
                entity.getTestStatus(),
                entity.getStatus(),
                entity.getDonorId(),
                entity.getNote(),
                entity.getRegistration() != null ? entity.getRegistration().getRegistrationId() : null
        );
    }

    public static BloodBag fromDTO(BloodBagDTO dto, BloodType bloodType, DonationRegistration registration) {
        BloodBag entity = new BloodBag();
        entity.setBloodBagId(dto.getBloodBagId());
        entity.setBagCode(dto.getBagCode());
        entity.setBloodType(bloodType);
        entity.setRh(dto.getRh());
        entity.setVolume(dto.getVolume());
        entity.setHematocrit(dto.getHematocrit());
        entity.setCollectedAt(dto.getCollectedAt());
        entity.setTestStatus(dto.getTestStatus());
        entity.setStatus(dto.getStatus());
        entity.setDonorId(dto.getDonorId());
        entity.setNote(dto.getNote());

        // ✅ Gán entity đã được fetch từ service
        entity.setRegistration(registration);

        return entity;
    }
}
