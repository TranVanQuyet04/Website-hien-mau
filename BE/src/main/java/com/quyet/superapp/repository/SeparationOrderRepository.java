package com.quyet.superapp.repository;

import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.enums.SeparationMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeparationOrderRepository extends JpaRepository<SeparationOrder, Integer> {
    // 🔍 Tìm theo loại tách máu (LY_TAM, GAN_TACH, ...)
    List<SeparationOrder> findBySeparationMethod(SeparationMethod method);

    // 📅 Tìm các thao tác tách máu thực hiện trong khoảng thời gian
    List<SeparationOrder> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end);

    // 👤 Tìm tất cả thao tác của 1 nhân viên theo ID
    List<SeparationOrder> findByPerformedBy_UserId(Long userId);

    // 📦 Tìm các tách máu thực hiện từ túi máu cụ thể
    List<SeparationOrder> findByBloodBag_BagCode(String bagCode);

    // ✅ Kiểm tra đã tách máu từ túi nào đó chưa
    boolean existsByBloodBag_BloodBagId(Long bloodBagId);
    Optional<SeparationOrder> findFirstByBloodBag_BloodBagId(Long bloodBagId);

}
