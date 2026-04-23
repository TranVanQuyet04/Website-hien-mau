package com.quyet.superapp.controller;

import com.quyet.superapp.dto.OccupationDTO;
import com.quyet.superapp.service.OccupationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/occupations")
@RequiredArgsConstructor
public class OccupationController {

    private final OccupationService occupationService;

    @GetMapping
    public ResponseEntity<List<OccupationDTO>> getAllOccupations() {
        return ResponseEntity.ok(occupationService.getAllOccupations());
    }
}

