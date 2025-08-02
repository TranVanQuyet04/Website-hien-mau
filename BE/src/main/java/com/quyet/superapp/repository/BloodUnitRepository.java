package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodUnit;
import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.enums.BloodUnitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {
    // Lấy danh sách theo trạng thái (ví dụ: AVAILABLE)
    List<BloodUnit> findByStatus(BloodUnitStatus status);

    // Lấy tất cả đơn vị máu theo loại thành phần (Plasma, RBC...)
    List<BloodUnit> findByComponent_BloodComponentId(Long componentId);

    // Tìm các đơn vị máu sắp hết hạn
    List<BloodUnit> findByExpirationDateBefore(LocalDate expiryDate);

    // Tìm theo mã định danh (unitCode nếu bạn có)
    Optional<BloodUnit> findByUnitCode(String unitCode);

    // Lấy danh sách đơn vị máu từ một lệnh tách máu
    List<BloodUnit> findBySeparationOrder(SeparationOrder separationOrder);

    boolean existsByUnitCode(String unitCode);

    @Query("SELECT COUNT(b) FROM BloodUnit b WHERE FUNCTION('DATE', b.createdAt) = CURRENT_DATE")
    Long countTodayUnits();

    long countByUnitCodeStartingWith(String prefix);

    List<BloodUnit> findByStatusAndComponent_BloodComponentId(
            BloodUnitStatus status,
            Long componentId
    );


}
