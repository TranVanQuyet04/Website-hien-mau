package com.quyet.superapp.util;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.entity.SeparationPresetConfig;
import org.springframework.stereotype.Component;

@Component
public class BloodSeparationCalculator {

    public BloodSeparationSuggestionDTO calculateFromPreset(BloodBag bloodBag, SeparationPresetConfig preset) {
        int volume = bloodBag.getVolume();
        if (volume <= 0) {
            throw new IllegalArgumentException("Thể tích túi máu không hợp lệ.");
        }

        String bloodGroup = (bloodBag.getBloodType() != null && bloodBag.getBloodType().getDescription() != null)
                ? bloodBag.getBloodType().getDescription()
                : "UNK";

        // 1. Tính hồng cầu và huyết tương theo tỷ lệ
        int red = (int) Math.round(volume * preset.getRbcRatio());
        int plasma = (int) Math.round(volume * preset.getPlasmaRatio());

        // 2. Tính tiểu cầu cố định hoặc phần còn lại
        int platelets = preset.getPlateletsFixed() > 0 ? preset.getPlateletsFixed() : volume - red - plasma;

        // 3. Cân bằng nếu tổng vượt quá volume
        int total = red + plasma + platelets;
        if (total > volume) {
            int excess = total - volume;
            plasma = Math.max(0, plasma - excess);

            // Nếu vẫn dư, giảm tiếp platelets
            total = red + plasma + platelets;
            if (total > volume) {
                platelets = Math.max(0, volume - red - plasma);
            }
        }

        return new BloodSeparationSuggestionDTO(
                red,
                plasma,
                platelets,
                "PRC-" + bloodGroup,
                "FFP-" + bloodGroup,
                "PLT-" + bloodGroup,
                String.format("Preset: %s - %s - %skg", preset.getMethod(), preset.getGender(), preset.getMinWeight())
        );
    }
}
