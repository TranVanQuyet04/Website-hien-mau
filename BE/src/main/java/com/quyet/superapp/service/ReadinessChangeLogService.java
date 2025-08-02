package com.quyet.superapp.service;

import com.quyet.superapp.dto.ReadinessChangeLogDTO;
import com.quyet.superapp.mapper.ReadinessChangeLogMapper;
import com.quyet.superapp.repository.ReadinessChangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadinessChangeLogService {
    private final ReadinessChangeLogRepository logRepo;
    private final ReadinessChangeLogMapper logMapper;

    public List<ReadinessChangeLogDTO> getHistoryByUserId(Long userId) {
        return logRepo.findByDonor_UserIdOrderByChangedAtDesc(userId)
                .stream()
                .map(logMapper::toDTO)
                .collect(Collectors.toList());
    }
}

