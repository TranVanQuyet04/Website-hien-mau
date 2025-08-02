package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.entity.VnPayment;
import com.quyet.superapp.enums.PaymentStatus;
import com.quyet.superapp.repository.BloodRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final VnPaymentService vnPaymentService;
    private final BloodRequestRepository bloodRequestRepository;

    public void processPayment(Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy yêu cầu máu"));

        // Lấy người nhận (member) từ yêu cầu truyền máu
        UserProfile recipientProfile = request.getRequester().getUserProfile();

        // ⚙️ Giá gốc cố định (có thể lấy từ bảng giá sau này)
        BigDecimal originalAmount = new BigDecimal("500000");

        // Kiểm tra BHYT còn hiệu lực
        boolean hasValidInsurance = recipientProfile != null && recipientProfile.isInsuranceStillValid();

        // Nếu có BHYT → giảm 80%, chỉ trả 20%
        BigDecimal discountRate = hasValidInsurance ? new BigDecimal("0.80") : BigDecimal.ZERO;
        BigDecimal discountAmount = originalAmount.multiply(discountRate);
        BigDecimal finalAmount = originalAmount.subtract(discountAmount);

        // Tạo thanh toán
        VnPayment payment = new VnPayment();
        payment.setBloodRequest(request);
        payment.setUser(request.getRequester()); // giả sử requester là người thanh toán
        payment.setAmount(finalAmount); // ✅ chỉ trả phần còn lại
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionCode("TXN-" + System.currentTimeMillis());
        payment.setStatus(PaymentStatus.SUCCESS);

        // (Optional) log hỗ trợ BHYT vào note / custom field nếu có
        // payment.setNote("Hỗ trợ BHYT 80%"); // nếu có field ghi chú

        vnPaymentService.save(payment);
    }

}
