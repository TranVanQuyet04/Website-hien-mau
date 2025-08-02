package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.ChatLogDTO;
import com.quyet.superapp.entity.ChatLog;
import com.quyet.superapp.entity.User;

import java.time.LocalDateTime;

public class ChatLogMapper {

    public static ChatLogDTO toDTO(ChatLog log) {
        return new ChatLogDTO(
                log.getChatId(),
                log.getUser() != null ? log.getUser().getUserId() : null,
                log.getMessage(),
                log.getSender(),
                log.getCreatedAt()
        );
    }

    public static ChatLog toEntity(ChatLogDTO dto, User user) {
        ChatLog log = new ChatLog();
        log.setChatId(dto.getChatId());
        log.setUser(user);
        log.setMessage(dto.getMessage());
        log.setSender(dto.getSender());
        log.setCreatedAt(
                dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now()
        );
        return log;
    }
}
