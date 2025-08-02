import React, { useState } from "react";
import {
  Form,
  Input,
  Button,
  Card,
  Typography,
  Space,
  Avatar,
  message
} from "antd";
import {
  UserOutlined,
  IdcardOutlined,
  PhoneOutlined,
  ArrowLeftOutlined
} from "@ant-design/icons";

const { Title, Text } = Typography;

const Forgot = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (values) => {
    const { cccd, phone } = values;
    
    setLoading(true);
    try {
      // Simulate API call to verify CCCD and phone number
      const response = await fetch('/api/verify-identity', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ cccd, phone }),
      });

      if (!response.ok) {
        throw new Error('Verification failed');
      }

      const data = await response.json();
      
      // Store the verified email for OTP sending
      localStorage.setItem("recoveryEmail", data.email);
      localStorage.setItem("verifiedCCCD", cccd);
      localStorage.setItem("verifiedPhone", phone);
      
      message.success("Thông tin xác thực thành công! Mã OTP đã được gửi đến email của bạn.");
      
      setTimeout(() => {
        window.location.href = "/verify-otp";
      }, 1500);
    } catch (error) {
      message.error("Thông tin CCCD hoặc số điện thoại không chính xác!");
    } finally {
      setLoading(false);
    }
  };

  const handleBackToLogin = () => {
    window.location.href = "/login";
  };

  return (
    <div className="regis-fullpage">
      <div className="form-wrapper" style={{ 
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center', 
        width: '100%',
        padding: '30px'
      }}>
        <Card className="login-card" bodyStyle={{ padding: 40 }} style={{ maxWidth: 600, width: '100%' }}>
          <Space direction="vertical" size="large" style={{ width: "100%" }}>
            <div className="login-header">
              <Avatar 
                src="/donor.png" 
                icon={<UserOutlined />} 
                className="profile-img-card" 
              />
              <Title level={3} style={{ color: '#771813', marginBottom: '8px' }}>
                Đặt lại mật khẩu
              </Title>
              <Text type="secondary">
                Nhập thông tin tài khoản của bạn để xác minh danh tính
              </Text>
            </div>

            <Form 
              form={form} 
              onFinish={handleSubmit} 
              layout="vertical" 
              size="large"
            >
              <Form.Item
                name="cccd"
                label={<span>Số CMND/CCCD/Hộ Chiếu<span className="required">*</span></span>}
                rules={[
                  { required: true, message: "Vui lòng nhập số CCCD!" },
                  { 
                    pattern: /^[0-9]{9,12}$/, 
                    message: "Số CCCD phải có từ 9-12 chữ số!" 
                  }
                ]}
              >
                <Input
                  prefix={<IdcardOutlined />}
                  placeholder="Nhập số giấy tờ tùy thân"
                  maxLength={12}
                />
              </Form.Item>

              <Form.Item
                name="phone"
                label={<span>Số điện thoại<span className="required">*</span></span>}
                rules={[
                  { required: true, message: "Vui lòng nhập số điện thoại!" },
                  { 
                    pattern: /^[0-9]{10,11}$/, 
                    message: "Số điện thoại phải có 10-11 chữ số!" 
                  }
                ]}
              >
                <Input
                  prefix={<PhoneOutlined />}
                  placeholder="Nhập số điện thoại"
                  maxLength={11}
                />
              </Form.Item>

              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  loading={loading}
                  block
                  className="btn-gradient"
                >
                  Tiếp tục
                </Button>
              </Form.Item>
            </Form>

            <div style={{ textAlign: 'center' }}>
              <Text type="secondary" style={{ fontSize: '12px', display: 'block', marginBottom: '8px' }}>
                Bạn đã có tài khoản?{' '}
                <Button
                  type="link"
                  onClick={handleBackToLogin}
                  style={{ 
                    color: '#771813',
                    padding: 0,
                    height: 'auto',
                    fontSize: '12px'
                  }}
                >
                  Đăng nhập
                </Button>
              </Text>
            </div>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default Forgot;