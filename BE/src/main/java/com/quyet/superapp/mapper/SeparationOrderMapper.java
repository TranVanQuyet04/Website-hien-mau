package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.SeparationOrderDTO;
import com.quyet.superapp.entity.ApheresisMachine;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.entity.User;

public class SeparationOrderMapper {
    public static SeparationOrderDTO toDTO(SeparationOrder order) {
        return new SeparationOrderDTO(
                order.getSeparationOrderId(),
                order.getBloodBag() != null ? order.getBloodBag().getBloodBagId() : null,
                order.getPerformedBy() != null ? order.getPerformedBy().getUserId() : null,
                order.getMachine() != null ? order.getMachine().getApheresisMachineId() : null, // 👈 Thêm dòng này
                order.getPerformedAt(),
                order.getSeparationMethod(),
                order.getNote()
        );
    }
    public static SeparationOrder fromDTO(
            SeparationOrderDTO dto,
            BloodBag bloodBag,
            User operator,
            ApheresisMachine machine
    ) {
        SeparationOrder order = new SeparationOrder();
        order.setSeparationOrderId(dto.getSeparationOrderId());
        order.setBloodBag(bloodBag);
        order.setPerformedBy(operator);
        order.setMachine(machine); // 👈 Gán máy
        order.setPerformedAt(dto.getPerformedAt());
        order.setSeparationMethod(dto.getSeparationType());
        order.setNote(dto.getNote());
        return order;
    }
}
