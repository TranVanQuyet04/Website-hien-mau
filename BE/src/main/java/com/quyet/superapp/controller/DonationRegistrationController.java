package com.quyet.superapp.controller;

import com.quyet.superapp.dto.DonationRegistrationDTO;
import com.quyet.superapp.dto.HealthCheckFailureLogDTO;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.service.DonationRegistrationService;
import com.quyet.superapp.service.HealthCheckFailureLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/donation")
@RequiredArgsConstructor
public class DonationRegistrationController {

    private final DonationRegistrationService donationRegistrationService;
    private final HealthCheckFailureLogService healthCheckFailureLogService;

    // ✅ Đăng ký hiến máu (chỉ đặt lịch, chưa xác nhận)
    @PostMapping("/register/{userId}")
    public ResponseEntity<DonationRegistrationDTO> registerDonation(
            @PathVariable Long userId,
            @RequestBody DonationRegistrationDTO dto) {
        return ResponseEntity.ok(donationRegistrationService.register(userId, dto));
    }

    // ✅ Lấy tất cả đơn đăng ký hiến máu
    @GetMapping
    public ResponseEntity<List<DonationRegistrationDTO>> getAll() {
        return ResponseEntity.ok(donationRegistrationService.getAllDTO());
    }

    // ✅ Lấy đơn đăng ký theo ID
    @GetMapping("/{id}")
    public ResponseEntity<DonationRegistrationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(donationRegistrationService.getDTOById(id));
    }

    // ✅ Xác nhận đơn đăng ký (chuyển trạng thái -> CONFIRMED)
    @PutMapping("/confirm")
    public ResponseEntity<DonationRegistrationDTO> confirmRegistration(
            @RequestParam("register_id") Long id) {
        return ResponseEntity.ok(donationRegistrationService.confirm(id));
    }

    //thêm unhappycase
    @GetMapping("/pending")
    public List<DonationRegistrationDTO> getPendingRegistrations() {
        return donationRegistrationService.getByStatus(DonationStatus.PENDING);
    }


    // ❌ Đánh dấu đơn không đủ điều kiện sức khỏe và ghi log
    @PutMapping("/fail-health")
    public ResponseEntity<DonationRegistrationDTO> failDueToHealth(
            @RequestParam("register_id") Long id,
            @RequestParam("reason") String reason,
            @RequestParam(value = "staff_note", required = false) String staffNote) {
        return ResponseEntity.ok(donationRegistrationService.markAsFailedHealth(id, reason, staffNote));
    }

    // ✅ Lấy danh sách log kiểm tra sức khỏe không đạt theo ID đăng ký
    @GetMapping("/health-log")
    public ResponseEntity<List<HealthCheckFailureLogDTO>> getHealthLogByRegistration(@RequestParam("register_id") Long registrationId) {
        return ResponseEntity.ok(healthCheckFailureLogService.getLogsByRegistrationId(registrationId));
    }

    @PostMapping("/confirm-donation")
    public ResponseEntity<String> confirmDonation(@RequestParam("register_id") Long regId) {
        donationRegistrationService.createDonationIfEligible(regId);
        return ResponseEntity.ok("Đã tạo bản ghi hiến máu thành công.");
    }


    // ✅ Lấy lịch sử hiến máu theo userId
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<DonationRegistrationDTO>> getHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(donationRegistrationService.getByUserId(userId));
    }
    @PutMapping("/mark-donated")
    public ResponseEntity<DonationRegistrationDTO> markAsDonated(@RequestParam("register_id") Long id) {
        return ResponseEntity.ok(donationRegistrationService.markAsDonated(id));
    }
    @PutMapping("/cancel")
    public ResponseEntity<DonationRegistrationDTO> cancelRegistration(@RequestParam("register_id") Long id) {
        return ResponseEntity.ok(donationRegistrationService.cancel(id));
    }
    @GetMapping("/check-slot")
    public ResponseEntity<Boolean> checkSlotFull(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("slot_id") Long slotId) {
        boolean full = donationRegistrationService.isSlotFull(date, slotId);
        return ResponseEntity.ok(full);
    }

}
