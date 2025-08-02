package com.quyet.superapp.controller;

import com.quyet.superapp.service.UrgentDonorRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/verify-donors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')") // ✅ áp dụng cho toàn controller
public class StaffVerifyDonorController {

    private final UrgentDonorRegistryService urgentDonorService;




    /**
     * Lấy danh sách người hiến máu cần xác minh
     */
    @GetMapping
    public ResponseEntity<?> getUnverifiedDonors() {
        return ResponseEntity.ok(urgentDonorService.getUnverifiedDonors());
    }

    /**
     * Xác minh người hiến máu
     */
    @PutMapping("/{id}/verify")
    public ResponseEntity<?> verifyDonor(@PathVariable Long id) {
        urgentDonorService.verifyDonor(id);
        return ResponseEntity.ok("✅ Đã xác minh người hiến máu thành công");
    }

    /**
     * Từ chối/xoá người đăng ký không hợp lệ
     */
    @DeleteMapping("/{id}/reject")
    public ResponseEntity<?> rejectDonor(@PathVariable Long id) {
        try {
            urgentDonorService.rejectDonor(id);
            return ResponseEntity.ok("❌ Đã xoá người hiến máu chưa xác minh");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("⚠️ Người hiến máu đã được xác minh, không thể xoá");
        }
    }

    @GetMapping("/ready")
    public ResponseEntity<?> getVerifiedReadyDonors() {
        return ResponseEntity.ok(urgentDonorService.getVerifiedUrgentDonorsReady());
    }

}
