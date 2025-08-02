package com.quyet.superapp.controller.Address;

import com.quyet.superapp.dto.Address.CityDTO;
import com.quyet.superapp.mapper.Address.CityMapper;
import com.quyet.superapp.repository.address.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @GetMapping
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toDTO)
                .toList();
    }
}

