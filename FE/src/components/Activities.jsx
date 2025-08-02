import React from "react";
import "../styles/Activities.css";

const Activities = () => {
  return (
    <div className="activities-wrapper">
      <h2 className="activities-title">Hoạt động hiến máu nổi bật</h2>

      <div className="activity-card-list">
        <div className="activity-card">
          <h4>Ngày hội Hiến máu Toàn quốc</h4>
          <p>Tham gia sự kiện thường niên nhằm nâng cao nhận thức và đóng góp cho cộng đồng.</p>
          <span className="activity-date">🗓️ 15/06/2025</span>
        </div>

        <div className="activity-card">
          <h4>Chiến dịch "Giọt máu hồng"</h4>
          <p>Chiến dịch mùa hè kêu gọi sinh viên, đoàn viên tham gia hiến máu tình nguyện.</p>
          <span className="activity-date">🗓️ 01/07/2025</span>
        </div>

        <div className="activity-card">
          <h4>Tuần lễ Hiến máu tại Hà Nội</h4>
          <p>Sự kiện đặc biệt tổ chức tại Nhà văn hóa Thanh niên với hơn 500 người tham gia.</p>
          <span className="activity-date">🗓️ 20/07/2025</span>
        </div>
      </div>
    </div>
  );
};

export default Activities;
