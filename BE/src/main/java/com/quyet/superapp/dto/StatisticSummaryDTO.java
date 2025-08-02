package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticSummaryDTO {
    private Long totalDonors;           // Tổng số người hiến máu
    private Long totalDonations;        // Tổng số lượt hiến
    private LocalDateTime lastDonation; // Ngày hiến gần nhất
    private String mostCommonBloodType; // Nhóm máu phổ biến nhất (A+, O-, ...)
}
