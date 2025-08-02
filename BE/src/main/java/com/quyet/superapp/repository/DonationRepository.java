package com.quyet.superapp.repository;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("SELECT MAX(d.donationDate) FROM Donation d WHERE d.status = 'DONATED'")
    LocalDateTime findLastDonationDate();


    @Query("""
    SELECT d.bloodType.description, COUNT(d)
    FROM Donation d
    WHERE d.status = 'DONATED'
      AND d.createdAt BETWEEN :start AND :end
    GROUP BY d.bloodType.description
""")
    List<Object[]> countByBloodGroup(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    @Query("""
    SELECT d.separatedComponent.name, COUNT(d)
    FROM Donation d
    WHERE d.status = 'DONATED'
      AND d.createdAt BETWEEN :start AND :end
    GROUP BY d.separatedComponent.name
""")
    List<Object[]> countBySeparatedComponent(LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT f.reason, COUNT(f)
    FROM HealthCheckFailureLog f
    WHERE f.createdAt BETWEEN :start AND :end
    GROUP BY f.reason
""")
    List<Object[]> countRejectionReasons(LocalDateTime start, LocalDateTime end);

    @Query(value = """
    SELECT DATEDIFF(DAY, donation_time, recovered_at) AS days, COUNT(*)
    FROM Donations
    WHERE status = 'DONATED'
      AND created_at BETWEEN :start AND :end
      AND recovered_at IS NOT NULL
    GROUP BY DATEDIFF(DAY, donation_time, recovered_at)
""", nativeQuery = true)
    List<Object[]> countRecoveryDaysDistributionNative(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end);



    // 🧮 9️⃣ Tính tỷ lệ bị từ chối
    default double calculateRejectionRate(LocalDateTime start, LocalDateTime end) {
        long rejected = countRejectedDonations(start, end);
        long total = countTotalDonations(start, end);
        if (total == 0) return 0.0;
        return (double) rejected / total;
    }

    // ⏱️ 🔟 Trung bình số ngày phục hồi
    default double calculateAverageRecoveryTime(LocalDateTime start, LocalDateTime end) {
        Double avg = getAverageRecoveryDays(start, end);
        return avg != null ? avg : 0.0;
    }

    // 🧪 1️⃣1️⃣ Tỷ lệ thành phần máu (sử dụng lại dữ liệu raw, xử lý sau ở Service nếu cần)
    default List<Object[]> calculateComponentRatios(LocalDateTime start, LocalDateTime end) {
        return countByComponent(start, end);
    }



    // 1️⃣ Tổng số người hiến máu duy nhất trong khoảng
    @Query("""
        SELECT COUNT(DISTINCT d.user.userId)
        FROM Donation d
        WHERE d.status = 'DONATED'
          AND d.createdAt BETWEEN :start AND :end
    """)
    long countDistinctDonorsBetween(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    // 2️⃣ Tổng lượt hiến máu KHẨN
    @Query("""
        SELECT COUNT(d)
        FROM Donation d
        WHERE d.status = 'DONATED'
          AND d.registration.isEmergency = true
          AND d.createdAt BETWEEN :start AND :end
    """)
    long countEmergencyDonations(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    // 3️⃣ Tổng lượt hiến máu THƯỜNG
    @Query("""
        SELECT COUNT(d)
        FROM Donation d
        WHERE d.status = 'DONATED'
          AND (d.registration.isEmergency = false OR d.registration.isEmergency IS NULL)
          AND d.createdAt BETWEEN :start AND :end
    """)
    long countRegularDonations(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    // 4️⃣ Tổng lượt bị từ chối
    @Query("""
        SELECT COUNT(d)
        FROM Donation d
        WHERE d.status = 'REJECTED'
          AND d.createdAt BETWEEN :start AND :end
    """)
    long countRejectedDonations(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end);

    // 5️⃣ Tổng lượt hiến máu (tính tỷ lệ từ chối)
    @Query("""
        SELECT COUNT(d)
        FROM Donation d
        WHERE d.createdAt BETWEEN :start AND :end
    """)
    long countTotalDonations(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end);

    // 6️⃣ Trung bình số ngày phục hồi
    @Query(value = """
    SELECT AVG(DATEDIFF(DAY, donation_time, recovered_at))
    FROM Donations
    WHERE recovered_at IS NOT NULL
      AND status = 'DONATED'
      AND created_at BETWEEN :start AND :end
""", nativeQuery = true)
    Double getAverageRecoveryDays(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);



    // 8️⃣ Thống kê theo thành phần máu
    @Query("""
        SELECT d.bloodComponent.name, COUNT(d)
        FROM Donation d
        WHERE d.status = 'DONATED'
          AND d.createdAt BETWEEN :start AND :end
        GROUP BY d.bloodComponent.name
    """)
    List<Object[]> countByComponent(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    // ⚙️ Các hàm khác đã có sẵn
    boolean existsByUser_UserIdAndBloodComponent_BloodComponentIdAndStatus(Long userId, Long componentId, DonationStatus status);

    boolean existsByRegistration_RegistrationId(Long registrationId);

    List<Donation> findByUser_UserId(Long userId);

    List<Donation> findByBloodUnitsIsEmpty();

    List<Donation> findByStatus(DonationStatus status);

    Donation findTopByUser_UserIdOrderByDonationDateDesc(Long userId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
