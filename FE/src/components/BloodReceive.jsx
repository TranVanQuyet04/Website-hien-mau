import React, { useEffect, useState } from "react";
import { Collapse, List, Typography, Alert, Spin, Card, Divider } from "antd";

const { Title, Paragraph, Text, Link } = Typography;
const { Panel } = Collapse;

const BloodReceive = () => {
  const [receiveMethods, setReceiveMethods] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const sampleData = [
      { 
        id: 1, 
        method_name: "📱 Đăng ký trực tuyến", 
        steps: [
          "Truy cập mục 'Gửi yêu cầu nhận máu' trên website.",
          "Chọn nhóm máu và số lượng cần.",
          "Xác nhận và nhận thông báo qua email hoặc SMS."
        ],
        note: "Vui lòng đảm bảo thông tin chính xác để quá trình xử lý nhanh chóng."
      },
      { 
        id: 2, 
        method_name: "📞 Liên hệ hotline", 
        steps: [
          "Gọi số 1900-123-456 (24/7).",
          "Cung cấp thông tin cá nhân và yêu cầu khẩn cấp.",
          "Đợi nhân viên liên hệ lại trong 15 phút."
        ],
        note: "Hotline hỗ trợ 24/7, ưu tiên trường hợp khẩn cấp."
      },
      { 
        id: 3, 
        method_name: "🏥 Đến trung tâm y tế", 
        steps: [
          "Mang CMND/CCCD đến trung tâm y tế gần nhất.",
          "Điền biểu mẫu yêu cầu.",
          "Nhận máu sau khi được bác sĩ phê duyệt (thường trong 1-2 giờ)."
        ],
        note: "Đảm bảo sức khỏe ổn định trước khi đến nhận máu."
      },
    ];
    setTimeout(() => {
      setReceiveMethods(sampleData);
      setLoading(false);
    }, 500);
  }, []);

  if (loading) return <Spin tip="Đang tải dữ liệu..." size="large" style={{ display: "block", marginTop: 80 }} />;
  if (error) return <Alert message={error} type="error" showIcon style={{ margin: 16 }} />;

  return (
    <div style={{ padding: 24 }}>
      <Title level={2}>🩸 Liên hệ với chúng tôi ngay:</Title>

      <Collapse accordion>
        {receiveMethods.map((method) => (
          <Panel header={method.method_name} key={method.id}>
            <List
              size="small"
              dataSource={method.steps}
              renderItem={(item, idx) => (
                <List.Item>
                  <Text>{idx + 1}. {item}</Text>
                </List.Item>
              )}
            />
            {method.note && (
              <Paragraph type="secondary" style={{ marginTop: 12 }}>
                <Text strong>Ghi chú:</Text> {method.note}
              </Paragraph>
            )}
          </Panel>
        ))}
      </Collapse>

      <Divider />

      <Card title="📞 Thông tin liên hệ hỗ trợ" bordered style={{ marginTop: 24 }}>
        <Paragraph>
          <Text strong>Hotline:</Text>{" "}
          <Link href="tel:1900123456">1900-123-456</Link>
        </Paragraph>
        <Paragraph>
          <Text strong>Email:</Text>{" "}
          <Link href="mailto:support@bloodbank.com">support@bloodbank.com</Link>
        </Paragraph>
        <Paragraph>
          <Text strong>Website:</Text>{" "}
          <Link href="https://bloodbank.com" target="_blank" rel="noopener noreferrer">
            https://bloodbank.com
          </Link>
        </Paragraph>
      </Card>
    </div>
  );
};

export default BloodReceive;