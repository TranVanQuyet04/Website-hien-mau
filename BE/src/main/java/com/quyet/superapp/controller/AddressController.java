package com.quyet.superapp.controller;

import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.quyet.superapp.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/nearby-streets")
    public List<String> getNearbyStreetNames(@RequestParam Double lat, @RequestParam Double lng) {
        return addressService.suggestAddressStreets(lat, lng);
    }


    @GetMapping("/search")
    public ResponseEntity<List<String>> searchSimilar(@RequestParam String keyword) {
        List<String> suggestions = addressService.searchSimilarAddresses(keyword);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AddressRequestDTO dto) {
        Address saved = addressService.createOrUpdateAddress(dto);
        return ResponseEntity.ok(saved);
    }
}