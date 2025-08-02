package com.quyet.superapp.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ChartDataDTO {
    private Map<String, Integer> bloodGroupCounts;      // Bar Chart: Nhóm máu
    private Map<String, Double> componentRatios;        // Pie Chart: Thành phần máu
    private List<ReadinessStackedDTO> readinessStacks;  // Stacked Column
    private Map<String, Integer> rejectionReasons;
    private Map<String, Integer> recoveryTimeChart;

}

