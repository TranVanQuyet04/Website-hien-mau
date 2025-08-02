package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.BloodSeparationSuggestion;
import com.quyet.superapp.entity.User;

import java.time.LocalDateTime;

public class BloodSeparationSuggestionMapper {

    public static BloodSeparationSuggestionDTO toDTO(BloodSeparationSuggestion entity) {
        BloodSeparationSuggestionDTO dto = new BloodSeparationSuggestionDTO();
        dto.setRedCellsMl(entity.getRedCellsMl());
        dto.setPlasmaMl(entity.getPlasmaMl());
        dto.setPlateletsMl(entity.getPlateletsMl());
        dto.setRedCellLabel(entity.getRedCellsCode());
        dto.setPlasmaLabel(entity.getPlasmaCode());
        dto.setPlateletsLabel(entity.getPlateletsCode());
        dto.setNote(entity.getDescription());
        return dto;
    }

    /**
     * Tạo entity từ DTO + dữ liệu hệ thống
     */
    public static BloodSeparationSuggestion fromDTO(BloodSeparationSuggestionDTO dto, User suggestedBy) {
        BloodSeparationSuggestion entity = new BloodSeparationSuggestion();
        entity.setRedCellsMl(dto.getRedCellsMl());
        entity.setPlasmaMl(dto.getPlasmaMl());
        entity.setPlateletsMl(dto.getPlateletsMl());
        entity.setRedCellsCode(dto.getRedCellLabel());
        entity.setPlasmaCode(dto.getPlasmaLabel());
        entity.setPlateletsCode(dto.getPlateletsLabel());
        entity.setDescription(dto.getNote());
        entity.setSuggestedBy(suggestedBy);
        entity.setSuggestedAt(LocalDateTime.now());
        return entity;
    }
}
