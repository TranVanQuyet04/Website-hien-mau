    package com.quyet.superapp.controller;
    
    import com.quyet.superapp.dto.UrgentRequestDTO;
    import com.quyet.superapp.enums.BloodRequestStatus;
    import com.quyet.superapp.service.UrgentRequestService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.*;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;
    
    import jakarta.validation.Valid;
    import java.util.List;
    
    @RestController
    @RequestMapping("/api/urgent-requests")
    @RequiredArgsConstructor
    public class UrgentRequestController {
        private final UrgentRequestService service;
    
        @PostMapping
        @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
        public ResponseEntity<UrgentRequestDTO> create(
                @Valid @RequestBody UrgentRequestDTO dto) {
            UrgentRequestDTO created = service.create(dto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(created);
        }
    
        @GetMapping
        @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
        public ResponseEntity<List<UrgentRequestDTO>> getAll() {
            return ResponseEntity.ok(service.getAll());
        }
    
        @GetMapping("/user/{userId}")
        @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
        public ResponseEntity<List<UrgentRequestDTO>> getByUser(
                @PathVariable Long userId) {
            return ResponseEntity.ok(service.getByUser(userId));
        }
    
        @GetMapping("/status/{status}")
        @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
        public ResponseEntity<List<UrgentRequestDTO>> getByStatus(
                @PathVariable String status) {
            BloodRequestStatus enumStatus = BloodRequestStatus.valueOf(status);
            return ResponseEntity.ok(service.getByStatus(enumStatus));
        }
    
        @PutMapping("/{id}/status")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<UrgentRequestDTO> updateStatus(
                @PathVariable Long id,
                @RequestParam String status) {
    
            BloodRequestStatus newStatus = BloodRequestStatus.valueOf(status);
            UrgentRequestDTO updated = service.updateStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        }
    
    
    }
