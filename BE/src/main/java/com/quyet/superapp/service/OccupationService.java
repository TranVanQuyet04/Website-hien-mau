package com.quyet.superapp.service;

import com.quyet.superapp.dto.OccupationDTO;
import com.quyet.superapp.repository.OccupationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OccupationService {

    private final OccupationRepository occupationRepository;

    public List<OccupationDTO> getAllOccupations() {
        return occupationRepository.findAll()
                .stream()
                .map(o -> new OccupationDTO(o.getId(), o.getName()))
                .collect(Collectors.toList());
    }
}

