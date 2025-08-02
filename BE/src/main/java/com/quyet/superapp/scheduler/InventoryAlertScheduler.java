package com.quyet.superapp.scheduler;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.BloodRequestStatus;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.BloodRequestRepository;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryAlertScheduler {

    private final BloodService bloodService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final UrgentDonorRegistryService urgentDonorRegistryService;
    private final BloodRequestRepository requestRepo;

    @Scheduled(cron = "0 0 * * * *") // chạy mỗi giờ
    public void autoRejectOverdueRequests() {
        LocalDateTime timeout = LocalDateTime.now().minusHours(24);
        List<BloodRequest> overdueRequests = requestRepo.findUnpaidAndExpiredRequests(timeout);

        for (BloodRequest request : overdueRequests) {
            request.setStatus(BloodRequestStatus.REJECTED);
            request.setCancelReason("Tự động huỷ do quá thời gian thanh toán.");
            requestRepo.save(request);

            emailService.sendEmail(
                    request.getRequester(),                  // ✅ kiểu User
                    "🛑 Đơn yêu cầu máu bị huỷ",             // subject
                    "<p>Đơn mã <b>" + request.getRequestCode() + "</b> đã bị huỷ do quá thời gian thanh toán.</p>", // content
                    "REQUEST_REJECTED"                       // type (chỉ là String ở đây)
            );


            notificationService.createNotification(
                    request.getRequester().getUserId(),
                    "🛑 Đơn yêu cầu máu bị huỷ",
                    "Đơn " + request.getRequestCode() + " đã bị huỷ vì không thanh toán đúng hạn."
            );

        }
    }



    @Scheduled(fixedRate = 3600000) // Mỗi giờ
    public void autoCheckInventoryAlert() {
        List<BloodInventoryAlertDTO> alerts = bloodService.getAllInventoryStatus().stream()
                .filter(dto -> !"NORMAL".equals(dto.getAlertLevel()))
                .toList();

        if (alerts.isEmpty()) return;

        // ✅ Gửi cho ADMIN & STAFF
        List<User> staffAndAdmins = new ArrayList<>();
        staffAndAdmins.addAll(userRepository.findByRole(RoleEnum.ADMIN));
        staffAndAdmins.addAll(userRepository.findByRole(RoleEnum.STAFF));

        for (User user : staffAndAdmins) {
            String htmlContent = buildEmailContent(alerts);
            emailService.sendEmail(user,
                    "[Cảnh báo kho máu] Báo cáo tổng hợp",
                    htmlContent,
                    "INVENTORY_ALERT");

            notificationService.createNotification(
                    user.getUserId(),
                    "📢 Có nhiều cảnh báo kho máu",
                    "Hệ thống phát hiện nhiều loại máu sắp thiếu. Kiểm tra email để biết chi tiết."
            );
        }

        // ✅ Gửi cho người hiến máu khẩn cấp (chỉ khi CRITICAL)
        boolean hasCritical = alerts.stream()
                .anyMatch(a -> "CRITICAL".equals(a.getAlertLevel()));

        if (hasCritical) {
            List<User> urgentDonors = urgentDonorRegistryService.getVerifiedUrgentDonorsReady();
            for (User donor : urgentDonors) {
                String htmlContent = buildEmailContent(alerts);
                emailService.sendEmail(donor,
                        "🩸 Khẩn cấp: Kho máu thiếu trầm trọng",
                        htmlContent +
                                "<p>Nếu bạn sẵn sàng hiến máu, vui lòng đến trung tâm hiến máu gần nhất.</p>",
                        "URGENT_DONOR_ALERT");
            }
        }
    }

    private String buildEmailContent(List<BloodInventoryAlertDTO> alerts) {
        StringBuilder content = new StringBuilder();
        content.append("<h3>🩸 Báo cáo cảnh báo kho máu</h3><ul>");

        for (BloodInventoryAlertDTO alert : alerts) {
            String prefix = switch (alert.getAlertLevel()) {
                case "CRITICAL" -> "❗ NGUY KỊCH";
                case "WARNING" -> "⚠️ Cảnh báo";
                default -> "ℹ️";
            };

            content.append(String.format(
                    "<li>%s: <b>%s - %s</b> còn lại <span style='color:red;'>%dml</span></li>",
                    prefix,
                    alert.getBloodType(),
                    alert.getComponent(),
                    alert.getQuantityMl()
            ));
        }

        content.append("</ul><p>Vui lòng kiểm tra ngay để xử lý kịp thời.</p>");
        return content.toString();
    }
}
