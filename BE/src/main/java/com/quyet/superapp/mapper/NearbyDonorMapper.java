package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonorWithDistance;
import com.quyet.superapp.dto.NearbyDonorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NearbyDonorMapper {

    @Mapping(source = "donor.donor.userProfile.fullName", target = "fullName")
    @Mapping(source = "donor.bloodType.description", target = "bloodType")
    @Mapping(source = "donor.address.fullAddress", target = "nearestAddress")
    @Mapping(source = "donor.donor.userProfile.phone", target = "phoneNumber")
    @Mapping(source = "donor.donor.userId", target = "userId")
    @Mapping(source = "distanceKm", target = "distanceKm")
    NearbyDonorDTO toDTO(DonorWithDistance source);
}
