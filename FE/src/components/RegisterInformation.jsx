/* === src/pages/register/RegisterInformation.jsx === */
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { 
  Form, 
  Input, 
  Select, 
  DatePicker, 
  Radio, 
  Button, 
  Row, 
  Col, 
  Card,
  Typography,
  message,
  Alert
} from "antd";
import { 
  UserOutlined, 
  IdcardOutlined, 
  HomeOutlined,
  CalendarOutlined 
} from "@ant-design/icons";
import RegisterProgress from "../components/RegisterProgress";
import { FaUser, FaEnvelope, FaAddressCard, FaLock } from "react-icons/fa";
import dayjs from "dayjs";

const { Title, Text } = Typography;
const { Option } = Select;

const RegisterInformation = () => {
  const navigate = useNavigate();
  const [form] = Form.useForm();

  useEffect(() => {
    const saved = localStorage.getItem("registerForm");
    if (saved) {
      try {
        const parsedData = JSON.parse(saved);
        // Convert date string back to dayjs object for DatePicker
        if (parsedData.dob) {
          parsedData.dob = dayjs(parsedData.dob);
        }
        form.setFieldsValue(parsedData);
      } catch (error) {
        console.error("Error parsing saved data:", error);
      }
    }
  }, [form]);

  const onFinish = (values) => {
    try {
      // Convert dayjs object to string for storage
      const formData = {
        ...values,
        dob: values.dob ? values.dob.format('YYYY-MM-DD') : '',
        // Auto-generate full address
        address: `${values.street}, ${values.ward}, ${values.district}, ${values.province}`
      };
      
      localStorage.setItem("registerForm", JSON.stringify(formData));
      message.success("Thông tin đã được lưu!");
      navigate("/register/contact");
    } catch (error) {
      message.error("Có lỗi xảy ra khi lưu thông tin!");
    }
  };

  const onValuesChange = (changedValues, allValues) => {
    // Save form data on every change
    try {
      const formData = {
        ...allValues,
        dob: allValues.dob ? allValues.dob.format('YYYY-MM-DD') : ''
      };
      localStorage.setItem("registerForm", JSON.stringify(formData));
    } catch (error) {
      console.error("Error saving form data:", error);
    }
  };

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
            currentStep={0} 
            steps={["Thông tin cá nhân", "Liên hệ", "Tài khoản", "Xác nhận"]} 
            icons={[<FaUser />, <FaEnvelope />, <FaAddressCard />, <FaLock />]} 
          />
          
          <Title level={3} style={{ textAlign: 'center', marginBottom: 32 }}>
            <UserOutlined style={{ marginRight: 8 }} />
            Thông tin cá nhân
          </Title>

          <Form
            form={form}
            layout="vertical"
            onFinish={onFinish}
            onValuesChange={onValuesChange}
            validateMessages={validateMessages}
            initialValues={{
              docType: "CCCD",
              gender: "Nam"
            }}
          >
            <Form.Item
              label="Chọn loại giấy tờ"
              name="docType"
              rules={[{ required: true }]}
              hasFeedback
            >
              <Select
                size="large"
                placeholder="Chọn loại giấy tờ"
                suffixIcon={<IdcardOutlined />}
              >
                <Option value="CCCD">Căn cước công dân</Option>
                <Option value="CMND">Chứng minh nhân dân</Option>
                <Option value="hộ chiếu">Hộ chiếu</Option>
              </Select>
            </Form.Item>

            <Form.Item
              label="Số giấy tờ"
              name="docNumber"
              rules={[
                { required: true },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value) return Promise.resolve();
                    
                    const docType = getFieldValue('docType');
                    if (docType === 'CCCD' && value.length !== 12) {
                      return Promise.reject(new Error('Số CCCD phải có đúng 12 chữ số'));
                    }
                    if (!/^\d+$/.test(value)) {
                      return Promise.reject(new Error('Chỉ được nhập số'));
                    }
                    return Promise.resolve();
                  },
                }),
              ]}
              extra={
                <Form.Item noStyle shouldUpdate={(prevValues, currentValues) => 
                  prevValues.docType !== currentValues.docType
                }>
                  {({ getFieldValue }) => (
                    <Text type="secondary">
                      {getFieldValue('docType') === 'CCCD' 
                        ? 'Nhập đầy đủ 12 chữ số' 
                        : 'Nhập số trên giấy tờ'
                      }
                    </Text>
                  )}
                </Form.Item>
              }
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập số giấy tờ"
                prefix={<IdcardOutlined />}
                maxLength={12}
                onChange={(e) => {
                  // Only allow numbers
                  const value = e.target.value.replace(/\D/g, '');
                  form.setFieldsValue({ docNumber: value });
                }}
              />
            </Form.Item>

            <Form.Item
              label="Họ và tên"
              name="fullName"
              rules={[
                { required: true },
                { min: 2, message: 'Họ tên phải có ít nhất 2 ký tự' },
                { max: 50, message: 'Họ tên không được quá 50 ký tự' }
              ]}
              extra={<Text type="secondary">Theo giấy tờ tùy thân</Text>}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập họ và tên đầy đủ"
                prefix={<UserOutlined />}
                maxLength={50}
              />
            </Form.Item>

            <Row gutter={16}>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Ngày sinh"
                  name="dob"
                  rules={[{ required: true }]}
                  hasFeedback
                >
                  <DatePicker
                    size="large"
                    placeholder="Chọn ngày sinh"
                    style={{ width: '100%' }}
                    format="DD/MM/YYYY"
                    suffixIcon={<CalendarOutlined />}
                    disabledDate={(current) => current && current >= dayjs().endOf('day')}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Giới tính"
                  name="gender"
                  rules={[{ required: true }]}
                >
                  <Radio.Group size="large">
                    <Radio value="Nam">Nam</Radio>
                    <Radio value="Nữ">Nữ</Radio>
                  </Radio.Group>
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              label="Nghề nghiệp"
              name="occupation"
              rules={[
                { required: true },
                { min: 2, message: 'Nghề nghiệp phải có ít nhất 2 ký tự' },
                { max: 30, message: 'Nghề nghiệp không được quá 30 ký tự' }
              ]}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập nghề nghiệp hiện tại"
                prefix={<UserOutlined />}
                maxLength={30}
              />
            </Form.Item>

            <Title level={5} style={{ marginTop: 24, marginBottom: 16 }}>
              <HomeOutlined style={{ marginRight: 8 }} />
              Địa chỉ thường trú
            </Title>

            <Form.Item
              label="Tỉnh/Thành phố"
              name="province"
              rules={[{ required: true }]}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập tỉnh/thành phố"
                prefix={<HomeOutlined />}
              />
            </Form.Item>

            <Row gutter={16}>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Quận/Huyện"
                  name="district"
                  rules={[{ required: true }]}
                  hasFeedback
                >
                  <Input
                    size="large"
                    placeholder="Nhập quận/huyện"
                    prefix={<HomeOutlined />}
                  />
                </Form.Item>
              </Col>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Phường/Xã"
                  name="ward"
                  rules={[{ required: true }]}
                  hasFeedback
                >
                  <Input
                    size="large"
                    placeholder="Nhập phường/xã"
                    prefix={<HomeOutlined />}
                  />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item
              label="Số nhà, tên đường"
              name="street"
              rules={[{ required: true }]}
              hasFeedback
            >
              <Input
                size="large"
                placeholder="Nhập số nhà, tên đường"
                prefix={<HomeOutlined />}
              />
            </Form.Item>

            <Form.Item
              label="Địa chỉ đầy đủ"
              style={{ marginBottom: 24 }}
            >
              <Form.Item noStyle shouldUpdate>
                {({ getFieldsValue }) => {
                  const { street, ward, district, province } = getFieldsValue();
                  const fullAddress = [street, ward, district, province]
                    .filter(Boolean)
                    .join(', ');
                  return (
                    <Input
                      size="large"
                      value={fullAddress}
                      disabled
                      style={{ backgroundColor: '#f5f5f5' }}
                      prefix={<HomeOutlined />}
                    />
                  );
                }}
              </Form.Item>
            </Form.Item>

            <Alert
              message="Thông tin cá nhân"
              description="Vui lòng nhập thông tin chính xác theo giấy tờ tùy thân. Thông tin này sẽ được sử dụng để xác minh danh tính của bạn."
              type="info"
              showIcon
              style={{ marginBottom: 24 }}
            />

            <Form.Item style={{ marginBottom: 0, textAlign: 'right' }}>
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
            </Form.Item>
          </Form>
        </Card>
      </div>
    </div>
  );
};

export default RegisterInformation;