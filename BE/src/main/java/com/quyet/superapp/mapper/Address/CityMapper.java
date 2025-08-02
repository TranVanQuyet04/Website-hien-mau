package com.quyet.superapp.mapper.Address;

import com.quyet.superapp.dto.Address.CityDTO;
import com.quyet.superapp.entity.address.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityDTO toDTO(City entity);
}

