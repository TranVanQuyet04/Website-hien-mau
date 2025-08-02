package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.EmailType;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test-email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @GetMapping("/send-to-gmail")
    public ResponseEntity<String> sendToGmail() {
        String to = "tranquyetcoder@gmail.com";
        String subject = "📢 Thử nghiệm gửi email tới Gmail cá nhân";
        String body = """
                <h3>Xin chào bạn Quyết!</h3>
                <p>Đây là email test được gửi từ hệ thống hỗ trợ hiến máu.</p>
                <p><b>Thời gian:</b> %s</p>
                <p>Trân trọng,<br>Hệ thống Hiến Máu</p>
                """.formatted(LocalDateTime.now());

        emailService.sendSimpleMessage(to, subject, body);
        return ResponseEntity.ok("✅ Đã gửi email đến " + to);
    }

    // ✅ Gửi email xác minh đơn giản
    @PostMapping("/verify/{userId}")
    public ResponseEntity<String> sendVerifyEmail(@PathVariable Long userId) {
        User user = findUser(userId);

        String subject = "Xác minh thành công";
        String body = """
                Xin chào %s,

                Bạn đã được xác minh là người hiến máu khẩn cấp.
                Xin cảm ơn sự đóng góp của bạn.

                Trân trọng,
                Hệ thống hỗ trợ hiến máu.
                """.formatted(user.getUserProfile().getFullName());

        emailService.sendEmail(user, subject, body, "VERIFY");
        return ResponseEntity.ok("✅ Đã gửi email xác minh đến: " + user.getEmail());
    }

    // ✅ Gửi email cảnh báo tồn kho
    @PostMapping("/alert/{userId}")
    public ResponseEntity<String> sendInventoryAlertEmail(
            @PathVariable Long userId,
            @RequestBody List<BloodInventoryAlertDTO> alerts
    ) {
        User user = findUser(userId);
        emailService.sendInventoryAlertSummaryEmail(user, alerts);
        return ResponseEntity.ok("✅ Đã gửi cảnh báo kho máu tới: " + user.getEmail());
    }

    // ✅ Gửi email tùy loại với dữ liệu mẫu
    @PostMapping("/custom-email/{type}")
    public ResponseEntity<String> sendCustomEmailToRawEmail(
            @PathVariable EmailType type,
            @RequestParam String toEmail
    ) {
        Object data = mockDataFor(type);

        String subject = "📢 " + type.name();
        String content = "📩 Nội dung test với data:\n\n" + data;

        emailService.sendSimpleMessage(toEmail, subject, content);
        return ResponseEntity.ok("✅ Đã gửi email loại `" + type + "` tới: " + toEmail);
    }


    // ✅ Hàm dùng chung lấy user
    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy người dùng với ID: " + userId));
    }

    // ✅ Hàm mock dữ liệu theo EmailType
    private Object mockDataFor(EmailType type) {
        return switch (type) {
            case INVENTORY_ALERT_FOR_REQUEST -> {
                BloodInventoryAlertDTO alert = new BloodInventoryAlertDTO();
                alert.setBloodType("A+");
                alert.setComponent("Hồng cầu");
                alert.setQuantityMl(120);
                alert.setMinThresholdMl(300);
                alert.setCriticalThresholdMl(500);
                alert.setAlertLevel("CRITICAL");
                yield List.of(alert);
            }
            case BLOOD_REQUEST_APPROVED, BLOOD_REQUEST_REJECTED, PAYMENT_REQUESTED, PAYMENT_CONFIRMATION -> mockBloodRequest();
            case DONATION_REMINDER -> null; // Không cần data (nội dung tĩnh)
            default -> null;
        };
    }

    // ✅ Mẫu đơn yêu cầu máu giả lập
    private BloodRequest mockBloodRequest() {
        // 🧑‍🦱 Tạo bệnh nhân giả
        Patient patient = new Patient();
        patient.setFullName("Nguyễn Văn A");
        // Bạn có thể gán thêm ID giả nếu cần:
        patient.setId(100L);

        // 🩸 Tạo đơn truyền máu giả
        BloodRequest req = new BloodRequest();
        req.setId(1L); // ✅ Không dùng setBloodRequestId
        req.setPatient(patient); // ✅ Không có setPatientName
        req.setQuantityBag(2);
        req.setRequestCode("BR-20250711-001");
        req.setTotalAmount(570000);
        req.setCancelReason("Không đạt yêu cầu");
        req.setApprovedAt(LocalDateTime.now());

        // 🧬 Thành phần máu
        BloodComponent comp = new BloodComponent();
        comp.setName("Hồng cầu");
        req.setComponent(comp);

        // 🧪 Nhóm máu
        BloodType type = new BloodType();
        type.setDescription("O+");
        req.setBloodType(type);

        return req;
    }

}
