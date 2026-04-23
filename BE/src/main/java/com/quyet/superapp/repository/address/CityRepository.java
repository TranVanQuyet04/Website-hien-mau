package com.quyet.superapp.repository.address;

import com.quyet.superapp.entity.address.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameCity(String nameCity);
}
