package com.quyet.superapp.service;

import com.quyet.superapp.entity.Donation;
import com.quyet.superapp.entity.DonationHistory;
import com.quyet.superapp.mapper.DonationHistoryMapper;
import com.quyet.superapp.repository.DonationHistoryRepository;
import com.quyet.superapp.repository.DonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final DonationHistoryRepository donationHistoryRepository;
    private final DonationHistoryMapper donationHistoryMapper;


    public List<Donation> getAll() {
        return repository.findAll();
    }

    public Optional<Donation> getById(Long id) {
        return repository.findById(id);
    }

    public Donation save(Donation donation) {
        return repository.save(donation);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Tính số ngày còn lại để phục hồi thành phần máu cụ thể
     */
    public int getDaysUntilRecover(Long userId, Long componentId) {
        List<Donation> donations = getByUserId(userId);

        // Tìm lần hiến cuối cùng cho thành phần máu cụ thể
        Optional<LocalDateTime> lastDonation = donations.stream()
                .filter(d -> d.getBloodComponent() != null
                        && d.getBloodComponent().getBloodComponentId().equals(componentId))
                .map(Donation::getDonationDate)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo);

        if (lastDonation.isEmpty()) {
            return 0; // Nếu chưa từng hiến, có thể hiến ngay
        }

        // Giả sử phục hồi là 30 ngày (sau này có thể cấu hình động)
        LocalDateTime nextEligible = lastDonation.get().plusDays(30);
        int daysLeft = (int) java.time.Duration.between(LocalDateTime.now(), nextEligible).toDays();

        return Math.max(daysLeft, 0); // Không âm
    }



    // ✅ Kiểm tra người dùng đã đủ thời gian để hiến lại thành phần máu cụ thể hay chưa
    public boolean canDonateNow(Long userId, Long componentId) {
        List<Donation> donations = getByUserId(userId).stream()
                .filter(d -> d.getBloodComponent() != null
                        && d.getBloodComponent().getBloodComponentId().equals(componentId))
                .filter(d -> d.getStatus() != null && d.getStatus().name().equals("DONATED"))
                .toList();

        return donations.stream()
                .map(Donation::getDonationDate)
                .filter(date -> date != null)
                .max(LocalDateTime::compareTo)
                .map(lastDonationDate -> lastDonationDate.plusDays(30).isBefore(LocalDateTime.now()))
                .orElse(true);
    }



    // ✅ Lấy tất cả đơn hiến máu theo userId
    public List<Donation> getByUserId(Long userId) {
        return repository.findByUser_UserId(userId); // Viết thêm trong DonationRepository
    }



    // ✅ Đếm số lượt hiến máu theo ngày
    public long countByDate(LocalDate date) {
        return repository.countByCreatedAtBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        );
    }


    // ✅ Lấy các đơn hiến máu chưa được phân tách (chưa có log)
    public List<Donation> getUnseparatedDonations() {
        return repository.findAll().stream()
                .filter(d -> d.getDonationDate() != null && d.getVolumeMl() != null) // đơn có dữ liệu
                .filter(d -> d.getBloodType() != null && d.getBloodComponent() != null) // có nhóm máu và thành phần
                .filter(d -> d.getDonationId() != null) // cần để mapping
                .filter(d -> d.getRegistration() != null) // đã qua đăng ký
                .filter(d -> d.getBloodComponent().getCode() != null) // có code thì mới phân tách
                .collect(Collectors.toList());
    }
    public void saveDonationHistory(Donation donation,
                                    boolean paymentCompleted,
                                    int paymentAmount,
                                    String transactionCode,
                                    String paymentMethod) {

        DonationHistory history = DonationHistory.builder()
                .donor(donation.getUser())
                .donation(donation)
                .donatedAt(Optional.ofNullable(donation.getDonationDate()).orElse(LocalDateTime.now()))
                .bloodType(Optional.ofNullable(donation.getBloodType())
                        .map(bt -> bt.getDescription())
                        .orElse("Không rõ"))
                .componentDonated(Optional.ofNullable(donation.getBloodComponent())
                        .map(bc -> bc.getName())
                        .orElse("Không rõ"))
                .volumeMl(donation.getVolumeMl())
                .donationLocation(donation.getLocation())
                .paymentCompleted(paymentCompleted)
                .paymentAmount(paymentAmount)
                .transactionCode(transactionCode)
                .paymentMethod(paymentMethod)
                .createdAt(LocalDateTime.now())
                .build();

        donationHistoryRepository.save(history);
    }


}