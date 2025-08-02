package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.VnPayment;
import org.springframework.data.jpa.repository.JpaRepository;



import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface VnPaymentRepository extends JpaRepository<VnPayment, Long> {
    long countByStatus(String status);

    List<VnPayment> findAllByBloodRequest_Id(Long requestId);


}