package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.BloodComponentRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import com.quyet.superapp.repository.BloodUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodUnitService {
    private final BloodUnitRepository repository;
    private final BloodBagRepository bloodBagRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository bloodComponentRepository;

    // ✅ Lấy tất cả đơn vị máu
    public List<BloodUnit> getAll() {
        return repository.findAll();
    }

    // ✅ Lấy đơn vị máu theo ID
    public BloodUnit getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Blood unit not found with id: " + id));
    }

    // ✅ Lưu mới hoặc cập nhật đơn vị máu
    public BloodUnit save(BloodUnit unit, Long bloodTypeId, Long componentId, Long bloodBagId) {
        BloodType bloodType = bloodTypeRepository.findById(bloodTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Blood type not found"));
        BloodComponent component = bloodComponentRepository.findById(componentId)
                .orElseThrow(() -> new IllegalArgumentException("Component not found"));
        BloodBag bag = bloodBagRepository.findById(bloodBagId)
                .orElseThrow(() -> new IllegalArgumentException("Blood bag not found"));

        unit.setBloodType(bloodType);
        unit.setComponent(component);
        unit.setBloodBag(bag);

        return repository.save(unit);
    }

    // ✅ Xóa đơn vị máu
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Blood unit does not exist with id: " + id);
        }
        repository.deleteById(id);
    }
    // ✅ Tìm đơn vị máu theo trạng thái
    public List<BloodUnit> findByStatus(BloodUnitStatus status) {
        return repository.findByStatus(status);
    }

    // ✅ Tìm đơn vị máu sắp hết hạn
    public List<BloodUnit> findExpiringBefore(LocalDate date) {
        return repository.findByExpirationDateBefore(date);
    }

    // ✅ Tìm theo mã đơn vị
    public BloodUnit findByUnitCode(String code) {
        return repository.findByUnitCode(code)
                .orElseThrow(() -> new IllegalArgumentException("No blood unit found with code: " + code));
    }
    public List<BloodUnit> getAvailableUnits(Long componentId) {
        return repository.findByStatusAndComponent_BloodComponentId(
                BloodUnitStatus.AVAILABLE,componentId);

    }
    public BloodUnit markAsUsed(Long unitId) {
        BloodUnit unit = getById(unitId);
        unit.setStatus(BloodUnitStatus.USED);
        return repository.save(unit);
    }
    public BloodUnit markAsExpired(Long unitId) {
        BloodUnit unit = getById(unitId);
        unit.setStatus(BloodUnitStatus.EXPIRED);
        return repository.save(unit);
    }


}
