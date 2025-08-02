package com.quyet.superapp.service;

import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.entity.DonorProfile;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.repository.DonorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonorReadinessService {

    private final DonationRegistrationService donationRegistrationService;
    private final UrgentDonorRegistryService urgentDonorRegistryService;
    private final DonorProfileRepository donorProfileRepository;


    public String processReadiness(Long userId, DonorReadinessLevel level, UrgentDonorRegistrationDTO dto) {
        DonorProfile profile = donorProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người hiến"));

        // ✅ Cập nhật trạng thái readiness vào hồ sơ người hiến
        profile.setReadinessLevel(level);
        donorProfileRepository.save(profile);

        switch (level) {
            case REGULAR:
                donationRegistrationService.createRegularRegistration(userId);
                return "✅ Đã đăng ký hiến máu thông thường.";

            case EMERGENCY_NOW:
            case EMERGENCY_FLEXIBLE:
                urgentDonorRegistryService.register(userId, level, dto);
                return "✅ Đăng ký thành công. Đơn đang chờ được xử lý.";

            case NOT_READY:
                return "📌 Đã ghi nhận bạn chưa sẵn sàng hiến máu.";

            default:
                return "❌ Lựa chọn không hợp lệ.";
        }
    }

}
