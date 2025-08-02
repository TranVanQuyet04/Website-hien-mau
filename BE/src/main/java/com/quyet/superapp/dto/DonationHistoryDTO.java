package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonationHistoryDTO {
    private Long id;

    private String donorName; // 👈 Gợi ý thêm nếu bạn muốn hiển thị tên người hiến

    private String bloodType;
    private String componentDonated;
    private Integer volumeMl;
    private String donationLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime donatedAt;

    private String status;

    private Boolean paymentRequired;
    private Boolean paymentCompleted;
    private Integer paymentAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    private String paymentMethod;
    private String transactionCode;
}
