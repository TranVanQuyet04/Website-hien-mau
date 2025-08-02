package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;
import org.springframework.stereotype.Component;

@Component
public class DonationRegistrationMapper {

    // Convert từ Entity → DTO (trả về front-end)
    public DonationRegistrationDTO toDTO(DonationRegistration entity) {
        DonationRegistrationDTO dto = new DonationRegistrationDTO();

        dto.setRegistrationId(entity.getRegistrationId());
        dto.setScheduledDate(entity.getScheduledDate()); // ✅ lấy đúng field mới
        dto.setSlotId(entity.getSlotId());                // ✅ thêm slotId
        dto.setLocation(entity.getLocation());
        dto.setBloodType(entity.getBloodType());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);

        User user = entity.getUser();
        if (user != null) {
            dto.setUserId(user.getUserId());
            dto.setEmail(user.getEmail());

            UserProfile userProfile = user.getUserProfile();
            if (userProfile != null) {
                dto.setFullName(userProfile.getFullName());
                dto.setReadyDate(userProfile.getDob());
                dto.setGender(userProfile.getGender());
                dto.setPhone(userProfile.getPhone());

                Address addr = userProfile.getAddress();
                if (addr != null) {
                    StringBuilder full = new StringBuilder();
                    if (addr.getAddressStreet() != null) {
                        full.append(addr.getAddressStreet());
                    }

                    Ward ward = addr.getWard();
                    if (ward != null) {
                        full.append(", ").append(ward.getWardName());
                        District district = ward.getDistrict();
                        if (district != null) {
                            full.append(", ").append(district.getDistrictName());
                            City city = district.getCity();
                            if (city != null) {
                                full.append(", ").append(city.getNameCity());
                            }
                        }
                    }

                    dto.setAddressFull(full.toString());
                }
            }
        }

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // Convert từ DTO → Entity (khi tạo đơn đăng ký mới)
    public DonationRegistration toEntity(DonationRegistrationDTO dto, User user) {
        DonationRegistration entity = new DonationRegistration();
        entity.setUser(user);
        entity.setScheduledDate(dto.getScheduledDate()); // ✅ ngày
        entity.setSlotId(dto.getSlotId());               // ✅ khung giờ
        entity.setLocation(dto.getLocation());
        entity.setBloodType(dto.getBloodType());
        return entity;
    }
}
