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
  LockOutlined,
  EyeInvisibleOutlined,
  EyeTwoTone
} from "@ant-design/icons";

const { Title, Text } = Typography;

const ChangePassword = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      message.success("Mật khẩu thay đổi thành công!");
      form.resetFields();
    } catch (err) {
      message.error("Có lỗi xảy ra khi đổi mật khẩu");
    } finally {
      setLoading(false);
    }
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
              <Title level={3} className="login-title">Đổi mật khẩu</Title>
              <Text type="secondary">Cập nhật mật khẩu mới của bạn</Text>
            </div>

            <Form 
              form={form} 
              onFinish={handleSubmit} 
              layout="vertical" 
              size="large"
            >
              <Form.Item
                name="newPassword"
                label={<span>Mật khẩu mới <span className="required">*</span></span>}
                rules={[
                  { required: true, message: "Vui lòng nhập mật khẩu mới!" },
                  { min: 6, message: "Mật khẩu phải từ 6 ký tự trở lên!" }
                ]}
              >
                <Input.Password
                  prefix={<LockOutlined />}
                  placeholder="Nhập mật khẩu mới"
                  iconRender={vis => vis ? <EyeTwoTone /> : <EyeInvisibleOutlined />}
                />
              </Form.Item>

              <Form.Item
                name="confirmPassword"
                label={<span>Xác nhận mật khẩu <span className="required">*</span></span>}
                dependencies={['newPassword']}
                rules={[
                  { required: true, message: "Vui lòng xác nhận mật khẩu!" },
                  ({ getFieldValue }) => ({
                    validator(_, value) {
                      if (!value || getFieldValue('newPassword') === value) {
                        return Promise.resolve();
                      }
                      return Promise.reject(new Error('Mật khẩu không khớp!'));
                    },
                  }),
                ]}
              >
                <Input.Password
                  prefix={<LockOutlined />}
                  placeholder="Xác nhận mật khẩu mới"
                  iconRender={vis => vis ? <EyeTwoTone /> : <EyeInvisibleOutlined />}
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
                  Đổi mật khẩu
                </Button>
              </Form.Item>
            </Form>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default ChangePassword;