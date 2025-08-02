package com.quyet.superapp.controller;

import com.quyet.superapp.dto.DashboardOverviewDTO;
import com.quyet.superapp.dto.DashboardResponseDTO;
import com.quyet.superapp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DashboardController {

//    private final DashboardService dashboardService;
//
//    @GetMapping
//    public ResponseEntity<DashboardResponseDTO> getDashboardStats() {
//        return ResponseEntity.ok(dashboardService.getDashboardStats());
//    }

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewDTO> getOverview() {
        return ResponseEntity.ok(dashboardService.getDashboardOverview());
    }
}
