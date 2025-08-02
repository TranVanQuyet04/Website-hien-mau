package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodBagDTO;
import com.quyet.superapp.dto.UpdateBloodBagRequest;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.mapper.BloodBagMapper;
import com.quyet.superapp.repository.BloodBagRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodBagService {
    private final BloodBagRepository bloodBagRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final DonationRegistrationRepository donationRegistrationRepository;

    public List<BloodBagDTO> getAll() {
        return bloodBagRepository.findAll()
                .stream()
                .map(BloodBagMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BloodBagDTO getById(Long id) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu với ID " + id));
        return BloodBagMapper.toDTO(bag);
    }

    public BloodBagDTO create(BloodBagDTO dto) {
        BloodType bloodType = bloodTypeRepository.findById(Long.parseLong(dto.getBloodType()))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm máu ID " + dto.getBloodType()));

        // ✅ Lấy đơn đăng ký từ DB
        DonationRegistration registration = donationRegistrationRepository.findById(dto.getRegistrationId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn đăng ký ID " + dto.getRegistrationId()));

        // ✅ Truyền thêm biến registration vào mapper
        BloodBag bag = BloodBagMapper.fromDTO(dto, bloodType, registration);

        return BloodBagMapper.toDTO(bloodBagRepository.save(bag));
    }


    @Transactional
    public BloodBagDTO update(Long id, UpdateBloodBagRequest req) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu với ID " + id));
        if (req.getBloodType() != null) {
            BloodType type = bloodTypeRepository.findById(req.getBloodType())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm máu"));
            bag.setBloodType(type);
        }
        if (req.getRh() != null) bag.setRh(req.getRh());
        if (req.getVolume() != null) bag.setVolume(req.getVolume());
        if (req.getHematocrit() != null) bag.setHematocrit(req.getHematocrit());
        if (req.getStatus() != null) bag.setStatus(req.getStatus());
        if (req.getTestStatus() != null) bag.setTestStatus(req.getTestStatus());
        if (req.getNote() != null) bag.setNote(req.getNote());
        bloodBagRepository.save(bag);

        return BloodBagMapper.toDTO(bag);
    }

    public void delete(Long id) {
        if (!bloodBagRepository.existsById(id)) {
            throw new IllegalArgumentException("Không tồn tại túi máu với ID " + id);
        }
        bloodBagRepository.deleteById(id);
    }

    public Optional<BloodBagDTO> findByCode(String code) {
        return bloodBagRepository.findByBagCode(code)
                .map(BloodBagMapper::toDTO);
    }

    public List<BloodBagDTO> getByRegistrationId(Long registrationId) {
        List<BloodBag> bags = bloodBagRepository.findByRegistration_RegistrationId(registrationId);
        return bags.stream()
                .map(BloodBagMapper::toDTO)
                .collect(Collectors.toList());
    }
}
