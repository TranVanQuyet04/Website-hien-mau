package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodComponentFullDTO;
import com.quyet.superapp.dto.BloodTypeFullDTO;
import com.quyet.superapp.dto.SimpleIdNameDTO;
import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.mapper.BloodComponentMapper;
import com.quyet.superapp.mapper.BloodTypeMapper;
import com.quyet.superapp.repository.BloodComponentRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodDataService {

    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository bloodComponentRepository;
    private final BloodTypeMapper bloodTypeMapper;
    private final BloodComponentMapper bloodComponentMapper;

    public List<BloodComponentFullDTO> getAllBloodComponentFull() {
        return bloodComponentRepository.findAll()
                .stream()
                .map(bloodComponentMapper::toFullDTO)
                .collect(Collectors.toList());
    }


    public List<SimpleIdNameDTO> getAllBloodTypes() {
        return bloodTypeRepository.findAll()
                .stream()
                .map(type -> new SimpleIdNameDTO(
                        type.getBloodTypeId(),
                        type.getDescription() + (type.getRh().equalsIgnoreCase("POSITIVE") ? "+" : "-")
                ))
                .collect(Collectors.toList());
    }


    public List<BloodTypeFullDTO> getAllBloodTypeFull() {
        return bloodTypeRepository.findAll()
                .stream()
                .map(bloodTypeMapper::toFullDTO)
                .collect(Collectors.toList());
    }

    public List<SimpleIdNameDTO> getAllBloodComponents() {
        return bloodComponentRepository.findAll()
                .stream()
                .map(comp -> new SimpleIdNameDTO(comp.getBloodComponentId(), comp.getName()))
                .collect(Collectors.toList());
    }
}
