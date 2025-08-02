package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UrgentRequestDTO;
import com.quyet.superapp.entity.UrgentRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UrgentRequestMapper {

    // ✅ Entity → DTO
    @Mapping(target = "requesterId", source = "requester.userId")
    UrgentRequestDTO toDto(UrgentRequest entity);

    // ✅ DTO → Entity
    // các trường sẽ được set trong service (requester, status, requestDate)
    @Mapping(target = "urgentRequestId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "requestDate", ignore = true)
    UrgentRequest toEntity(UrgentRequestDTO dto);
}
