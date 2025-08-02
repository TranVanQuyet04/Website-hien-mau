package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.entity.Transfusion;
import org.springframework.stereotype.Component;

@Component
public class TransfusionMapper {

    public TransfusionDTO toDTO(Transfusion t) {
        TransfusionDTO dto = new TransfusionDTO();

        dto.setId(t.getTransfusionId());
        dto.setTransfusionDate(t.getTransfusionDate());
        dto.setStatus(t.getStatus());
        dto.setNotes(t.getNotes());

        // Liên kết ID
        dto.setRequestId(t.getRequest() != null ? t.getRequest().getId() : null);
        dto.setRecipientId(t.getRecipient() != null ? t.getRecipient().getUserId() : null);
        dto.setBloodUnitId(t.getBloodUnit() != null ? t.getBloodUnit().getBloodUnitId() : null);

        // Thông tin người nhận
        dto.setRecipientName(t.getRecipientName());
        dto.setRecipientPhone(t.getRecipientPhone());

        // Từ yêu cầu máu
        if (t.getRequest() != null) {
            dto.setUrgencyLevel(t.getRequest().getUrgencyLevel().name());
            dto.setTriageLevel(t.getRequest().getTriageLevel());
        }

        // Từ túi máu
        if (t.getBloodUnit() != null) {
            dto.setBloodType(t.getBloodUnit().getBloodType().getDescription());
            dto.setBloodBagCode(t.getBloodUnit().getUnitCode());
            dto.setBloodUnitId(t.getBloodUnit().getBloodUnitId());
            dto.setVolumeMl(t.getVolumeTakenMl() != null ? t.getVolumeTakenMl() : t.getBloodUnit().getQuantityMl());
            dto.setBagCount(1); // hoặc tính tổng nếu có bảng phụ
        }



        // Người phê duyệt
        dto.setApprovedBy(t.getApprovedBy()); // thêm cột này vào entity nếu chưa có

        return dto;
    }

}
