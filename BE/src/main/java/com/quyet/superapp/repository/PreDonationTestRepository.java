package com.quyet.superapp.repository;


import com.quyet.superapp.entity.PreDonationTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreDonationTestRepository extends JpaRepository<PreDonationTest, Long> {
    boolean existsByHealthCheckForm_Id(Long healthCheckId);
    Optional<PreDonationTest> findByHealthCheckForm_Id(Long healthCheckId);
    boolean existsByHealthCheckForm_Donation_DonationId(Long donationId);
}
