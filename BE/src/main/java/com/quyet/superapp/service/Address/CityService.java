package com.quyet.superapp.service.Address;

import com.quyet.superapp.dto.Address.CityDTO;
import com.quyet.superapp.mapper.Address.CityMapper;
import com.quyet.superapp.repository.address.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public List<CityDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toDTO)
                .toList();
    }
}