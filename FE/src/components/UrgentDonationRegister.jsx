import React, { useEffect, useState } from "react";
import {
  Form,
  Input,
  Button,
  Card,
  message,
  Select,
  Typography,
  Divider,
} from "antd";
import axios from "axios";
import AuthService from "../services/auth.service";
import { HeartOutlined } from "@ant-design/icons";
import { apiUrl } from "../config/api";

const { Option } = Select;
const { Title, Text } = Typography;

const UrgentDonationRegister = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (user) {
      form.setFieldsValue({
        bloodTypeId: user.blood_type_id || null,
      });
    }

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const { latitude, longitude } = pos.coords;
        form.setFieldsValue({ latitude, longitude });
      },
      () => message.warning("⚠️ Không thể lấy vị trí từ trình duyệt.")
    );
  }, [form]);

  const handleSubmit = async (values) => {
    try {
      setLoading(true);
      const user = AuthService.getCurrentUser();
      const accessToken = user?.accessToken;

      if (!accessToken) {
        message.error("Bạn cần đăng nhập để tiếp tục.");
        return;
      }

      const payload = {
        bloodTypeId: values.bloodTypeId,
        latitude: values.latitude,
        longitude: values.longitude,
        readinessLevel: values.readinessLevel,
      };

      await axios.post(apiUrl("api/urgent-donors/register"), payload, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });

      message.success("✅ Đăng ký hiến máu khẩn cấp thành công!");
      form.resetFields();
    } catch (err) {
      console.error("❌ Lỗi:", err);
      message.error("❌ Đăng ký thất bại!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card
      title={
        <Title level={3} style={{ margin: 0 }}>
          <HeartOutlined style={{ color: "#f5222d", marginRight: 8 }} />
          Đăng ký hiến máu khẩn cấp
        </Title>
      }
      style={{ maxWidth: 600, margin: "auto", marginTop: 30 }}
      bordered
    >
      <Text type="secondary">
        Chọn nhóm máu, mức độ sẵn sàng và hệ thống sẽ tự xác định vị trí của bạn.
      </Text>

      <Divider />

      <Form layout="vertical" form={form} onFinish={handleSubmit}>
        <Form.Item
          label="Nhóm máu"
          name="bloodTypeId"
          rules={[{ required: true, message: "Vui lòng chọn nhóm máu" }]}
        >
          <Select placeholder="Chọn nhóm máu">
            <Option value={1}>A+</Option>
            <Option value={2}>A-</Option>
            <Option value={3}>B+</Option>
            <Option value={4}>B-</Option>
            <Option value={5}>AB+</Option>
            <Option value={6}>AB-</Option>
            <Option value={7}>O+</Option>
            <Option value={8}>O-</Option>
          </Select>
        </Form.Item>

        <Form.Item
          label="Mức độ sẵn sàng"
          name="readinessLevel"
          rules={[{ required: true }]}
          initialValue="EMERGENCY_NOW"
        >
          <Select>
            <Option value="EMERGENCY_NOW">Hiến ngay khi cần</Option>
            <Option value="EMERGENCY_FLEXIBLE">Sẵn sàng khi được gọi</Option>
          </Select>
        </Form.Item>

        <Form.Item label="Vĩ độ (Latitude)" name="latitude" rules={[{ required: true }]}>
          <Input readOnly />
        </Form.Item>

        <Form.Item label="Kinh độ (Longitude)" name="longitude" rules={[{ required: true }]}>
          <Input readOnly />
        </Form.Item>

        <Divider />

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            loading={loading}
            block
            size="large"
          >
            🚑 Gửi đăng ký
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};

export default UrgentDonationRegister;
