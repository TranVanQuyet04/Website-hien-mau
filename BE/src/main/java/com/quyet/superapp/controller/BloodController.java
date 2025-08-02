package com.quyet.superapp.controller;

import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.service.BloodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blood")
@RequiredArgsConstructor
public class BloodController {

    private final BloodService bloodService;

    // --- BLOOD INVENTORY ---

    @GetMapping("/inventory")
    public List<BloodInventory> getAllInventory() {
        return bloodService.getInventory();
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<BloodInventory> getInventoryById(@PathVariable Long id) {
        return bloodService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inventory/create")
    public BloodInventory createInventory(@RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<BloodInventory> updateInventory(@PathVariable Long id, @RequestBody BloodInventory updated) {
        BloodInventory result = bloodService.updateBlood(id, updated);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/inventory/{id}")
    public void deleteInventory(@PathVariable Long id) {
        bloodService.deleteInventory(id);
    }
}
