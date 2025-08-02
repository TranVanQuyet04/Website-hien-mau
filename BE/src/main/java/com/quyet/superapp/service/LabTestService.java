package com.quyet.superapp.service;

import com.quyet.superapp.dto.CreateLabTestRequest;
import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.entity.LabTestResult;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.LabTestResultMapper;
import com.quyet.superapp.repository.BloodUnitRepository;
import com.quyet.superapp.repository.LabTestResultRepository;
import com.quyet.superapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LabTestService {

    private final LabTestResultRepository labTestResultRepository;
    private final BloodUnitRepository bloodUnitRepository;
    private final UserRepository userRepository;
    /**
     * Tạo kết quả xét nghiệm cho một đơn vị máu
     */
    @Transactional
    public LabTestResultDTO createLabTestResult(CreateLabTestRequest request) {
        // 1. Kiểm tra đơn vị máu có tồn tại không
        BloodUnit unit = bloodUnitRepository.findById(request.getBloodUnitId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn vị máu."));

        // 2. Kiểm tra đã có kết quả chưa
        if (labTestResultRepository.existsByBloodUnit_BloodUnitId(unit.getBloodUnitId())) {
            throw new IllegalStateException("Đơn vị máu đã được xét nghiệm.");
        }

        // 3. Kiểm tra người xét nghiệm
        User tester = userRepository.findById(request.getTestedById())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên xét nghiệm."));

        // 4. Tạo kết quả xét nghiệm
        LabTestResult result = new LabTestResult();
        result.setBloodUnit(unit);
        result.setHivNegative(request.isHivNegative());
        result.setHbvNegative(request.isHbvNegative());
        result.setHcvNegative(request.isHcvNegative());
        result.setSyphilisNegative(request.isSyphilisNegative());
        result.setMalariaNegative(request.isMalariaNegative());

        // 5. Đánh giá tổng hợp
        boolean passed = request.isHivNegative() &&
                request.isHbvNegative() &&
                request.isHcvNegative() &&
                request.isSyphilisNegative() &&
                request.isMalariaNegative();
        result.setPassed(passed);
        result.setTestedAt(LocalDateTime.now());
        result.setTestedBy(tester);

        // 6. Lưu vào DB
        LabTestResult saved = labTestResultRepository.save(result);

        return LabTestResultMapper.toDTO(saved);
    }
    /**
     * Truy xuất kết quả theo đơn vị máu
     */
    public Optional<LabTestResultDTO> getByBloodUnit(Long bloodUnitId) {
        return labTestResultRepository.findByBloodUnit_BloodUnitId(bloodUnitId)
                .map(LabTestResultMapper::toDTO);
    }
    /**
     * Kiểm tra xem đơn vị máu đã được xét nghiệm chưa
     */
    public boolean isTested(Long bloodUnitId) {
        return labTestResultRepository.existsByBloodUnit_BloodUnitId(bloodUnitId);
    }

    public List<LabTestResultDTO> getAllResults() {
        return labTestResultRepository.findAll().stream()
                .map(LabTestResultMapper::toDTO)
                .toList();
    }

    @Transactional
    public void deleteResult(Long id) {
        labTestResultRepository.deleteById(id);
    }
}
