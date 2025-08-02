package com.quyet.superapp.service.Address;

import com.quyet.superapp.dto.Address.DistrictDTO;
import com.quyet.superapp.mapper.Address.DistrictMapper;
import com.quyet.superapp.repository.address.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public List<DistrictDTO> getDistrictsByCity(Long cityId) {
        return districtRepository.findByCity_CityId(cityId).stream()
                .map(districtMapper::toDTO)
                .toList();
    }
}
