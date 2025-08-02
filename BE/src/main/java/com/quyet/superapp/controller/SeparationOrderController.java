package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.dto.CreateSeparationWithSuggestionRequest;
import com.quyet.superapp.dto.SeparationOrderDTO;
import com.quyet.superapp.dto.SeparationResultDTO;
import com.quyet.superapp.entity.SeparationOrder;
import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.mapper.SeparationOrderMapper;
import com.quyet.superapp.service.SeparationOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/separation-orders")
public class SeparationOrderController {

    private final SeparationOrderService separationOrderService;

    // ✅ Tạo mới lệnh tách máu có tính toán preset
    @PostMapping("/create-with-suggestion")
    public ResponseEntity<SeparationResultDTO> createWithSuggestion(
            @RequestBody CreateSeparationWithSuggestionRequest request
    ) {
        return ResponseEntity.ok(separationOrderService.createWithSuggestion(request));
    }

    // ✅ Tạo mới lệnh tách máu thủ công (chỉ lưu lệnh, không sinh đơn vị máu)
    @PostMapping("/basic-manual")
    public ResponseEntity<SeparationOrderDTO> createBasicManual(@RequestBody SeparationOrderDTO dto) {
        var order = separationOrderService.createSeparationOrder(
                dto.getBloodBagId(),
                dto.getPerformedById(),
                dto.getApheresisMachineId(),
                dto.getSeparationType(),
                dto.getNote()
        );
        return ResponseEntity.ok(SeparationOrderMapper.toDTO(order));
    }

    // ✅ Tạo mới lệnh tách máu thủ công có sinh đơn vị máu (theo tỷ lệ mặc định)
    @PostMapping("/create-manual")
    public ResponseEntity<SeparationResultDTO> createManualWithUnits(
            @RequestParam Long bloodBagId,
            @RequestParam Long operatorId,
            @RequestParam(required = false) Long machineId,
            @RequestParam SeparationMethod type,
            @RequestParam int redCellsMl,
            @RequestParam int plasmaMl,
            @RequestParam int plateletsMl,
            @RequestParam(required = false) String note
    ) {
        return ResponseEntity.ok(
                separationOrderService.createSeparationOrderEntity(
                        bloodBagId, operatorId, machineId, type,
                        redCellsMl, plasmaMl, plateletsMl, note
                )
        );
    }


    // ✅ Lấy tất cả lệnh tách
    @GetMapping
    public ResponseEntity<List<SeparationOrderDTO>> getAll() {
        List<SeparationOrderDTO> result = separationOrderService.getAll()
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo loại tách
    @GetMapping("/type")
    public ResponseEntity<List<SeparationOrderDTO>> getByType(@RequestParam SeparationMethod type) {
        List<SeparationOrderDTO> result = separationOrderService.findByType(type)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo nhân viên thao tác
    @GetMapping("/operator")
    public ResponseEntity<List<SeparationOrderDTO>> getByOperator(@RequestParam Long userId) {
        List<SeparationOrderDTO> result = separationOrderService.findByOperator(userId)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Lấy theo mã túi máu
    @GetMapping("/bag")
    public ResponseEntity<List<SeparationOrderDTO>> getByBagCode( @RequestParam String bagCode) {
        List<SeparationOrderDTO> result = separationOrderService.findByBagCode(bagCode)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // ✅ Kiểm tra túi máu đã được tách chưa
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfSeparated(@RequestParam Long bloodBagId) {
        return ResponseEntity.ok(separationOrderService.hasBeenSeparated(bloodBagId));
    }

    // ✅ Lọc theo khoảng thời gian
    @GetMapping("/time-range")
    public ResponseEntity<List<SeparationOrderDTO>> getByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        List<SeparationOrderDTO> result = separationOrderService.findBetween(start, end)
                .stream()
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    @PostMapping("/manual-with-input")
    public ResponseEntity<SeparationResultDTO> createManualWithInput(
            @RequestParam Long bloodBagId,
            @RequestParam Long operatorId,
            @RequestParam SeparationMethod type,
            @RequestParam int redCellsMl,
            @RequestParam int plasmaMl,
            @RequestParam int plateletsMl,
            @RequestParam(required = false) String note
    ) {
        return ResponseEntity.ok(
                separationOrderService.createManualSeparationWithInput(
                        bloodBagId, operatorId, type, redCellsMl, plasmaMl, plateletsMl, note
                )
        );
    }
    @PutMapping("/update-suggestion")
    public ResponseEntity<Void> updateSeparationSuggestion(
            @RequestParam Long bloodBagId,
            @RequestBody BloodSeparationSuggestionDTO dto
    ) {
        separationOrderService.updateSeparationByBloodBagId(bloodBagId, dto);
        return ResponseEntity.ok().build();
    }


}
