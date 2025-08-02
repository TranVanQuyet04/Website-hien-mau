package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodPrice;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.entity.BloodComponent;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BloodPriceRepository extends JpaRepository<BloodPrice, Long> {
    Optional<BloodPrice> findByBloodTypeAndBloodComponent(BloodType bloodType, BloodComponent component);
}
