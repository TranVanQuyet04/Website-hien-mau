package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 🧮 1. Tổng quan hệ thống
    @PostMapping("/summary")
    public ResponseEntity<StatisticSummaryDTO> getSummary(@RequestBody StatisticFilterDTO filter) {
        return ResponseEntity.ok(statisticsService.getSummary(filter));
    }

    // 📊 2. KPI tóm tắt
    @PostMapping("/kpi")
    public ResponseEntity<SummaryKPIDTO> getKPI(@RequestBody StatisticFilterDTO filter) {
        return ResponseEntity.ok(statisticsService.getSummaryKPI(filter));
    }


    // 📈 3. Dữ liệu biểu đồ tổng hợp
    @PostMapping("/chart-data")
    public ResponseEntity<ChartDataDTO> getChartData(@RequestBody StatisticFilterDTO filter) {
        return ResponseEntity.ok(statisticsService.getChartData(filter));
    }

    // 📋 4. Danh sách người hiến máu chi tiết (phân trang nếu cần)
    @PostMapping("/donors")
    public ResponseEntity<List<DonorDetailDTO>> getDonorDetails(@RequestBody StatisticFilterDTO filter) {
        return ResponseEntity.ok(statisticsService.getDonorDetails(filter));
    }
}
