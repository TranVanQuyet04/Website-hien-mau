package com.quyet.superapp.util;

import com.quyet.superapp.enums.EmailType;
import com.quyet.superapp.email.DefaultEmailTemplateBuilder;
import com.quyet.superapp.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppEmailSender {

    private final JavaMailSender mailSender;
    private final DefaultEmailTemplateBuilder templateBuilder;

    /**
     * Gửi email dựa trên template tương ứng với loại email
     *
     * @param type      loại email (xác minh, cảnh báo, cảnh báo khẩn cấp, v.v.)
     * @param data      dữ liệu (BloodRequest, OTP, etc.) để render nội dung
     * @param recipient email người nhận
     * @param actor     người thực hiện hành động (người gửi hoặc liên quan)
     */
    public void sendEmail(EmailType type, Object data, String recipient, User actor) {
        try {
            // Tạo subject và nội dung HTML từ builder
            String subject = templateBuilder.buildSubject(type, data);
            String body = templateBuilder.buildBody(type, data);

            // Tạo email MIME
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true); // Gửi nội dung HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            // Log ra lỗi nếu gửi mail thất bại
            System.err.println("❌ [EmailSender] Lỗi khi gửi mail tới " + recipient + ": " + e.getMessage());

            // TODO: Có thể mở rộng thành throw new EmailSendException(...) nếu cần retry / alert
        }
    }
}
