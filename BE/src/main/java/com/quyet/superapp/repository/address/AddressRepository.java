package com.quyet.superapp.repository.address;

import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByAddressStreetIgnoreCaseAndWard(String addressStreet, Ward ward);

    @Query("""
    SELECT a FROM Address a
    WHERE a.latitude IS NOT NULL AND a.longitude IS NOT NULL
    ORDER BY FUNCTION('distance', a.latitude, a.longitude, :lat, :lng)
""")
    List<Address> findClosestAddresses(@Param("lat") Double lat, @Param("lng") Double lng);


    @Query("SELECT a FROM Address a WHERE LOWER(a.addressStreet) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Address> searchSimilarAddresses(@Param("keyword") String keyword);

}
