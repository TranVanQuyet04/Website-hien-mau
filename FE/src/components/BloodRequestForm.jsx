import React, { useState, useEffect } from "react";
import {
  Layout,
  Button,
  Input,
  Select,
  Checkbox,
  DatePicker,
  Form,
  Row,
  Col,
  Card,
  Space,
  Divider,
  Typography,
  Badge,
  Alert,
  Progress,
  message,
  App
} from "antd";
import {
  PrinterOutlined,
  SaveOutlined,
  UserOutlined,
  PhoneOutlined,
  HeartOutlined,
  MedicineBoxOutlined,
  ClockCircleOutlined,
  ExclamationCircleOutlined,
  CheckCircleOutlined,
  FileTextOutlined,
  CalendarOutlined
} from "@ant-design/icons";
import dayjs from "dayjs";
import axios from 'axios';
import AuthService from '../services/auth.service';

const { Header, Content } = Layout;
const { Option } = Select;
const { TextArea } = Input;
const { Title, Text } = Typography;

const BloodRequestForm = () => {
  const [form] = Form.useForm();
  const [currentStep, setCurrentStep] = useState(0);
  const [formData, setFormData] = useState({
    doctorId: null,
    patientName: "",
    patientPhone: "",
    patientAge: "",
    patientGender: "",
    patientWeight: "",
    patientBloodGroup: "",
    bloodTypeId: null,
    componentId: null,
    quantityBag: "",
    quantityMl: "",
    urgencyLevel: "",
    triageLevel: "RED",
    reason: "",
    neededAt: "",
    crossmatchRequired: false,
    hasTransfusionHistory: false,
    hasReactionHistory: false,
    isPregnant: false,
    hasAntibodyIssue: false,
    warningNote: "",
    specialNote: "",
    isUnmatched: false,
    codeRedId: null,
    medical_record_id: "",
    hospital_name: "BỆNH VIỆN NGỌC HUYẾT",
    department: "KHOA TRUYỀN MÁU - HÓA SINH",
    doctor_signature: "",
    lab_signature: "",
    requester_name: ""
  });


  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formProgress, setFormProgress] = useState(0);

  // Mock current user


  const currentUser = AuthService.getCurrentUser();
  const token = localStorage.getItem('token');

