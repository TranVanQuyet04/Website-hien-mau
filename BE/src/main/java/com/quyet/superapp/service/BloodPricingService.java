package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodComponentPricing;
import com.quyet.superapp.repository.BloodComponentPricingRepository;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BloodPricingService {

    private final BloodComponentPricingRepository pricingRepo;
    private final BloodComponentRepository componentRepo;


    public int getLatestPriceForComponent(Long componentId) {
        BloodComponent component = componentRepo.findById(componentId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy thành phần máu"));
        return pricingRepo
                .findTopByComponentAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(component, LocalDate.now())
                .map(BloodComponentPricing::getUnitPrice)
                .orElseThrow(() -> new RuntimeException("❌ Không có giá áp dụng"));
    }

}
