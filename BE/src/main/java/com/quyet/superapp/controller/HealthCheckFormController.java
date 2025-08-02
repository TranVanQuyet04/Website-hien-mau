package com.quyet.superapp.controller;

import com.quyet.superapp.dto.HealthCheckFormDTO;
import com.quyet.superapp.service.HealthCheckFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/health-check")
@RequiredArgsConstructor
@Validated
public class HealthCheckFormController {

    private final HealthCheckFormService healthCheckFormService;

    // ✅ Gửi phiếu khám sức khỏe (tự động đánh giá pass/fail)
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<HealthCheckFormDTO> submit(@Valid @RequestBody HealthCheckFormDTO dto) {
        HealthCheckFormDTO result = healthCheckFormService.submit(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy phiếu theo registrationId
    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<HealthCheckFormDTO> getByRegistrationId(@RequestParam("registrationId") Long regId) {
        return ResponseEntity.ok(healthCheckFormService.getByRegistrationId(regId));
    }

    // ✅ Cập nhật lại phiếu khám (nếu cho phép sửa)
    @PutMapping("/update")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<HealthCheckFormDTO> update(@RequestBody HealthCheckFormDTO dto) {
        return ResponseEntity.ok(healthCheckFormService.update(dto));
    }

    // ✅ Lấy hoặc tạo mới phiếu khám nếu chưa có
    @GetMapping("/get-or-create")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<HealthCheckFormDTO> getOrCreate(@RequestParam("registrationId") Long regId) {
        return ResponseEntity.ok(healthCheckFormService.getOrCreateByRegistrationId(regId));
    }
}
