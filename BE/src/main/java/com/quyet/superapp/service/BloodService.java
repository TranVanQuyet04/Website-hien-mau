package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.mapper.BloodInventoryMapper;
import com.quyet.superapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodService {

    private final BloodInventoryRepository bloodRepo;
    private final BloodUnitRepository bloodUnitRepo;
    private final LabTestResultRepository labTestResultRepo;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository bloodComponentRepository;

    private String getStatus(int quantity, int min, int critical) {
        if (quantity < critical) return "CRITICAL";
        else if (quantity < min) return "WARNING";
        else return "NORMAL";
    }

    @Transactional
    public BloodInventory updateBlood(Long id, BloodInventory updated) {
        return bloodRepo.findById(id)
                .map(blood -> {
                    if (updated.getBloodType() == null || updated.getBloodType().getBloodTypeId() == null) {
                        throw new IllegalArgumentException("Thiếu thông tin bloodTypeId");
                    }
                    if (updated.getComponent() == null || updated.getComponent().getBloodComponentId() == null) {
                        throw new IllegalArgumentException("Thiếu thông tin componentId");
                    }

                    // Lấy lại từ DB để tránh lỗi transient
                    BloodType bloodType = bloodTypeRepository.findById(updated.getBloodType().getBloodTypeId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy BloodType"));

                    BloodComponent component = bloodComponentRepository.findById(updated.getComponent().getBloodComponentId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy BloodComponent"));

                    int oldQuantity = Optional.ofNullable(blood.getTotalQuantityMl()).orElse(0);
                    int min = Optional.ofNullable(blood.getMinThresholdMl()).orElse(0);
                    int critical = Optional.ofNullable(blood.getCriticalThresholdMl()).orElse(0);
                    String oldStatus = getStatus(oldQuantity, min, critical);

                    // Gán dữ liệu mới
                    blood.setBloodType(bloodType);
                    blood.setComponent(component);
                    blood.setTotalQuantityMl(updated.getTotalQuantityMl());
                    blood.setLastUpdated(LocalDateTime.now());

                    BloodInventory saved = bloodRepo.save(blood);

                    String newStatus = getStatus(saved.getTotalQuantityMl(), min, critical);

                    if (!newStatus.equals(oldStatus) && !"NORMAL".equals(newStatus)) {
                        String content = String.format(
                                "<p><b>Cảnh báo!</b> Nhóm máu <b>%s - %s</b> vừa chuyển sang trạng thái <b>%s</b>, còn lại <span style='color:red;'>%dml</span>.</p>",
                                saved.getBloodType().getDescription(),
                                saved.getComponent().getName(),
                                newStatus,
                                saved.getTotalQuantityMl()
                        );

                        List<User> recipients = userRepository.findByRole(RoleEnum.ADMIN);
                        recipients.addAll(userRepository.findByRole(RoleEnum.STAFF));

                        for (User user : recipients) {
                            emailService.sendEmail(user, "[CẢNH BÁO] Cập nhật kho máu", content, "REALTIME_ALERT");
                            notificationService.createNotification(
                                    user.getUserId(),
                                    "⚠️ Cập nhật kho máu",
                                    "Có nhóm máu vừa chuyển sang trạng thái cảnh báo. Kiểm tra email để biết thêm."
                            );
                        }
                    }

                    return saved;
                })
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kho máu với ID = " + id));
    }

    public List<String> suggestBloodDonation() {
        return getAllInventoryStatus().stream()
                .filter(alert -> "CRITICAL".equals(alert.getAlertLevel()))
                .sorted(Comparator.comparing(BloodInventoryAlertDTO::getQuantityMl))
                .map(alert -> String.format(
                        "\uD83D\uDCE2 Nhóm máu %s - %s đang ở mức nguy kịch (%dml)! Cần bổ sung gấp.",
                        alert.getBloodType(), alert.getComponent(), alert.getQuantityMl()
                ))
                .distinct()
                .toList();
    }

    public Map<String, Long> forecastUsage7Days() {
        List<BloodUnit> usedUnits = bloodUnitRepo.findByStatus(BloodUnitStatus.USED);
        return usedUnits.stream()
                .collect(Collectors.groupingBy(
                        unit -> unit.getBloodType().getDescription() + " - " + unit.getComponent().getName(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() * 7));
    }

    @Transactional
    public List<BloodInventoryAlertDTO> getAllInventoryStatus() {
        return bloodRepo.findAll().stream()
                .map(inv -> {
                    int quantity = Optional.ofNullable(inv.getTotalQuantityMl()).orElse(0);
                    int min = Optional.ofNullable(inv.getMinThresholdMl()).orElse(0);
                    int critical = Optional.ofNullable(inv.getCriticalThresholdMl()).orElse(0);
                    String status = getStatus(quantity, min, critical);
                    return new BloodInventoryAlertDTO(
                            inv.getBloodInventoryId(),
                            inv.getBloodType().getDescription(),
                            inv.getComponent().getName(),
                            quantity, min, critical, status
                    );
                }).toList();
    }

    public List<BloodInventory> getInventory() {
        return bloodRepo.findAll();
    }

    public Optional<BloodInventory> getInventoryById(Long id) {
        return bloodRepo.findById(id);
    }

    public BloodInventory addBlood(BloodInventory inventory) {
        inventory.setLastUpdated(LocalDateTime.now());
        return bloodRepo.save(inventory);
    }

    public void deleteInventory(Long id) {
        bloodRepo.deleteById(id);
    }

    public Optional<BloodInventory> searchBloodByTypeAndComponent(BloodType bloodType, BloodComponent component) {
        return bloodRepo.findByBloodTypeAndComponent(bloodType, component);
    }

    public List<BloodInventoryDTO> getAllDTOs() {
        return bloodRepo.findAll().stream()
                .map(BloodInventoryMapper::toDTO)
                .toList();
    }

    public List<BloodInventoryDTO> getAllInventoryDTO() {
        return getAllDTOs();
    }

    @Transactional
    public void storeBloodUnit(Long bloodUnitId) {
        BloodUnit unit = bloodUnitRepo.findById(bloodUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn vị máu"));

        LabTestResult test = labTestResultRepo.findByBloodUnit_BloodUnitId(bloodUnitId)
                .orElseThrow(() -> new IllegalStateException("Chưa có kết quả xét nghiệm"));

        if (!test.isPassed()) {
            throw new IllegalStateException("Đơn vị máu không đạt yêu cầu xét nghiệm");
        }

        unit.setStatus(BloodUnitStatus.STORED);
        unit.setStoredAt(LocalDateTime.now());
        bloodUnitRepo.save(unit);

        BloodType type = unit.getBloodType();
        BloodComponent component = unit.getComponent();
        BloodInventory inventory = bloodRepo.findByBloodTypeAndComponent(type, component)
                .orElseGet(() -> {
                    BloodInventory newInv = new BloodInventory();
                    newInv.setBloodType(type);
                    newInv.setComponent(component);
                    newInv.setTotalQuantityMl(0);
                    return newInv;
                });

        int newTotal = inventory.getTotalQuantityMl() + unit.getQuantityMl();
        inventory.setTotalQuantityMl(newTotal);
        inventory.setLastUpdated(LocalDateTime.now());

        bloodRepo.save(inventory);
    }
}
