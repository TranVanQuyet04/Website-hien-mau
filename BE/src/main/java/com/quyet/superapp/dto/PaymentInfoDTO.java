package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoDTO {

    private Long bloodRequestId;               // Mã đơn yêu cầu máu

    private String patientName;                // Tên bệnh nhân
    private String componentName;              // Tên thành phần máu
    private String bloodTypeName;              // Nhóm máu
    private Integer quantityBag;               // Số lượng túi
    private Integer unitPrice;                 // Đơn giá (1 túi)
    private Integer totalAmount;               // Thành tiền = quantity * unitPrice

    private String paymentStatus;              // UNPAID, PAID, DEFERRED, WAIVED
    private Boolean deferredPayment;           // Nếu có trả sau
    private String deferredPaymentReason;      // Lý do trả sau (nếu có)

    private LocalDateTime approvedAt;          // Ngày duyệt yêu cầu
    private LocalDateTime deadline;            // Hạn thanh toán (nếu có)

    private String requesterName;              // Nhân viên gửi yêu cầu
    private String doctorName;                 // Bác sĩ phụ trách
}
