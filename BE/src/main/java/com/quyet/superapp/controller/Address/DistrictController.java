package com.quyet.superapp.controller.Address;

import com.quyet.superapp.dto.Address.DistrictDTO;
import com.quyet.superapp.mapper.Address.DistrictMapper;
import com.quyet.superapp.repository.address.DistrictRepository;
import com.quyet.superapp.service.Address.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping(value = "/by-city", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DistrictDTO>> getDistrictsByCityId(@RequestParam Long cityId) {
        List<DistrictDTO> districts = districtService.getDistrictsByCity(cityId);
        return ResponseEntity.ok(districts);
    }
}

