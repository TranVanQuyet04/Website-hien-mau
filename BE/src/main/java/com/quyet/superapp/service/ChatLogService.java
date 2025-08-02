package com.quyet.superapp.service;

import com.quyet.superapp.dto.ChatLogDTO;
import com.quyet.superapp.entity.ChatLog;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.ChatLogMapper;
import com.quyet.superapp.repository.ChatLogRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatLogService {

    private final ChatLogRepository chatLogRepository;
    private final UserRepository userRepository;

    public List<ChatLogDTO> getAll() {
        return chatLogRepository.findAll().stream()
                .map(ChatLogMapper::toDTO)
                .collect(Collectors.toList());
    }


    public List<ChatLogDTO> getByUserId(Long userId) {
        return chatLogRepository.findByUserUserId(userId).stream()
                .map(ChatLogMapper::toDTO)
                .collect(Collectors.toList());

    }

    public Optional<ChatLogDTO> getById(Long id) {
        return chatLogRepository.findById(id)
                .map(ChatLogMapper::toDTO);
    }

    public ChatLogDTO create(ChatLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + dto.getUserId()));
        ChatLog log = ChatLogMapper.toEntity(dto, user);
        log.setCreatedAt(LocalDateTime.now());
        return ChatLogMapper.toDTO(chatLogRepository.save(log));
    }

    public void delete(Long id) {
        chatLogRepository.deleteById(id);
    }
}
