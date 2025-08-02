import React, { useState, useEffect } from "react";
import {
  Card,
  Typography,
  Divider,
  Button,
  Row,
  Checkbox,
  Select,
  message
} from "antd";
import {
  SmileTwoTone,
  CheckCircleTwoTone,
  ExclamationCircleTwoTone,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/auth.service";
import benefits from "../data/benefits.json";
import axios from "axios";

const { Title, Paragraph, Text } = Typography;

const UrgentDonationWrapper = () => {
  const navigate = useNavigate();
  const user = AuthService.getCurrentUser();
  const userId = user?.userId || user?.id;
  const [agree, setAgree] = useState(false);
  const [conflict, setConflict] = useState(null);
  const [changing, setChanging] = useState(false);
  const [selectedMode, setSelectedMode] = useState("EMERGENCY_FLEXIBLE");
  const [readinessOptions] = useState(["EMERGENCY_NOW"]);
const [isUndecided, setIsUndecided] = useState(false);
  const handleAccept = () => {
    if (!userId) navigate("/login");
    else navigate(`/user/${userId}/urgent-register`);
  };
  const handleLeaveGroup = async () => {
    try {
      await axios.post(
        "http://localhost:8080/api/urgent-donors/leave",
        {},
        { headers: { Authorization: `Bearer ${user?.accessToken}` } }
      );
      message.success("  Bạn đã rút khỏi nhóm hiến máu khẩn cấp.");
      fetchCurrentStatus();
    } catch (err) {
      console.error("  Leave group failed:", err);
      message.error("Không thể rút khỏi nhóm");
    }
  };
const fetchCurrentStatus = async () => {
  try {
    const res = await axios.get(
      `http://localhost:8080/api/urgent-donors/current-status`,
      { headers: { Authorization: `Bearer ${user?.accessToken}` } }
    );
    const { mode, status } = res.data;

    if (status === "UNDECIDED") {
      setIsUndecided(true);
      setConflict(null);
      return;
    }

    setIsUndecided(false); // đã có trạng thái
    if (mode) setSelectedMode(mode);
    setConflict({
      hasConflict: mode && mode !== selectedMode,
      currentMode: mode,
      message: "Bạn đang ở chế độ khác, hãy xác nhận nếu muốn thay đổi.",
    });
  } catch (err) {
    console.error("  Cannot fetch current status:", err);
  }
};
const readinessLabels = {
  EMERGENCY_NOW: "Sẵn sàng ngay",
  EMERGENCY_FLEXIBLE: "Sẵn sàng khi cần",
  UNDECIDED: "Đã rút khỏi nhóm",
};

  useEffect(() => {
    if (user?.accessToken) fetchCurrentStatus();
  }, []);

  const handleChangeReadiness = async (newLevel) => {
    try {
      setChanging(true);
      await axios.post(
        "http://localhost:8080/api/urgent-donors/confirm-change-mode",
        { readinessLevel: newLevel },
        { headers: { Authorization: `Bearer ${user?.accessToken}` } }
      );
      message.success("Đã chuyển sang chế độ Sẵn sàng ngay");
      setSelectedMode(newLevel);
      fetchCurrentStatus();
    } catch (err) {
      console.error("  Change readiness failed:", err);
      message.error("Không thể chuyển chế độ");
    } finally {
      setChanging(false);
    }
  };

  return (
    <div
      style={{
        width: "100%",
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        padding: "8px 12px",
        backgroundColor: "#fafafa",
        transition: "all 0.3s ease",
      }}
    >
      <Card
        bordered={false}
        style={{
          width: "100%",
          maxWidth: "900px",
          borderRadius: 16,
          boxShadow: "0 4px 12px rgba(0,0,0,0.06)",
          padding: "16px 24px",
          background: "#fff",
          transition: "all 0.3s ease",
        }}
      >
        <Title level={3} style={{ color: "#d32f2f", marginBottom: 10, marginTop: 4 }}>
          🩸 Tham gia nhóm hiến máu khẩn cấp – Hành động nhỏ, ý nghĩa lớn
        </Title>

        <Paragraph style={{ fontSize: "15.5px", marginBottom: 16 }}>
          Bạn có thể là người tạo nên điều kỳ diệu trong những khoảnh khắc sinh tử.
          Chúng tôi rất trân trọng nếu bạn sẵn sàng tham gia mạng lưới <strong>hiến máu khẩn cấp</strong> – nơi những giọt máu được trao đúng lúc, đúng người cần.
        </Paragraph>

        <Divider orientation="left" plain style={{ margin: "12px 0" }}>
          🎁 Quyền lợi dành cho người hiến máu khẩn cấp
        </Divider>
        <div style={{ display: "flex", flexDirection: "column", gap: 8, paddingLeft: 4, marginBottom: 16 }}>
          {benefits.map((item, index) => (
            <div key={index} style={{ display: "flex", alignItems: "start" }}>
              <CheckCircleTwoTone twoToneColor="#52c41a" style={{ marginRight: 8, fontSize: 18 }} />
              <Text>{item.replace("✔ ", "")}</Text>
            </div>
          ))}
        </div>

        <Divider orientation="left" plain style={{ margin: "12px 0" }}>
          📜 Chính sách & Cam kết
        </Divider>
        <div style={{ display: "flex", flexDirection: "column", gap: 8, paddingLeft: 4, marginBottom: 20 }}>
          <div style={{ display: "flex" }}>
            <ExclamationCircleTwoTone twoToneColor="#faad14" style={{ marginRight: 8, fontSize: 18 }} />
            <Text>Tôi xác nhận rằng tất cả thông tin cá nhân cung cấp là chính xác và đầy đủ.</Text>
          </div>
          <div style={{ display: "flex" }}>
            <ExclamationCircleTwoTone twoToneColor="#faad14" style={{ marginRight: 8, fontSize: 18 }} />
            <Text>Tôi hiểu rằng việc đăng ký là tự nguyện, có thể từ chối nếu không đủ sức khỏe tại thời điểm liên hệ.</Text>
          </div>
          <div style={{ display: "flex" }}>
            <ExclamationCircleTwoTone twoToneColor="#faad14" style={{ marginRight: 8, fontSize: 18 }} />
            <Text>Tôi đồng ý được liên hệ khẩn cấp qua điện thoại trong những trường hợp cần truyền máu phù hợp.</Text>
          </div>
        </div>

{!isUndecided && conflict?.hasConflict && (
  <div
    style={{
      backgroundColor: "#fffbe6",
      border: "1px solid #ffe58f",
      borderRadius: 8,
      padding: "16px",
      marginBottom: 20,
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
      gap: 12,
    }}
  >
    <div>
  <Text style={{ fontWeight: 600, color: "#d48806" }}>
    ⚠ Bạn đã đăng ký ở chế độ khác
  </Text>
  <div style={{ marginTop: 4 }}>
    <Text>Hiện tại bạn đang ở chế độ: </Text>
    {conflict.currentMode === "EMERGENCY_NOW" ? (
      <Text>Sẵn sàng ngay</Text>
    ) : (
      <Select
  value={readinessLabels[conflict.currentMode] || conflict.currentMode}
  disabled={changing}
  size="small"
  style={{ minWidth: 200 }}
  onChange={handleChangeReadiness}
  options={readinessOptions
    .filter((level) => level !== conflict.currentMode)
    .map((level) => ({
      value: level,
      label: readinessLabels[level] || level.replace("_", " ") // Hiển thị nhãn tiếng Việt nếu có
    }))}
  dropdownMatchSelectWidth={false}
/>
    )}
  </div>
      <div>
        <Text style={{ fontStyle: "italic" }}>
          Lưu ý: {conflict.message || "Hãy xác nhận lại trước khi chuyển đổi chế độ."}
        </Text>
      </div>
      <div style={{ marginTop: 12 }}>
        <Button type="link" danger onClick={handleLeaveGroup}>
          Rút khỏi nhóm
        </Button>
      </div>
    </div>
  </div>
)}


        <Checkbox
          checked={agree}
          onChange={(e) => setAgree(e.target.checked)}
          style={{ marginBottom: 24 }}
        >
          Tôi đã đọc và đồng ý với các quyền lợi & điều khoản trên
        </Checkbox>

        <Row justify="center">
          <Button
            type="primary"
            onClick={handleAccept}
            disabled={!agree || conflict?.hasConflict}
            size="large"
            style={{
              backgroundColor: agree && !conflict?.hasConflict ? "#f44336" : "#f8d7da",
              borderColor: agree && !conflict?.hasConflict ? "#f44336" : "#f8d7da",
              color: agree && !conflict?.hasConflict ? "#fff" : "#a94442",
              padding: "0 24px",
              borderRadius: 6,
              cursor: agree && !conflict?.hasConflict ? "pointer" : "not-allowed",
              transition: "all 0.3s ease"
            }}
          >
            Tiếp tục
          </Button>
        </Row>

        <Divider style={{ margin: "24px 0 12px" }} />
        <Paragraph style={{ fontSize: "15px", textAlign: "center", marginTop: 8 }}>
          <SmileTwoTone twoToneColor="#fadb14" style={{ fontSize: 20 }} />{" "}
          <Text strong>Cảm ơn bạn vì trái tim nhân ái và tinh thần sẻ chia.</Text><br />
          Mỗi giọt máu bạn trao đi là một hy vọng bạn gửi lại cho cuộc đời.
        </Paragraph>
      </Card>
    </div>
  );
};

export default UrgentDonationWrapper;
