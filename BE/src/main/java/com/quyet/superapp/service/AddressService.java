package com.quyet.superapp.service;

import com.quyet.superapp.dto.AddressRequestDTO;
import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import com.quyet.superapp.mapper.AddressMapper;
import com.quyet.superapp.repository.address.AddressRepository;
import com.quyet.superapp.repository.address.WardRepository;
import com.quyet.superapp.util.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final WardRepository wardRepository;
    private final AddressMapper addressMapper;

    // ✅ Gợi ý tên đường gần nhất theo tọa độ
    public List<String> suggestAddressStreets(double lat, double lng) {
        return addressRepository.findAll().stream()
                .filter(a -> a.getLatitude() != null && a.getLongitude() != null)
                .sorted(Comparator.comparingDouble(a ->
                        GeoUtils.calculateDistanceKm(lat, lng, a.getLatitude(), a.getLongitude())
                ))
                .map(Address::getAddressStreet)
                .distinct()
                .limit(10)
                .toList();
    }

    // ✅ Tìm địa chỉ gần giống từ DB (autocomplete)
    public List<String> searchSimilarAddresses(String keyword) {
        return addressRepository.searchSimilarAddresses(keyword).stream()
                .map(Address::getAddressStreet)
                .distinct()
                .limit(10)
                .toList();
    }

    // ✅ Chuẩn hóa chuỗi địa chỉ
    public String normalizeStreet(String input) {
        if (input == null) return null;

        String result = input.trim()
                .replaceAll("(?i)\\bQ\\.", "Quận")
                .replaceAll("(?i)\\bP\\.", "Phường")
                .replaceAll("(?i)\\bTP\\.", "Thành phố")
                .replaceAll("\\s+", " ");

        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }

    // ✅ Tạo hoặc cập nhật địa chỉ (không tạo trùng)
    public Address createOrUpdateAddress(AddressRequestDTO dto) {
        Ward ward = wardRepository.findById(dto.getWardId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phường/xã"));

        String normalizedStreet = normalizeStreet(dto.getAddressStreet());

        Optional<Address> existing = addressRepository
                .findByAddressStreetIgnoreCaseAndWard(normalizedStreet, ward);

        if (existing.isPresent()) {
            return existing.get();
        }

        Address address = new Address();
        address.setAddressStreet(normalizedStreet);
        address.setWard(ward);
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());
        return addressRepository.save(address);
    }
}
