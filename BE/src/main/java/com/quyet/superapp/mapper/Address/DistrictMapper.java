package com.quyet.superapp.mapper.Address;


import com.quyet.superapp.dto.Address.DistrictDTO;
import com.quyet.superapp.entity.address.District;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
    @Mapping(source = "city.cityId", target = "cityId")
    DistrictDTO toDTO(District entity);
}