useEffect(() => {
  if (currentUser) {
    setFormData(prev => ({
      ...prev,
      requesterId: currentUser.userId,
      doctorId: currentUser.userId  
    }));
  }
}, [form]);

  // Calculate form progress
  useEffect(() => {
    const values = form.getFieldsValue();
    const requiredFields = ['patient_name', 'age', 'gender', 'weight', 'contact', 'bloodTypeId', 'bloodComponentId', 'unit_count', 'quantity_ml', 'urgency_level', 'clinical_indication', 'required_time'];
    const filledFields = requiredFields.filter(field => values[field] && values[field] !== '');
    const progress = Math.round((filledFields.length / requiredFields.length) * 100);
    setFormProgress(progress);
  }, [form]);

  const handleFormChange = (changedValues, allValues) => {
    // Lấy label của nhóm máu và chế phẩm
    const selectedBloodType = bloodTypeOptions.find(opt => opt.value === allValues.bloodTypeId);
    const selectedComponent = bloodComponentOptions.find(opt => opt.value === allValues.bloodComponentId);

    // Cập nhật formData đầy đủ
    setFormData(prev => ({
      ...prev,
      ...allValues,
      blood_type: selectedBloodType?.label || "",
      blood_component: selectedComponent?.label || ""
    }));

    // Tính progress
    const requiredFields = [
      'patient_name', 'age', 'gender', 'weight', 'contact',
      'bloodTypeId', 'bloodComponentId', 'unit_count', 'quantity_ml',
      'urgency_level', 'clinical_indication', 'required_time'
    ];
    const filledFields = requiredFields.filter(field => allValues[field] !== undefined && allValues[field] !== '');
    const progress = Math.round((filledFields.length / requiredFields.length) * 100);
    setFormProgress(progress);
  };

  const unitCount = Form.useWatch('unit_count', form);
  const bloodComponentId = Form.useWatch('bloodComponentId', form);

  useEffect(() => {
    if (!unitCount || !bloodComponentId) return;

    let volume = 0;
    if (bloodComponentId === 3) volume = unitCount * 250;
    else if (bloodComponentId === 2) volume = unitCount * 200;
    else if (bloodComponentId === 1) volume = unitCount * 50;

    form.setFieldsValue({ quantity_ml: volume });
  }, [unitCount, bloodComponentId]);
  const validateForm = (values) => {
    const age = parseInt(values.age);
    const weight = parseFloat(values.weight);
    const quantity = parseInt(values.quantity_ml);
    const units = parseInt(values.unit_count);

    if (!(age > 0 && age <= 120)) return "Tuổi phải trong khoảng 1 - 120.";
    if (!(weight >= 3 && weight <= 300)) return "Cân nặng phải từ 3kg đến 300kg.";
    if (!(units > 0 && Number.isInteger(units))) return "Số túi phải là số nguyên dương.";
    // if (!(quantity === units * 250 || quantity === units * 500)) return "Thể tích phải khớp với số túi.";
    if (values.contact && !/^0\d{9}$/.test(values.contact)) return "Số điện thoại không hợp lệ.";
    return null;
  };

  const handleSubmit = async (values) => {
    const error = validateForm(values);
    if (error) {
      message.error(error);
      return;
    }

    setIsSubmitting(true);
    try {
      const payload = {
        requesterId: formData.requesterId,
        doctorId: formData.doctorId,
        patientName: values.patient_name,
        patientPhone: values.contact,
        patientAge: Number(values.age),
        patientGender: values.gender,
        patientWeight: Number(values.weight),
        patientBloodGroup: formData.blood_type || "", // lấy từ formData đã map label
        bloodTypeId: values.bloodTypeId,
        componentId: values.bloodComponentId,
        quantityBag: Number(values.unit_count),
        quantityMl: Number(values.quantity_ml),
        urgencyLevel: values.urgency_level, // đã là BINH_THUONG, KHAN_CAP, CAP_CUU
        triageLevel: formData.triageLevel || "RED",
        reason: values.clinical_indication,
        neededAt: values.required_time ? dayjs(values.required_time).format("YYYY-MM-DDTHH:mm:ss") : null,
        crossmatchRequired: values.crossmatch_required || false,
        hasTransfusionHistory: values.previous_transfusion || false,
        hasReactionHistory: values.previous_reaction || false,
        isPregnant: values.is_pregnant || false,
        hasAntibodyIssue: values.abnormal_antibody || false,
        warningNote: values.warning_factor || "",
        specialNote: values.special_notes || "",
        isUnmatched: false,
        codeRedId: null
      };

      const response = await axios.post(
        "http://localhost:8080/api/blood-requests",
        payload,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
          }
        }
      );

      message.success("Gửi yêu cầu máu thành công!");
      setFormData(prev => ({ ...prev, ...values, submitted_at: new Date() }));
      setCurrentStep(2);
    } catch (err) {
      console.error(err);
      message.error("❌ Lỗi khi gửi yêu cầu máu.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const requiredMessage = (label) => [{ required: true, message: `Vui lòng nhập ${label}` }];

  const bloodTypeOptions = [
    { value: "", label: "Chưa biết" },
    { value: 1, label: "A+" },
    { value: 2, label: "A-" },
    { value: 3, label: "B+" },
    { value: 4, label: "B-" },
    { value: 5, label: "AB+" },
    { value: 6, label: "AB-" },
    { value: 7, label: "O+" },
    { value: 8, label: "O-" }
  ];

  const bloodComponentOptions = [
    { value: 3, label: "Hồng cầu (PRBC)" },
    { value: 2, label: "Huyết tương" },
    { value: 1, label: "Tiểu cầu" }
  ];

  const urgencyOptions = [
    { value: "BINH_THUONG", label: "Bình thường" },
    { value: "KHAN_CAP", label: "Khẩn cấp" }
  ];

  const getBloodTypeColor = (bloodType) => {
    const colors = {
      'A+': 'blue', 'A-': 'cyan', 'B+': 'green', 'B-': 'lime',
      'AB+': 'purple', 'AB-': 'magenta', 'O+': 'orange', 'O-': 'red'
    };
    return colors[bloodType] || 'default';
  };

  const getBloodTypeLabel = (bloodTypeId) => {
    const option = bloodTypeOptions.find(opt => opt.value === bloodTypeId);
    return option ? option.label : "Chưa biết";
  };

  const getComponentLabel = (componentId) => {
    const option = bloodComponentOptions.find(opt => opt.value === componentId);
    return option ? option.label : "";
  };

  const getUrgencyLabel = (urgencyLevel) => {
    const option = urgencyOptions.find(opt => opt.value === urgencyLevel);
    return option ? option.label : "";
  };

  const handlePrint = () => {
    const printContent = document.getElementById('printable-form');
    const winPrint = window.open('', '', 'left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0');
    winPrint.document.write(`
      <html>
        <head>
          <title>Phiếu Yêu Cầu Truyền Máu</title>
          <style>
            body { font-family: 'Times New Roman', serif; margin: 0; padding: 20px; }
            .hospital-header { text-align: center; margin-bottom: 30px; }
            .hospital-name { color: #d32f2f; font-size: 24px; font-weight: bold; margin: 0; }
            .department { color: #1976d2; font-size: 18px; margin: 5px 0; }
            .form-title { font-size: 20px; font-weight: bold; text-decoration: underline; margin: 15px 0; }
            .patient-info { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin: 20px 0; }
            .field { margin: 8px 0; font-size: 14px; }
            .signatures { display: flex; justify-content: space-between; margin-top: 50px; }
            .signature-box { text-align: center; width: 200px; }
            @media print {
              body { margin: 0; }
              .no-print { display: none !important; }
            }
          </style>
        </head>
        <body>
          ${printContent.innerHTML}
        </body>
      </html>
    `);
    winPrint.document.close();
    winPrint.focus();
    winPrint.print();
    winPrint.close();
  };

  return (
    <App>
      <Layout style={{ minHeight: '100vh' }}>
        <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
          <Row justify="space-between" align="middle">
            <Col>
              <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
                <ExclamationCircleOutlined style={{ marginRight: 8 }} />
                Phiếu Yêu Cầu Truyền Máu
              </Title>
            </Col>
            <Col>
              <Space>
                <Text type="secondary">
                  <CalendarOutlined style={{ marginRight: 4 }} />
                  {new Date().toLocaleDateString('vi-VN')}
                </Text>
                <Text type="secondary">
                  <UserOutlined style={{ marginRight: 4 }} />
                  Quản trị viên
                </Text>
              </Space>
            </Col>
          </Row>
        </Header>

        <Content style={{ padding: '24px' }}>
          <div style={{ maxWidth: '1000px', margin: '0 auto' }}>
            <Alert
              message="Hướng dẫn sử dụng"
              description="Vui lòng điền đầy đủ thông tin bệnh nhân và yêu cầu truyền máu. Hệ thống sẽ tự động xác thực và gửi yêu cầu đến bộ phận xử lý."
              type="info"
              showIcon
              closable
              style={{ marginBottom: '24px' }}
            />

            <Card
              title={
                <Space>
                  <FileTextOutlined style={{ color: '#1890ff' }} />
                  <span>Form Yêu Cầu Truyền Máu</span>
                  <Progress
                    percent={formProgress}
                    size="small"
                    style={{ width: 200 }}
                    strokeColor={formProgress === 100 ? '#52c41a' : '#1890ff'}
                  />
                </Space>
              }
              style={{ marginBottom: '24px' }}
            >
              <Form
                form={form}
                layout="vertical"
                onFinish={handleSubmit}
                onValuesChange={handleFormChange}
                initialValues={formData}
              >
                <div style={{ marginBottom: '32px' }}>
                  <Title level={5} style={{ color: '#1890ff', marginBottom: '16px' }}>
                    <UserOutlined style={{ marginRight: 8 }} />
                    Thông tin bệnh nhân
                  </Title>

                  <Row gutter={16}>
                    <Col span={8}>
                      <Form.Item
                        label={
                          <Space>
                            <FileTextOutlined />
                            <span>Mã bệnh án</span>
                          </Space>
                        }
                        name="medical_record_id"
                        rules={[{ required: true }]}
                      >
                        <Input
                          size="large"
                          placeholder="Nhập mã bệnh án"
                        />
                      </Form.Item>
                    </Col>

                    <Col span={8}>
                      <Form.Item
                        label={
                          <Space>
                            <PhoneOutlined />
                            <span>Số điện thoại</span>
                          </Space>
                        }
                        name="contact"
                        rules={requiredMessage('số điện thoại')}
                      >
                        <Input
                          size="large"
                          placeholder="0xxxxxxxxx"
                          prefix={<PhoneOutlined style={{ color: '#1890ff' }} />}
                        />
                      </Form.Item>
                    </Col>

                    <Col span={8}>
                      <Form.Item
                        label={
                          <Space>
                            <UserOutlined />
                            <span>Người phụ trách</span>
                          </Space>
                        }
                        name="requester_name"
                        rules={requiredMessage('người yêu cầu')}
                      >
                        <Input
                          size="large"
                          placeholder="Tên bác sĩ/nhân viên"
                          prefix={<UserOutlined style={{ color: '#1890ff' }} />}
                        />
                      </Form.Item>
                    </Col>
                  </Row>

                  <Row gutter={16}>
                    <Col span={9}>
                      <Form.Item
                        label={
                          <Space>
                            <UserOutlined />
                            <span>Họ tên bệnh nhân</span>
                          </Space>
                        }
                        name="patient_name"
                        rules={requiredMessage('họ tên bệnh nhân')}
                      >
                        <Input
                          size="large"
                          placeholder="Nhập họ tên đầy đủ"
                          prefix={<UserOutlined style={{ color: '#1890ff' }} />}
                        />
                      </Form.Item>
                    </Col>
                    <Col span={3}>
                      <Form.Item
                        label={
                          <Space>
                            <CalendarOutlined />
                            <span>Tuổi</span>
                          </Space>
                        }
                        name="age"
                        rules={requiredMessage('tuổi')}
                      >
                        <Input
                          size="large"
                          type="number"
                          min={1}
                          max={120}
                          placeholder="Tuổi"
                        />
                      </Form.Item>
                    </Col>

                    <Col span={3}>
                      <Form.Item
                        label={
                          <Space>
                            <UserOutlined />
                            <span>Giới tính</span>
                          </Space>
                        }
                        name="gender"
                        rules={requiredMessage('giới tính')}
                      >
                        <Select size="large" placeholder="Chọn">
                          <Option value="Nam">Nam</Option>
                          <Option value="Nữ">Nữ</Option>
                        </Select>
                      </Form.Item>
                    </Col>

                    <Col span={4}>
                      <Form.Item
                        label={
                          <Space>
                            <span>Cân nặng (kg)</span>
                          </Space>
                        }
                        name="weight"
                        rules={requiredMessage('cân nặng')}
                      >
                        <Input
                          size="large"
                          type="number"
                          min={3}
                          max={300}
                          placeholder="kg"
                        />
                      </Form.Item>
                    </Col>

                    <Col span={5}>
                      <Form.Item
                        label={
                          <Space>
                            <HeartOutlined />
                            <span>Nhóm máu</span>
                          </Space>
                        }
                        name="bloodTypeId"
                        rules={requiredMessage('nhóm máu')}
                      >
                        <Select size="large" placeholder="Chọn">
                          <Option value="">Chưa biết</Option>
                          <Option value={1}>
                            <Badge color="blue" text="A+" />
                          </Option>
                          <Option value={2}>
                            <Badge color="cyan" text="A-" />
                          </Option>
                          <Option value={3}>
                            <Badge color="green" text="B+" />
                          </Option>
                          <Option value={4}>
                            <Badge color="lime" text="B-" />
                          </Option>
                          <Option value={5}>
                            <Badge color="purple" text="AB+" />
                          </Option>
                          <Option value={6}>
                            <Badge color="magenta" text="AB-" />
                          </Option>
                          <Option value={7}>
                            <Badge color="orange" text="O+" />
                          </Option>
                          <Option value={8}>
                            <Badge color="red" text="O-" />
                          </Option>
                        </Select>
                      </Form.Item>
                    </Col>
                  </Row>
                </div>

                <Divider />

                <div style={{ marginBottom: '32px' }}>
                  <Title level={5} style={{ color: '#1890ff', marginBottom: '16px' }}>
                    <MedicineBoxOutlined style={{ marginRight: 8 }} />
                    Yêu cầu truyền máu
                  </Title>

                  <Row gutter={16}>
                    <Col span={6}>
                      <Form.Item
                        label={
                          <Space>
                            <MedicineBoxOutlined />
                            <span>Loại chế phẩm</span>
                          </Space>
                        }
                        name="bloodComponentId"
                        rules={requiredMessage('loại chế phẩm')}
                      >
                        <Select size="large" placeholder="Chọn chế phẩm">
                          <Option value={3}>
                            <Badge color="red" text="Hồng cầu (PRBC)" />
                          </Option>
                          <Option value={2}>
                            <Badge color="yellow" text="Huyết tương" />
                          </Option>
                          <Option value={1}>
                            <Badge color="blue" text="Tiểu cầu" />
                          </Option>
                        </Select>
                      </Form.Item>
                    </Col>

                    <Col span={4}>
                      <Form.Item
                        label={
                          <Space>
                            <span>Số đơn vi máu</span>
                          </Space>
                        }
                        name="unit_count"
                        rules={requiredMessage('Số đơn vi máu')}
                      >
                        <Input
                          size="large"
                          type="number"
                          min={1}
                          placeholder="Đơn vị"
                        />
                      </Form.Item>
                    </Col>

                    <Col span={3}>
                      <Form.Item
                        label={
                          <Space>
                            <span>Thể tích (ml)</span>
                          </Space>
                        }
                        name="quantity_ml"
                        rules={requiredMessage('thể tích')}
                      >
                        <Input
                          size="large"
                          type="number"
                          placeholder="ml"
                        />
                      </Form.Item>
                    </Col>

                    <Col span={4}>
                      <Form.Item
                        label={
                          <Space>
                            <ExclamationCircleOutlined />
                            <span>Mức độ ưu tiên</span>
                          </Space>
                        }
                        name="urgency_level"
                        rules={requiredMessage('mức độ')}
                      >
                        <Select size="large" placeholder="Chọn mức độ">
                          <Option value="BINH_THUONG">
                            <Badge color="green" text="Bình thường" />
                          </Option>
                          <Option value="KHAN_CAP">
                            <Badge color="orange" text="Khẩn cấp" />
                          </Option>
                        </Select>
                      </Form.Item>
                    </Col>

                    <Col span={7}>
                      <Form.Item
                        label={
                          <Space>
                            <ClockCircleOutlined />
                            <span>Thời gian cần máu</span>
                          </Space>
                        }
                        name="required_time"
                        rules={requiredMessage('thời gian cần máu')}
                      >
                        <DatePicker
                          size="large"
                          showTime
                          style={{ width: '100%' }}
                          placeholder="Chọn thời gian"
                        />
                      </Form.Item>
                    </Col>
                  </Row>

                  <Row gutter={16}>
                    <Col span={24}>
                      <Form.Item
                        label={
                          <Space>
                            <FileTextOutlined />
                            <span>Chẩn đoán lâm sàng</span>
                          </Space>
                        }
                        name="clinical_indication"
                        rules={requiredMessage('chẩn đoán lâm sàng')}
                      >
                        <TextArea
                          rows={3}
                          size="large"
                          placeholder="Mô tả chi tiết chẩn đoán và lý do cần truyền máu..."
                          showCount
                          maxLength={500}
                        />
                      </Form.Item>
                    </Col>
                  </Row>
                </div>

                <Divider />

                <div style={{ marginBottom: '32px' }}>
                  <Title level={5} style={{ color: '#faad14', marginBottom: '16px' }}>
                    <ExclamationCircleOutlined style={{ marginRight: 8 }} />
                    Thông tin y tế quan trọng
                  </Title>

                  <Card size="small" style={{ backgroundColor: '#fff7e6', border: '1px solid #ffd591' }}>
                    <Row gutter={16}>
                      <Col span={24}>
                        <Space wrap size="large">
                          <Form.Item name="crossmatch_required" valuePropName="checked" style={{ margin: 0 }}>
                            <Checkbox>
                              <Space>
                                <CheckCircleOutlined style={{ color: '#1890ff' }} />
                                <span>Yêu cầu Crossmatch</span>
                              </Space>
                            </Checkbox>
                          </Form.Item>
                          <Form.Item name="previous_transfusion" valuePropName="checked" style={{ margin: 0 }}>
                            <Checkbox>
                              <Space>
                                <HeartOutlined style={{ color: '#ff4d4f' }} />
                                <span>Từng truyền máu</span>
                              </Space>
                            </Checkbox>
                          </Form.Item>
                          <Form.Item name="previous_reaction" valuePropName="checked" style={{ margin: 0 }}>
                            <Checkbox>
                              <Space>
                                <ExclamationCircleOutlined style={{ color: '#faad14' }} />
                                <span>Có phản ứng trước đây</span>
                              </Space>
                            </Checkbox>
                          </Form.Item>
                          <Form.Item name="is_pregnant" valuePropName="checked" style={{ margin: 0 }}>
                            <Checkbox>
                              <Space>
                                <UserOutlined style={{ color: '#ff85c0' }} />
                                <span>Có thai</span>
                              </Space>
                            </Checkbox>
                          </Form.Item>
                          <Form.Item name="abnormal_antibody" valuePropName="checked" style={{ margin: 0 }}>
                            <Checkbox>
                              <Space>
                                <MedicineBoxOutlined style={{ color: '#722ed1' }} />
                                <span>Phát hiện kháng thể bất thường</span>
                              </Space>
                            </Checkbox>
                          </Form.Item>
                        </Space>
                      </Col>
                    </Row>
                  </Card>
                </div>

                <Row gutter={16}>
                  <Col span={12}>
                    <Form.Item
                      label={
                        <Space>
                          <ExclamationCircleOutlined />
                          <span>Yếu tố cảnh báo</span>
                        </Space>
                      }
                      name="warning_factor"
                    >
                      <TextArea
                        rows={3}
                        size="large"
                        placeholder="Chia túi nhỏ, chống chỉ định dị ứng, thuốc lợi tiểu..."
                        showCount
                        maxLength={300}
                      />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      label={
                        <Space>
                          <FileTextOutlined />
                          <span>Ghi chú đặc biệt</span>
                        </Space>
                      }
                      name="special_notes"
                    >
                      <TextArea
                        rows={3}
                        size="large"
                        placeholder="Truyền nhóm O-, bệnh lý nền..."
                        showCount
                        maxLength={300}
                      />
                    </Form.Item>
                  </Col>
                </Row>

                <Form.Item>
                  <Space size="large">
                    <Button
                      type="primary"
                      danger
                      htmlType="submit"
                      icon={<SaveOutlined />}
                      size="large"
                      loading={isSubmitting}
                      style={{ minWidth: '150px' }}
                    >
                      {isSubmitting ? 'Đang gửi...' : 'Gửi yêu cầu'}
                    </Button>
                    <Button
                      onClick={handlePrint}
                      icon={<PrinterOutlined />}
                      size="large"
                      style={{ minWidth: '120px' }}
                    >
                      In phiếu
                    </Button>
                  </Space>
                </Form.Item>
              </Form>
            </Card>

            {/* Preview Section */}
            <Card
              title={
                <Space>
                  <FileTextOutlined style={{ color: '#1890ff' }} />
                  <span>Xem trước phiếu in</span>
                  <Badge
                    count={formProgress < 100 ? 'Chưa hoàn thành' : 'Sẵn sàng'}
                    color={formProgress < 100 ? 'orange' : 'green'}
                  />
                </Space>
              }
              className="no-print"
              style={{
                backgroundColor: '#fafafa',
                border: '1px solid #d9d9d9',
                marginBottom: 24,
              }}
            >
              <div
                id="printable-form"
                style={{
                  width: '190mm',
                  minHeight: '270mm',
                  margin: '0 auto',
                  padding: '10mm',
                  backgroundColor: 'white',
                  fontFamily: 'Times New Roman, serif',
                  fontSize: '13px',
                  lineHeight: '1.5',
                  border: '1px solid #ccc',
                  boxShadow: 'none',
                  position: 'relative',
                  color: '#000',
                }}
              >
                {/* Header */}
                <div
                  className="hospital-header"
                  style={{
                    textAlign: 'center',
                    borderBottom: '3px solid #1890ff',
                    paddingBottom: '10px',
                    marginBottom: '20px'
                  }}
                >
                  <div
                    className="hospital-name"
                    style={{
                      color: '#d32f2f',
                      fontSize: '28px',
                      fontWeight: 'bold',
                      margin: '0 0 5px 0',
                      textShadow: '1px 1px 2px rgba(0,0,0,0.1)'
                    }}
                  >
                    {formData.hospital_name}
                  </div>
                  <div
                    className="department"
                    style={{
                      color: '#1976d2',
                      fontSize: '18px',
                      textDecoration: 'underline',
                      margin: '5px 0 5px 0',
                      fontWeight: '500'
                    }}
                  >
                    {formData.department}
                  </div>
                  <div
                    className="form-title"
                    style={{
                      fontSize: '22px',
                      fontWeight: 'bold',
                      margin: '5px 0',
                      color: '#333',
                      textTransform: 'uppercase',
                      letterSpacing: '1px'
                    }}
                  >
                    PHIẾU YÊU CẦU TRUYỀN MÁU
                  </div>
                </div>

                {/* Thông tin cơ bản với layout grid */}
                <div>
                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr',
                    gap: '20px',
                    marginBottom: '15px',
                    padding: '15px',
                    backgroundColor: '#f8f9fa',
                    borderRadius: '8px',
                    border: '1px solid #e9ecef'
                  }}>
                    <div style={{ fontWeight: 'bold', color: '#1890ff' }}>
                      <span style={{ color: '#333' }}>Mã bệnh án:</span> {formData.medical_record_id}
                    </div>
                    <div style={{ fontWeight: 'bold', color: '#1890ff', textAlign: 'right' }}>
                      <span style={{ color: '#333' }}>Ngày lập:</span> {new Date().toLocaleDateString('vi-VN')}
                    </div>
                  </div>

                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '2fr 1fr',
                    gap: '20px',
                    marginBottom: '10px'
                  }}>
                    <div className="field" style={{ fontSize: '15px', fontWeight: '500' }}>
                      <strong style={{ color: '#333' }}>Họ tên bệnh nhân:</strong>
                      <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.patient_name}</span>
                    </div>
                    <div className="field" style={{ fontSize: '15px', fontWeight: '500' }}>
                      <strong style={{ color: '#333' }}>Liên hệ:</strong>
                      <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.contact}</span>
                    </div>
                  </div>

                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr 1fr 1fr',
                    gap: '15px',
                  }}>
                    <div className="field" style={{ fontSize: '14px' }}>
                      <strong>Tuổi:</strong>
                      <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.age}</span>
                    </div>
                    <div className="field" style={{ fontSize: '14px' }}>
                      <strong>Giới tính:</strong>
                      <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.gender}</span>
                    </div>
                    <div className="field" style={{ fontSize: '14px' }}>
                      <strong>Cân nặng:</strong>
                      <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.weight} kg</span>
                    </div>
                    <div className="field" style={{ fontSize: '14px' }}>
                      <strong>Nhóm máu:</strong>
                      <span style={{
                        color: '#fff',
                        backgroundColor: getBloodTypeColor(formData.blood_type),
                        padding: '2px 8px',
                        borderRadius: '4px',
                        fontWeight: 'bold',
                        marginLeft: '8px'
                      }}>{formData.blood_type}</span>
                    </div>
                  </div>
                </div>

                <div style={{
                  height: '2px',
                  background: 'linear-gradient(to right, #1890ff, #52c41a)',
                  margin: '10px 0'
                }}></div>

                {/* Thông tin y tế với thiết kế card */}
                <div>
                  <div style={{
                    backgroundColor: '#fff2e8',
                    padding: '15px',
                    borderRadius: '8px',
                    border: '1px solid #ffd591',
                    marginBottom: '15px'
                  }}>
                    <div className="field" style={{ fontSize: '15px', marginBottom: '8px' }}>
                      <strong style={{ color: '#d84315' }}>Chẩn đoán lâm sàng:</strong>
                      <div style={{
                        marginTop: '5px',
                        padding: '8px',
                        backgroundColor: '#fff',
                        borderRadius: '4px',
                        fontStyle: 'italic'
                      }}>
                        {formData.clinical_indication}
                      </div>
                    </div>
                  </div>

                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr',
                    gap: '20px'
                  }}>
                    <div>
                      <div className="field" style={{ fontSize: '14px', marginBottom: '8px' }}>
                        <strong>Loại chế phẩm máu:</strong>
                        <span style={{ color: '#1890ff', marginLeft: '8px' }}>{formData.blood_component}</span>
                      </div>
                      <div className="field" style={{ fontSize: '14px', marginBottom: '8px' }}>
                        <strong>Số lượng:</strong>
                        <span style={{
                          color: '#fff',
                          backgroundColor: '#52c41a',
                          padding: '2px 8px',
                          borderRadius: '4px',
                          fontWeight: 'bold',
                          marginLeft: '8px'
                        }}>
                          {formData.unit_count} túi - {formData.quantity_ml} ml
                        </span>
                      </div>
                    </div>
                    <div>
                      <div className="field" style={{ fontSize: '14px', marginBottom: '8px' }}>
                        <strong>Mức độ ưu tiên:</strong>
                        <span style={{
                          color: '#fff',
                          backgroundColor: formData.urgency_level === 'Khẩn cấp' ? '#faad14' : '#52c41a',
                          padding: '2px 8px',
                          borderRadius: '4px',
                          fontWeight: 'bold',
                          marginLeft: '8px'
                        }}>
                          {formData.urgency_level}
                        </span>
                      </div>
                      <div className="field" style={{ fontSize: '14px', marginBottom: '8px' }}>
                        <strong>Thời gian cần máu:</strong>
                        <div style={{
                          marginTop: '5px',
                          color: '#1890ff',
                          fontWeight: 'bold'
                        }}>
                          {formData.required_time ? new Date(formData.required_time).toLocaleString("vi-VN") : ""}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div style={{
                  height: '2px',
                  background: 'linear-gradient(to right, #52c41a, #1890ff)',
                  margin: '10px 0'
                }}></div>

                {/* Checkbox với thiết kế hiện đại */}
                <div style={{ marginBottom: '25px' }}>
                  <div style={{
                    backgroundColor: '#f6ffed',
                    padding: '15px',
                    borderRadius: '8px',
                    border: '1px solid #b7eb8f'
                  }}>
                    <div style={{
                      fontSize: '16px',
                      fontWeight: 'bold',
                      marginBottom: '12px',
                      color: '#389e0d'
                    }}>
                      Thông tin y tế quan trọng:
                    </div>
                    <div style={{
                      display: 'grid',
                      gridTemplateColumns: '1fr 1fr',
                      gap: '12px'
                    }}>
                      <div style={{ fontSize: '14px' }}>
                        <span style={{
                          display: 'inline-block',
                          width: '20px',
                          height: '20px',
                          border: '2px solid #1890ff',
                          borderRadius: '3px',
                          textAlign: 'center',
                          lineHeight: '16px',
                          marginRight: '8px',
                          backgroundColor: formData.crossmatch_required ? '#1890ff' : 'transparent',
                          color: formData.crossmatch_required ? 'white' : 'transparent'
                        }}>
                          ✓
                        </span>
                        Yêu cầu Crossmatch
                      </div>
                      <div style={{ fontSize: '14px' }}>
                        <span style={{
                          display: 'inline-block',
                          width: '20px',
                          height: '20px',
                          border: '2px solid #1890ff',
                          borderRadius: '3px',
                          textAlign: 'center',
                          lineHeight: '16px',
                          marginRight: '8px',
                          backgroundColor: formData.previous_transfusion ? '#1890ff' : 'transparent',
                          color: formData.previous_transfusion ? 'white' : 'transparent'
                        }}>
                          ✓
                        </span>
                        Từng truyền máu
                      </div>
                      <div style={{ fontSize: '14px' }}>
                        <span style={{
                          display: 'inline-block',
                          width: '20px',
                          height: '20px',
                          border: '2px solid #1890ff',
                          borderRadius: '3px',
                          textAlign: 'center',
                          lineHeight: '16px',
                          marginRight: '8px',
                          backgroundColor: formData.previous_reaction ? '#1890ff' : 'transparent',
                          color: formData.previous_reaction ? 'white' : 'transparent'
                        }}>
                          ✓
                        </span>
                        Có phản ứng trước đây
                      </div>
                      <div style={{ fontSize: '14px' }}>
                        <span style={{
                          display: 'inline-block',
                          width: '20px',
                          height: '20px',
                          border: '2px solid #1890ff',
                          borderRadius: '3px',
                          textAlign: 'center',
                          lineHeight: '16px',
                          marginRight: '8px',
                          backgroundColor: formData.is_pregnant ? '#1890ff' : 'transparent',
                          color: formData.is_pregnant ? 'white' : 'transparent'
                        }}>
                          ✓
                        </span>
                        Có thai
                      </div>
                      <div style={{ fontSize: '14px', gridColumn: 'span 2' }}>
                        <span style={{
                          display: 'inline-block',
                          width: '20px',
                          height: '20px',
                          border: '2px solid #1890ff',
                          borderRadius: '3px',
                          textAlign: 'center',
                          lineHeight: '16px',
                          marginRight: '8px',
                          backgroundColor: formData.abnormal_antibody ? '#1890ff' : 'transparent',
                          color: formData.abnormal_antibody ? 'white' : 'transparent'
                        }}>
                          ✓
                        </span>
                        Phát hiện kháng thể bất thường
                      </div>
                    </div>
                  </div>
                </div>

                <div style={{
                  height: '2px',
                  background: 'linear-gradient(to right, #faad14, #ff4d4f)',
                  margin: '10px 0'
                }}></div>

                {/* Ghi chú với thiết kế card */}
                <div style={{ marginBottom: '25px' }}>
                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr',
                    gap: '20px'
                  }}>
                    <div>
                      <div style={{
                        backgroundColor: '#fff1f0',
                        padding: '15px',
                        borderRadius: '8px',
                        border: '1px solid #ffccc7',
                        minHeight: '50px'
                      }}>
                        <div style={{
                          fontSize: '14px',
                          fontWeight: 'bold',
                          marginBottom: '4px',
                          color: '#cf1322'
                        }}>
                          Yếu tố cảnh báo:
                        </div>
                        <div style={{
                          fontSize: '13px',
                          lineHeight: '1.5',
                          fontStyle: 'italic'
                        }}>
                          {formData.warning_factor || "Không có"}
                        </div>
                      </div>
                    </div>
                    <div>
                      <div style={{
                        backgroundColor: '#e6f7ff',
                        padding: '15px',
                        borderRadius: '8px',
                        border: '1px solid #91d5ff',
                        minHeight: '50px'
                      }}>
                        <div style={{
                          fontSize: '14px',
                          fontWeight: 'bold',
                          marginBottom: '4px',
                          color: '#0958d9'
                        }}>
                          Ghi chú đặc biệt:
                        </div>
                        <div style={{
                          fontSize: '13px',
                          lineHeight: '1.5',
                          fontStyle: 'italic'
                        }}>
                          {formData.special_notes || "Không có"}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Chữ ký với thiết kế hiện đại */}
                <div
                  className="signatures"
                  style={{
                    display: 'grid',
                    gridTemplateColumns: '1fr 1fr',
                    gap: '40px',
                    marginTop: '10px',
                  }}
                >
                  <div
                    className="signature-box"
                    style={{
                      textAlign: 'center',
                      padding: '20px',
                      border: '2px dashed #d9d9d9',
                      borderRadius: '8px',
                      backgroundColor: '#fafafa'
                    }}
                  >
                    <div style={{
                      fontSize: '16px',
                      fontWeight: 'bold',
                      marginBottom: '8px',
                      color: '#1890ff'
                    }}>
                      Bác sĩ yêu cầu
                    </div>
                    <div style={{
                      fontSize: '12px',
                      color: '#666',
                      marginBottom: '10px'
                    }}>
                      (Ký tên và đóng dấu)
                    </div>
                    <div style={{
                      fontSize: '14px',
                      fontWeight: 'bold',
                      color: '#333',
                      borderTop: '1px solid #d9d9d9',
                      paddingTop: '10px'
                    }}>
                      {formData.requester_name}
                    </div>
                  </div>
                  <div
                    className="signature-box"
                    style={{
                      textAlign: 'center',
                      padding: '20px',
                      border: '2px dashed #d9d9d9',
                      borderRadius: '8px',
                      backgroundColor: '#fafafa'
                    }}
                  >
                    <div style={{
                      fontSize: '16px',
                      fontWeight: 'bold',
                      marginBottom: '8px',
                      color: '#1890ff'
                    }}>
                      Người điền phiếu
                    </div>
                    <div style={{
                      fontSize: '12px',
                      color: '#666',
                      marginBottom: '40px'
                    }}>
                      (Ký tên và ghi rõ họ tên)
                    </div>
                    <div style={{
                      fontSize: '14px',
                      fontWeight: 'bold',
                      color: '#333',
                      borderTop: '1px solid #d9d9d9',
                      paddingTop: '10px'
                    }}>
                      ________________________
                    </div>
                  </div>
                </div>

                {/* Footer với thông tin liên hệ */}
                <div style={{
                  position: 'absolute',
                  bottom: '10mm',
                  left: '20mm',
                  right: '20mm',
                  textAlign: 'center',
                  fontSize: '12px',
                  color: '#666',
                  borderTop: '1px solid #e8e8e8',
                  paddingTop: '5px'
                }}>
                  <div style={{ marginBottom: '4px' }}>
                    <strong>Hotline:</strong> 1900-xxxx |
                    <strong> Email:</strong> info@ngochuyethosp.vn |
                    <strong> Website:</strong> www.ngochuyethosp.vn
                  </div>
                  <div>
                    <strong>Địa chỉ:</strong> 123 Đường ABC, Quận XYZ, TP.HCM
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </Content>
      </Layout>
    </App>
  );
};
export default BloodRequestForm;