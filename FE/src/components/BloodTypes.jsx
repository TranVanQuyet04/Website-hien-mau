import React, { useEffect, useState } from "react";
import { Row, Col, Tag, Typography, Spin, Tooltip } from "antd";
import { FireTwoTone, HeartTwoTone, ShareAltOutlined } from "@ant-design/icons";
import { motion } from "framer-motion";
import "../styles/BloodTypes.css";

import BloodTypeStatsChart from "./BloodTypeStatsChart";
import BloodTypeQuiz from "./BloodTypeQuiz";

const { Title, Text, Paragraph } = Typography;

const bloodData = [
  { id: 1, name: "A+", rh: "+", receives: ["A+", "A-", "O+", "O-"], gives: ["A+", "AB+"], stars: 3, funfact: `A+ là nhóm máu phổ biến (~30%). Có thể nhận từ A+, A-, O+, O-.` },
  { id: 2, name: "A-", rh: "-", receives: ["A-", "O-"], gives: ["A+", "A-", "AB+", "AB-"], stars: 4, funfact: `A- là nhóm máu hiếm (~6%). Có thể hiến cho cả Rh- và Rh+.` },
  { id: 3, name: "B+", rh: "+", receives: ["B+", "B-", "O+", "O-"], gives: ["B+", "AB+"], stars: 3, funfact: `B+ chiếm khoảng 9% dân số toàn cầu. Là nhóm phổ biến ở châu Á.` },
  { id: 4, name: "B-", rh: "-", receives: ["B-", "O-"], gives: ["B+", "B-", "AB+", "AB-"], stars: 5, funfact: `B- là nhóm máu cực hiếm (~2%). Người B- nên hiến máu định kỳ.` },
  { id: 5, name: "AB+", rh: "+", receives: ["Tất cả"], gives: ["AB+"], stars: 4, funfact: `AB+ là người nhận phổ thông. Dùng nhiều trong các ca phức tạp.` },
  { id: 6, name: "AB-", rh: "-", receives: ["AB-", "A-", "B-", "O-"], gives: ["AB+", "AB-"], stars: 5, funfact: `AB- là nhóm máu hiếm nhất (~0.5%). Rất cần thiết trong cấp cứu.` },
  { id: 7, name: "O+", rh: "+", receives: ["O+", "O-"], gives: ["O+", "A+", "B+", "AB+"], stars: 3, funfact: `O+ là nhóm phổ biến nhất (~37%). Hiến được cho mọi nhóm Rh+.` },
  { id: 8, name: "O-", rh: "-", receives: ["O-"], gives: ["Tất cả"], stars: 5, funfact: `O- là nhóm hiến "phổ thông", có thể dùng trong cấp cứu khẩn cấp.` }
];

const BloodTypes = () => {
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    setTimeout(() => setLoading(false), 300);
  }, []);

  if (loading) return <Spin tip="Đang tải..." size="large" style={{ display: "block", marginTop: 80 }} />;

  return (
    <div style={{ padding: 24 }}>
      <Title level={2} style={{ color: "#cf1322", marginBottom: 12 }}>🩸 Các loại nhóm máu</Title>

      {/* 🧠 Quiz kiến thức */}
      <BloodTypeQuiz />

      {/* 🔄 Danh sách các nhóm máu */}
      <Row gutter={[16, 24]} style={{ marginTop: 24 }}>
        {bloodData.map((blood) => (
          <Col xs={24} sm={12} md={8} lg={6} key={blood.id}>
            <motion.div
              className="flip-card"
              whileHover={{ scale: 1.03 }}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.4 }}
            >
              <div className="flip-card-inner">
                {/* FRONT */}
                <div className="flip-card-front">
                  <div className="blood-card">
                    <div className="blood-header">
                      <Tooltip title={`Nhóm máu ${blood.name} (${blood.rh})`}>
                        <Title level={3} className="blood-title">
                          <FireTwoTone /> {blood.name}
                          <Tag color={blood.rh === "+" ? "volcano" : "blue"}>Rh {blood.rh}</Tag>
                        </Title>
                      </Tooltip>
                      <Tag color={blood.stars >= 4 ? "magenta" : "green"}>
                        {blood.stars >= 4 ? "Hiếm" : "Phổ thông"}
                      </Tag>
                    </div>

                    <div className="rating-stars">
                      {"⭐".repeat(blood.stars)}{"☆".repeat(5 - blood.stars)}
                    </div>

                    <Text><ShareAltOutlined /> <b>Nhận từ:</b> {" "}
                      {blood.receives.map(g => (
                        <Tag key={g} className="tag-receive">{g}</Tag>
                      ))}
                    </Text>
                    <br />
                    <Text><HeartTwoTone twoToneColor="#eb2f96" /> <b>Hiến cho:</b> {" "}
                      {blood.gives.map(g => (
                        <Tag key={g} className="tag-give">{g}</Tag>
                      ))}
                    </Text>
                  </div>
                </div>

                {/* BACK */}
                <div className="flip-card-back">
                  <div className="blood-card back">
                    <Title level={5}>📚 Thông tin thú vị</Title>
                    <Paragraph style={{ fontSize: 13 }}>{blood.funfact}</Paragraph>
                  </div>
                </div>
              </div>
            </motion.div>
          </Col>
        ))}
      </Row>

      {/* 📊 Biểu đồ tỷ lệ nhóm máu */}
      <div style={{ marginTop: 48 }}>
        <BloodTypeStatsChart />
      </div>
    </div>
  );
};

export default BloodTypes;