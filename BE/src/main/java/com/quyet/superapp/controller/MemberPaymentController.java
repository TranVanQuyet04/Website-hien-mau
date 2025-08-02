package com.quyet.superapp.controller;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.VnPayment;
import com.quyet.superapp.enums.PaymentStatus;
import com.quyet.superapp.mapper.VnPaymentMapper;
import com.quyet.superapp.repository.BloodRequestRepository;
import com.quyet.superapp.repository.VnPaymentRepository;
import com.quyet.superapp.service.VnPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class MemberPaymentController {

    private final VnPaymentService paymentService;
    private final BloodRequestRepository bloodRequestRepository;
    private final VnPaymentRepository vnPaymentRepository;

    // ❗ Nếu dùng static mapper thì không cần inject
    // Nếu bạn dùng @Component trong VnPaymentMapper thì inject vào đây
    private final VnPaymentMapper paymentMapper;

    /**
     * 🔍 Lấy thông tin tất cả giao dịch theo ID đơn yêu cầu máu
     */
    @GetMapping("/by-request/{requestId}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getPaymentInfoByRequest(@PathVariable Long requestId) {
        var payments = vnPaymentRepository.findAllByBloodRequest_Id(requestId);

        if (payments.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "❌ Không tìm thấy giao dịch nào cho đơn này."));
        }

        var result = payments.stream()
                .map(paymentMapper::toDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    /**
     * 💰 Thực hiện thanh toán đơn yêu cầu máu
     */
    @PostMapping("/pay")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> payForRequest(@RequestParam Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy đơn yêu cầu máu."));

        BigDecimal amount = BigDecimal.valueOf(request.getTotalAmount() != null ? request.getTotalAmount() : 0);

        VnPayment payment = new VnPayment();
        payment.setBloodRequest(request);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setAmount(amount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionCode("TXN-" + System.currentTimeMillis());
        payment.setUser(request.getRequester()); // ✅ Có thể thay bằng currentUser nếu tích hợp SecurityContext
        payment.setUser(request.getRequester()); // hoặc lấy từ SecurityContext
        vnPaymentRepository.save(payment);

        return ResponseEntity.ok(Map.of("message", "✅ Thanh toán đơn yêu cầu máu thành công."));
    }
}
