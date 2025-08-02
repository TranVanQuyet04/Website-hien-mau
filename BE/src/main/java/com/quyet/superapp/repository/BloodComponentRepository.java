package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BloodComponentRepository extends JpaRepository<BloodComponent, Long> {

    boolean existsByCode(String code);


    Optional<BloodComponent> findByBloodComponentIdAndIsActiveTrue(Long id);

    Optional<BloodComponent> findByName(String name);

    // (tuỳ chọn) Tìm theo code y tế: PRC, FFP, PLT
    Optional<BloodComponent> findByCode(String code);
    List<BloodComponent> findAllByOrderByBloodComponentIdAsc();
    List<BloodComponent> findByIsApheresisCompatibleTrueAndIsActiveTrueOrderByBloodComponentIdAsc();

    List<BloodComponent> findByIsApheresisCompatibleTrue();


    // 🔍 Tìm theo tên thành phần máu (không phân biệt hoa thường)
    Optional<BloodComponent> findByNameIgnoreCase(String name);

    // 🔍 Tìm theo mã viết tắt (code y tế): PRC, FFP, PLT
    Optional<BloodComponent> findByCodeIgnoreCase(String code);

    // 🔍 Tìm theo danh sách tên (phục vụ cho dropdown lọc)
    List<BloodComponent> findByNameIn(List<String> names);

    // 🔍 Tìm tất cả theo nhóm loại: Ví dụ: ["Huyết tương", "Tiểu cầu"]
    List<BloodComponent> findByCodeIn(List<String> codes);
}
