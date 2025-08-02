package com.quyet.superapp.controller.Address;

import com.quyet.superapp.dto.Address.DistrictDTO;
import com.quyet.superapp.mapper.Address.DistrictMapper;
import com.quyet.superapp.repository.address.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    @GetMapping("/by-city")
    public List<DistrictDTO> getByCity(@RequestParam Long cityId) {
        return districtRepository.findByCity_CityId(cityId).stream()
                .map(districtMapper::toDTO)
                .toList();
    }
}

