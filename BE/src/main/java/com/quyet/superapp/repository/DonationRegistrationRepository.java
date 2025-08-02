package com.quyet.superapp.repository;

import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.enums.DonationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRegistrationRepository extends JpaRepository<DonationRegistration, Long> {

    List<DonationRegistration> findByStatus(DonationStatus status);

    List<DonationRegistration> findByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndStatus(Long userId, DonationStatus status);

    @EntityGraph(attributePaths = {
            "user",
            "user.userProfile",
            "user.userProfile.address",
            "user.userProfile.address.ward",
            "user.userProfile.address.ward.district",
            "user.userProfile.address.ward.district.city"
    })
    @Query("SELECT dr FROM DonationRegistration dr")
    List<DonationRegistration> findAllWithDetails();

    // ✅ Thêm mới
    int countByScheduledDateAndSlotId(java.time.LocalDate scheduledDate, Long slotId);
}
