package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.VnPaymentDTO;
import com.quyet.superapp.entity.VnPayment;
import com.quyet.superapp.enums.PaymentStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper tiện ích để chuyển đổi giữa Entity và DTO cho giao dịch thanh toán VNPay
 */
@Component
public class VnPaymentMapper {

    public VnPaymentDTO toDTO(VnPayment payment) {
        return new VnPaymentDTO(
                payment.getPaymentId(),
                payment.getBloodRequest() != null ? payment.getBloodRequest().getId() : null,
                payment.getUser() != null ? payment.getUser().getUserId() : null,
                payment.getUser() != null && payment.getUser().getUserProfile() != null
                        ? payment.getUser().getUserProfile().getFullName()
                        : null,
                payment.getAmount(),
                payment.getPaymentTime(),
                payment.getTransactionCode(),
                payment.getStatus().name()
        );
    }

    public VnPayment toEntity(VnPaymentDTO dto) {
        VnPayment payment = new VnPayment();
        payment.setPaymentId(dto.getId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentTime(dto.getPaymentTime());
        payment.setTransactionCode(dto.getTransactionCode());
        payment.setStatus(PaymentStatus.valueOf(dto.getStatus()));
        return payment;
    }
}
