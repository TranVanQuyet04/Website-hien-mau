package com.quyet.superapp.controller;

import com.quyet.superapp.entity.Notification;
import com.quyet.superapp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    // 📬 Gửi thông báo đến user cụ thể từ API
    @PostMapping("/send-to-user/{userId}")
    public ResponseEntity<String> sendToUser(
            @PathVariable Long userId,
            @RequestParam String message,
            @RequestParam(required = false) String redirectUrl) {
        service.sendNotification(userId, message, redirectUrl);
        return ResponseEntity.ok("Đã gửi thông báo đến user " + userId);
    }

    @PutMapping("/mark-as-read/{id}")
    public ResponseEntity<String> markAsRead(@PathVariable Long id) {
        Notification n = service.getById(id).orElse(null);
        if (n == null) return ResponseEntity.notFound().build();

        n.setIsRead(true);
        service.update(id, n);
        return ResponseEntity.ok("Đã đánh dấu là đã đọc");
    }


    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Lấy tất cả thông báo theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getByUserId(userId));
    }

    // ✅ Lấy các thông báo CHƯA ĐỌC theo user
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUnreadByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification notification) {
        return ResponseEntity.ok(service.create(notification));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> update(@PathVariable Long id, @RequestBody Notification notification) {
        Notification updated = service.update(id, notification);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
