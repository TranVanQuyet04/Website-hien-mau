package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.DonationHistoryDTO;
import com.quyet.superapp.entity.DonationHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DonationHistoryMapper {

    @Mapping(target = "donorName", source = "donor.userProfile.fullName") // map từ entity sang DTO
    DonationHistoryDTO toDTO(DonationHistory history);

    List<DonationHistoryDTO> toDTOs(List<DonationHistory> list);
}
