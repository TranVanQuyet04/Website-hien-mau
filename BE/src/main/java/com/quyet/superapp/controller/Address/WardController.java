package com.quyet.superapp.controller.Address;

import com.quyet.superapp.dto.Address.WardDTO;
import com.quyet.superapp.mapper.Address.WardMapper;
import com.quyet.superapp.repository.address.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
@RequiredArgsConstructor
public class WardController {
    private final WardRepository wardRepository;
    private final WardMapper wardMapper;

    @GetMapping("/by-district")
    public List<WardDTO> getByDistrict(@RequestParam Long districtId) {
        return wardRepository.findByDistrict_DistrictId(districtId).stream()
                .map(wardMapper::toDTO)
                .toList();
    }
}

