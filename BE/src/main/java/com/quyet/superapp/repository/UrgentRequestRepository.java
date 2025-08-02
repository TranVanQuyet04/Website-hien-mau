package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentRequest;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrgentRequestRepository extends JpaRepository<UrgentRequest, Long> {

    Optional<UrgentRequest> findByRequester(User user); // ✅ OK


    // ✅ Lọc theo trạng thái
    List<UrgentRequest> findByStatus(BloodRequestStatus status);

    // ✅ Lọc theo người gửi
    List<UrgentRequest> findByRequesterUserId(Long userId);

    // ✅ Thống kê theo trạng thái (ví dụ: Pending bao nhiêu cái)
    long countByStatus(BloodRequestStatus status); // ⚠ Đổi String → enum

    // 🔍 Tìm tất cả yêu cầu PENDING để admin duyệt
    List<UrgentRequest> findAllByStatus(BloodRequestStatus status);
}
