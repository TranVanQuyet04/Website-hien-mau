package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.dto.UpdateBloodBagRequest;
import com.quyet.superapp.service.BloodBagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blood-bags")
@RequiredArgsConstructor
public class BloodBagController {

    private final BloodBagService bloodBagService;

    // ✅ Lấy danh sách tất cả túi máu
    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<BloodBagDTO>> getAll() {
        return ResponseEntity.ok(bloodBagService.getAll());
    }

    // ✅ Lấy thông tin túi máu theo ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<BloodBagDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bloodBagService.getById(id));
    }

    // ✅ Tạo mới túi máu
    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<BloodBagDTO> create(@Valid @RequestBody BloodBagDTO dto) {
        BloodBagDTO created = bloodBagService.create(dto);
        return ResponseEntity.ok(created);
    }

    // ✅ Cập nhật một phần túi máu (volume, hematocrit, status...)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<BloodBagDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBloodBagRequest req) {
        return ResponseEntity.ok(bloodBagService.update(id, req));
    }

    // ✅ Xoá túi máu
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bloodBagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Tìm theo mã túi máu
    @GetMapping("/find-by-code")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<BloodBagDTO> findByCode(@RequestParam String code) {
        Optional<BloodBagDTO> result = bloodBagService.findByCode(code);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/by-registration")
    public ResponseEntity<List<BloodBagDTO>> getByRegistrationId(@RequestParam Long registrationId) {
        List<BloodBagDTO> result = bloodBagService.getByRegistrationId(registrationId);
        return ResponseEntity.ok(result);
    }



}
