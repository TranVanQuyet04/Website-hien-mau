package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodInventoryDTO;
import com.quyet.superapp.entity.BloodInventory;

public class BloodInventoryMapper {
    private static final int DEFAULT_BAG_SIZE = 350; // bạn có thể đổi thành cấu hình từ DB

    public static BloodInventoryDTO toDTO(BloodInventory inventory) {
        int estimatedBags = inventory.getTotalQuantityMl() / DEFAULT_BAG_SIZE;

        return new BloodInventoryDTO(
                inventory.getBloodInventoryId(),
                inventory.getBloodType().getBloodTypeId(),
                inventory.getBloodType().getDescription(),
                inventory.getComponent().getBloodComponentId(),
                inventory.getComponent().getName(),
                inventory.getTotalQuantityMl(),
                DEFAULT_BAG_SIZE,
                estimatedBags,
                inventory.getCreatedAt(),
                inventory.getLastUpdated(),
                inventory.getUpdatedAt()
        );
    }

    public static BloodInventory toEntity(BloodInventoryDTO dto) {
        BloodInventory entity = new BloodInventory();
        entity.setBloodInventoryId(dto.getBloodInventoryId());
        entity.setTotalQuantityMl(dto.getTotalQuantityMl());
        entity.setLastUpdated(dto.getLastUpdated());
        return entity;
    }
}
