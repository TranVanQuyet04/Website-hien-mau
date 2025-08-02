import React, { useState, useEffect } from "react";
import {
  Card,
  Form,
  Input,
  InputNumber,
  Button,
  message,
  Typography,
  Select,
  Divider,
  Row,
  Col,
  Checkbox,
} from "antd";
import dayjs from "dayjs";
import axios from "axios";
import AuthService from "../services/auth.service";
import { CreditCardOutlined } from "@ant-design/icons";

const { Title } = Typography;
const { Option } = Select;

const VnPayPayment = ({ request, selectedUnits = [] }) => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const user = AuthService.getCurrentUser();

  const pricePerMl = {
    "Hồng cầu": 2600,
    "Huyết tương": 1400,
    "Tiểu cầu": 4800,
  };

  const SUPPORT_DEVICE_FEE = 200000;
  const BHYT_DISCOUNT_PERCENT = 0.8;

  const calculateAmount = (units, bhyt, thietBi) => {
    const basePrice = units.reduce((sum, unit) => {
      const price = pricePerMl[unit.componentName] || 0;
      return sum + price * (unit.quantityMl || unit.volume || 0);
    }, 0);

    let total = basePrice;
    if (thietBi) total += SUPPORT_DEVICE_FEE;
    if (bhyt) total *= (1 - BHYT_DISCOUNT_PERCENT);

    return Math.round(total);
  };

  const updateAmount = () => {
    const values = form.getFieldsValue();
    const amount = calculateAmount(selectedUnits, values.bhyt, values.thietBi);
    form.setFieldsValue({ amount });
  };

  useEffect(() => {
    updateAmount();
  }, [selectedUnits]);

  const handleValuesChange = () => {
    updateAmount();
  };

  const markUnitsAsUsed = async () => {
    const token = localStorage.getItem("token");
    for (const unit of selectedUnits) {
      try {
        await axios.put(`http://localhost:8080/api/blood-units/${unit.bloodUnitId}/mark-used`, {}, {
          headers: { Authorization: `Bearer ${token}` },
        });
        console.log(`✅ Marked unit ${unit.bloodUnitId} as USED`);
      } catch (err) {
        console.error(`❌ Failed to mark unit ${unit.bloodUnitId} as USED`, err);
      }
    }
  };

