package com.quyet.superapp.repository;

import com.quyet.superapp.entity.LabTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, Long> {
    // Tìm theo đơn vị máu
    Optional<LabTestResult> findByBloodUnit_BloodUnitId(Long bloodUnitId);

    // Kiểm tra xem đơn vị máu đã được xét nghiệm chưa
    boolean existsByBloodUnit_BloodUnitId(Long bloodUnitId);

    // Tìm các kết quả xét nghiệm bởi một nhân viên cụ thể
    List<LabTestResult> findByTestedBy_UserId(Long userId);

    // Tìm tất cả kết quả trong khoảng thời gian
    List<LabTestResult> findByTestedAtBetween(LocalDateTime start, LocalDateTime end);

    // Tìm tất cả kết quả đạt chuẩn
    List<LabTestResult> findByPassedTrue();
}
