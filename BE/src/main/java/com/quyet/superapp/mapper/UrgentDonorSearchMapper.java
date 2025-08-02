package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UrgentDonorSearchResultDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UrgentDonorSearchMapper {

    public UrgentDonorSearchResultDTO toDTO(UrgentDonorRegistry registry) {
        UserProfile profile = registry.getDonor().getUserProfile();

        return UrgentDonorSearchResultDTO.builder()
                .id(registry.getId())
                .fullName(profile.getFullName())
                .phone(profile.getPhone())
                .address(profile.getAddress() != null ? profile.getAddress().getFullAddress() : "")
                .bloodType(registry.getBloodType().getDescription())
                .component(registry.getBloodComponent().getName())
                .distance(null)
                .build();
    }

    public UrgentDonorSearchResultDTO toDTO(UrgentDonorRegistry registry, double distance) {
        UserProfile profile = registry.getDonor().getUserProfile();
        double roundedDistance = Math.round(distance * 10.0) / 10.0;

        return UrgentDonorSearchResultDTO.builder()
                .id(registry.getId())
                .fullName(profile.getFullName())
                .phone(profile.getPhone())
                .address(profile.getAddress() != null ? profile.getAddress().getFullAddress() : "")
                .bloodType(registry.getBloodType().getDescription())
                .component(registry.getBloodComponent().getName())
                .distance(roundedDistance)
                .build();
    }
}
