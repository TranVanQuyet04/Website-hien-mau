package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.BloodPrice;
import com.quyet.superapp.repository.BloodPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BloodPriceService {

    private final BloodPriceRepository bloodPriceRepository;

    // ✅ Tính số tiền cần thanh toán dựa trên bloodType + component + quantity
    public double calculateBloodRequestAmount(BloodRequest request) {
        Optional<BloodPrice> priceOpt = bloodPriceRepository
                .findByBloodTypeAndBloodComponent(request.getBloodType(), request.getComponent());

        if (priceOpt.isEmpty()) return 0.0;

        BloodPrice price = priceOpt.get();
        return price.getPricePerBag() * request.getQuantityBag(); // hoặc cộng thêm theo ML nếu cần
    }
}
