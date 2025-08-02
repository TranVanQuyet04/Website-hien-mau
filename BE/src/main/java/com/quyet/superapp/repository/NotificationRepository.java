package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser_UserId(Long userId); // ✅ Tìm theo người nhận

    List<Notification> findByUser_UserIdAndIsReadFalse(Long userId); // 🔔 Thông báo chưa đọc

    List<Notification> findByUserUserIdOrderBySentAtDesc(Long userId);
}
