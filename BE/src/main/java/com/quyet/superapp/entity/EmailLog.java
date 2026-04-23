package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_logs")
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

    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @Column(name = "subject", length = 255)
    private String subject;

    @Lob
    @Column(name = "body", columnDefinition = "LONGTEXT")
    private String body;

    @Column(name = "`type`", length = 50)
    private String type;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "`status`", length = 20)
    private String status; // SUCCESS, FAILED

}
