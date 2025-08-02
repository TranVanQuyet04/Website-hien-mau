package com.quyet.superapp.repository.address;

import com.quyet.superapp.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WardRepository extends JpaRepository<Ward, Long> {

    List<Ward> findByDistrict_DistrictId(Long districtId);

    // ✅ Tìm theo tên phường (bỏ qua hoa thường)
    Optional<Ward> findByWardNameIgnoreCase(String wardName);
}
