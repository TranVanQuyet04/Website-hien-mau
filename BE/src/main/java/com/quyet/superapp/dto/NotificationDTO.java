package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead; // ✅ đổi tên cho đồng nhất với entity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
