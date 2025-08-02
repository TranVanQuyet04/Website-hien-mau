package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodRequestConfirmDTO;
import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.repository.UrgentDonorRegistryRepository;
import com.quyet.superapp.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StaffRequestController {

    private final BloodRequestService service;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepo;

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> confirmReceived(@RequestBody BloodRequestConfirmDTO dto) {
        BloodRequest confirmed = service.confirmReceivedVolume(dto.getRequestId(), dto.getConfirmedVolumeMl());
        return ResponseEntity.ok(BloodRequestMapper.toDTO(confirmed));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }




}
