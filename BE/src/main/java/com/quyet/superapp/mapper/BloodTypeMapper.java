package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodTypeDTO;
import com.quyet.superapp.dto.BloodTypeFullDTO;
import com.quyet.superapp.entity.BloodType;
import org.springframework.stereotype.Component;

@Component
public class BloodTypeMapper {

    // Entity → FullDTO (cho bảng danh sách)
    public BloodTypeFullDTO toFullDTO(BloodType entity) {
        return BloodTypeFullDTO.builder()
                .id(entity.getBloodTypeId())
                .bloodGroup(entity.getDescription())
                .rh(entity.getRh())
                .note(entity.getNote())
                .isActive(entity.getIsActive())
                .build();
    }

    // DTO → Entity (dùng khi tạo/sửa)
    public BloodType toEntity(BloodTypeDTO dto) {
        BloodType entity = new BloodType();
        entity.setBloodTypeId(dto.getBloodTypeId());
        entity.setDescription(dto.getDescription());
        entity.setRh(dto.getRh());
        entity.setNote(dto.getNote());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        return entity;
    }

    // Entity → DTO đơn giản
    public BloodTypeDTO toDTO(BloodType entity) {
        return BloodTypeDTO.builder()
                .bloodTypeId(entity.getBloodTypeId())
                .description(entity.getDescription())
                .rh(entity.getRh())
                .note(entity.getNote())
                .isActive(entity.getIsActive())
                .build();
    }
}
