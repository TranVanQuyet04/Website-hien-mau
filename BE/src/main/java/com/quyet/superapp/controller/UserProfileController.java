package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UserProfileDTO;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.UserProfileMapper;
import com.quyet.superapp.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/userprofiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // ✅ [GET] Lấy danh sách tất cả hồ sơ người dùng
    @GetMapping
    public ResponseEntity<List<UserProfileDTO>> getAllProfiles() {
        List<UserProfileDTO> profiles = userProfileService.getAllProfiles()
                .stream()
                .map(UserProfileMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(profiles);
    }

    // ✅ [GET] Lấy thông tin hồ sơ theo userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        UserProfile profile = userProfileService.getProfileByUserId(userId);
        return ResponseEntity.ok(UserProfileMapper.toDTO(profile));
    }

    // ✅ [POST] Tạo mới hồ sơ người dùng
    @PostMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> createProfile(@PathVariable Long userId,
                                                        @RequestBody @Valid UserProfileDTO dto) {
        UserProfile createdProfile = userProfileService.createProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toDTO(createdProfile));
    }

    // ✅ [PUT] Cập nhật hồ sơ người dùng
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> updateProfile(@PathVariable Long userId,
                                                        @RequestBody @Valid UserProfileDTO dto) {
        UserProfile updatedProfile = userProfileService.updateProfile(userId, dto);
        return ResponseEntity.ok(UserProfileMapper.toDTO(updatedProfile));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userProfileService.deleteById(id);
    }

    /**
     * Lấy hồ sơ người dùng hiện tại (dựa vào JWT -> lấy username)
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('MEMBER', 'STAFF', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile profile = userProfileService.getByUsername(username);
        UserProfileDTO dto = UserProfileMapper.toDTO(profile);  // ✅ dùng mapper để format lại
        return ResponseEntity.ok(dto);                          // ✅ trả về DTO mới
    }



}
