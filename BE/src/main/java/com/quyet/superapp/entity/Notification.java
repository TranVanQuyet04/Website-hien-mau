package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NotificationId")
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", columnDefinition = "NVARCHAR(200)")
    private String content;

    @Column(name = "sent_at", columnDefinition = "DATETIME")
    private LocalDateTime sentAt;

    @Column(name = "[read]") // vẫn để SQL nhận đúng tên cột
    private Boolean isRead;  // Java field đổi cho rõ nghĩa

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
