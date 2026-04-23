package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ChatLogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Chat_Id")
    private Long chatId;


    @ManyToOne(optional = true)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "message", length = 100)
    private String message;

    @Column(name = "sender", length = 20)
    private String sender;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
}
