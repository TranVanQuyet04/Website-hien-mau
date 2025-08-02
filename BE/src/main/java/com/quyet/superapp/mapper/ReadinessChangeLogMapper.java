package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.ReadinessChangeLogDTO;
import com.quyet.superapp.entity.ReadinessChangeLog;
import org.springframework.stereotype.Component;

@Component
public class ReadinessChangeLogMapper {
    public ReadinessChangeLogDTO toDTO(ReadinessChangeLog log) {
        return new ReadinessChangeLogDTO(
                log.getFromLevel(),
                log.getToLevel(),
                log.getChangedAt()
        );
    }
}
