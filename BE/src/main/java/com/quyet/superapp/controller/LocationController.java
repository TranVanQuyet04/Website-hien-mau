package com.quyet.superapp.controller;

import com.quyet.superapp.entity.address.City;
import com.quyet.superapp.entity.address.District;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.repository.UserProfileRepository;
import com.quyet.superapp.repository.address.CityRepository;
import com.quyet.superapp.repository.address.DistrictRepository;
import com.quyet.superapp.repository.address.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final UserProfileRepository userProfileRepository;

    /**
     * GET /api/locations/cities
     * Lấy toàn bộ danh sách các thành phố
     */
    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityRepository.findAll();
        return ResponseEntity.ok(cities);
    }

    /**
     * GET /api/locations/districts?cityId=1
     * Lấy danh sách quận theo thành phố
     */
    @GetMapping("/districts")
    public ResponseEntity<List<District>> getDistrictsByCity(@RequestParam("cityId") Long cityId) {
        List<District> districts = districtRepository.findByCity_CityId(cityId);
        return ResponseEntity.ok(districts);
    }

    /**
     * GET /api/locations/wards?districtId=1
     * Lấy danh sách phường theo quận
     */
    @GetMapping("/wards")
    public ResponseEntity<List<Ward>> getWardsByDistrict(@RequestParam("districtId") Long districtId) {
        List<Ward> wards = wardRepository.findByDistrict_DistrictId(districtId);
        return ResponseEntity.ok(wards);
    }

    /**
     * GET /api/locations/streets?wardId=3&keyword=Nguyen
     * Gợi ý tên đường dựa trên lịch sử `UserProfile.location`
     */
    @GetMapping("/streets")
    public ResponseEntity<List<String>> getSuggestedStreets(
            @RequestParam("wardId") Long wardId,
            @RequestParam("keyword") String keyword) {
        List<String> streets = userProfileRepository.findSuggestedStreets(wardId, keyword);
        return ResponseEntity.ok(streets);
    }

}
