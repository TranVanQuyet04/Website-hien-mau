package com.quyet.superapp.repository;

import com.quyet.superapp.entity.ReadinessChangeLog;
import com.quyet.superapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReadinessChangeLogRepository extends JpaRepository<ReadinessChangeLog, Long> {
    List<ReadinessChangeLog> findByDonor(User user);

    @Query("SELECT r FROM ReadinessChangeLog r WHERE r.donor = :user "
            + "AND (:from IS NULL OR r.changedAt >= :from) "
            + "AND (:to IS NULL OR r.changedAt <= :to)")
    List<ReadinessChangeLog> findByUserAndDateRange(
            @Param("user") User user,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    List<ReadinessChangeLog> findByDonor_UserIdOrderByChangedAtDesc(Long userId);

}
