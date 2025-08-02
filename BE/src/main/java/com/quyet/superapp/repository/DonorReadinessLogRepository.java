package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonorReadinessLog;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorReadinessLogRepository extends JpaRepository<DonorReadinessLog, Long> {
    List<DonorReadinessLog> findByUser(User user);
}
