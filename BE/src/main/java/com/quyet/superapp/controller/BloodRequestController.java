package com.quyet.superapp.controller;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.exception.RegistrationException;
import com.quyet.superapp.mapper.BloodRequestMapper;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.BloodInventoryService;
import com.quyet.superapp.service.BloodRequestService;
import com.quyet.superapp.service.PaymentService;
import com.quyet.superapp.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api/blood-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodRequestController {

    private final BloodRequestService requestService;
    private final BloodInventoryService inventoryService;
    private final UserRepository userRepository;
    private final PatientService patientService;
    private final PaymentService paymentService;

    @GetMapping("/{id}/payment-info")
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN', 'STAFF')")
    public ResponseEntity<PaymentInfoDTO> getPaymentInfo(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getPaymentInfo(id));
    }


    @PostMapping("/{id}/pay")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> payForRequest(@PathVariable Long id) {
        paymentService.processPayment(id); // tính giá, giảm BHYT, lưu giao dịch
        return ResponseEntity.ok(Map.of("message", "✅ Thanh toán thành công"));
    }
    // ================================
    // [STAFF] Tạo yêu cầu máu với bệnh nhân mới
    // ================================
    @PostMapping("/new")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> createRequestWithNewPatient(@Valid @RequestBody BloodRequestWithNewPatientDTO dto) {
        patientService.validateInsurance(dto);  // chỉ gọi, không cần gán
        BloodRequest created = requestService.createRequestWithNewPatient(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }


    // ================================
    // [STAFF] Tạo yêu cầu máu với bệnh nhân đã có
    // ================================
    @PostMapping("/existing")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<?> createRequestWithExistingPatient(@Valid @RequestBody BloodRequestWithExistingPatientDTO dto) {
        BloodRequest created = requestService.createRequestWithExistingPatient(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }

    // ================================
    // [STAFF + ADMIN] Kiểm tra số CMND/CCCD bệnh nhân
    // ================================
    @GetMapping("/check-citizen/{citizenId}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> checkCitizenInfo(@PathVariable String citizenId) {
        Optional<User> userOpt = userRepository.findByCitizenId(citizenId);
        if (userOpt.isPresent()) {
            var u = userOpt.get();
            var p = u.getUserProfile();
            Map<String, Object> result = new HashMap<>();
            result.put("userId", u.getUserId());
            result.put("fullName", p.getFullName());
            result.put("phone", p.getPhone());
            result.put("age", (p.getDob() != null) ? Period.between(p.getDob(), LocalDate.now()).getYears() : null);
            result.put("gender", p.getGender());
            result.put("bloodGroup", (p.getBloodType() != null) ? p.getBloodType().getDescription() : null);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    // ================================
    // [STAFF] Xác nhận số lượng máu nhận được
    // ================================
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> confirmReceived(@PathVariable Long id, @RequestParam int confirmedVolumeMl) {
        BloodRequest updated = requestService.confirmReceivedVolume(id, confirmedVolumeMl);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ================================
    // [ADMIN] Duyệt hoặc từ chối yêu cầu máu
    // ================================
    @PutMapping("/approves")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> approveRequest(@RequestBody ApproveBloodRequestDTO dto) {
        BloodRequest approved = requestService.approveRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(approved));
    }

    // ================================
    // [ADMIN] Cập nhật trạng thái yêu cầu
    // ================================
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloodRequestDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        BloodRequest updated = requestService.updateStatus(id, status);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(updated));
    }

    // ================================
    // [ADMIN + STAFF] Xem chi tiết yêu cầu máu
    // ================================
//    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
//    public ResponseEntity<BloodRequestDTO> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(requestService.getById(id));
//    }

    // ================================
    // [ADMIN + STAFF] Kiểm tra tồn kho phù hợp
    // ================================
    @GetMapping("/{id}/check-inventory")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<InventoryCheckResultDTO> checkInventory(@PathVariable Long id) {
        BloodRequest request = requestService.findById(id);
        InventoryCheckResultDTO result = inventoryService.checkInventoryForRequest(request);
        return ResponseEntity.ok(result);
    }

    // ================================
    // [ADMIN + STAFF] Lọc theo trạng thái và độ khẩn
    // ================================
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodRequestDTO>> filterRequests(
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(requestService.filterRequests(urgency, status));
    }

    // ================================
    // [ADMIN] Danh sách yêu cầu hoàn tất
    // ================================
    @GetMapping("/admin/requests/completed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getCompletedRequests() {
        return ResponseEntity.ok(requestService.getCompletedRequests());
    }

    // ================================
    // [ADMIN] Danh sách yêu cầu đang xử lý
    // ================================
    @GetMapping("/admin/requests/processing")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getProcessingRequests() {
        return ResponseEntity.ok(requestService.getProcessingRequests());
    }

    // ================================
    // [ADMIN] Yêu cầu KHẨN CẤP chờ duyệt
    // ================================
    @GetMapping("/admin/urgent/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentPending() {
        return ResponseEntity.ok(requestService.getUrgentPendingRequests());
    }

    // ================================
    // [ADMIN] Yêu cầu KHẨN CẤP đang hoạt động
    // ================================
    @GetMapping("/admin/urgent/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentActive() {
        return ResponseEntity.ok(requestService.getUrgentActiveRequests());
    }

    // ================================
    // [ADMIN] Lịch sử yêu cầu KHẨN CẤP
    // ================================
    @GetMapping("/admin/urgent/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloodRequestDTO>> getUrgentHistory() {
        return ResponseEntity.ok(requestService.getUrgentRequestHistory());
    }

    // ================================
    // [STAFF + ADMIN] Tra cứu đơn giá thành phần máu
    // ================================
    @GetMapping("/pricing/{componentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Integer> getComponentPrice(@PathVariable Long componentId) {
        return ResponseEntity.ok(requestService.getPriceForComponent(componentId));
    }

    /**
     * STAFF gửi yêu cầu máu mới (thường hoặc khẩn cấp/cấp cứu)
     * Nếu là BÌNH THƯỜNG, hệ thống tự duyệt dựa theo tồn kho
     */
    @PostMapping
    @PreAuthorize("hasRole('STAFF')")

    public ResponseEntity<BloodRequestDTO> createBloodRequest(@RequestBody CreateBloodRequestDTO dto) {
        BloodRequest created = requestService.createRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(created));
    }

    /**
     * ADMIN lấy danh sách tất cả yêu cầu máu
     * Dùng cho màn hình dashboard để xử lý hoặc theo dõi
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<BloodRequestDTO>> getAllRequestsForAdmin() {
        List<BloodRequestDTO> list = requestService.getAllRequests();
        return ResponseEntity.ok(list);
    }

    /**
     * ADMIN duyệt yêu cầu máu theo cấp độ khẩn
     * Hỗ trợ duyệt hoàn toàn, duyệt 1 phần, hoặc từ chối
     */
    @PutMapping("/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<BloodRequestDTO> approveBloodRequest(@RequestBody ApproveBloodRequestDTO dto) {
        BloodRequest approved = requestService.approveRequest(dto);
        return ResponseEntity.ok(BloodRequestMapper.toDTO(approved));
    }
    /**
     * STAFF hoặc ADMIN xem các yêu cầu máu đã được duyệt
     */
    @GetMapping("/approved")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<BloodRequestDTO>> getApprovedRequests() {
        List<BloodRequest> approved = requestService.getApprovedRequests();
        List<BloodRequestDTO> dtoList = approved.stream()
                .map(BloodRequestMapper::toDTO)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Lấy chi tiết yêu cầu máu theo ID (dành cho ADMIN hoặc STAFF)
     */
    // ✅ Controller
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BloodRequestDTO> getRequestById(@PathVariable Long id) {
        BloodRequestDTO dto = requestService.getById(id); // đúng kiểu rồi
        return ResponseEntity.ok(dto);
    }


}
