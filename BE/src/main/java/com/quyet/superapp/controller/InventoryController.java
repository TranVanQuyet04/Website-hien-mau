package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.repository.BloodInventoryRepository;
import com.quyet.superapp.scheduler.InventoryAlertScheduler;
import com.quyet.superapp.service.BloodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryAlertScheduler inventoryAlertScheduler;
    private final BloodService bloodService;
    private final BloodInventoryRepository bloodInventoryRepository;

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions() {
        return ResponseEntity.ok(bloodService.suggestBloodDonation());
    }


    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> getInventorySummary() {
        List<Object[]> results = bloodInventoryRepository.findGroupCounts();
        List<Map<String, Object>> data = results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("bloodType", row[0]);
            map.put("quantity", row[1]);
            return map;
        }).toList();
        return ResponseEntity.ok(data);
    }


    @GetMapping("/alerts")
    public ResponseEntity<List<BloodInventoryAlertDTO>> getAllAlerts() {
        return ResponseEntity.ok(bloodService.getAllInventoryStatus());
    }

    // ✅ Gọi thủ công cảnh báo tồn kho
    @PostMapping("/trigger")
    public ResponseEntity<?> triggerAlert() {
        inventoryAlertScheduler.autoCheckInventoryAlert(); // gọi thủ công
        return ResponseEntity.ok("Đã kiểm tra cảnh báo tồn kho thủ công.");
    }
}
