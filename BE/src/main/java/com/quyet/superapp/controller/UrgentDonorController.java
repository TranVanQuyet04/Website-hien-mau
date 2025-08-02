package com.quyet.superapp.controller;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.dto.*;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.service.DonorReadinessService;
import com.quyet.superapp.service.ReadinessChangeLogService;
import com.quyet.superapp.service.UrgentDonorRegistryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urgent-donors")
@RequiredArgsConstructor
public class UrgentDonorController {

    private final UrgentDonorRegistryService urgentDonorRegistryService;
    private final ReadinessChangeLogService readinessChangeLogService;
    private final DonorReadinessService donorReadinessService;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UrgentDonorRegistryService.class);

//    @PostMapping("/search")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<UrgentDonorMatchResultDTO>> searchDonors(@RequestBody UrgentDonorSearchRequestDTO dto) {
//        List<UrgentDonorMatchResultDTO> result = urgentDonorRegistryService.searchUrgentDonors(dto);
//        return ResponseEntity.ok(result);
//    }


    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UrgentDonorMatchResultDTO>> searchUrgentDonors(
            @RequestBody UrgentDonorSearchRequestDTO request) {
        List<UrgentDonorMatchResultDTO> results = urgentDonorRegistryService.searchUrgentDonors(request);
        return ResponseEntity.ok(results);
    }



    // UrgentDonorController.java
    @GetMapping("/search")
    public ResponseEntity<List<UrgentDonorSearchResultDTO>> searchDonors(
            @RequestParam Long bloodTypeId,
            @RequestParam Long componentId,
            @RequestParam(defaultValue = "20") double maxDistanceKm) {
        List<UrgentDonorSearchResultDTO> result = urgentDonorRegistryService.searchNearbyDonors(bloodTypeId, componentId, maxDistanceKm);
        return ResponseEntity.ok(result);
    }





    @PostMapping("/process-readiness")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponseDTO<String>> processReadiness(
            @RequestBody @Valid UrgentDonorRegistrationDTO dto,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        Long userId = currentUser.getUserId();
        DonorReadinessLevel level = dto.getReadinessLevel();
        String message = donorReadinessService.processReadiness(userId, level, dto);
        return ResponseEntity.ok(ApiResponseDTO.success(message));
    }

    @GetMapping("/nearby")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<NearbyDonorDTO>>> findNearbyDonors(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(required = false) Long bloodType,
            @RequestParam(defaultValue = "20.0") Double maxDistanceKm
    ) {
        log.info("\uD83D\uDCF1 Quét người hiến gần: lat={}, lng={}, bloodType={}, radius={}km", lat, lng, bloodType, maxDistanceKm);
        try {
            List<NearbyDonorDTO> results = urgentDonorRegistryService.findNearbyVerifiedDonors(lat, lng, bloodType, maxDistanceKm);
            String msg = results.isEmpty()
                    ? "\u274c Không tìm thấy người hiến máu phù hợp."
                    : "\u2705 Tìm thấy " + results.size() + " người phù hợp";
            return ResponseEntity.ok(ApiResponseDTO.success(results, msg));
        } catch (Exception e) {
            log.error("\u274c Lỗi khi tìm người hiến gần: " + e.getMessage(), e);
            return ResponseEntity.internalServerError().body(ApiResponseDTO.fail("Lỗi hệ thống"));
        }
    }

    @GetMapping("/nearby-hospital")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<NearbyDonorDTO>>> findNearbyDonorsNearHospital(
            @RequestParam(defaultValue = "5.0") double radiusKm
    ) {
        List<NearbyDonorDTO> results = urgentDonorRegistryService.findNearbyVerifiedDonors(radiusKm);
        return ResponseEntity.ok(ApiResponseDTO.success(results, "Gần bệnh viện: " + results.size() + " người"));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> getReadinessHistory(@AuthenticationPrincipal UserPrincipal principal) {
        Long userId = principal.getUserId();
        List<ReadinessChangeLogDTO> logs = readinessChangeLogService.getHistoryByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/current-status")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<CurrentUrgentDonorStatusDTO> getCurrentStatus(Authentication authentication) {
        Long userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        return ResponseEntity.ok(urgentDonorRegistryService.getCurrentUrgentStatus(userId));
    }

    @GetMapping("/check-readiness-conflict")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ReadinessConflictCheckDTO> checkReadinessConflict(
            @RequestParam DonorReadinessLevel newMode,
            Authentication authentication
    ) {
        Long userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        ReadinessConflictCheckDTO result = urgentDonorRegistryService.checkConflict(userId, newMode);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/confirm-change-mode")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> confirmChangeMode(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody DonorReadinessRequestDTO dto) {
        try {
            String message = urgentDonorRegistryService.forceChangeReadinessLevel(principal.getUserId(), dto.getReadinessLevel());
            if (message.startsWith("\u26a0\ufe0f")) {
                return ResponseEntity.badRequest().body(Map.of("message", message));
            }
            return ResponseEntity.ok(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> registerUrgentDonor(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody @Valid UrgentDonorRegistrationDTO dto) {
        Long userId = principal.getUserId();
        urgentDonorRegistryService.register(userId, dto.getReadinessLevel(), dto);
        return ResponseEntity.ok(ApiResponseDTO.success("\uD83E\uDDE8 Đăng ký hiến máu khẩn cấp thành công."));
    }

    @GetMapping("/unverified")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<UnverifiedDonorDTO>> getUnverified() {
        return ResponseEntity.ok(urgentDonorRegistryService.getUnverifiedDonors());
    }

    @PutMapping("/verify/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<?> verifyDonor(@PathVariable Long id) {
        urgentDonorRegistryService.verifyDonor(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reject/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<Void> rejectDonor(@PathVariable Long id) {
        urgentDonorRegistryService.rejectDonor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verified")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDTO<List<UrgentDonorListItemDTO>>> getVerified() {
        List<UrgentDonorListItemDTO> result = urgentDonorRegistryService.getAllVerifiedUrgentDonorsForTable();
        return ResponseEntity.ok(ApiResponseDTO.success(result, "Đã xác minh: " + result.size() + " người"));
    }

    @PostMapping("/leave")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> leaveUrgentGroup(Authentication authentication) {
        Long userId = ((UserPrincipal) authentication.getPrincipal()).getUserId();
        urgentDonorRegistryService.leaveUrgentDonorGroup(userId);
        return ResponseEntity.ok().body(Map.of("message", "\u274c Đã rời khỏi nhóm hiến máu khẩn cấp."));
    }

    @GetMapping("/detail/{userId}")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<UrgentDonorDetailDTO> getDonorDetail(@PathVariable Long userId) {
        UrgentDonorDetailDTO dto = urgentDonorRegistryService.getDonorDetailById(userId);
        return ResponseEntity.ok(dto);
    }
}