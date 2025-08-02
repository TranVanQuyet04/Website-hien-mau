package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ApheresisMachineDTO;
import com.quyet.superapp.service.ApheresisMachineService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/apheresis-machines")
public class ApheresisMachineController {

    private final ApheresisMachineService apheresisMachineService;

    // ✅ Lấy danh sách tất cả máy
    @GetMapping
    public ResponseEntity<List<ApheresisMachineDTO>> getAll() {
        return ResponseEntity.ok(apheresisMachineService.getAll());
    }

    // ✅ Lấy chi tiết máy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApheresisMachineDTO> getById(@PathVariable Long id) {
        return apheresisMachineService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo máy mới
    @PostMapping
    public ResponseEntity<ApheresisMachineDTO> create(@RequestBody ApheresisMachineDTO dto) {
        return ResponseEntity.ok(apheresisMachineService.create(dto));
    }

    // ✅ Cập nhật máy
    @PutMapping("/{id}")
    public ResponseEntity<ApheresisMachineDTO> update(@PathVariable Long id,
                                                      @RequestBody ApheresisMachineDTO dto) {
        return ResponseEntity.ok(apheresisMachineService.update(id, dto));
    }

    // ✅ Xoá máy theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        apheresisMachineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Lấy danh sách máy đang hoạt động
    @GetMapping("/active")
    public ResponseEntity<List<ApheresisMachineDTO>> getActiveMachines() {
        return ResponseEntity.ok(apheresisMachineService.getActiveMachines());
    }

    // ✅ Lọc máy cần bảo trì trước ngày
    @GetMapping("/due-maintenance")
    public ResponseEntity<List<ApheresisMachineDTO>> getMachinesDueForMaintenance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate
    ) {
        return ResponseEntity.ok(apheresisMachineService.getMachinesDueForMaintenance(beforeDate));
    }

}
