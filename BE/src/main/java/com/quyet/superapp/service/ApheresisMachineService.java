package com.quyet.superapp.service;

import com.quyet.superapp.dto.ApheresisMachineDTO;
import com.quyet.superapp.entity.ApheresisMachine;
import com.quyet.superapp.mapper.ApheresisMachineMapper;
import com.quyet.superapp.repository.ApheresisMachineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApheresisMachineService {

    private final ApheresisMachineRepository machineRepository;

    /**
     * Lấy tất cả máy tách máu
     */
    public List<ApheresisMachineDTO> getAll() {
        return machineRepository.findAll()
                .stream()
                .map(ApheresisMachineMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tìm theo ID
     */
    public Optional<ApheresisMachineDTO> getById(Long id) {
        return machineRepository.findById(id)
                .map(ApheresisMachineMapper::toDTO);
    }

    /**
     * Tạo mới máy
     */
    public ApheresisMachineDTO create(ApheresisMachineDTO dto) {
        if (machineRepository.findBySerialNumber(dto.getSerialNumber()).isPresent()) {
            throw new IllegalArgumentException("Serial number đã tồn tại.");
        }

        ApheresisMachine machine = ApheresisMachineMapper.toEntity(dto);
        return ApheresisMachineMapper.toDTO(machineRepository.save(machine));
    }

    /**
     * Cập nhật máy
     */
    public ApheresisMachineDTO update(Long id, ApheresisMachineDTO dto) {
        ApheresisMachine machine = machineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy máy với ID " + id));

        machine.setManufacturer(dto.getManufacturer());
        machine.setModel(dto.getModel());
        machine.setLastMaintenance(dto.getLastMaintenance() != null
                ? dto.getLastMaintenance()
                : machine.getLastMaintenance()); // giữ lại giá trị cũ nếu null
        machine.setActive(dto.isActive());
        machine.setNote(dto.getNote());

        return ApheresisMachineMapper.toDTO(machineRepository.save(machine));
    }

    /**
     * Xóa máy
     */
    public void delete(Long id) {
        if (!machineRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tồn tại máy với ID " + id);
        }
        machineRepository.deleteById(id);
    }

    /**
     * Kiểm tra máy có tồn tại không
     */
    public boolean existsById(Long id) {
        return machineRepository.existsById(id);
    }

    /**
     * Danh sách máy đang hoạt động
     */
    public List<ApheresisMachineDTO> getActiveMachines() {
        return machineRepository.findByIsActiveTrue()
                .stream()
                .map(ApheresisMachineMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Danh sách máy cần bảo trì trước ngày được truyền vào
     */
    public List<ApheresisMachineDTO> getMachinesDueForMaintenance(LocalDate beforeDate) {
        return machineRepository.findByLastMaintenanceBefore(beforeDate)
                .stream()
                .map(ApheresisMachineMapper::toDTO)
                .collect(Collectors.toList());
    }
}
