package com.quyet.superapp.repository;

import com.quyet.superapp.entity.SeparationPresetConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeparationPresetConfigRepository extends JpaRepository<SeparationPresetConfig, Long> {
    @Query("""
SELECT s FROM SeparationPresetConfig s
WHERE s.gender = :gender
  AND s.minWeight <= :weight
  AND s.method = :method
  AND s.leukoreduced = :leukoreduced
ORDER BY s.minWeight DESC
""")
    Optional<SeparationPresetConfig> findBestMatch( @Param("bloodType") String bloodType,
                                                    @Param("weight") Double weight,
                                                    @Param("gender") String gender,
                                                    @Param("isEmergency") boolean isEmergency);

}
