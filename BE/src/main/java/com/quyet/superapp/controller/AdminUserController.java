package com.quyet.superapp.controller;

import com.quyet.superapp.dto.AdminCreateUserRequestDTO;
import com.quyet.superapp.dto.UnverifiedDonorDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.exception.MultiFieldException;
import com.quyet.superapp.mapper.UrgentDonorMapper;
import com.quyet.superapp.repository.DonorReadinessLogRepository;
import com.quyet.superapp.repository.UrgentDonorRegistryRepository;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.UrgentDonorRegistryService;
import com.quyet.superapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final DonorReadinessLogRepository donorReadinessLogRepo;
    private final UserRepository userRepo;
    private final UrgentDonorRegistryRepository urgentDonorRegistryRepo;
    private final UrgentDonorMapper urgentDonorMapper;
    private final UrgentDonorRegistryService urgentDonorService;

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody @Valid AdminCreateUserRequestDTO dto) {
        try {
            userService.createUserByAdmin(dto);
            return ResponseEntity.ok("Tạo tài khoản thành công");
        } catch (MultiFieldException ex) {
            return ResponseEntity.badRequest().body(ex.getFieldErrors());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi không xác định: " + e.getMessage());
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donor-readiness-logs")
    public ResponseEntity<?> getAllReadinessLogs() {
        return ResponseEntity.ok(donorReadinessLogRepo.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/donor-readiness-logs/{username}")
    public ResponseEntity<?> getReadinessLogsByUser(@PathVariable String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return ResponseEntity.ok(donorReadinessLogRepo.findByUser(user));
    }

    @GetMapping("/urgent-donors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getVerifiedUrgentDonors() {
        return ResponseEntity.ok(urgentDonorService.getVerifiedUrgentDonors());
    }



    @GetMapping("/unverified-donors")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> getUnverifiedDonors() {
        return ResponseEntity.ok(urgentDonorService.getUnverifiedDonors());
    }

    @PutMapping("/verify-donor/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> verifyDonor(@PathVariable Long id) {
        urgentDonorService.verifyDonor(id);
        return ResponseEntity.ok("Xác minh thành công");
    }

    @DeleteMapping("/reject-donor/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> rejectDonor(@PathVariable Long id) {
        urgentDonorService.rejectDonor(id);
        return ResponseEntity.ok("Đã xoá người đăng ký không hợp lệ");
    }

}
