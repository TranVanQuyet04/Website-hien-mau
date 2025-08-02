import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../services/user.service";
import BenefitCarousel from "./BenefitCarousel";
import Footer from "./Footer";
import DonationInfoSection from './DonationInfoSection';
import { Button, Typography, Row, Col, Card, Divider } from 'antd';
import { motion } from 'framer-motion';
import "../styles/Home.css";

const { Title, Paragraph } = Typography;

const infoSections = [
  {
    title: "Lợi ích khi hiến máu",
    summary: "Hiểu rõ về tác động tích cực và lợi ích sức khỏe khi hiến máu thường xuyên.",
    blogId: "3"
  },
  {
    title: "Ai có thể hiến máu?",
    summary: "Kiểm tra điều kiện để biết bạn có đủ điều kiện tham gia hiến máu không.",
    blogId: "4"
  },
  {
    title: "Quy trình hiến máu",
    summary: "Tìm hiểu các bước cơ bản trong quy trình hiến máu an toàn và hiệu quả.",
    blogId: "5"
  }
];

const Home = () => {
  const [content, setContent] = useState("");
  const navigate = useNavigate();
const token = localStorage.getItem("token");
const user = JSON.parse(localStorage.getItem("user") || "{}");
  useEffect(() => {
    let isMounted = true;
    UserService.getPublicContent().then(
      (res) => {
        if (isMounted) setContent(res.data);
      },
      (err) => {
        if (isMounted) {
          const _content = err?.response?.data || err.message || "Lỗi tải nội dung.";
          setContent(_content);
        }
      }
    );
    return () => {
      isMounted = false;
    };
  }, []);

  return (
    <div className="home-wrapper">
      {/* 🔥 Video Background Hero */}
      <div className="video-hero-wrapper">
        <video autoPlay muted loop playsInline className="background-video">
          <source src="/blood3.mp4" type="video/mp4" />
          Trình duyệt không hỗ trợ video.
        </video>

        <div className="video-overlay">
          <h1>Hiến máu - Cứu người</h1>
          <p>Mỗi giọt máu cho đi là một cuộc đời ở lại</p>
<Button
  type="primary"
  size="large"
  className="cta-button"
  onClick={() => {
    const token = localStorage.getItem("token");
    const user = JSON.parse(localStorage.getItem("user") || "{}");

    if (token && user.userId) {
      // ✅ Đã đăng nhập → Chuyển đến trang đăng ký hiến máu
      navigate(`/user/${user.userId}/register`);
    } else {
      // ❌ Chưa đăng nhập → Đưa về trang đăng nhập
      navigate("/login");
    }
  }}
>
  Hiến máu ngay
</Button>

        </div>
      </div>

      {/* <motion.div
        initial={{ opacity: 0, y: 30 }}
        whileInView={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.8 }}
        viewport={{ once: true }}
      >
        <Title level={2} className="section-intro-heading">
          Khám phá hành trình hiến máu đầy ý nghĩa
        </Title>
      </motion.div> */}

      <BenefitCarousel />
      <DonationInfoSection />

      <Divider />

      <section className="info-section">
        <Row gutter={[32, 32]} justify="center">
          {infoSections.map((item, index) => (
            <Col xs={22} sm={10} md={6} key={index}>
              <Card hoverable className="info-card" onClick={() => navigate(`/blog/${item.blogId}`)}>
                <Title level={4}>{item.title}</Title>
                <Paragraph>{item.summary}</Paragraph>
              </Card>
            </Col>
          ))}
        </Row>
      </section>

      <section className="blog-preview-section">
        <Title className="section-title">Từ Blog của chúng tôi</Title>
        <div className="grid-cards">
          <div className="blog-card no-left-border" onClick={() => navigate("/blog/1")}>
            <img src="/banner1.jpg" alt="Blog 1" />
            <h4>Vì sao nên hiến máu?</h4>
            <p>Khám phá tác động tích cực từ việc bạn cho đi giọt máu quý giá.</p>
          </div>
          <div className="blog-card no-left-border" onClick={() => navigate("/blog/2")}>
            <img src="/banner2.jpg" alt="Blog 2" />
            <h4>Mẹo nhỏ trước khi hiến máu</h4>
            <p>Những lưu ý giúp bạn cảm thấy thoải mái và an toàn khi hiến máu.</p>
          </div>
        </div>
        <div style={{ textAlign: "center", marginTop: "1rem" }}>
          <Button type="primary" size="large" onClick={() => navigate("/blog")}>Xem tất cả bài viết</Button>
        </div>
      </section>

      <Footer />
    </div>
  );
};

export default Home;