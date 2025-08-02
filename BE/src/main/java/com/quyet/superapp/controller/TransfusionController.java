package com.quyet.superapp.controller;

import com.quyet.superapp.dto.TransfusionDTO;
import com.quyet.superapp.entity.Transfusion;
import com.quyet.superapp.mapper.TransfusionMapper;
import com.quyet.superapp.service.TransfusionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transfusions")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class TransfusionController {

    private final TransfusionService svc;
    private final TransfusionMapper map;

    @GetMapping
    public List<TransfusionDTO> getAll() {
        return svc.findAll().stream()
                .map(map::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransfusionDTO> getById(@PathVariable Long id) {
        Transfusion entity = svc.findById(id);
        return ResponseEntity.ok(map.toDTO(entity));
    }


    @GetMapping("/filter")
    public List<TransfusionDTO> filterTransfusions(
            @RequestParam(required = false) Long recipientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        return svc.filterTransfusions(recipientId, fromDate, toDate)
                .stream().map(map::toDTO).toList();
    }


}
