package com.quyet.superapp.service;

import com.quyet.superapp.entity.SeparationPresetConfig;
import com.quyet.superapp.repository.SeparationPresetConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeparationPresetService {

    private final SeparationPresetConfigRepository presetRepository;

    public SeparationPresetConfig getPreset(String gender, Double weight, String method, boolean leukoreduced) {
        return presetRepository.findBestMatch(gender, weight, method, leukoreduced)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy preset phù hợp cho cấu hình được chọn."));
    }
}