const handleSubmit = async (values) => {
  if (!user) {
    return message.error("Bạn cần đăng nhập để thực hiện thanh toán.");
  }

  try {
    setLoading(true);

    const payload = {
      user: { userId: user.userId },
      amount: values.amount,
      transactionCode: values.transactionCode,
      status: values.status,
      paymentTime: dayjs().toISOString(),
      bloodRequest: { id: request?.id },
      // bhyt: values.bhyt || false,
      // thietBi: values.thietBi || false,
    };

    // ✅ 1. Gửi thông tin thanh toán
    await axios.post("http://localhost:8080/api/vnpay/create", payload, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    });

    // ✅ 2. Đánh dấu các đơn vị máu là đã sử dụng
    await markUnitsAsUsed();

    // ✅ 3. Cập nhật trạng thái yêu cầu máu sang COMPLETED
    await axios.put(
      "http://localhost:8080/api/blood-requests/approve",
      {
        bloodRequestId: request?.id,
        status: "COMPLETED",
        approvedBy: user.userId,
        confirmedVolumeMl: selectedUnits.reduce(
          (sum, unit) => sum + (unit.quantityMl || unit.volume || 0),
          0
        ),
      },
      {
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        },
      }
    );

    message.success("✅ Thanh toán và cập nhật trạng thái thành công!");
    form.resetFields();
  } catch (err) {
    console.error(err);
    message.error("❌ Thanh toán thất bại. Vui lòng thử lại!");
  } finally {
    setLoading(false);
  }
};

  return (
    <Card
      style={{
        maxWidth: 800,
        margin: "auto",
        marginTop: 30,
        borderRadius: 16,
        boxShadow: "0 8px 32px rgba(0,0,0,0.08)",
        border: "1px solid #f0f0f0"
      }}
    >
      <div style={{
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        margin: "-24px -24px 24px -24px",
        padding: "24px",
        borderRadius: "16px 16px 0 0"
      }}>
        <Title level={3} style={{
          marginBottom: 0,
          color: "white",
          textAlign: "center",
          textShadow: "0 2px 4px rgba(0,0,0,0.3)"
        }}>
          💳 Thanh toán
        </Title>
      </div>

      <div style={{ marginBottom: 32 }}>
        <div style={{
          display: "flex",
          alignItems: "center",
          marginBottom: 16,
          fontSize: "16px",
          fontWeight: 600,
          color: "#1890ff"
        }}>
          <span style={{
            background: "linear-gradient(90deg, #1890ff, #722ed1)",
            width: "4px",
            height: "20px",
            marginRight: "12px",
            borderRadius: "2px"
          }}></span>
          🩸 Các đơn vị máu đã chọn
        </div>

        {selectedUnits.length === 0 ? (
          <div style={{
            textAlign: "center",
            padding: "32px",
            background: "#fafafa",
            borderRadius: "12px",
            border: "2px dashed #d9d9d9"
          }}>
            <div style={{ fontSize: "48px", marginBottom: "12px" }}>🩸</div>
            <p style={{ color: "#8c8c8c", margin: 0, fontSize: "16px" }}>
              Không có đơn vị máu nào được chọn
            </p>
          </div>
        ) : (
          <div style={{
            background: "#f6ffed",
            border: "1px solid #b7eb8f",
            borderRadius: "12px",
            padding: "20px"
          }}>
            {selectedUnits.map((unit, index) => (
              <div key={unit.bloodUnitId} style={{
                display: "flex",
                alignItems: "center",
                padding: "12px 16px",
                marginBottom: index < selectedUnits.length - 1 ? "8px" : "0",
                background: "white",
                borderRadius: "8px",
                border: "1px solid #d9f7be",
                boxShadow: "0 2px 8px rgba(0,0,0,0.04)"
              }}>
                <span style={{
                  fontSize: "18px",
                  marginRight: "12px",
                  color: "#52c41a"
                }}>✅</span>
                <div>
                  <strong style={{ color: "#1890ff", fontSize: "16px" }}>
                    {unit.unitCode || unit.bloodUnitCode}
                  </strong>
                  <span style={{ color: "#595959", marginLeft: "8px" }}>
                    {unit.bloodTypeName} / {unit.componentName} / {unit.quantityMl || unit.volume}ml
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div style={{ marginBottom: 24 }}>
        <div style={{
          display: "flex",
          alignItems: "center",
          marginBottom: 24,
          fontSize: "16px",
          fontWeight: 600,
          color: "#1890ff"
        }}>
          <span style={{
            background: "linear-gradient(90deg, #1890ff, #722ed1)",
            width: "4px",
            height: "20px",
            marginRight: "12px",
            borderRadius: "2px"
          }}></span>
          🧾 Thông tin thanh toán
        </div>

        <Form
          layout="vertical"
          form={form}
          onFinish={handleSubmit}
          onValuesChange={handleValuesChange}
          initialValues={{ status: "PENDING", thietBi: false, bhyt: false }}
          style={{
            background: "#fafafa",
            padding: "24px",
            borderRadius: "12px",
            border: "1px solid #f0f0f0"
          }}
        >
          <Row gutter={[16, 0]}>
            <Col span={12}>
              <Form.Item
                label={<span style={{ fontWeight: 600, color: "#262626" }}>Số tiền thanh toán</span>}
                name="amount"
                rules={[{ required: true }]}
              >
                <InputNumber
                  formatter={(value) => `${value} VNĐ`}
                  style={{
                    width: "100%",
                    borderRadius: "8px"
                  }}
                  disabled
                  size="large"
                />
              </Form.Item>
            </Col>

            <Col span={12}>
              <Form.Item
                label={<span style={{ fontWeight: 600, color: "#262626" }}>Trạng thái thanh toán</span>}
                name="status"
                rules={[{ required: true }]}
              >
                <Select size="large" style={{ borderRadius: "8px" }}>
                  <Option value="PENDING">
                    <span style={{ color: "#faad14" }}>🕐 Đang xử lý</span>
                  </Option>
                  <Option value="SUCCESS">
                    <span style={{ color: "#52c41a" }}>✅ Thành công</span>
                  </Option>
                  <Option value="FAILED">
                    <span style={{ color: "#ff4d4f" }}>❌ Thất bại</span>
                  </Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item
            label={<span style={{ fontWeight: 600, color: "#262626" }}>Mã giao dịch</span>}
            name="transactionCode"
            rules={[{ required: true, message: "Vui lòng nhập mã giao dịch" }]}
          >
            <Input
              placeholder="VD: VNP12345678"
              size="large"
              style={{ borderRadius: "8px" }}
              prefix={<span style={{ color: "#1890ff" }}>🔢</span>}
            />
          </Form.Item>

          <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
            <Col span={12}>
              <Form.Item name="thietBi" valuePropName="checked">
                <div style={{
                  padding: "16px",
                  border: "2px solid #f0f0f0",
                  borderRadius: "8px",
                  background: "white",
                  transition: "all 0.3s ease"
                }}>
                  <Checkbox style={{ fontSize: "16px" }}>
                    <span style={{ marginLeft: "8px" }}>🔧 Có sử dụng thiết bị hỗ trợ</span>
                  </Checkbox>
                </div>
              </Form.Item>
            </Col>

            <Col span={12}>
              <Form.Item name="bhyt" valuePropName="checked">
                <div style={{
                  padding: "16px",
                  border: "2px solid #f0f0f0",
                  borderRadius: "8px",
                  background: "white",
                  transition: "all 0.3s ease"
                }}>
                  <Checkbox style={{ fontSize: "16px" }}>
                    <span style={{ marginLeft: "8px" }}>🏥 Có BHYT</span>
                  </Checkbox>
                </div>
              </Form.Item>
            </Col>
          </Row>

          <Form.Item style={{ marginBottom: 0 }}>
            <Button
              icon={<CreditCardOutlined />}
              type="primary"
              htmlType="submit"
              block
              loading={loading}
              disabled={selectedUnits.length === 0}
              size="large"
              style={{
                height: "56px",
                borderRadius: "12px",
                fontSize: "16px",
                fontWeight: 600,
                background: selectedUnits.length === 0 ? "#d9d9d9" : "linear-gradient(135deg, #1890ff 0%, #722ed1 100%)",
                border: "none",
                boxShadow: selectedUnits.length > 0 ? "0 4px 16px rgba(24,144,255,0.3)" : "none"
              }}
            >
              {selectedUnits.length === 0 ? "Vui lòng chọn đơn vị máu" : "Xác nhận thanh toán"}
            </Button>
          </Form.Item>
        </Form>
      </div>
    </Card>
  );
};

export default VnPayPayment;
