package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonorDetailDTO;
import com.quyet.superapp.dto.EmergencyDonorDTO;
import com.quyet.superapp.dto.UnverifiedDonorDTO;
import com.quyet.superapp.dto.VerifiedUrgentDonorDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UrgentDonorMapper {

    public DonorDetailDTO toDonorDetailDTO(UrgentDonorRegistry registry, int totalDonations,
                                           LocalDate lastDonationDate, String status, double distanceKm) {
        var profile = registry.getDonor().getUserProfile();
        return DonorDetailDTO.builder()
                .fullName(profile != null ? profile.getFullName() : "Chưa có hồ sơ")
                .bloodGroup(registry.getBloodType().getDescription())
                .component(registry.getBloodComponent().getName())
                .readinessLevel(registry.getReadinessLevel().name())
                .totalDonations((long) totalDonations)             // ✅ đóng ngoặc đúng chỗ
                .lastDonationDate(lastDonationDate != null ? lastDonationDate.atStartOfDay() : null)
                // ✅ LocalDate đúng kiểu
                .status(status)
                .distanceKm(distanceKm)
                .build();
    }



    public VerifiedUrgentDonorDTO toVerifiedDTO(UrgentDonorRegistry entity) {
        var dto = new VerifiedUrgentDonorDTO();
        var user = entity.getDonor();

        dto.setUserId(user.getUserId());

        if (user.getUserProfile() != null) {
            var profile = user.getUserProfile();
            dto.setFullName(profile.getFullName());
            dto.setPhone(profile.getPhone());
            dto.setBloodType(profile.getBloodType() != null
                    ? profile.getBloodType().getDescription()
                    : "Chưa rõ");
            dto.setAddressFull(profile.getAddress() != null
                    ? profile.getAddress().getFullAddress()
                    : "Chưa cập nhật");
        } else {
            dto.setFullName("Chưa có hồ sơ");
            dto.setPhone("N/A");
            dto.setBloodType("N/A");
            dto.setAddressFull("N/A");
        }
        return dto;
    }


    public UnverifiedDonorDTO toUnverifiedDTO(UrgentDonorRegistry entity) {
            var dto = new UnverifiedDonorDTO();
            dto.setDonorRegistryId(entity.getId()); // ✅ Thêm dòng này
            var user = entity.getDonor();
            dto.setUserId(user.getUserId());

            if (user.getUserProfile() != null) {
                var profile = user.getUserProfile();
                dto.setFullName(profile.getFullName());
                dto.setPhone(profile.getPhone());
                dto.setGender(profile.getGender());
                dto.setDob(profile.getDob());
                dto.setBloodType(profile.getBloodType() != null ? profile.getBloodType().getDescription() : "Chưa rõ");
                dto.setAddressFull(profile.getAddress() != null ? profile.getAddress().getFullAddress() : "N/A");
            } else {
                dto.setFullName("Chưa có hồ sơ");
                dto.setPhone("N/A");
                dto.setGender("N/A");
                dto.setDob(null);
                dto.setAddressFull("N/A");
                dto.setBloodType("N/A");
            }

            dto.setRegisteredAt(entity.getRegisteredAt());
            dto.setStatus(Boolean.TRUE.equals(entity.getIsVerified()) ? "ĐÃ XÁC MINH" : "CHỜ XÁC MINH");
            return dto;
        }
    }

