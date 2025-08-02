package com.quyet.superapp.controller;

import com.quyet.superapp.config.jwt.UserPrincipal;
import com.quyet.superapp.dto.ApiResponseDTO;
import com.quyet.superapp.dto.DonorReadinessRequestDTO;
import com.quyet.superapp.dto.UrgentDonorRegistrationDTO;
import com.quyet.superapp.enums.DonorReadinessLevel;
import com.quyet.superapp.service.DonorReadinessService;
import com.quyet.superapp.service.DonorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorProfileService donorProfileService;
    private final DonorReadinessService donorReadinessService;

    /**
     * ✅ Gọi API /api/donors/readiness với JSON body:
     * {
     *   "readinessLevel": "EMERGENCY_NOW"
     * }
     */
    @PostMapping("/process-readiness")
    public ResponseEntity<ApiResponseDTO<String>> processReadiness(
            @Valid @RequestBody UrgentDonorRegistrationDTO dto,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        Long userId = currentUser.getUserId();
        DonorReadinessLevel level = dto.getReadinessLevel();

        String message = donorReadinessService.processReadiness(userId, level, dto);

        return ResponseEntity.ok(
                new ApiResponseDTO<>(true, message)
        );
    }



    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(donorProfileService.getByUserId(principal.getUserId()));
    }

}
