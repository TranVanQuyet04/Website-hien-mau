// src/components/Footer.jsx
import React from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Footer.css";

const Footer = () => {
  const navigate = useNavigate();

  return (
    <footer className="blood-footer">
      <div className="blood-footer-wrapper">
        <div className="blood-footer-top">
          {/* Truyền cảm hứng */}
          <div className="footer-box quote-box">
            <h4>Hiến Máu – Hành Động Nhân Văn</h4>
            <p>
              “Một giọt máu cho đi, một cuộc đời ở lại.” <br />
              Hiến máu là cách đơn giản nhất để cứu người. Hãy cùng lan tỏa sự sống.
            </p>
          </div>

          {/* Thông tin liên hệ */}
          <div className="footer-box info-box">
            <h4>Liên Hệ Với Chúng Tôi</h4>
            <p>Trường Đại học FPT cơ sở HCM</p>
            <p>Lô E2a-7, Đường D1, Khu Công nghệ cao, Phường Tăng Nhơn Phú, TPHCM</p>
            <p>📞 (028) 7300 5588</p>
          </div>

          {/* CTA */}
          <div className="footer-box cta-box">
            <h4>Bạn Đã Sẵn Sàng Cứu Người?</h4>
            <p>Tham gia cộng đồng người hiến máu và nhận thông tin mới nhất từ hệ thống.</p>
            <button className="cta-button" onClick={() => navigate("/login")}>
              Tham Gia Ngay
            </button>
          </div>
        </div>
      </div>

      <div className="blood-footer-bottom">
        <p>© 2025 Trung Tâm Hiến Máu Việt Nam. Mọi quyền được bảo lưu.</p>
        <div className="blood-footer-links">
          {/* <span>Chính sách bảo mật</span>
          <span>Điều khoản sử dụng</span>
          <span>Cookie & Theo dõi</span> */}
        </div>
      </div>
    </footer>
  );
};

export default Footer;