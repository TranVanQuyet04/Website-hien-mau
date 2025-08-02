package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodInventory;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.service.BloodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-inventory")
@RequiredArgsConstructor
public class BloodInventoryController {

    private final BloodService bloodService;

    @GetMapping
    public List<BloodInventoryDTO> getAll() {
        return bloodService.getAllInventoryDTO();
    }

    @GetMapping("/status/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodInventoryAlertDTO>> getAllInventoryStatus() {
        return ResponseEntity.ok(bloodService.getAllInventoryStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodInventory> getById(@PathVariable Long id) {
        return bloodService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public BloodInventory add(@RequestBody BloodInventory inventory) {
        return bloodService.addBlood(inventory);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BloodInventory> update(@PathVariable Long id, @RequestBody BloodInventory inventory) {
        BloodInventory updated = bloodService.updateBlood(id, inventory);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloodService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<BloodInventory> searchByTypeAndComponent(
            @RequestParam Long bloodTypeId,
            @RequestParam Long componentId
    ) {
        BloodType type = new BloodType();
        type.setBloodTypeId(bloodTypeId);
        BloodComponent component = new BloodComponent();
        component.setBloodComponentId(componentId);

        return bloodService.searchBloodByTypeAndComponent(type, component)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/store/{bloodUnitId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Void> storeBlood(@PathVariable Long bloodUnitId) {
        bloodService.storeBloodUnit(bloodUnitId);
        return ResponseEntity.ok().build();
    }
}
