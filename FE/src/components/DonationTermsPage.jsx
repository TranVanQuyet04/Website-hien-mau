import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../styles/DonationTermsPage.css";

const termsContent = {
  1: {
    title: "Hiến máu theo lịch hẹn (thường)",
    description: `
      Bạn đã chọn hình thức tham gia hiến máu theo lịch hẹn không khẩn cấp.
      Điều này giúp ngân hàng máu lên kế hoạch và chuẩn bị chu đáo cho bạn.
    `,
    conditions: [
      "Cam kết cung cấp thông tin chính xác và đầy đủ.",
      "Đồng ý nhận thông báo mời hiến máu qua email hoặc điện thoại.",
      "Có thể sắp xếp thời gian hiến máu trong vòng 3–5 ngày sau khi nhận thông báo.",
    ],
  },
  2: {
    title: "Tham gia nhóm hiến máu khẩn cấp",
    description: `
      Bạn đã chọn tham gia nhóm phản ứng nhanh hỗ trợ các ca cấp cứu ngoài kế hoạch.
      Đây là vai trò đặc biệt cần khả năng phản hồi nhanh.
    `,
    conditions: [
      "Sẵn sàng phản hồi trong vòng 1–2 giờ kể từ khi nhận thông báo khẩn cấp.",
      "Có phương tiện/khả năng di chuyển nhanh đến điểm hiến máu gần nhất.",
      "Chấp nhận có thể bị gọi vào thời gian không cố định.",
    ],
  },
  3: {
    title: "Hiến máu khẩn cấp tại bệnh viện",
    description: `
      Bạn đã chọn sẵn sàng hiến máu ngay tại bệnh viện hoặc khu vực lân cận.
      Đây là lựa chọn lý tưởng cho nhân viên y tế, sinh viên y hoặc cư dân gần viện.
    `,
    conditions: [
      "Luôn trong trạng thái có thể được gọi và đến bệnh viện trong thời gian ngắn.",
      "Không từ chối nếu có nhu cầu máu khẩn cấp trừ khi lý do sức khoẻ bất khả kháng.",
      "Chấp nhận tham gia trong ca trực hoặc cuối tuần nếu được yêu cầu.",
    ],
  },
  4: {
    title: "Chưa sẵn sàng hiến máu",
    description: `
      Bạn đã chọn chưa thể hiến máu hiện tại nhưng vẫn muốn giữ liên hệ với hệ thống.
      Đây là lựa chọn phù hợp với người đang bận hoặc cần kiểm tra sức khoẻ.
    `,
    conditions: [
      "Cam kết cập nhật tình trạng sức khỏe/thời gian sẵn sàng trong tương lai.",
      "Cho phép hệ thống lưu hồ sơ tạm thời và gửi thông tin phù hợp.",
      "Không bị ràng buộc hiến máu cho đến khi xác nhận lại.",
    ],
  },
};

const DonationTermsPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const optionId = location.state?.optionId;

  if (!optionId || !termsContent[optionId]) {
    return (
      <div className="error-message">
        Lỗi: Không xác định lựa chọn hiến máu. Vui lòng quay lại trang trước.
      </div>
    );
  }

  const content = termsContent[optionId];

  const handleAgree = () => {
    navigate("/login");
  };

  return (
    <div className="terms-container">
      <h2 className="terms-title">{content.title}</h2>
      <p className="terms-description">{content.description}</p>

      <div className="terms-box">
        <h4 className="terms-box-title">Bạn cần đồng ý các điều kiện sau:</h4>
        <ul className="terms-list">
          {content.conditions.map((cond, idx) => (
            <li key={idx}>{cond}</li>
          ))}
        </ul>
      </div>

      <button onClick={handleAgree} className="btn-terms-confirm">
        Tôi đồng ý và tiếp tục
      </button>
    </div>
  );
};

export default DonationTermsPage;
