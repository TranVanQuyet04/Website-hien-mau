package com.quyet.superapp.controller;

import com.quyet.superapp.dto.ReadinessChangeLogDTO;
import com.quyet.superapp.dto.UrgentDonorListItemDTO;
import com.quyet.superapp.dto.UserDTO;
import com.quyet.superapp.service.UrgentDonorRegistryService;
import com.quyet.superapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final UrgentDonorRegistryService urgentDonorRegistryService;

    @GetMapping("/readiness-change-logs")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<ReadinessChangeLogDTO>> getReadinessLogs(
            @RequestParam Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        List<ReadinessChangeLogDTO> logs =
                urgentDonorRegistryService.getReadinessChangeLogsByUserIdAndDate(userId, from, to);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/urgent-donors/inactive")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<UrgentDonorListItemDTO>> getInactiveDonors() {
        return ResponseEntity.ok(urgentDonorRegistryService.getInactiveUrgentDonors());
    }



    @GetMapping("/urgent-donors/detail/{userId}")
    public ResponseEntity<?> getDonorDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(urgentDonorRegistryService.getDonorDetailById(userId));
    }


    // Bước 4: Controller
    @GetMapping("/urgent-donors/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<?> getUrgentDonorList(
            @RequestParam(required = false) Long bloodTypeId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDonationDate,
            @RequestParam(required = false) Integer minRecoveryTime // 👈 mới
    ) {
        return ResponseEntity.ok(
                urgentDonorRegistryService.getAllVerifiedUrgentDonorsFiltered(bloodTypeId, location, minDonationDate, minRecoveryTime)
        );
    }



    @GetMapping("/staffs")
    public ResponseEntity<List<UserDTO>> getAllStaffAndDoctors(
            @RequestParam(name = "staffPosition", required = false) String staffPosition
    ) {
        List<UserDTO> users = (staffPosition == null || staffPosition.isBlank()) ?
                userService.getAllStaffAndDoctors() :
                userService.getByStaffPosition(staffPosition);
        return ResponseEntity.ok(users);
    }


//    @GetMapping("/doctors")
//    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
//    public List<DoctorDTO> getDoctors() {
//        return userService.getUsersByStaffPosition("Doctor");
//    }

}

