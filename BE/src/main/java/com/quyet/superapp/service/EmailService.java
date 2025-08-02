package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.email.EmailTemplateBuilder;
import com.quyet.superapp.enums.EmailType;
import com.quyet.superapp.entity.EmailLog;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.EmailLogRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * EmailService gửi email + lưu log + hỗ trợ EmailType.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;
    private final EmailTemplateBuilder templateBuilder;

    public void sendDonationReminder(User user) {
        String subject = "⏰ Nhắc nhở hiến máu";
        String body = """
        Xin chào %s,

        Bạn đã đủ điều kiện để hiến máu tiếp theo. Hãy xem lại hồ sơ và chọn thời gian hiến máu phù hợp.

        👉 Đăng ký tại: http://localhost:5173/donation

        Trân trọng,
        Hệ thống hiến máu
        """.formatted(user.getUserProfile().getFullName());

        sendEmail(user, subject, body, EmailType.DONATION_REMINDER.name());
    }



    /**
     * Gửi email dựa trên EmailType + data DTO/Entity.
     */
    public void sendEmail(User user, EmailType type, Object data) {
        String subject = templateBuilder.buildSubject(type, data);
        String body = templateBuilder.buildBody(type, data);
        sendEmail(user, subject, body, type.name());
    }

    public void sendEmailTo(String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gửi email và lưu log (subject/body/type).
     */
    public void sendEmail(User user, String subject, String content, String type) {
        EmailLog emailLog = new EmailLog();
        emailLog.setUser(user);
        emailLog.setRecipientEmail(user.getEmail());
        emailLog.setSubject(subject);
        emailLog.setBody(content);
        emailLog.setType(type);
        emailLog.setSentAt(LocalDateTime.now());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true); // HTML
            mailSender.send(message);

            emailLog.setStatus("SUCCESS");
            log.info("📧 Email gửi thành công đến: {}", user.getEmail());
        } catch (Exception e) {
            emailLog.setStatus("FAILED");
            log.error("❌ Gửi email thất bại đến {}: {}", user.getEmail(), e.getMessage(), e);
        }

        emailLogRepository.save(emailLog);
    }

    /**
     * Gửi email nhanh không cần log.
     */
    public void sendSimpleMessage(String toEmail, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // hoặc logger
        }
    }

    /**
     * Gửi email cảnh báo tổng hợp kho máu đến admin.
     */
    public void sendInventoryAlertSummaryEmail(User user, List<BloodInventoryAlertDTO> alerts) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h3>📢 Cảnh báo kho máu</h3>");
        htmlContent.append("<p>Dưới đây là danh sách các loại máu đang thiếu:</p><ul>");

        for (BloodInventoryAlertDTO alert : alerts) {
            htmlContent.append("<li><b>")
                    .append(alert.getBloodType()).append(" - ").append(alert.getComponent())
                    .append("</b>: <span style='color:red;'>")
                    .append(alert.getQuantityMl()).append("ml</span> (")
                    .append(alert.getAlertLevel()).append(")</li>");
        }

        htmlContent.append("</ul>");

        sendEmail(user, "🩸 Tổng hợp cảnh báo kho máu", htmlContent.toString(), "ALERT");
    }

}
