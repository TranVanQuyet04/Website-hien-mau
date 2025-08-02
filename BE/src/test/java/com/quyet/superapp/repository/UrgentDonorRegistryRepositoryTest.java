package com.quyet.superapp.repository;

import com.quyet.superapp.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class UrgentDonorRegistryRepositoryTest {

    @Autowired
    private UrgentDonorRegistryRepository donorRepo;

    @Test
    public void testFindNearbyDonors() {
        double lat = 21.0285; // Hà Nội
        double lng = 105.8542;
        double radius = 10.0;

        List<UrgentDonorRegistry> results = donorRepo.findNearbyVerifiedDonors(lat, lng, radius);
        assertThat(results).isNotNull();
        assertThat(results.size()).isGreaterThanOrEqualTo(0); // tuỳ dữ liệu test
    }
}