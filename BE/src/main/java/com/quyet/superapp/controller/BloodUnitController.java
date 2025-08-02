package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.mapper.BloodUnitMapper;
import com.quyet.superapp.service.BloodUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/blood-units")
@RequiredArgsConstructor
public class BloodUnitController {

    private final BloodUnitService service;

    @GetMapping
    public ResponseEntity<List<BloodUnitDTO>> getAll() {
        List<BloodUnitDTO> result = service.getAll().stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-id")
    public ResponseEntity<BloodUnitDTO> getById(@RequestParam Long id) {
        BloodUnit unit = service.getById(id);
        return ResponseEntity.ok(BloodUnitMapper.toDTO(unit));
    }

    @PostMapping
    public ResponseEntity<BloodUnitDTO> create(@RequestBody BloodUnitDTO dto) {
        BloodUnit entity = BloodUnitMapper.fromDTO(dto);
        BloodUnit saved = service.save(entity, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }
    //cập nhật
    @PutMapping
    public ResponseEntity<BloodUnitDTO> update(@RequestParam  Long id, @RequestBody BloodUnitDTO dto) {
        BloodUnit existing = service.getById(id);
        BloodUnit updated = BloodUnitMapper.fromDTO(dto);
        updated.setBloodUnitId(id);
        BloodUnit saved = service.save(updated, dto.getBloodTypeId(), dto.getComponentId(), dto.getBloodBagId());
        return ResponseEntity.ok(BloodUnitMapper.toDTO(saved));
    }
    //xóa
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<BloodUnitDTO>> findByStatus(@RequestParam BloodUnitStatus status) {
        List<BloodUnitDTO> result = service.findByStatus(status).stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<BloodUnitDTO>> findExpiringBefore(
            @RequestParam("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BloodUnitDTO> result = service.findExpiringBefore(date).stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-code")
    public ResponseEntity<BloodUnitDTO> findByUnitCode(@RequestParam String code) {
        BloodUnit unit = service.findByUnitCode(code);
        return ResponseEntity.ok(BloodUnitMapper.toDTO(unit));
    }
    @GetMapping("/available/filter")
    public ResponseEntity<List<BloodUnitDTO>> getAvailableUnitsByComponent(
            @RequestParam Long componentId) {

        List<BloodUnit> units = service.getAvailableUnits(componentId);

        List<BloodUnitDTO> dtos = units.stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/mark-used")
    public ResponseEntity<BloodUnitDTO> markUnitAsUsed(@PathVariable Long id) {
        BloodUnit updated = service.markAsUsed(id);
        return ResponseEntity.ok(BloodUnitMapper.toDTO(updated));
    }
    @PutMapping("/{id}/mark-expired")
    public ResponseEntity<BloodUnitDTO> markUnitAsExpired(@PathVariable Long id) {
        BloodUnit updated = service.markAsExpired(id);
        return ResponseEntity.ok(BloodUnitMapper.toDTO(updated));
    }

}
