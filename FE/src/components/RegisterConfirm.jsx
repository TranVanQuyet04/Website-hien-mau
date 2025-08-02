import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {
  Card,
  Button,
  Typography,
  Space,
  Descriptions,
  Alert,
  message,
} from "antd";
import {
  CheckCircleOutlined,
  ArrowLeftOutlined,
  SafetyOutlined,
} from "@ant-design/icons";
import { FaUser, FaEnvelope, FaAddressCard, FaLock } from "react-icons/fa";
import RegisterProgress from "../components/RegisterProgress";

const { Title, Text } = Typography;

const RegisterConfirm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const saved = localStorage.getItem("registerForm");
    if (saved) {
      try {
        const parsedData = JSON.parse(saved);
        setFormData(parsedData);
      } catch (error) {
        console.error("Error parsing saved data:", error);
        message.error("Có lỗi xảy ra khi tải thông tin!");
        navigate("/register/information");
      }
    } else {
      message.warning("Không tìm thấy thông tin đăng ký!");
      navigate("/register/information");
    }
  }, [navigate]);

  const handleBack = () => navigate("/register/account");
const handleSubmit = async () => {
  setLoading(true);

  try {
    // CHUYỂN ĐỔI dữ liệu theo API backend yêu cầu
  const finalFormData = {
  username: formData.username,
  password: formData.password,
  fullName: formData.fullName,
  dob: formData.dob,
  cccd: formData.docNumber,
  occupation: formData.occupation,
  gender: formData.gender,
  contactInfo: {
    email: formData.email,
    phone: formData.phone,
  },
  role: formData.role || "MEMBER", // fallback nếu chưa chọn vai trò
  addressId: 68
   // thêm addressId vào finalFormData
};

    const response = await axios.post(
      "http://localhost:8080/api/auth/register",
      finalFormData,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    localStorage.removeItem("registerForm");

    message.success({
      content: "Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản.",
      duration: 4,
    });

    navigate("/login");
  } catch (error) {
    console.error("Lỗi đăng ký:", error);
    const errorMsg =
      error.response?.data?.message || "Đăng ký thất bại. Vui lòng thử lại!";
    message.error(errorMsg);
  } finally {
    setLoading(false);
  }
};


  const getGenderText = (gender) => {
    const genderMap = {
      male: "Nam",
      female: "Nữ",
      other: "Khác",
    };
    return genderMap[gender] || gender;
  };

  const getDocTypeText = (docType) => {
    const docTypeMap = {
      cccd: "Căn cước công dân",
      cmnd: "Chứng minh nhân dân",
      passport: "Hộ chiếu",
    };
    return docTypeMap[docType] || docType;
  };

  const formatDate = (dateString) => {
    if (!dateString) return "---";
    const date = new Date(dateString);
    return date.toLocaleDateString("vi-VN");
  };

  return (
    <div className="regis-fullpage">
      <div className="regis-container">
        <Card
          className="register-card"
          style={{ maxWidth: 800, margin: "0 auto" }}
        >
          <RegisterProgress
            currentStep={3}
            steps={["Thông tin cá nhân", "Liên hệ", "Tài khoản", "Xác nhận"]}
            icons={[<FaUser />, <FaEnvelope />, <FaAddressCard />, <FaLock />]}
          />

          <Title level={3} style={{ textAlign: "center", marginBottom: 32 }}>
            <CheckCircleOutlined style={{ marginRight: 8, color: "#52c41a" }} />
            Xác nhận thông tin đăng ký
          </Title>

          <Alert
            message="Kiểm tra thông tin"
            description="Vui lòng kiểm tra kỹ thông tin trước khi hoàn tất đăng ký. Một số thông tin có thể không thể thay đổi sau khi đăng ký."
            type="info"
            showIcon
            icon={<SafetyOutlined />}
            style={{ marginBottom: 24 }}
          />

          <Card
            type="inner"
            title={<Text strong>Thông tin cá nhân</Text>}
            style={{ marginBottom: 16 }}
          >
            <Descriptions column={{ xs: 1, sm: 2, md: 2 }} size="small">
              <Descriptions.Item label="Họ và tên">
                <Text strong>{formData.fullName || "---"}</Text>
              </Descriptions.Item>
              <Descriptions.Item label="Ngày sinh">
                {formatDate(formData.dob)}
              </Descriptions.Item>
              <Descriptions.Item label="Giới tính">
                {getGenderText(formData.gender) || "---"}
              </Descriptions.Item>
              <Descriptions.Item label="Nghề nghiệp">
                {formData.occupation || "---"}
              </Descriptions.Item>
              <Descriptions.Item label={getDocTypeText(formData.docType) || "Giấy tờ"}>
                {formData.docNumber || "---"}
              </Descriptions.Item>
            </Descriptions>
          </Card>

          <Card
            type="inner"
            title={<Text strong>Thông tin liên hệ</Text>}
            style={{ marginBottom: 16 }}
          >
            <Descriptions column={{ xs: 1, sm: 2, md: 2 }} size="small">
              <Descriptions.Item label="Email">
                <Text copyable>{formData.email || "---"}</Text>
              </Descriptions.Item>
              <Descriptions.Item label="Số điện thoại">
                <Text copyable>{formData.phone || "---"}</Text>
              </Descriptions.Item>
              <Descriptions.Item label="Địa chỉ" span={2}>
                {formData.address || "---"}
              </Descriptions.Item>
            </Descriptions>
          </Card>

          <Card
            type="inner"
            title={<Text strong>Thông tin tài khoản</Text>}
            style={{ marginBottom: 24 }}
          >
            <Descriptions column={{ xs: 1, sm: 2, md: 2 }} size="small">
              <Descriptions.Item label="Tên đăng nhập">
                <Text code copyable>{formData.username || "---"}</Text>
              </Descriptions.Item>
              <Descriptions.Item label="Mật khẩu">
                <Text type="secondary">••••••••</Text>
              </Descriptions.Item>
            </Descriptions>
          </Card>

          <Space style={{ width: "100%", justifyContent: "space-between" }}>
            <Button
              size="large"
              onClick={handleBack}
              icon={<ArrowLeftOutlined />}
              disabled={loading}
              style={{ minWidth: 120 }}
            >
              Quay lại
            </Button>

            <Button
              type="primary"
              size="large"
              onClick={handleSubmit}
              loading={loading}
              icon={!loading && <CheckCircleOutlined />}
              style={{
                minWidth: 160,
                background: "linear-gradient(45deg, #ff6b6b, #ee5a52)",
                border: "none",
              }}
            >
              {loading ? "Đang xử lý..." : "Hoàn tất đăng ký"}
            </Button>
          </Space>
        </Card>
      </div>
    </div>
  );
};

export default RegisterConfirm;
