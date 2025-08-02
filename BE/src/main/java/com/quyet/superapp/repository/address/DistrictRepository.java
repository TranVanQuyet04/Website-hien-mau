package com.quyet.superapp.repository.address;

import com.quyet.superapp.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByCity_CityId(Long cityId);

}
