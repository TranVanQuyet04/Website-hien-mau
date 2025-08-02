package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.entity.LabTestResult;
import com.quyet.superapp.entity.User;

public class LabTestResultMapper {

    // Convert từ Entity sang DTO
    public static LabTestResultDTO toDTO(LabTestResult entity) {
        LabTestResultDTO dto = new LabTestResultDTO();
        dto.setLabTestResultId(entity.getLabTestResultId());
        dto.setBloodUnitId(entity.getBloodUnit() != null ? entity.getBloodUnit().getBloodUnitId() : null);

        dto.setHivNegative(entity.isHivNegative());
        dto.setHbvNegative(entity.isHbvNegative());
        dto.setHcvNegative(entity.isHcvNegative());
        dto.setSyphilisNegative(entity.isSyphilisNegative());
        dto.setMalariaNegative(entity.isMalariaNegative());
        dto.setPassed(entity.isPassed());

        dto.setTestedAt(entity.getTestedAt());

        if (entity.getTestedBy() != null) {
            dto.setTestedById(entity.getTestedBy().getUserId());
            dto.setTestedByName(entity.getTestedBy().getUsername());
        }

        return dto;
    }

    // Convert từ DTO sang Entity (trường hợp cần)
    public static LabTestResult fromDTO(LabTestResultDTO dto, User testedBy) {
        LabTestResult entity = new LabTestResult();

        entity.setLabTestResultId(dto.getLabTestResultId());
        entity.setHivNegative(dto.isHivNegative());
        entity.setHbvNegative(dto.isHbvNegative());
        entity.setHcvNegative(dto.isHcvNegative());
        entity.setSyphilisNegative(dto.isSyphilisNegative());
        entity.setMalariaNegative(dto.isMalariaNegative());

        entity.setPassed(dto.isHivNegative() && dto.isHbvNegative()
                && dto.isHcvNegative() && dto.isSyphilisNegative() && dto.isMalariaNegative());

        entity.setTestedAt(dto.getTestedAt());
        entity.setTestedBy(testedBy);

        return entity;
    }
}
