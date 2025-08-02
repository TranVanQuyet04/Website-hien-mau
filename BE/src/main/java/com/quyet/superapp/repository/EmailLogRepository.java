package com.quyet.superapp.repository;

import com.quyet.superapp.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findByUser_UserId(Long userId);

    List<EmailLog> findByStatusIgnoreCase(String status);

    List<EmailLog> findByTypeIgnoreCase(String type);

    List<EmailLog> findBySentAtBetween(LocalDateTime start, LocalDateTime end);
}
