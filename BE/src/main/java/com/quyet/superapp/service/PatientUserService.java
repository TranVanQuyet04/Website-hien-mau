package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.BloodTypeRepository;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientUserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BloodTypeRepository bloodTypeRepo;

    // Tạo hoặc lấy lại user ẩn danh cho bệnh nhân mới
    @Transactional
    public User getOrCreatePatientUser(String name, String phone, String gender, Double weight, String bloodGroup) {

        // 1. Check xem đã có user theo phone?
        Optional<User> existing = userRepo.findByUsername(phone);
        if (existing.isPresent()) return existing.get();

        // 2. Tạo user mới
        User user = new User();
        user.setUsername(phone);
        user.setPassword("{noop}patient"); // dummy password, hoặc random
        user.setEmail("anonymous_" + System.currentTimeMillis() + "@hospital.local");
        user.setEnable(true);
        user.setCreatedAt(LocalDateTime.now());

        // 3. Gán role = MEMBER
        Role memberRole = roleRepo.findByName(String.valueOf(RoleEnum.MEMBER))
                .orElseThrow(() -> new RuntimeException("Role MEMBER không tồn tại"));
        user.setRole(memberRole);

        // 4. Gán userProfile
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(name);
        profile.setPhone(phone);
        profile.setGender(gender);
        profile.setWeight(weight);
        profile.setCreatedAt(LocalDateTime.now());

        // 5. Map nhóm máu từ "O+", "A-", v.v.
        BloodType bloodType = bloodTypeRepo.findByDescription(bloodGroup)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu: " + bloodGroup));
        profile.setBloodType(bloodType);

        user.setUserProfile(profile);

        return userRepo.save(user);
    }
}

