package com.quyet.superapp.service;

import com.quyet.superapp.dto.PreDonationTestDTO;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.entity.PreDonationTest;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.mapper.PreDonationTestMapper;
import com.quyet.superapp.repository.BloodTypeRepository;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.repository.HealthCheckFormRepository;
import com.quyet.superapp.repository.PreDonationTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreDonationTestService {

    private final PreDonationTestRepository testRepo;
    private final HealthCheckFormRepository healthCheckFormRepo;
    private final BloodTypeRepository bloodTypeRepo;
    private final PreDonationTestMapper preDonationTestMapper;


    public List<PreDonationTestDTO> getAll() {
        return testRepo.findAll().stream()
                .map(preDonationTestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PreDonationTestDTO getById(Long id) {
        return testRepo.findById(id)
                .map(preDonationTestMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi xét nghiệm"));
    }

    public PreDonationTestDTO create(PreDonationTestDTO dto) {
        if (testRepo.existsByHealthCheckForm_Id(dto.getHealthCheckId())) {
            throw new IllegalStateException("Đã có xét nghiệm cho phiếu khám này.");
        }

        HealthCheckForm form = healthCheckFormRepo.findById(dto.getHealthCheckId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu khám sức khỏe"));

        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu"));

        PreDonationTest entity = preDonationTestMapper.toEntity(dto, form, bloodType);
        return preDonationTestMapper.toDTO(testRepo.save(entity));
    }

    public void delete(Long id) {
        if (!testRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy bản ghi để xoá");
        }
        testRepo.deleteById(id);
    }
    public PreDonationTestDTO update(PreDonationTestDTO dto) {
        PreDonationTest entity = testRepo.findById(dto.getPreDonationTestId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bản ghi để cập nhật"));

        HealthCheckForm form = healthCheckFormRepo.findById(dto.getHealthCheckId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu khám sức khỏe"));

        BloodType bloodType = bloodTypeRepo.findById(dto.getBloodTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhóm máu"));

        preDonationTestMapper.updateEntity(entity, dto, form, bloodType);
        return preDonationTestMapper.toDTO(testRepo.save(entity));
    }

    public boolean existsByDonationId(Long donationId) {
        return testRepo.existsByHealthCheckForm_Donation_DonationId(donationId);
    }


}
