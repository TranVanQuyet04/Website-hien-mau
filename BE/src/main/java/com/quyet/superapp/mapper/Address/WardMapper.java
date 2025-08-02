package com.quyet.superapp.mapper.Address;

import com.quyet.superapp.dto.Address.WardDTO;
import com.quyet.superapp.entity.address.Ward;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WardMapper {
    @Mapping(source = "district.districtId", target = "districtId")
    WardDTO toDTO(Ward entity);
}

