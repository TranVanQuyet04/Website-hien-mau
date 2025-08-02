import React, { useState } from "react";
import {
  FaTint, FaCheck, FaIdCard, FaSyringe, FaHeartbeat, FaVial,
  FaWeight, FaBirthdayCake, FaCalendarAlt, FaVirus
} from "react-icons/fa";
import "../styles/DonationInfoSection.css";

const BloodInfoSection = () => {
  const [selectedGroup, setSelectedGroup] = useState(null);

  const eligibilityCriteria = [
    { icon: <FaIdCard />, text: "Mang theo chứng minh nhân dân/hộ chiếu" },
    { icon: <FaSyringe />, text: "Không nghiện ma túy, rượu bia và các chất kích thích" },
    { icon: <FaVirus />, text: "Không mắc hoặc không có hành vi nguy cơ lây nhiễm HIV, viêm gan B, viêm gan C, và các virus truyền máu" },
    { icon: <FaHeartbeat />, text: "Không mắc các bệnh mãn tính hoặc cấp tính về tim mạch, huyết áp, hô hấp, dạ dày…" },
    { icon: <FaVial />, text: "Chỉ số huyết sắc tố (Hb) ≥120g/l (≥125g/l nếu hiến từ 350ml trở lên)" },
    { icon: <FaWeight />, text: "Cân nặng: Nam ≥ 45 kg, Nữ ≥ 45 kg" },
    { icon: <FaBirthdayCake />, text: "Người khỏe mạnh trong độ tuổi từ 18 đến 60 tuổi" },
    { icon: <FaCalendarAlt />, text: "Thời gian giữa 2 lần hiến máu tối thiểu 12 tuần" },
    { icon: <FaCheck />, text: "Kết quả test nhanh âm tính với kháng nguyên siêu vi B" },
  ];

  const bloodCompatibility = [
    {
      group: "O-",
      give: "O-, O+, A-, A+, B-, B+, AB-, AB+",
      receive: "O-",
      info: "Nhóm máu O- là nhóm máu hiếm và có thể truyền cho bất kỳ ai. Nhưng người O- chỉ có thể nhận từ chính họ."
    },
    {
      group: "O+",
      give: "O+, A+, B+, AB+",
      receive: "O-, O+",
      info: "O+ là nhóm máu phổ biến nhất. Người O+ có thể truyền cho các nhóm Rh dương."
    },
    {
      group: "A-",
      give: "A-, A+, AB-, AB+",
      receive: "O-, A-",
      info: "A- hiếm hơn A+. Người nhóm máu này có thể truyền cho A và AB, cả dương và âm."
    },
    {
      group: "A+",
      give: "A+, AB+",
      receive: "O-, O+, A-, A+",
      info: "A+ phổ biến thứ hai. Có thể nhận máu từ các nhóm A và O bất kỳ Rh."
    },
  ];

  return (
    <div className="blood-wrapper">
      {/* KHỐI 1: TIÊU CHUẨN */}
      <div className="section-card">
        <h2 className="section-title gold">🧬 Tiêu chuẩn tham gia hiến máu</h2>
        <div className="eligibility-grid">
          {eligibilityCriteria.map((item, i) => (
            <div key={i} className="eligibility-card">
              <div className="eligibility-icon">{item.icon}</div>
              <div className="eligibility-text">{item.text}</div>
            </div>
          ))}
        </div>
      </div>

      {/* KHOẢNG TRẮNG GIỮA 2 KHỐI */}
      <div className="section-spacer"></div>

      {/* KHỐI 2: NHÓM MÁU */}
      <div className="section-card">
        <h2 className="section-title gold">💧 Nhóm máu và khả năng tương thích</h2>
        <p className="subtitle">Tìm hiểu khả năng truyền/nhận máu của từng nhóm máu.</p>
        <div className="compatibility-grid">
          {bloodCompatibility.map((item, i) => (
            <div
              key={i}
              className={`compatibility-card ${selectedGroup === i ? "active" : ""}`}
              onClick={() => setSelectedGroup(selectedGroup === i ? null : i)}
            >
              <h3>{item.group}</h3>
              <p><FaCheck /> <strong>Có thể cho:</strong> {item.give}</p>
              <p><FaTint /> <strong>Có thể nhận:</strong> {item.receive}</p>
              {selectedGroup === i && (
                <div className="group-info">
                  <hr />
                  <p>{item.info}</p>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default BloodInfoSection;
