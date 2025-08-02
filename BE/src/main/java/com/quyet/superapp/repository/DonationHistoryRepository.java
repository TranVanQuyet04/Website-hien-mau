package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonationHistory;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {

    List<DonationHistory> findByDonorOrderByDonatedAtDesc(User donor);

    List<DonationHistory> findByDonor(User user);
}
