package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EmailLogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "subject", columnDefinition = "NVARCHAR(255)")
    private String subject;

    @Column(name = "body", columnDefinition = "NTEXT")
    private String body;

    @Column(name = "type", columnDefinition = "NVARCHAR(50)")
    private String type;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "status", columnDefinition = "VARCHAR(20)")
    private String status; // SUCCESS, FAILED

}
