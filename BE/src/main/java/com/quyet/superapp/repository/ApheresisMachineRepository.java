package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ApheresisMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApheresisMachineRepository extends JpaRepository<ApheresisMachine,Long> {
    // 🔍 Tìm theo số serial
    Optional<ApheresisMachine> findBySerialNumber(String serialNumber);

    // 🔍 Tìm tất cả máy đang hoạt động
    List<ApheresisMachine> findByIsActiveTrue();

    // 🔍 Tìm theo nhà sản xuất
    List<ApheresisMachine> findByManufacturerIgnoreCase(String manufacturer);

    // 🔍 Lọc máy cần bảo trì (quá ngày)
    List<ApheresisMachine> findByLastMaintenanceBefore(java.time.LocalDate deadline);
}
