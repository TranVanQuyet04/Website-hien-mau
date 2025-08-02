package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatLogDTO {
    private Long chatId;
    private Long userId;
    private String message;
    private String sender;
    private LocalDateTime createdAt;
}
