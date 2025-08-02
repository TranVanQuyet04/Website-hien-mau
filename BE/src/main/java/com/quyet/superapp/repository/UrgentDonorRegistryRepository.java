package com.quyet.superapp.repository;

import com.quyet.superapp.dto.DonorDetailDTO;
import com.quyet.superapp.dto.ReadinessStackedDTO;
import com.quyet.superapp.entity.UrgentDonorRegistry;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.DonorReadinessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrgentDonorRegistryRepository extends JpaRepository<UrgentDonorRegistry, Long> {

    @Query("""
SELECT new com.quyet.superapp.dto.DonorDetailDTO(
    p.fullName,
    CONCAT(bt.description, CASE WHEN bt.rh = '+' THEN '+' ELSE '-' END),
    bc.name,
    CAST(r.readinessLevel AS string),
    COUNT(d),
    MAX(d.donationDate)
)
FROM UrgentDonorRegistry r
JOIN r.donor u
JOIN u.userProfile p
JOIN r.bloodType bt
JOIN r.bloodComponent bc
LEFT JOIN Donation d ON d.registration.user.userId = u.userId AND d.status = 'DONATED'
WHERE r.isVerified = true
  AND (:from IS NULL OR r.createdAt >= :from)
  AND (:to IS NULL OR r.createdAt <= :to)
  AND (:bloodTypeId IS NULL OR bt.bloodTypeId = :bloodTypeId)
  AND (:componentId IS NULL OR bc.bloodComponentId = :componentId)
  AND (:readinessLevel IS NULL OR r.readinessLevel = :readinessLevel)
GROUP BY p.fullName, bt.description, bt.rh, bc.name, r.readinessLevel
""")
    List<DonorDetailDTO> getFilteredDonorDetails(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("bloodTypeId") Long bloodTypeId,
            @Param("componentId") Long componentId,
            @Param("readinessLevel") String readinessLevel
    );





    // Biểu đồ stacked readiness
    @Query("""
SELECT new com.quyet.superapp.dto.ReadinessStackedDTO(
    u.bloodType.description,
    SUM(CASE WHEN u.readinessLevel = 'EMERGENCY_NOW' THEN 1 ELSE 0 END),
    SUM(CASE WHEN u.readinessLevel = 'EMERGENCY_FLEXIBLE' THEN 1 ELSE 0 END),
    SUM(CASE WHEN u.readinessLevel = 'REGULAR' THEN 1 ELSE 0 END)
)
FROM UrgentDonorRegistry u
WHERE u.isVerified = true AND u.isAvailable = true
AND u.createdAt BETWEEN :from AND :to
GROUP BY u.bloodType.description
""")
    List<ReadinessStackedDTO> getStackedReadiness(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);


    // Danh sách chi tiết người hiến
    @Query("""
SELECT new com.quyet.superapp.dto.DonorDetailDTO(
    p.fullName,
    u.bloodType.description,
    u.bloodComponent.name,
    CAST(u.readinessLevel AS string),
    COUNT(d.donationId),
    MAX(d.donationDate)
)
FROM UrgentDonorRegistry u
JOIN u.donor dUser
JOIN dUser.userProfile p
LEFT JOIN Donation d ON d.user = dUser AND d.status = 'DONATED'
WHERE u.isVerified = true AND u.isAvailable = true
AND u.createdAt BETWEEN :from AND :to
GROUP BY p.fullName, u.bloodType.description, u.bloodComponent.name, u.readinessLevel
""")
    List<DonorDetailDTO> getDonorDetailDTO(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);




    @Query("""
    SELECT u FROM UrgentDonorRegistry u
    WHERE u.isVerified = true AND u.isAvailable = true AND u.bloodComponent.bloodComponentId = :componentId
""")
    List<UrgentDonorRegistry> findAllVerifiedWithComponent(@Param("componentId") Long componentId);


    List<UrgentDonorRegistry> findByBloodType_BloodTypeIdAndBloodComponent_BloodComponentIdAndIsVerifiedTrueAndIsAvailableTrue(
            Long bloodTypeId, Long componentId
    );


    // UrgentDonorRegistryRepository.java
    @Query("""
    SELECT u FROM UrgentDonorRegistry u
    JOIN u.donor d
    JOIN d.userProfile p
    WHERE u.isVerified = true
      AND u.bloodType.bloodTypeId = :bloodTypeId
      AND u.bloodComponent.bloodComponentId = :componentId
""")
    List<UrgentDonorRegistry> findByBloodTypeAndComponent(
            @Param("bloodTypeId") Long bloodTypeId,
            @Param("componentId") Long componentId
    );





    @Query("""
        SELECT d.donor FROM UrgentDonorRegistry d
        WHERE d.readinessLevel = :level AND d.isAvailable = true AND d.isVerified = true
    """)
    List<User> findUrgentDonorsByLevel(@Param("level") DonorReadinessLevel level);

    List<UrgentDonorRegistry> findAllByIsVerifiedTrueAndIsAvailableTrue();

    @Query("SELECT r FROM UrgentDonorRegistry r WHERE r.isAvailable = false")
    List<UrgentDonorRegistry> findUnavailableDonors();

    List<UrgentDonorRegistry> findByIsVerifiedTrueAndIsAvailableTrue();
    List<UrgentDonorRegistry> findByIsVerifiedFalse();

    Optional<UrgentDonorRegistry> findByDonor_UserIdAndIsVerifiedTrue(Long userId);

    @Query("""
        SELECT u.donor FROM UrgentDonorRegistry u
        WHERE u.isVerified = true AND u.isAvailable = true
        AND (u.readinessLevel = 'EMERGENCY_NOW' OR u.readinessLevel = 'EMERGENCY_FLEXIBLE')
    """)
    List<User> findVerifiedUrgentDonorsReady();

    @Query("""
        SELECT u FROM UrgentDonorRegistry u
        WHERE (u.isVerified IS NULL OR u.isVerified = false)
    """)
    List<UrgentDonorRegistry> findUnverifiedDonors();

    Optional<UrgentDonorRegistry> findByDonor_UserId(Long userId);

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.bloodType.bloodTypeId = :bloodTypeId AND u.isAvailable = true ORDER BY u.lastContacted ASC")
    List<UrgentDonorRegistry> findAvailableDonors(@Param("bloodTypeId") Long bloodTypeId);

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.isAvailable = true")
    List<UrgentDonorRegistry> findAvailableDonorsAll();

    @Query("""
    SELECT u FROM UrgentDonorRegistry u
    WHERE u.isAvailable = true AND u.isVerified = true
      AND (6371 * acos(
           cos(radians(:lat)) * cos(radians(u.latitude)) *
           cos(radians(u.longitude) - radians(:lng)) +
           sin(radians(:lat)) * sin(radians(u.latitude))
      )) < :radius
""")
    List<UrgentDonorRegistry> findNearbyVerifiedDonors(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radius") double radius
    );



    Optional<UrgentDonorRegistry> findByDonor(User donor);

    boolean existsByDonor_UserId(Long userId);

    List<UrgentDonorRegistry> findByReadinessLevel(DonorReadinessLevel level); // ✅ Đúng tên field

    @Query("SELECT u FROM UrgentDonorRegistry u WHERE u.isVerified = true AND u.isAvailable = true")
    List<UrgentDonorRegistry> findAllVerified();
}
