package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodComponentDTO;
import com.quyet.superapp.dto.BloodComponentFullDTO;
import com.quyet.superapp.entity.BloodComponent;
import org.springframework.stereotype.Component;

@Component
public class BloodComponentMapper {

    // ✅ Entity → DTO đơn giản
    public BloodComponentDTO toDTO(BloodComponent entity) {
        if (entity == null) return null;

        return BloodComponentDTO.builder()
                .bloodComponentId(entity.getBloodComponentId())
                .name(entity.getName())
                .code(entity.getCode())
                .storageTemp(entity.getStorageTemp())
                .storageDays(entity.getStorageDays())
                .usage(entity.getUsage())
                .isApheresisCompatible(entity.getIsApheresisCompatible())
                .type(entity.getType())
                .isActive(entity.getIsActive())
                .build();
    }

    // ✅ DTO → Entity
    public BloodComponent toEntity(BloodComponentDTO dto) {
        if (dto == null) return null;

        return BloodComponent.builder()
                .bloodComponentId(dto.getBloodComponentId())
                .name(dto.getName())
                .code(dto.getCode())
                .storageTemp(dto.getStorageTemp())
                .storageDays(dto.getStorageDays())
                .usage(dto.getUsage())
                .isApheresisCompatible(dto.getIsApheresisCompatible())
                .type(dto.getType())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    // ✅ Entity → Full DTO (cho bảng danh sách)
    public BloodComponentFullDTO toFullDTO(BloodComponent entity) {
        if (entity == null) return null;

        return BloodComponentFullDTO.builder()
                .id(entity.getBloodComponentId())
                .name(entity.getName())
                .code(entity.getCode())
                .temperature(entity.getStorageTemp())
                .shelfLifeDays(entity.getStorageDays())
                .isMachineSeparated(entity.getIsApheresisCompatible())
                .application(entity.getUsage())
                .build();
    }



}
