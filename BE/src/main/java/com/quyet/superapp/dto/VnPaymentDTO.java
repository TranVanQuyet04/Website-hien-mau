package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPaymentDTO {
    private Long id;
    private Long requestId;           // ✅ ID của đơn yêu cầu máu
    private Long userId;              // 👤 ID của người thực hiện thanh toán (staff)
    private String userFullName;      // 👤 Tên staff (nếu muốn show rõ hơn)
    private BigDecimal amount;        // 💰 Số tiền
    private LocalDateTime paymentTime; // ⏰ Thời gian thanh toán
    private String transactionCode;   // 🔗 Mã giao dịch
    private String status;            // 📌 Trạng thái: SUCCESS, FAILED, ...
}
