package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ChatLogDTO;
import com.quyet.superapp.service.ChatLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatlogs")
@RequiredArgsConstructor
public class ChatLogController {

    private final ChatLogService chatLogService;

    @GetMapping
    public List<ChatLogDTO> getAll() {
        return chatLogService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatLogDTO> getById(@PathVariable Long id) {
        return chatLogService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<ChatLogDTO> getByUserId(@PathVariable Long userId) {
        return chatLogService.getByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<ChatLogDTO> create(@RequestBody ChatLogDTO dto) {
        try {
            return ResponseEntity.ok(chatLogService.create(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chatLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
