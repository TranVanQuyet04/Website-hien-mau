    package com.quyet.superapp.repository;

    import com.quyet.superapp.dto.BloodRequestSummaryDTO;
    import com.quyet.superapp.dto.DonorDetailDTO;
    import com.quyet.superapp.entity.BloodRequest;
    import com.quyet.superapp.entity.User;
    import com.quyet.superapp.enums.BloodRequestStatus;
    import com.quyet.superapp.enums.UrgencyLevel;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;

    public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

        @Query("""
    SELECT new com.quyet.superapp.dto.BloodRequestSummaryDTO(
        br.id,
        p.fullName,
        p.bloodGroup,
        bc.name,
        p.age,
        br.quantityMl,
        br.triageLevel,
        br.status,
        br.createdAt
    )
    FROM BloodRequest br
    JOIN br.patient p
    JOIN br.component bc
""")
        List<BloodRequestSummaryDTO> findAllSummaryRequests();

        List<BloodRequest> findByStatus(BloodRequestStatus status);


        List<BloodRequest> findByStatusIn(List<BloodRequestStatus> statuses);


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


        @Query("""
    SELECT COUNT(r)
    FROM BloodRequest r
    WHERE r.status = 'COMPLETED'
      AND r.createdAt BETWEEN :start AND :end
""")
        long countSuccessfulRequests(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);


        long countByUrgencyLevelAndStatusIn(UrgencyLevel urgencyLevel, List<BloodRequestStatus> statuses);

        long countByStatusAndCreatedAtBetween(BloodRequestStatus status, LocalDateTime from, LocalDateTime to);




        @Query("""
        SELECT br FROM BloodRequest br
        WHERE br.status = 'APPROVED'
          AND br.paymentStatus != 'SUCCESS'
          AND br.createdAt <= :expiredTime
    """)
        List<BloodRequest> findUnpaidAndExpiredRequests(@Param("expiredTime") LocalDateTime expiredTime);


        // 🔍 Truy vấn yêu cầu máu KHẨN còn hoạt động
        // ✅ Repository:
        @Query("SELECT r FROM BloodRequest r " +
                "WHERE r.urgencyLevel IN (:levels) " +
                "AND r.status IN :statuses")
        List<BloodRequest> findUrgentActiveRequests(@Param("statuses") List<BloodRequestStatus> statuses,
                                                    @Param("levels") List<UrgencyLevel> levels);



        // 🔍 Truy vấn yêu cầu KHẨN đang chờ duyệt
        @Query("SELECT r FROM BloodRequest r WHERE UPPER(r.urgencyLevel) IN ('KHẨN CẤP', 'CẤP CỨU') AND r.status = 'PENDING'")
        List<BloodRequest> findUrgentPendingRequests();

        // 🔍 Lịch sử tất cả yêu cầu KHẨN
        @Query("SELECT r FROM BloodRequest r WHERE UPPER(r.urgencyLevel) IN ('KHẨN CẤP', 'CẤP CỨU')")
        List<BloodRequest> findUrgentRequestHistory();

        // ✅ Kiểm tra trùng mã bệnh án
        boolean existsByPatientRecordCode(String patientRecordCode);

        // ✅ Kiểm tra bác sĩ có đang phụ trách đơn nào không
        boolean existsByDoctorAndStatusIn(User doctor, List<String> statuses);

        // 📊 Thống kê số đơn trong khoảng thời gian
        long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

        // 🔍 Đơn chưa thanh toán quá thời gian
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'UNPAID' AND r.approvedAt < :cutoff")
        List<BloodRequest> findOverdueUnpaidRequests(@Param("cutoff") LocalDateTime cutoff);

        // ✅ Truy vấn theo trạng thái


        // 🧾 Lấy danh sách đơn đã thanh toán
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'PAID'")
        List<BloodRequest> findAllPaidRequests();

        // 🧾 Lấy danh sách đơn được hoãn thanh toán
        @Query("SELECT r FROM BloodRequest r WHERE r.paymentStatus = 'DEFERRED'")
        List<BloodRequest> findAllDeferredPayments();

        // ❌ Hủy đơn quá hạn chưa thanh toán
        @Modifying
        @Query("UPDATE BloodRequest r SET r.status = 'CANCELLED', r.cancelReason = :reason " +
                "WHERE r.paymentStatus = 'UNPAID' AND r.approvedAt < :cutoff")
        int cancelOverdueUnpaidRequests(@Param("cutoff") LocalDateTime cutoff, @Param("reason") String reason);
    }
