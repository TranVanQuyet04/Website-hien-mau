package com.quyet.superapp.controller;

import com.quyet.superapp.entity.EmailLog;
import com.quyet.superapp.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/email-logs")
@RequiredArgsConstructor
public class EmailLogController {

    private final EmailLogRepository emailLogRepository;

    // ✅ 1. Lấy tất cả hoặc theo userId
    @GetMapping
    public ResponseEntity<List<EmailLog>> getAllLogs(@RequestParam(required = false) Long userId) {
        List<EmailLog> logs = (userId == null)
                ? emailLogRepository.findAll()
                : emailLogRepository.findByUser_UserId(userId);
        return ResponseEntity.ok(logs);
    }

    // ✅ 2. Lọc theo trạng thái gửi (SUCCESS / FAILED)
    @GetMapping("/status")
    public ResponseEntity<List<EmailLog>> getByStatus(@RequestParam String status) {
        return ResponseEntity.ok(emailLogRepository.findByStatusIgnoreCase(status));
    }

    // ✅ 3. Lọc theo loại email (ALERT, INVENTORY_ALERT, etc.)
    @GetMapping("/type")
    public ResponseEntity<List<EmailLog>> getByType(@RequestParam String type) {
        return ResponseEntity.ok(emailLogRepository.findByTypeIgnoreCase(type));
    }

    // ✅ 4. Lọc theo khoảng thời gian gửi
    @GetMapping("/date-range")
    public ResponseEntity<List<EmailLog>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(emailLogRepository.findBySentAtBetween(start, end));
    }
}
