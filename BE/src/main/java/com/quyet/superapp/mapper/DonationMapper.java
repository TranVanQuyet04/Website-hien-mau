package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRequestDTO;
import com.quyet.superapp.entity.Donation;

public class DonationMapper {
    public static DonationRequestDTO toDTO(Donation donation) {
        DonationRequestDTO dto = new DonationRequestDTO();
        dto.setDonationId(donation.getDonationId());

        if (donation.getUser() != null) {
            dto.setUserId(donation.getUser().getUserId());
            if (donation.getUser().getUserProfile() != null) {
                dto.setWeight(donation.getUser().getUserProfile().getWeight());
            }
        }

        if (donation.getRegistration() != null)
            dto.setRegistrationId(donation.getRegistration().getRegistrationId());

        if (donation.getBloodType() != null)
            dto.setBloodTypeId(donation.getBloodType().getBloodTypeId());

        if (donation.getBloodComponent() != null)
            dto.setComponentId(donation.getBloodComponent().getBloodComponentId());

        dto.setDonationDate(donation.getDonationDate().toLocalDate());
        dto.setVolumeMl(donation.getVolumeMl());
        dto.setLocation(donation.getLocation());
        dto.setNotes(donation.getNotes());
        dto.setCreatedAt(donation.getCreatedAt());
        dto.setUpdatedAt(donation.getUpdatedAt());
        dto.setStatus(donation.getStatus());
        return dto;
    }
}