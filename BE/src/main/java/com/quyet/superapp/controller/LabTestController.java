package com.quyet.superapp.controller;

import com.quyet.superapp.dto.CreateLabTestRequest;
import com.quyet.superapp.dto.LabTestResultDTO;
import com.quyet.superapp.service.LabTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    public ResponseEntity<LabTestResultDTO> createLabTest(@RequestBody CreateLabTestRequest request) {
        LabTestResultDTO result = labTestService.createLabTestResult(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{bloodUnitId}")
    public ResponseEntity<LabTestResultDTO> getByBloodUnit(@PathVariable Long bloodUnitId) {
        return labTestService.getByBloodUnit(bloodUnitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check/{bloodUnitId}")
    public ResponseEntity<Boolean> checkTested(@PathVariable Long bloodUnitId) {
        return ResponseEntity.ok(labTestService.isTested(bloodUnitId));
    }

    @GetMapping
    public ResponseEntity<List<LabTestResultDTO>> getAll() {
        List<LabTestResultDTO> results = labTestService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{labTestResultId}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long labTestResultId) {
        labTestService.deleteResult(labTestResultId);
        return ResponseEntity.noContent().build();
    }
}
