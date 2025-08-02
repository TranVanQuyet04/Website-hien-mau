package com.quyet.superapp.service;

import com.quyet.superapp.dto.InventoryCheckResultDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.InventoryCheckResultStatus;
import com.quyet.superapp.repository.BloodInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BloodInventoryService {

    private final BloodInventoryRepository bloodInventoryRepository;
    private final BloodTypeService bloodTypeService;
    private final NotificationService notificationService;

    /**
     * ✅ Kiểm tra kho máu có thể đáp ứng yêu cầu truyền máu hay không
     */
    public InventoryCheckResultDTO checkInventoryForRequest(BloodRequest request) {
        BloodType recipientType = request.getBloodType();
        BloodComponent component = request.getComponent();
        int quantityNeeded = request.getQuantityBag();

        // ⚠️ Validate thiếu cấu hình nhóm máu hoặc thành phần
        if (recipientType == null || component == null) {
            return new InventoryCheckResultDTO(
                    false,
                    quantityNeeded,
                    0,
                    null,
                    List.of(),
                    "❓ Nhóm máu hoặc thành phần chưa được cấu hình đúng.",
                    InventoryCheckResultStatus.UNKNOWN_COMPONENT_TYPE
            );
        }

        // 🔍 Tìm nhóm máu tương thích và tồn kho
        List<BloodType> compatibleTypes = bloodTypeService.findCompatibleTypes(recipientType);
        List<BloodInventory> available = bloodInventoryRepository
                .findByBloodTypeInAndComponentAndUsable(compatibleTypes, component.getBloodComponentId());

        int totalAvailable = available.stream()
                .mapToInt(BloodInventory::getAvailableBagCount)
                .sum();

        boolean canFulfill = totalAvailable >= quantityNeeded;

        LocalDate nearestExpiry = available.stream()
                .flatMap(inv -> inv.getBloodUnits().stream())
                .map(BloodUnit::getExpiryDate)
                .filter(date -> date != null)
                .min(LocalDate::compareTo)
                .orElse(null);

        List<String> fallbackGroups = compatibleTypes.stream()
                .map(BloodType::getDescription)
                .collect(Collectors.toList());

        // ✅ Tính toán trạng thái và thông điệp
        InventoryCheckResultStatus status;
        String message;

        if (canFulfill) {
            boolean willBeLow = available.stream()
                    .anyMatch(inv -> inv.getAvailableBagCount() - quantityNeeded < inv.getMinThresholdMl());

            if (willBeLow) {
                status = InventoryCheckResultStatus.LOW_AFTER_DISPATCH;
                message = "🟡 Đáp ứng yêu cầu nhưng sẽ gây thiếu máu sau khi xuất.";
            } else {
                status = InventoryCheckResultStatus.AVAILABLE;
                message = "✅ Có thể đáp ứng yêu cầu.";
            }
        } else if (totalAvailable > 0) {
            status = InventoryCheckResultStatus.PARTIAL_AVAILABLE;
            message = "⚠️ Không đủ nhóm chính nhưng có thể thay thế bằng nhóm tương thích.";
        } else {
            status = InventoryCheckResultStatus.INSUFFICIENT;
            message = "❌ Không đủ máu, kể cả nhóm tương thích.";
        }

        // 🔔 Gửi cảnh báo nếu nguy hiểm
        if (status == InventoryCheckResultStatus.LOW_AFTER_DISPATCH || status == InventoryCheckResultStatus.INSUFFICIENT) {
            String alertMessage = "[⚠️ Cảnh báo kho máu] Yêu cầu máu #" + request.getId()
                    + " không đủ tồn kho hoặc sắp vượt ngưỡng.\nChi tiết: " + message;

            log.warn("⚠️ Cảnh báo tồn kho máu thấp sau kiểm tra - RequestID: {}, Status: {}, Sẵn có: {}, Cần: {}",
                    request.getId(), status, totalAvailable, quantityNeeded);

            // Gửi email cho admin
            notificationService.sendInventoryAlertEmail(alertMessage);

            // Gửi in-app notification
            notificationService.sendInventoryAlertNotification(alertMessage);
        }

        return new InventoryCheckResultDTO(
                canFulfill,
                quantityNeeded,
                totalAvailable,
                nearestExpiry,
                fallbackGroups,
                message,
                status
        );
    }

    // 🔎 Tìm kho máu theo đúng nhóm máu và thành phần
    public List<BloodInventory> findAvailable(BloodType type, BloodComponent component) {
        return bloodInventoryRepository.findByBloodTypeAndComponentAndUsable(
                type.getBloodTypeId(),
                component.getBloodComponentId()
        );
    }

    // 🔎 Tìm kho máu theo danh sách nhóm máu tương thích và thành phần
    public List<BloodInventory> findAvailableInTypes(List<BloodType> types, BloodComponent component) {
        return bloodInventoryRepository.findByBloodTypeInAndComponentAndUsable(
                types, component.getBloodComponentId()
        );
    }
    public void updateOrAdd(BloodUnit unit) {
        var bloodType = unit.getBloodType();
        var component = unit.getComponent();

        BloodInventory inventory = bloodInventoryRepository
                .findByBloodTypeAndComponent(bloodType, component)
                .orElseGet(() -> {
                    BloodInventory newInventory = new BloodInventory();
                    newInventory.setBloodType(bloodType);
                    newInventory.setComponent(component);
                    newInventory.setTotalQuantityMl(0);
                    newInventory.setCreatedAt(LocalDateTime.now());

                    // ✅ Gán bagCode và status khi tạo mới
                    newInventory.setBagCode(unit.getBloodBag() != null ? unit.getBloodBag().getBagCode() : null);
                    newInventory.setStatus(unit.getStatus() != null ? unit.getStatus().name() : "AVAILABLE");

                    return newInventory;
                });

        int updatedQuantity = inventory.getTotalQuantityMl() + unit.getQuantityMl();
        inventory.setTotalQuantityMl(updatedQuantity);
        inventory.setLastUpdated(LocalDateTime.now());

        // ✅ Gán lại nếu đơn vị máu có thông tin mới
        if (unit.getBloodBag() != null && unit.getBloodBag().getBagCode() != null) {
            inventory.setBagCode(unit.getBloodBag().getBagCode());
        }

        if (unit.getStatus() != null) {
            inventory.setStatus(unit.getStatus().name());
        }

        bloodInventoryRepository.save(inventory);
    }
}
