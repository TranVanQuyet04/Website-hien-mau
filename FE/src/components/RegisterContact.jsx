/* === src/pages/register/RegisterContact.jsx === */
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { 
  Form, 
  Input, 
  Button, 
  Card,
  Typography,
  message,
  Space,
  Alert
} from "antd";
import { 
  MailOutlined, 
  PhoneOutlined, 
  HomeOutlined, 
  ArrowLeftOutlined 
} from "@ant-design/icons";
import RegisterProgress from "../components/RegisterProgress";
import { FaUser, FaEnvelope, FaAddressCard, FaLock } from "react-icons/fa";

const { Title, Text } = Typography;
const { TextArea } = Input;

const RegisterContact = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

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

  const onFinish = (values) => {
    try {
      const saved = localStorage.getItem("registerForm");
      const existingData = saved ? JSON.parse(saved) : {};
      const updatedData = { ...existingData, ...values };
      
      localStorage.setItem("registerForm", JSON.stringify(updatedData));
      message.success("Thông tin liên hệ đã được lưu!");
      navigate("/register/account");
    } catch (error) {
      message.error("Có lỗi xảy ra khi lưu thông tin!");
    }
  };

  const onValuesChange = (changedValues, allValues) => {
    try {
      const saved = localStorage.getItem("registerForm");
      const existingData = saved ? JSON.parse(saved) : {};
      const updatedData = { ...existingData, ...allValues };
      localStorage.setItem("registerForm", JSON.stringify(updatedData));
    } catch (error) {
      console.error("Error saving form data:", error);
    }
  };

  const handleBack = () => navigate("/register/information");

  const validateMessages = {
    required: "${label} là bắt buộc!",
    types: {
      email: "${label} không hợp lệ!",
    },
    pattern: {
      mismatch: "${label} không đúng định dạng!",
    },
  };

  return (
    <div className="regis-fullpage">
      <div className="regis-container">
        <Card className="register-card" style={{ maxWidth: 800, margin: '0 auto' }}>
          <RegisterProgress
            currentStep={1}
            steps={["Thông tin cá nhân", "Liên hệ", "Tài khoản", "Xác nhận"]}
            icons={[<FaUser />, <FaEnvelope />, <FaAddressCard />, <FaLock />]}
          />
          
          <Title level={3} style={{ textAlign: 'center', marginBottom: 32 }}>
            <MailOutlined style={{ marginRight: 8 }} />
            Thông tin liên hệ
          </Title>

          <Form
            form={form}
            layout="vertical"
            onFinish={onFinish}
            onValuesChange={onValuesChange}
            validateMessages={validateMessages}
          >
            <Form.Item
              label="Email"
              name="email"
              rules={[
                { required: true },
                { type: 'email' }
              ]}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập địa chỉ email"
                prefix={<MailOutlined />}
                type="email"
              />
            </Form.Item>

            <Form.Item
              label="Số điện thoại"
              name="phone"
              rules={[
                { required: true },
                {
                  pattern: /^(0[3-9])[0-9]{8,9}$/,
                  message: 'Số điện thoại không hợp lệ (VD: 0912345678)'
                }
              ]}
              extra={<Text type="secondary">Nhập số điện thoại di động 10-11 chữ số</Text>}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập số điện thoại (VD: 0912345678)"
                prefix={<PhoneOutlined />}
                maxLength={11}
                onChange={(e) => {
                  // Only allow numbers
                  const value = e.target.value.replace(/[^\d]/g, '');
                  form.setFieldsValue({ phone: value });
                }}
              />
            </Form.Item>

            <Form.Item
              label="Địa chỉ liên hệ"
              name="address"
              rules={[
                { required: true },
                { min: 10, message: 'Địa chỉ phải có ít nhất 10 ký tự' },
                { max: 200, message: 'Địa chỉ không được quá 200 ký tự' }
              ]}
              extra={<Text type="secondary">Có thể khác với địa chỉ trên giấy tờ</Text>}
              hasFeedback
            >
              <TextArea
                size="large"
                placeholder="Nhập địa chỉ liên hệ chi tiết"
                rows={4}
                showCount
                maxLength={200}
                style={{ resize: 'none' }}
              />
            </Form.Item>

            <Alert
              message="Thông tin liên hệ"
              description="Vui lòng cung cấp thông tin liên hệ chính xác. Chúng tôi sẽ sử dụng thông tin này để xác thực tài khoản và liên hệ khi cần thiết."
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

export default RegisterContact;