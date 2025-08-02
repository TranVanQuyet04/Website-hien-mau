package com.quyet.superapp.controller;

import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.DonationHistoryMapper;
import com.quyet.superapp.repository.DonationHistoryRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donation-history")
@RequiredArgsConstructor
public class DonationHistoryController {

    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationHistoryMapper donationHistoryMapper;
    private final UserRepository userRepository;

    // ✅ Lấy tất cả lịch sử hiến máu của 1 người dùng (member đang đăng nhập hoặc staff xem)
    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN', 'MEMBER')")
    public ResponseEntity<List<DonationHistoryDTO>> getHistoryByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<DonationHistoryDTO> dtoList = donationHistoryMapper.toDTOs(
                donationHistoryRepository.findByDonor(user)
        );
        return ResponseEntity.ok(dtoList);
    }

    // ✅ Lấy toàn bộ lịch sử hiến máu (dành cho admin xem tổng thể)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DonationHistoryDTO>> getAllHistories() {
        List<DonationHistoryDTO> dtoList = donationHistoryMapper.toDTOs(donationHistoryRepository.findAll());
        return ResponseEntity.ok(dtoList);
    }
}
