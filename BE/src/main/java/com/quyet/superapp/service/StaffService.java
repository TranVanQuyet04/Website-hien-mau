package com.quyet.superapp.service;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final UserRepository userRepository;

    public void updateStaffStatus(Long userId, boolean enable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        user.setEnable(enable);
        userRepository.save(user);
    }
}
