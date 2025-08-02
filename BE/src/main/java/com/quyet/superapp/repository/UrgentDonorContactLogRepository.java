package com.quyet.superapp.repository;

import com.quyet.superapp.entity.UrgentDonorContactLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrgentDonorContactLogRepository extends JpaRepository<UrgentDonorContactLog, Long> {
    @Query("SELECT COUNT(l) > 0 FROM UrgentDonorContactLog l " +
            "WHERE l.donor.userId = :donorId AND l.bloodRequest.id = :requestId")
    boolean existsByDonorAndRequest(@Param("donorId") Long donorId,
                                    @Param("requestId") Long requestId);


    @Query("SELECT l FROM UrgentDonorContactLog l WHERE l.bloodRequest.id = :requestId")
    List<UrgentDonorContactLog> findByRequestId(@Param("requestId") Long requestId);}
