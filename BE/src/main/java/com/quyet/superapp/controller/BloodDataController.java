package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodComponentFullDTO;
import com.quyet.superapp.dto.BloodMasterDataResponseDTO;
import com.quyet.superapp.dto.BloodTypeFullDTO;
import com.quyet.superapp.dto.SimpleIdNameDTO;
import com.quyet.superapp.service.BloodComponentService;
import com.quyet.superapp.service.BloodDataService;
import com.quyet.superapp.service.BloodTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-data")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BloodDataController {

    private final BloodDataService bloodDataService;
    private final BloodTypeService bloodTypeService;
    private final BloodComponentService bloodComponentService;

    // ✅ Lấy danh sách nhóm máu đơn giản (dùng cho combobox)
    @GetMapping("/types")
    public ResponseEntity<List<SimpleIdNameDTO>> getBloodTypes() {
        return ResponseEntity.ok(bloodDataService.getAllBloodTypes());
    }

    // ✅ Lấy danh sách thành phần máu đơn giản
    @GetMapping("/components")
    public ResponseEntity<List<SimpleIdNameDTO>> getBloodComponents() {
        return ResponseEntity.ok(bloodDataService.getAllBloodComponents());
    }

    // ✅ Lấy cả hai (dùng để load master data nhanh)
    @GetMapping("/master")
    public ResponseEntity<BloodMasterDataResponseDTO> getMasterData() {
        List<BloodTypeFullDTO> types = bloodTypeService.getAllFull();
        List<BloodComponentFullDTO> components = bloodComponentService.getAllFull(); // bạn cần thêm hàm getAllFull() nếu chưa có
        return ResponseEntity.ok(
                BloodMasterDataResponseDTO.builder()
                        .bloodTypes(types)
                        .bloodComponents(components)
                        .build()
        );
    }


    // ✅ Lấy danh sách nhóm máu đầy đủ (dùng cho bảng như ảnh bạn gửi)
    @GetMapping("/types/full")
    public ResponseEntity<List<BloodTypeFullDTO>> getFullBloodTypes() {
        return ResponseEntity.ok(bloodDataService.getAllBloodTypeFull());
    }
}
