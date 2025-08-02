package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.RequestLogDTO;
import com.quyet.superapp.entity.RequestLog;  // ✅ Đúng entity
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestLogMapper {
    @Mapping(target = "actionLabel", ignore = true)
    RequestLogDTO toDTO(RequestLog entity);
    List<RequestLogDTO> toDTOs(List<RequestLog> entities);
}
