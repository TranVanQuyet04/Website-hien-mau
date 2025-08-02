package com.quyet.superapp.service;

import com.quyet.superapp.entity.DonorProfile;
import com.quyet.superapp.entity.DonorReadinessLog;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.repository.DonorProfileRepository;
import com.quyet.superapp.repository.DonorReadinessLogRepository;
import com.quyet.superapp.repository.UrgentDonorRegistryRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonorProfileService {

    private final UserRepository userRepo;
    private final DonorProfileRepository donorProfileRepo;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepo;
    private final DonorReadinessLogRepository donorReadinessLogRepo;

    public void markNotReady(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        updateReadiness(user.getUsername(), DonorReadinessLevel.NOT_READY.name());
    }

    public DonorProfile getByUserId(Long userId) {
        return donorProfileRepo.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ người hiến với ID: " + userId));
    }


    public void updateReadiness(String username, String readinessStr) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        DonorReadinessLevel newLevel = DonorReadinessLevel.valueOf(readinessStr);

        // Tìm hoặc tạo profile
        DonorProfile profile = donorProfileRepo.findByUser(user)
                .orElseGet(() -> new DonorProfile(user));
        DonorReadinessLevel oldLevel = profile.getReadinessLevel();

        // Ghi log nếu có thay đổi readiness
        if (oldLevel != null && oldLevel != newLevel) {
            DonorReadinessLog log = new DonorReadinessLog();
            log.setUser(user);
            log.setOldLevel(oldLevel);
            log.setNewLevel(newLevel);
            log.setChangedAt(LocalDateTime.now());
            donorReadinessLogRepo.save(log);
        }

        // Cập nhật readiness mới
        profile.setUser(user);
        profile.setReadinessLevel(newLevel);
        donorProfileRepo.save(profile);

        // Cập nhật danh sách người hiến khẩn cấp
        if (newLevel == DonorReadinessLevel.EMERGENCY_NOW || newLevel == DonorReadinessLevel.EMERGENCY_FLEXIBLE) {
            UrgentDonorRegistry entry = urgentDonorRegistryRepo.findByDonor(user).orElse(null);
            if (entry == null) {
                entry = new UrgentDonorRegistry();
                entry.setDonor(user);
                entry.setRegisteredAt(LocalDateTime.now());
            }
            entry.setReadinessLevel(newLevel);
            entry.setIsAvailable(true);
            urgentDonorRegistryRepo.save(entry);
        } else {
            urgentDonorRegistryRepo.findByDonor(user).ifPresent(reg -> {
                reg.setIsAvailable(false);
                urgentDonorRegistryRepo.save(reg);
            });
        }
    }
}
