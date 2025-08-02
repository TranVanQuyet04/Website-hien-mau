package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.City;

import java.time.format.DateTimeFormatter;

public class UserProfileMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ✅ Entity → DTO
    public static UserProfileDTO toDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();

        dto.setUserId(profile.getUser() != null ? profile.getUser().getUserId() : null);
        dto.setFullName(profile.getFullName());
        dto.setDob(profile.getDob());
        dto.setGender(profile.getGender());
        dto.setBloodTypeId(profile.getBloodType() != null ? profile.getBloodType().getBloodTypeId() : null);
        dto.setPhone(profile.getPhone());

        dto.setEmail(profile.getUser() != null ? profile.getUser().getEmail() : null);
        dto.setOccupation(profile.getOccupation());
        dto.setLastDonationDate(profile.getLastDonationDate());
        dto.setRecoveryTime(profile.getRecoveryTime());
        dto.setLocation(profile.getLocation());
        dto.setCitizenId(profile.getCitizenId());
        dto.setHeight(profile.getHeight());
        dto.setWeight(profile.getWeight());

        // ✅ Bảo hiểm y tế
        dto.setHasInsurance(profile.getHasInsurance());
        dto.setInsuranceCardNumber(profile.getInsuranceCardNumber());
        dto.setInsuranceValidTo(profile.getInsuranceValidTo());

        // ✅ Địa chỉ
            dto.setAddressId(profile.getAddress().getAddressId()); // ✅ Thêm dòng này


        // ✅ Địa chỉ đầy đủ nếu đủ thông tin ward/district/city
        if (profile.getAddress() != null && profile.getAddress().getWard() != null) {
            Address addr = profile.getAddress();
            Ward ward = addr.getWard();
            District district = ward.getDistrict();
            City city = district.getCity();

            String fullAddress = String.format(
                    "%s, %s, %s, %s",
                    addr.getAddressStreet(),
                    ward.getWardName(),
                    district.getDistrictName(),
                    city.getNameCity()
            );
            dto.setAddressFull(fullAddress);
        }

        return dto;
    }

    // ✅ DTO → Entity
    public static UserProfile toEntity(UserProfileDTO dto, User user, Address address) {
        UserProfile profile = new UserProfile();

        profile.setUser(user);
        profile.setDob(dto.getDob());
        profile.setFullName(dto.getFullName());
        profile.setGender(dto.getGender());
        profile.setPhone(dto.getPhone());
        profile.setEmail(dto.getEmail());
        profile.setOccupation(dto.getOccupation());
        profile.setLastDonationDate(dto.getLastDonationDate());
        profile.setRecoveryTime(dto.getRecoveryTime());
        profile.setLocation(dto.getLocation());
        profile.setCitizenId(dto.getCitizenId());
        profile.setAddress(address);
        profile.setHeight(dto.getHeight());
        profile.setWeight(dto.getWeight());

        // ✅ Bảo hiểm y tế
        profile.setHasInsurance(dto.getHasInsurance());
        profile.setInsuranceCardNumber(dto.getInsuranceCardNumber());
        profile.setInsuranceValidTo(dto.getInsuranceValidTo());

        return profile;
    }
}
