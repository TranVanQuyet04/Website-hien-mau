package com.quyet.superapp.service;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationRegistration;
import com.quyet.superapp.entity.HealthCheckForm;
import com.quyet.superapp.entity.PreDonationTest;
import com.quyet.superapp.enums.DonationStatus;
import com.quyet.superapp.exception.MemberException;
import com.quyet.superapp.exception.ResourceNotFoundException;
import com.quyet.superapp.repository.DonationRegistrationRepository;
import com.quyet.superapp.repository.DonationRepository;
import com.quyet.superapp.repository.HealthCheckFormRepository;
import com.quyet.superapp.repository.PreDonationTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EligibilityEvaluationService {

    private final DonationRegistrationRepository registrationRepo;
    private final HealthCheckFormRepository healthCheckRepo;
    private final PreDonationTestRepository testRepo;
    private final DonationRepository donationRepo;

    public String evaluateAndCreateDonation(Long registrationId) {

        DonationRegistration registration = registrationRepo.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn đăng ký"));

        // Kiểm tra phiếu khám
        HealthCheckForm form = healthCheckRepo.findByRegistration_RegistrationId(registrationId);
        if (form == null || !Boolean.TRUE.equals(form.getIsEligible())) {
            throw new MemberException("NOT_ELIGIBLE_HEALTH", "Người hiến chưa đạt yêu cầu sức khỏe");
        }

        // Kiểm tra xét nghiệm
        PreDonationTest test = testRepo.findByHealthCheckForm_Id(form.getId())
                .orElseThrow(() -> new MemberException("MISSING_TEST", "Chưa có kết quả xét nghiệm máu"));
        if (test == null) {
            throw new MemberException("MISSING_TEST", "Chưa có kết quả xét nghiệm máu");
        }

        // Đánh giá kết quả xét nghiệm
        if (!Boolean.TRUE.equals(test.getHivResult())
                || !Boolean.TRUE.equals(test.getHbvResult())
                || !Boolean.TRUE.equals(test.getHcvResult())
                || !Boolean.TRUE.equals(test.getSyphilisResult())
                || test.getHbLevel() < 12.5) {
            // Cập nhật trạng thái đơn
            registration.setStatus(DonationStatus.FAILED_TEST);
            registrationRepo.save(registration);
            throw new MemberException("FAILED_TEST", "Người hiến không đạt yêu cầu xét nghiệm máu");
        }

        // Nếu đã có bản ghi Donation → không tạo lại
        if (donationRepo.existsByRegistration_RegistrationId(registrationId)) {
            return "Đã tạo bản ghi hiến máu trước đó.";
        }

        Donation donation = Donation.builder()
                .registration(registration)
                .user(registration.getUser())
                .bloodType(test.getBloodType())
                .donationDate(LocalDateTime.now())
                .volumeMl(450) // hoặc lấy từ preset
                .status(DonationStatus.DONATED)
                .build();

        donationRepo.save(donation);

        // Cập nhật trạng thái đơnx`
        registration.setStatus(DonationStatus.DONATED);
        registrationRepo.save(registration);

        return "Người hiến đủ điều kiện và đã được tạo bản ghi hiến máu.";
    }
}
