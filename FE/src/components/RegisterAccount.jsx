/* === src/pages/register/RegisterAccount.jsx === */
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { 
  Form, 
  Input, 
  Button, 
  Card,
  Typography,
  message,
  Space,
  Progress,
  Alert
} from "antd";
import { 
  UserOutlined, 
  LockOutlined, 
  SafetyOutlined, 
  ArrowLeftOutlined,
  EyeInvisibleOutlined,
  EyeOutlined
} from "@ant-design/icons";
import RegisterProgress from "../components/RegisterProgress";
import { FaUser, FaEnvelope, FaAddressCard, FaLock } from "react-icons/fa";

const { Title, Text } = Typography;

const RegisterAccount = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const [passwordStrength, setPasswordStrength] = useState(0);
  const [passwordStatus, setPasswordStatus] = useState('');

  useEffect(() => {
    const saved = localStorage.getItem("registerForm");
    if (saved) {
      try {
        const parsedData = JSON.parse(saved);
        form.setFieldsValue(parsedData);
      } catch (error) {
        console.error("Error parsing saved data:", error);
      }
    }
  }, [form]);

  const calculatePasswordStrength = (password) => {
    if (!password) return 0;
    
    let strength = 0;
    const checks = [
      { regex: /.{8,}/, points: 25 }, // Length >= 8
      { regex: /[a-z]/, points: 25 }, // Lowercase
      { regex: /[A-Z]/, points: 25 }, // Uppercase
      { regex: /[0-9]/, points: 25 }, // Numbers
      { regex: /[^A-Za-z0-9]/, points: 25 } // Special chars
    ];

    checks.forEach(check => {
      if (check.regex.test(password)) {
        strength += check.points;
      }
    });

    return Math.min(strength, 100);
  };

  const getPasswordStatus = (strength) => {
    if (strength < 25) return { status: 'exception', text: 'Rất yếu' };
    if (strength < 50) return { status: 'normal', text: 'Yếu' };
    if (strength < 75) return { status: 'active', text: 'Trung bình' };
    if (strength < 100) return { status: 'success', text: 'Mạnh' };
    return { status: 'success', text: 'Rất mạnh' };
  };

  const onFinish = (values) => {
    try {
      const saved = localStorage.getItem("registerForm");
      const existingData = saved ? JSON.parse(saved) : {};
      const updatedData = { ...existingData, ...values };
      
      localStorage.setItem("registerForm", JSON.stringify(updatedData));
      message.success("Thông tin tài khoản đã được lưu!");
      navigate("/register/confirm");
    } catch (error) {
      message.error("Có lỗi xảy ra khi lưu thông tin!");
    }
  };

  const onValuesChange = (changedValues, allValues) => {
    if (changedValues.password) {
      const strength = calculatePasswordStrength(changedValues.password);
      setPasswordStrength(strength);
      setPasswordStatus(getPasswordStatus(strength));
    }

    try {
      const saved = localStorage.getItem("registerForm");
      const existingData = saved ? JSON.parse(saved) : {};
      const updatedData = { ...existingData, ...allValues };
      localStorage.setItem("registerForm", JSON.stringify(updatedData));
    } catch (error) {
      console.error("Error saving form data:", error);
    }
  };

  const handleBack = () => navigate("/register/contact");

  const validateMessages = {
    required: "${label} là bắt buộc!",
    pattern: {
      mismatch: "${label} không đúng định dạng!",
    },
  };

  return (
    <div className="regis-fullpage">
      <div className="regis-container">
        <Card className="register-card" style={{ maxWidth: 800, margin: '0 auto' }}>
          <RegisterProgress
            currentStep={2}
            steps={["Thông tin cá nhân", "Liên hệ", "Tài khoản", "Xác nhận"]}
            icons={[<FaUser />, <FaEnvelope />, <FaAddressCard />, <FaLock />]}
          />
          
          <Title level={3} style={{ textAlign: 'center', marginBottom: 32 }}>
            <LockOutlined style={{ marginRight: 8 }} />
            Tạo tài khoản
          </Title>

          <Form
            form={form}
            layout="vertical"
            onFinish={onFinish}
            onValuesChange={onValuesChange}
            validateMessages={validateMessages}
          >
            <Form.Item
              label="Tên đăng nhập"
              name="username"
              rules={[
                { required: true },
                { min: 3, message: 'Tên đăng nhập phải có ít nhất 3 ký tự' },
                { max: 20, message: 'Tên đăng nhập không được quá 20 ký tự' },
                {
                  pattern: /^[a-zA-Z0-9_]+$/,
                  message: 'Chỉ được sử dụng chữ cái, số và dấu gạch dưới'
                }
              ]}
              extra={<Text type="secondary">Chỉ được sử dụng chữ cái, số và dấu gạch dưới (_)</Text>}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập tên đăng nhập"
                prefix={<UserOutlined />}
                maxLength={20}
              />
            </Form.Item>

            <Form.Item
              label="Mật khẩu"
              name="password"
              rules={[
                { required: true },
                { min: 6, message: 'Mật khẩu phải có ít nhất 6 ký tự' }
              ]}
              hasFeedback
            >
              <Input.Password
                size="large"
                placeholder="Nhập mật khẩu"
                prefix={<LockOutlined />}
                iconRender={(visible) => (visible ? <EyeOutlined /> : <EyeInvisibleOutlined />)}
              />
            </Form.Item>

            {passwordStrength > 0 && (
              <div style={{ marginBottom: 16 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
                  <Text type="secondary">Độ mạnh mật khẩu:</Text>
                  <Text type={passwordStatus.status === 'exception' ? 'danger' : 'success'}>
                    {passwordStatus.text}
                  </Text>
                </div>
                <Progress 
                  percent={passwordStrength} 
                  status={passwordStatus.status}
                  showInfo={false}
                  size="small"
                />
              </div>
            )}

            <Form.Item
              label="Nhập lại mật khẩu"
              name="confirmPassword"
              dependencies={['password']}
              rules={[
                { required: true },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue('password') === value) {
                      return Promise.resolve();
                    }
                    return Promise.reject(new Error('Mật khẩu xác nhận không khớp!'));
                  },
                }),
              ]}
              hasFeedback
            >
              <Input.Password
                size="large"
                placeholder="Nhập lại mật khẩu"
                prefix={<LockOutlined />}
                iconRender={(visible) => (visible ? <EyeOutlined /> : <EyeInvisibleOutlined />)}
              />
            </Form.Item>

            <Alert
              message="Bảo mật tài khoản"
              description="Vui lòng tạo mật khẩu mạnh và bảo mật thông tin đăng nhập của bạn. Không chia sẻ thông tin tài khoản với người khác."
              type="info"
              showIcon
              style={{ marginBottom: 24 }}
            />

            <Form.Item style={{ marginBottom: 0 }}>
              <Space style={{ width: '100%', justifyContent: 'space-between' }}>
                <Button
                  size="large"
                  onClick={handleBack}
                  icon={<ArrowLeftOutlined />}
                  style={{ minWidth: 120 }}
                >
                  Quay lại
                </Button>
                
                <Button
                  type="primary"
                  htmlType="submit"
                  size="large"
                  style={{
                    minWidth: 120,
                    background: 'linear-gradient(45deg, #ff6b6b, #ee5a52)',
                    border: 'none'
                  }}
                >
                  Tiếp theo
                </Button>
              </Space>
            </Form.Item>
          </Form>
        </Card>
      </div>
    </div>
  );
};

export default RegisterAccount;