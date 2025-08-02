package com.quyet.superapp.email;

import com.quyet.superapp.dto.BloodInventoryAlertDTO;
import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.enums.EmailType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultEmailTemplateBuilder implements EmailTemplateBuilder {

    @Override
    public String buildSubject(EmailType type, Object data) {
        return switch (type) {
            case BLOOD_REQUEST_CREATED -> "🩸 Yêu cầu máu mới đã được tạo";
            case BLOOD_REQUEST_WAITING_DONOR -> "📢 Đơn đang chờ người hiến máu khẩn cấp";
            case BLOOD_REQUEST_APPROVED -> "✅ Đơn yêu cầu máu đã được duyệt";
            case BLOOD_REQUEST_REJECTED -> "❌ Đơn yêu cầu máu đã bị từ chối";
            case BLOOD_REQUEST_CONFIRMED -> "📄 Đơn yêu cầu máu đã hoàn tất";

            case INVENTORY_ALERT_FOR_REQUEST -> "⚠️ Cảnh báo kho máu – thiếu nhóm máu";
            case URGENT_DONOR_ALERT -> "🆘 Cần bạn hiến máu khẩn cấp gần đây!";
            case URGENT_DONOR_RESPONSE -> "📥 Có người hiến máu khẩn cấp phản hồi";
            case URGENT_DONOR_VERIFIED -> "✅ Bạn đã được xác minh là người hiến khẩn cấp";
            case URGENT_DONOR_REJECTED -> "❌ Bạn đã bị từ chối khỏi nhóm hiến máu khẩn cấp";

            case PAYMENT_REQUESTED -> "💳 Yêu cầu thanh toán dịch vụ truyền máu";
            case PAYMENT_CONFIRMATION -> "💰 Xác nhận thanh toán thành công";



            default -> "📩 Thông báo từ hệ thống hiến máu";
        };
    }


    @Override
    public String buildBody(EmailType type, Object data) {
        if (type == EmailType.BLOOD_REQUEST_APPROVED && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            String approvedAt = r.getApprovedAt() != null ? r.getApprovedAt().toString() : "Chưa cập nhật";
            String neededAt = r.getNeededAt() != null ? r.getNeededAt().toLocalDate().toString() : "Chưa xác định";

            return """
        <h3 style="color:green;">✅ YÊU CẦU TRUYỀN MÁU ĐÃ ĐƯỢC DUYỆT</h3>
        <p>Đơn yêu cầu truyền máu cho bệnh nhân <b>%s</b> đã được duyệt.</p>
        <ul>
          <li>🔸 Nhóm máu: <b>%s</b></li>
          <li>🔸 Thành phần máu: <b>%s</b></li>
          <li>🔸 Số lượng: <b>%d túi</b></li>
          <li>🔸 Ngày cần truyền: <b>%s</b></li>
        </ul>
        <p>🕒 Thời gian duyệt: <b>%s</b></p>
        <hr/>
        <p>Vui lòng kiểm tra và chuẩn bị thực hiện truyền máu đúng lịch.</p>
        """.formatted(
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    neededAt,
                    approvedAt
            );
        }


        if (type == EmailType.DONATION_REMINDER && data instanceof String fullName) {
            return """
        <h3>⏰ ĐẾN LÚC HIẾN MÁU RỒI!</h3>
        <p>Xin chào <b>%s</b>,</p>
        <p>Bạn đã đủ điều kiện để hiến máu lần tiếp theo.</p>
        <p>Hãy đăng ký lịch hiến máu mới tại:</p>
        <a href="https://yourapp.com/donation/register"
           style="display:inline-block;padding:10px 20px;background-color:#1890ff;color:white;
                  border-radius:5px;text-decoration:none;font-weight:bold;">
           🩸 Đăng ký hiến máu
        </a>
        <p>Cảm ơn vì sự đóng góp của bạn!</p>
        """.formatted(fullName);
        }


        if (type == EmailType.BLOOD_REQUEST_CONFIRMED && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            int confirmedVolume = r.getConfirmedVolumeMl() != null ? r.getConfirmedVolumeMl() : 0;
            String paymentStatus = r.getPaymentStatus() != null ? r.getPaymentStatus().name() : "Chưa thanh toán";


            return """
                <h3>📄 XÁC NHẬN HOÀN TẤT TRUYỀN MÁU</h3>
                <p>Yêu cầu truyền máu cho bệnh nhân <b>%s</b> đã được xử lý thành công.</p>
                <ul>
                  <li>Nhóm máu truyền: <b>%s</b></li>
                  <li>Thành phần máu: <b>%s</b></li>
                  <li>Thể tích xác nhận: <b>%d ml</b></li>
                  <li>Trạng thái thanh toán: <b>%s</b></li>
                </ul>
                <p>Cảm ơn bạn đã phối hợp xử lý!</p>
                """.formatted(
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    confirmedVolume,
                    paymentStatus
            );
        }

        if (type == EmailType.URGENT_DONOR_ALERT && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";

            return """
                <h3>🆘 KHẨN CẤP – CẦN BẠN HIẾN MÁU</h3>
                <p>Hệ thống đang tìm người hiến máu cho bệnh nhân <b>%s</b>.</p>
                <ul>
                  <li>Nhóm máu cần: <b>%s</b></li>
                  <li>Thành phần: <b>%s</b></li>
                  <li>Số lượng: <b>%d túi</b></li>
                  <li>Địa điểm: <b>Bệnh viện chính</b></li>
                </ul>
                <p>Nếu bạn sẵn sàng hỗ trợ, vui lòng bấm vào liên kết bên dưới:</p>
                <a href="https://yourapp.com/urgent-confirm?requestId=%d">👉 Tôi có thể giúp</a>
                """.formatted(
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    r.getId()
            );
        }

        if (type == EmailType.INVENTORY_ALERT_FOR_REQUEST && data instanceof BloodInventoryAlertDTO dto) {
            return """
                <h3>⚠️ CẢNH BÁO KHO MÁU</h3>
                <p>Nhóm máu <b>%s</b> hiện đang ở mức <b>%s</b></p>
                <ul>
                  <li>Lượng hiện tại: %d ml</li>
                  <li>Ngưỡng cảnh báo: %d ml</li>
                  <li>Ngưỡng nghiêm trọng: %d ml</li>
                </ul>
                """.formatted(
                    dto.getBloodType(),
                    dto.getAlertLevel(),
                    dto.getQuantityMl(),
                    dto.getMinThresholdMl(),
                    dto.getCriticalThresholdMl()
            );
        }

        if (type == EmailType.PAYMENT_CONFIRMATION && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            int amount = r.getTotalAmount() != null ? r.getTotalAmount() : 0;
            String requestCode = r.getRequestCode() != null ? r.getRequestCode() : "N/A";

            return """
        <h3>💰 THANH TOÁN THÀNH CÔNG</h3>
        <p>🔖 Mã đơn: <b>%s</b></p>
        <p>Đơn truyền máu cho bệnh nhân <b>%s</b> đã được thanh toán thành công.</p>
        <ul>
          <li>Nhóm máu: <b>%s</b></li>
          <li>Thành phần: <b>%s</b></li>
          <li>Số lượng: <b>%d túi</b></li>
          <li>Tổng số tiền: <b>%,d VNĐ</b></li>
        </ul>

        <p>📄 Bạn có thể xem biên lai tại:</p>
        <a href="https://yourapp.com/payment/receipt/%d"
           style="display:inline-block;padding:10px 20px;background-color:#52c41a;color:white;
                  border-radius:5px;text-decoration:none;font-weight:bold;">
           📄 Xem Biên Lai
        </a>
        """.formatted(
                    requestCode,
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    amount,
                    r.getId()
            );
        }

        if (type == EmailType.BLOOD_REQUEST_STATUS_UPDATE && data instanceof BloodRequest r) {
            String status = r.getStatus() != null ? r.getStatus().name() : "Không rõ";
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            String requestCode = r.getRequestCode() != null ? r.getRequestCode() : "Chưa có";

            return """
    <div style="font-family:sans-serif;">
        <img src="https://yourapp.com/assets/hospital-logo.png" alt="Hospital Logo" height="60" />
        <h3>📢 CẬP NHẬT TRẠNG THÁI YÊU CẦU</h3>
        <p>Đơn <b>%s</b> vừa được cập nhật trạng thái:</p>
        <p><b>Trạng thái mới:</b> <span style="color:#fa541c;">%s</span></p>
        <ul>
            <li>Nhóm máu: <b>%s</b></li>
            <li>Thành phần: <b>%s</b></li>
            <li>Số lượng: <b>%d túi</b></li>
        </ul>
        <p>📌 Hãy kiểm tra đơn và tiếp tục xử lý nếu cần.</p>
        <a href="https://yourapp.com/staff/requests/%d"
           style="display:inline-block;padding:10px 20px;background-color:#722ed1;color:white;
                  border-radius:5px;text-decoration:none;font-weight:bold;">
           👉 Xem trạng thái mới
        </a>
    </div>
    """.formatted(
                    requestCode,
                    status,
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    r.getId()
            );
        }
        if (type == EmailType.BLOOD_REQUEST_CREATED && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            String requestCode = r.getRequestCode() != null ? r.getRequestCode() : "Chưa có";
            String neededAt = r.getNeededAt() != null ? r.getNeededAt().toLocalDate().toString() : "Chưa xác định";

            return """
    <div style="font-family:sans-serif;">
        <img src="https://yourapp.com/assets/hospital-logo.png" alt="Hospital Logo" height="60" />
        <h3>🩸 YÊU CẦU TRUYỀN MÁU MỚI</h3>
        <p>Mã yêu cầu: <b>%s</b></p>
        <ul>
            <li><b>Bệnh nhân:</b> %s</li>
            <li><b>Nhóm máu:</b> %s</li>
            <li><b>Thành phần:</b> %s</li>
            <li><b>Số lượng:</b> %d túi</li>
            <li><b>Ngày cần:</b> %s</li>
        </ul>
        <p>🧑‍⚕️ Nhân viên phụ trách: <b>%s</b></p>
        <p>Vui lòng duyệt yêu cầu trong thời gian sớm nhất.</p>
        <a href="https://yourapp.com/admin/requests/%d"
           style="display:inline-block;padding:10px 20px;background-color:#1677ff;color:white;
                  border-radius:5px;text-decoration:none;font-weight:bold;">
           👉 Xem chi tiết đơn
        </a>
    </div>
    """.formatted(
                    requestCode,
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    neededAt,
                    r.getRequester().getUserProfile().getFullName(),
                    r.getId()
            );
        }


        if (type == EmailType.BLOOD_REQUEST_REJECTED && data instanceof BloodRequest r) {
            String reason = r.getCancelReason() != null ? r.getCancelReason() : "Không có lý do cụ thể";
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";

            return """
        <h3>❌ YÊU CẦU TRUYỀN MÁU BỊ TỪ CHỐI</h3>
        <p>Đơn yêu cầu truyền máu cho bệnh nhân <b>%s</b> đã bị từ chối.</p>
        <ul>
          <li>Nhóm máu: <b>%s</b></li>
          <li>Thành phần: <b>%s</b></li>
          <li>Số lượng: <b>%d túi</b></li>
        </ul>
        <p><b>Lý do:</b> %s</p>
        <p>Vui lòng kiểm tra lại hoặc liên hệ với quản trị viên nếu cần.</p>
        """.formatted(
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    reason
            );
        }

        if (type == EmailType.URGENT_DONOR_RESPONSE && data instanceof BloodRequest r) {
            return """
        <h3>📥 PHẢN HỒI TỪ NGƯỜI HIẾN MÁU KHẨN CẤP</h3>
        <p>Đã có người sẵn sàng hỗ trợ hiến máu cho đơn <b>%s</b>.</p>
        <ul>
          <li>Bệnh nhân: <b>%s</b></li>
          <li>Nhóm máu cần: <b>%s</b></li>
          <li>Thành phần: <b>%s</b></li>
        </ul>
        <p>📌 Hãy kiểm tra và xác nhận người hiến phù hợp.</p>
        """.formatted(
                    r.getRequestCode(),
                    r.getPatient().getFullName(),
                    r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A",
                    r.getComponent() != null ? r.getComponent().getName() : "N/A"
            );
        }


        if (type == EmailType.URGENT_DONOR_VERIFIED && data instanceof String fullName) {
            return """
        <h3>✅ XÁC MINH THÀNH CÔNG</h3>
        <p>Xin chào <b>%s</b>,</p>
        <p>Bạn đã được xác minh là người hiến máu khẩn cấp.</p>
        <p>Chúng tôi sẽ ưu tiên liên hệ với bạn khi có trường hợp cần máu gấp gần khu vực bạn.</p>
        <p>Xin cảm ơn vì sự đóng góp đầy ý nghĩa của bạn!</p>
        """.formatted(fullName);
        }

        if (type == EmailType.PAYMENT_REQUESTED && data instanceof BloodRequest r) {
            String bloodType = r.getBloodType() != null ? r.getBloodType().getDescription() : "N/A";
            String component = r.getComponent() != null ? r.getComponent().getName() : "N/A";
            int amount = r.getTotalAmount() != null ? r.getTotalAmount() : 0;
            String requestCode = r.getRequestCode() != null ? r.getRequestCode() : "Mã chưa có";
            Long requestId = r.getId();

            return """
        <h3>💳 YÊU CẦU THANH TOÁN DỊCH VỤ TRUYỀN MÁU</h3>
        <p>📝 Mã đơn: <b>%s</b></p>
        <p>Đơn truyền máu cho bệnh nhân <b>%s</b> đã sẵn sàng để thanh toán.</p>
        <ul>
          <li>Nhóm máu: <b>%s</b></li>
          <li>Thành phần: <b>%s</b></li>
          <li>Số lượng: <b>%d túi</b></li>
          <li>Tổng chi phí: <b>%,d VNĐ</b></li>
        </ul>
        <p>🧾 Quét mã QR để thanh toán:</p>
        <img src="https://yourapp.com/payment/qr/%d" alt="QR Code" width="180"/>

        <p>Hoặc bấm nút dưới để thanh toán ngay:</p>
        <a href="https://yourapp.com/payment/pay-now?requestId=%d"
           style="display:inline-block;padding:10px 20px;background-color:#ff4d4f;color:white;
                  border-radius:5px;text-decoration:none;font-weight:bold;">
           👉 Thanh toán ngay
        </a>
        """.formatted(
                    requestCode,
                    r.getPatient().getFullName(),
                    bloodType,
                    component,
                    r.getQuantityBag(),
                    amount,
                    requestId,
                    requestId
            );
        }


        return "<p>[⚠️ EmailType <b>" + type + "</b> chưa được hỗ trợ trong template builder]</p>";

    }
}
