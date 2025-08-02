package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodTypeDTO;
import com.quyet.superapp.dto.BloodTypeFullDTO;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.mapper.BloodTypeMapper;
import com.quyet.superapp.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodTypeService {

    private final BloodTypeRepository repository;
    private final BloodTypeMapper bloodTypeMapper;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodTypeMapper mapper;

    public boolean isCompatible(BloodType donor, BloodType receiver) {
        if (donor == null || receiver == null) return false;

        // Lấy danh sách các nhóm máu có thể truyền cho người nhận
        List<BloodType> compatibleList = findCompatibleTypes(receiver);

        // So sánh donor có nằm trong danh sách tương thích không
        return compatibleList.stream()
                .anyMatch(bt -> bt.getDescription().equalsIgnoreCase(donor.getDescription()));
    }


    public void deactivate(Long id) {
        BloodType type = bloodTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));
        type.setIsActive(false);
        bloodTypeRepository.save(type);
    }

    public void reactivate(Long id) {
        BloodType type = bloodTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));
        type.setIsActive(true);
        bloodTypeRepository.save(type);
    }


    // ✅ Lấy tất cả dạng DTO đơn giản (cho dropdown, API đơn giản)
    public List<BloodTypeDTO> getAll() {
        return repository.findAll().stream()
                .map(bloodTypeMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy tất cả dạng DTO đầy đủ (cho bảng danh sách quản lý)
    public List<BloodTypeFullDTO> getAllFull() {
        return repository.findAll().stream()
                .map(bloodTypeMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    // ✅ Tạo nhóm máu mới
    public BloodTypeDTO create(BloodTypeDTO dto) {
        BloodType entity = bloodTypeMapper.toEntity(dto);
        BloodType saved = repository.save(entity);
        return bloodTypeMapper.toDTO(saved);
    }

    // ✅ Cập nhật nhóm máu
    public BloodTypeDTO update(Long id, BloodTypeDTO dto) {
        BloodType existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhóm máu"));

        existing.setDescription(dto.getDescription());
        existing.setRh(dto.getRh());
        existing.setNote(dto.getNote());
        existing.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return bloodTypeMapper.toDTO(repository.save(existing));
    }

    // ✅ Xóa nhóm máu
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy nhóm máu");
        }
        repository.deleteById(id);
    }

    // ✅ Tìm các nhóm máu tương thích với người nhận
    public List<BloodType> findCompatibleTypes(BloodType recipient) {
        String desc = recipient.getDescription();
        List<String> compatible = switch (desc) {
            case "A+" -> List.of("A+", "A-", "O+", "O-");
            case "A-" -> List.of("A-", "O-");
            case "B+" -> List.of("B+", "B-", "O+", "O-");
            case "B-" -> List.of("B-", "O-");
            case "AB+" -> List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
            case "AB-" -> List.of("A-", "B-", "AB-", "O-");
            case "O+" -> List.of("O+", "O-");
            case "O-" -> List.of("O-");
            default -> List.of();
        };
        return repository.findByDescriptionIn(compatible);
    }

    // ✅ Shortcut tìm theo chuỗi description
    public List<BloodType> findCompatibleByDescription(String description) {
        BloodType dummy = new BloodType();
        dummy.setDescription(description);
        return findCompatibleTypes(dummy);
    }
}
