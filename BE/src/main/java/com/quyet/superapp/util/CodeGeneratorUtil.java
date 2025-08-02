package com.quyet.superapp.util;

import com.quyet.superapp.entity.BloodBag;
import com.quyet.superapp.repository.BloodUnitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CodeGeneratorUtil {
    private static final Random RANDOM = new Random();

    public static String generateUnitCode(BloodBag bag, String componentCode, long index) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String bloodType = bag.getBloodType().getDescription(); // AB, A, B, O
        return String.format("%s-%s-%s-%03d", date, bloodType, componentCode, index);
    }
    // Sinh mã unitCode đảm bảo không trùng bằng cách đếm số lượng đã có trong ngày và tăng dần
    public static String generateUniqueUnitCode(BloodBag bag, String componentCode, BloodUnitRepository repository) {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        long index = repository.countByUnitCodeStartingWith(datePrefix); // Đếm số mã hôm nay đã có

        String unitCode;
        do {
            index++;
            unitCode = generateUnitCode(bag, componentCode, index);
        } while (repository.existsByUnitCode(unitCode)); // Tránh trường hợp trùng khi chạy song song

        return unitCode;
    }
}
