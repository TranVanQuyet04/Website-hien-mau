package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodRequestDTO;
import com.quyet.superapp.dto.BloodRequestSummaryDTO;
import com.quyet.superapp.dto.NearbyDonorDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.service.BloodRequestService;
import com.quyet.superapp.service.UrgentDonorRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/blood-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminBloodRequestController {

    private final BloodRequestService service;
    private final UrgentDonorRegistryService urgentDonorRegistryService;
    private final BloodRequestService bloodRequestService;

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequestDTO> getRequestDetail(@PathVariable Long id) {
        return ResponseEntity.ok(bloodRequestService.getDetailById(id));
    }


    @GetMapping("/summary")
    public ResponseEntity<List<BloodRequestSummaryDTO>> getSummaryList() {
        return ResponseEntity.ok(bloodRequestService.getSummaryList());
    }

    // ✅ 1. Lấy danh sách người hiến gần bệnh viện
    @GetMapping("/nearby-hospital")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<NearbyDonorDTO>> getNearbyDonorsNearHospital(
            @RequestParam(defaultValue = "5.0") double radiusKm) {
        List<NearbyDonorDTO> result = urgentDonorRegistryService.findNearbyVerifiedDonors(radiusKm);
        return ResponseEntity.ok(result);
    }

    // ✅ 2. Lấy danh sách tất cả yêu cầu máu
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    // ✅ 3. Duyệt đơn truyền máu
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> approveRequest(@PathVariable("id") Long id) {
        BloodRequest updated = service.updateStatus(id, "APPROVED");
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ✅ 4. Từ chối đơn truyền máu
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> rejectRequest(@PathVariable("id") Long id) {
        BloodRequest updated = service.updateStatus(id, "REJECTED");
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ✅ 5. Đánh dấu WAITING_DONOR (tìm người hiến khẩn cấp)
    @PutMapping("/{id}/mark-waiting-donor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> markAsWaitingDonor(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String reason = body.getOrDefault("reason", "Chưa rõ lý do");
        BloodRequest updated = service.markAsWaitingDonor(id, reason);
        return ResponseEntity.ok("Đơn #" + updated.getId() + " đã chuyển sang trạng thái WAITING_DONOR");
    }

    // ✅ 6. (Mới) Huỷ đơn từ phía admin (nếu cần thêm chức năng này)
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cancelRequestByAdmin(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String reason = body.getOrDefault("reason", "Không rõ lý do");
        service.cancelByAdmin(id, reason);
        return ResponseEntity.ok("Đơn #" + id + " đã bị huỷ");
    }
}
