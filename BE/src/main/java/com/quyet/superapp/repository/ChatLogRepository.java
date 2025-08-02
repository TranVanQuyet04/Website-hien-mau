package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

    List<ChatLog> findByUserUserId(Long userId); // ✅ Đúng 100%

}