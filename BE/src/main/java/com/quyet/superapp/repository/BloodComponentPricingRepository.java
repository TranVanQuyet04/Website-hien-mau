package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodComponentPricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BloodComponentPricingRepository extends JpaRepository<BloodComponentPricing, Long> {

    /**
     * 🔍 Tìm giá hiện tại của component (ngày áp dụng mới nhất <= hôm nay)
     */
    Optional<BloodComponentPricing> findTopByComponentAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
            BloodComponent component, LocalDate date
    );
}
