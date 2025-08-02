package com.quyet.superapp.entity;

import com.quyet.superapp.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VnPayPayments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Payment_Id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Request_Id")
    private BloodRequest bloodRequest;

    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "Payment_Time", columnDefinition = "DATETIME")
    private LocalDateTime paymentTime;

    @Column(name = "Transaction_Code", columnDefinition = "NVARCHAR(100)")
    private String transactionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private PaymentStatus status;
}
