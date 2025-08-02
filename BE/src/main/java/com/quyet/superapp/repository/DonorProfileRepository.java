package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonorProfile;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DonorProfileRepository extends JpaRepository<DonorProfile, Long> {
    Optional<DonorProfile> findByUser(User user);
    // ✅ Dùng đúng cú pháp nested:
    Optional<DonorProfile> findByUser_UserId(Long userId);



}
