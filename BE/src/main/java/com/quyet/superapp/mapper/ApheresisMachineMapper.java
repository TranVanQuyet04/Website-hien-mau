package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.ApheresisMachineDTO;
import com.quyet.superapp.entity.ApheresisMachine;

public class ApheresisMachineMapper {

    public static ApheresisMachineDTO toDTO(ApheresisMachine entity) {
        return ApheresisMachineDTO.builder()
                .id(entity.getApheresisMachineId())
                .serialNumber(entity.getSerialNumber())
                .manufacturer(entity.getManufacturer())
                .model(entity.getModel())
                .isActive(entity.isActive())
                .lastMaintenance(entity.getLastMaintenance())
                .note(entity.getNote())
                .build();
    }

    public static ApheresisMachine toEntity(ApheresisMachineDTO dto) {
        return ApheresisMachine.builder()
                .apheresisMachineId(dto.getId())
                .serialNumber(dto.getSerialNumber())
                .manufacturer(dto.getManufacturer())
                .model(dto.getModel())
                .isActive(dto.isActive())
                .lastMaintenance(dto.getLastMaintenance())
                .note(dto.getNote())
                .build();
    }
}
