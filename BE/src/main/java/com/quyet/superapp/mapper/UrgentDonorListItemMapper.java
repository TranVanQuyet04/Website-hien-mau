package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UrgentDonorListItemDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.enums.DonorReadinessLevel;
import org.springframework.stereotype.Component;

@Component
public class UrgentDonorListItemMapper {

    public UrgentDonorListItemDTO toDTO(UrgentDonorRegistry entity) {
        if (entity == null || entity.getDonor() == null) return null;

        var user = entity.getDonor();
        var profile = user.getUserProfile();

        DonorReadinessLevel readinessLevel = entity.getReadinessLevel(); // ✅ enum đúng kiểu
        String readinessDescription = switch (readinessLevel) {
            case EMERGENCY_NOW -> "HIẾN NGAY";
            case EMERGENCY_FLEXIBLE -> "LINH HOẠT";
            case REGULAR -> "THÔNG THƯỜNG";
            case NOT_READY -> "CHƯA SẴN SÀNG";
            case UNDECIDED -> "CHƯA CHỌN";
            default -> "--";
        };

        return new UrgentDonorListItemDTO(
                user.getUserId(),
                profile != null ? profile.getFullName() : "--",
                profile != null ? profile.getPhone() : "--",
                entity.getBloodType() != null ? entity.getBloodType().getDescription() : "--",
                readinessLevel,                // ✅ truyền enum
                readinessDescription,          // ✅ truyền mô tả
                (profile != null && profile.getLastDonationDate() != null)
                        ? profile.getLastDonationDate().toLocalDate().toString()
                        : "--",
                (profile != null && profile.getRecoveryTime() != null)
                        ? profile.getRecoveryTime() + " ngày"
                        : "--",
                (profile != null && profile.getNote() != null)
                        ? profile.getNote()
                        : ""
        );
    }
}
