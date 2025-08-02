package com.quyet.superapp.service.Address;

import com.quyet.superapp.dto.Address.WardDTO;
import com.quyet.superapp.mapper.Address.WardMapper;
import com.quyet.superapp.repository.address.WardRepository;
import com.quyet.superapp.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WardService {
    private final WardRepository wardRepository;
    private final WardMapper wardMapper;

    public List<WardDTO> suggestWardsByLocation(double lat, double lng) {
        return wardRepository.findAll().stream()
                .filter(w -> w.getLatitude() != null && w.getLongitude() != null)
                .sorted(Comparator.comparingDouble(w ->
                        GeoUtils.calculateDistanceKm(lat, lng, w.getLatitude(), w.getLongitude())
                ))
                .map(wardMapper::toDTO)
                .limit(5)
                .toList();
    }

    public List<WardDTO> getWardsByDistrict(Long districtId) {
        return wardRepository.findByDistrict_DistrictId(districtId).stream()
                .map(wardMapper::toDTO)
                .toList();
    }
}
