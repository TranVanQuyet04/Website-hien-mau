import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/DonationIntentSelection.css";

const donationOptions = [
  {
    id: 1,
    title: "Tôi sẵn sàng hiến máu khi có thông báo (thường)",
    details: [
      "✓ Không gấp, có thể hẹn trước",
      "→ Dành cho trường hợp không khẩn cấp, liên hệ trước vài ngày",
    ],
    terms: "Tôi đồng ý nhận thông báo từ hệ thống và sẵn sàng tham gia theo lịch hẹn phù hợp.",
  },
  {
    id: 2,
    title: "Tôi muốn tham gia nhóm hiến máu khẩn cấp (ngoài bệnh viện)",
    details: [
      "✓ Có thể phản hồi nhanh, nhưng không thường xuyên có mặt tại bệnh viện",
      "✓ Có thể đến nơi cần máu (gần nhà, hoặc bệnh viện gần) trong thời gian ngắn",
    ],
    terms: "Tôi cam kết phản hồi nhanh khi hệ thống gửi yêu cầu và đảm bảo có thể di chuyển trong thời gian ngắn.",
  },
  {
    id: 3,
    title: "Tôi đang ở gần/ở trong bệnh viện và có thể hiến máu khẩn cấp ngay",
    details: [
      "✓ Luôn sẵn sàng tại bệnh viện hoặc khu vực lân cận",
      "✓ Dành cho nhân viên y tế, sinh viên y, người sống gần viện",
    ],
    terms: "Tôi đồng ý cho phép hệ thống ưu tiên tôi trong các trường hợp khẩn cấp gần khu vực đang ở.",
  },
  {
    id: 4,
    title: "Tôi chưa thể hiến máu hiện tại, nhưng sẽ cập nhật sau",
    details: [
      "→ Cho phép người dùng giữ hồ sơ nhưng chưa sẵn sàng",
      "→ Tránh mất dữ liệu tiềm năng",
    ],
    terms: "Tôi xác nhận thông tin cá nhân và sẽ chủ động cập nhật trạng thái khi đã sẵn sàng hiến máu.",
  },
];

const DonationIntentSelection = () => {
  const [selectedOption, setSelectedOption] = useState(null);
  const [agreed, setAgreed] = useState(false);
  const navigate = useNavigate();

  const handleConfirm = () => {
    if (selectedOption && agreed) {
      navigate("/agree-terms", { state: { optionId: selectedOption.id } });
    }
  };

  return (
    <div className="intent-container">
      <h2 className="intent-title">Bạn muốn tham gia hiến máu như thế nào?</h2>

      {donationOptions.map((option) => (
        <div
          key={option.id}
          className={`intent-option ${
            selectedOption?.id === option.id ? "selected" : ""
          }`}
          onClick={() => {
            setSelectedOption(option);
            setAgreed(false);
          }}
        >
          <label className="option-label">
            <div className="option-title">
              <input
                type="radio"
                checked={selectedOption?.id === option.id}
                onChange={() => {}}
                style={{ marginRight: "10px" }}
              />
              {option.title}
            </div>
            <ul className="option-details">
              {option.details.map((item, index) => (
                <li key={index}>{item}</li>
              ))}
            </ul>
          </label>
        </div>
      ))}

      {selectedOption && (
        <div className="terms-box">
          <label>
            <input
              type="checkbox"
              checked={agreed}
              onChange={(e) => setAgreed(e.target.checked)}
              style={{ marginRight: "10px" }}
            />
            <strong>Điều khoản:</strong> {selectedOption.terms}
          </label>
        </div>
      )}

      <button
        className="btn-continue"
        onClick={handleConfirm}
        disabled={!selectedOption || !agreed}
      >
        Tiếp tục
      </button>
    </div>
  );
};

export default DonationIntentSelection;
