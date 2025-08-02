package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Transfusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransfusionRepository extends JpaRepository<Transfusion, Long> {

    @Query("SELECT t FROM Transfusion t " +
            "WHERE (:recipientId IS NULL OR t.recipient.id = :recipientId) " +
            "AND (:fromDate IS NULL OR t.transfusionDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.transfusionDate <= :toDate)")
    List<Transfusion> filterTransfusions(
            @Param("recipientId") Long recipientId,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}
