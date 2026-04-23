import React, { useState, useEffect } from "react";
import {
  Layout,
  Typography,
  DatePicker,
  Card,
  Button,
  Divider,
  message,
  Row,
  Col,
  Input,
  Space,
  Badge,
  Tooltip,
  Alert,
  Steps,
  Progress,
  Tag,
  Spin,
  Empty,
  Result
} from "antd";
import {
  ScheduleOutlined,
  HeartOutlined,
  CalendarOutlined,
  ClockCircleOutlined,
  EnvironmentOutlined,
  UserOutlined,
  CheckCircleOutlined,
  LoadingOutlined,
  InfoCircleOutlined,
  WarningOutlined,
  StarOutlined
} from "@ant-design/icons";
import dayjs from "dayjs";
import axios from "axios";
import AuthService from "../services/auth.service";
import { getAuthHeader } from "../services/user.service";
import { useNavigate } from "react-router-dom";
import { API_BASE_URL, apiUrl } from "../config/api";

const { Title, Text, Paragraph } = Typography;
const { Header, Content } = Layout;
const { Step } = Steps;



const API_BASE = API_BASE_URL;

const generateSlots = () => {
  const slots = [];
  for (let hour = 7; hour <= 20; hour++) {
    slots.push(`${hour.toString().padStart(2, "0")}:00`);
    slots.push(`${hour.toString().padStart(2, "0")}:30`);
  }
  return slots;
};


const DonationRegister = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedSlot, setSelectedSlot] = useState(null); // slot.time
  const [selectedSlotId, setSelectedSlotId] = useState(null); // slot.slotId
  const [selectedLocation, setSelectedLocation] = useState("FPTU Campus");
  const [loading, setLoading] = useState(false);
  const [availableSlots, setAvailableSlots] = useState({});
  const [slotsLoading, setSlotsLoading] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);
  const [lastDonationDate, setLastDonationDate] = useState(null);
  const currentUser = AuthService.getCurrentUser();
  const slots = generateSlots();
  const navigate = useNavigate();
const isTooSoonToDonate = () => {
  if (!selectedDate || !lastDonationDate) return false;
  const diffDays = selectedDate.diff(lastDonationDate, 'day');
  return diffDays < 84; // ít hơn 12 tuần
};

// Hàm check xem slot có đã qua giờ không (chỉ áp dụng cho ngày hôm nay)
const isSlotPassed = (slotTime, selectedDate) => {
  if (!selectedDate) return false;

  // Chỉ check cho ngày hôm nay
  const isToday = selectedDate.isSame(dayjs(), 'day');
  if (!isToday) return false;

  const now = dayjs();
  const [hour, minute] = slotTime.split(':').map(Number);
  const slotDateTime = dayjs().hour(hour).minute(minute).second(0);

  return now.isAfter(slotDateTime);
};

const checkSlotFull = async (date, slotId) => {
  try {
    const res = await axios.get(
      apiUrl("api/donation/check-slot"),
      {
        params: {
          date: date.format("YYYY-MM-DD"),
          slot_id: slotId,
        },
        headers: getAuthHeader(),
      }
    );
    return res.data === true;
  } catch {
    return true; // mặc định là full nếu lỗi
  }
};
  // Mapping slot ID
  const slotIdMap = {
    "07:00": 1, "07:30": 2, "08:00": 3, "08:30": 4, "09:00": 5,
    "09:30": 6, "10:00": 7, "10:30": 8, "11:00": 9, "11:30": 10,
    "12:00": 11, "12:30": 12, "13:00": 13, "13:30": 14, "14:00": 15,
    "14:30": 16, "15:00": 17, "15:30": 18, "16:00": 19, "16:30": 20,
    "17:00": 21, "17:30": 22, "18:00": 23, "18:30": 24, "19:00": 25,
    "19:30": 26, "20:00": 27, "20:30": 28,
  };

  useEffect(() => {
  let isMounted = true;

  const fetchSlotsStatus = async () => {
    if (!selectedDate || !currentUser?.userId) return;

    setSlotsLoading(true);

    try {
      // 1. Gọi API lấy lịch sử hiến máu
      const res = await axios.get(apiUrl(`api/donation/history/${currentUser.userId}`), {
        headers: getAuthHeader(),
      });

      // 2. Lọc các lần hiến máu có trạng thái "DONATED"
      const donatedDates = res.data
        .filter((h) => h.status === "DONATED")
        .map((h) => dayjs(h.donation_date))
        .sort((a, b) => b.diff(a)); // sắp xếp giảm dần

      const lastDonation = donatedDates.length > 0 ? donatedDates[0] : null;

      if (isMounted) {
        setLastDonationDate(lastDonation);
      }

      // 3. Kiểm tra điều kiện 84 ngày
      if (lastDonation && selectedDate.diff(lastDonation, 'day') < 84) {
        if (isMounted) {
          message.warning(`⛔ Bạn chỉ có thể đăng ký sau 12 tuần kể từ lần hiến gần nhất: ${lastDonation.format("DD/MM/YYYY")}.`);
          setAvailableSlots({});
          setSlotsLoading(false);
        }
        return;
      }

      // 4. Fetch trạng thái slot nếu hợp lệ
      const updatedSlots = {};

      await Promise.all(
        slots.map(async (time) => {
          const slotId = slotIdMap[time];
          if (!slotId) return;

          try {
            const res = await axios.get(apiUrl("api/donation/check-slot"), {
              params: {
                date: selectedDate.format("YYYY-MM-DD"),
                slot_id: slotId,
              },
              headers: getAuthHeader(),
            });

            const isSlotFull = res.data === true;
            const isPassed = isSlotPassed(time, selectedDate);

            updatedSlots[time] = {
              available: !isSlotFull && !isPassed,
              slotId,
              isFull: isSlotFull,
              isPassed,
            };
          } catch {
            updatedSlots[time] = {
              available: false,
              slotId,
              isFull: true,
              isPassed: isSlotPassed(time, selectedDate),
            };
          }
        })
      );

      if (isMounted) {
        setAvailableSlots(updatedSlots);
      }
    } catch (error) {
      console.error("❌ Lỗi khi kiểm tra slot:", error);
      if (isMounted) {
        message.error("Không thể kiểm tra lịch sử hiến máu hoặc slot.");
      }
    } finally {
      if (isMounted) {
        setSlotsLoading(false);
      }
    }
  };

  fetchSlotsStatus();

  // 🧹 Cleanup để ngăn memory leak
  return () => {
    isMounted = false;
  };
}, [selectedDate, currentUser?.userId]);



  // Update current step based on selections
  useEffect(() => {
    if (!selectedDate) {
      setCurrentStep(0);
    } else if (!selectedSlot) {
      setCurrentStep(1);
    } else {
      setCurrentStep(2);
    }
  }, [selectedDate, selectedSlot]);

  const handleDateChange = (date) => {
    setSelectedDate(date);
    setSelectedSlot(null);
    setSelectedSlotId(null);
  };

  const handleSubmit = async () => {
    if (!selectedDate || !selectedSlot || !selectedSlotId || !selectedLocation) {
      message.warning("Vui lòng chọn đầy đủ thông tin.");
      return;
    }

    if (!currentUser || !currentUser.userId) {
      message.error("Vui lòng đăng nhập trước khi đăng ký.");
      return;
    }

    // Double check xem slot có bị qua giờ không trước khi submit
    if (isSlotPassed(selectedSlot, selectedDate)) {
      message.error("Khung giờ đã chọn đã qua. Vui lòng chọn khung giờ khác.");
      setSelectedSlot(null);
      setSelectedSlotId(null);
      return;
    }

    try {
      setLoading(true);
      const payload = {
        scheduledDate: selectedDate.format("YYYY-MM-DD"),
        readyDate: dayjs().format("YYYY-MM-DD"),
        slotId: selectedSlotId,
        location: selectedLocation,
      };

      await axios.post(
        apiUrl(`api/donation/register/${currentUser.userId}`),
        payload,
        { headers: getAuthHeader() }
      );

      message.success("✅ Đăng ký hiến máu thành công!");

      setSelectedDate(null);
      setSelectedSlot(null);
      setSelectedSlotId(null);
      setSelectedLocation("FPTU Campus");

      setTimeout(() => {
        navigate(`/user/${currentUser.userId}/donation-history`);
      }, 1000);
    } catch (err) {
      const errorMsg = err.response?.data?.message || "Đăng ký thất bại.";
      message.error(`❌ ${errorMsg}`);
    } finally {
      setLoading(false);
    }
  };

  // Tính toán thống kê slots
  const getTotalSlots = () => slots.length;
  const getAvailableCount = () => Object.values(availableSlots).filter(slot => slot.available).length;
  const getFullCount = () => Object.values(availableSlots).filter(slot => slot.isFull).length;
  const getPassedCount = () => Object.values(availableSlots).filter(slot => slot.isPassed).length;

  // Render slot với style đẹp hơn
  const renderSlot = (slot) => {
    const slotData = availableSlots?.[slot];
    const isAvailable = slotData?.available;
    const isPassed = slotData?.isPassed;
    const isFull = slotData?.isFull;
    const isSelected = selectedSlot === slot;

    let status = 'available';
    let color = 'success';
    let icon = <CheckCircleOutlined />;
    let title = "Khung giờ có sẵn";

    if (isPassed) {
      status = 'passed';
      color = 'default';
      icon = <ClockCircleOutlined />;
      title = "Khung giờ đã qua";
    } else if (isFull) {
      status = 'full';
      color = 'error';
      icon = <WarningOutlined />;
      title = "Khung giờ đã đầy";
    }

    return (
      <Col key={slot} xs={12} sm={8} md={6} lg={4}>
        <Tooltip title={title}>
          <Badge
            status={isSelected ? 'processing' : color}
            offset={[-5, 5]}
            dot={isSelected}
          >
            <Button
              block
              size="large"
              type={isSelected ? "primary" : "default"}
              disabled={!isAvailable}
              className={`
                h-16 font-semibold transition-all duration-300
                ${isSelected ? 'shadow-lg border-2 border-blue-400' : ''}
                ${isAvailable && !isSelected ? 'hover:shadow-md hover:border-blue-300' : ''}
                ${isPassed ? 'opacity-50' : ''}
              `}
              style={{
                backgroundColor: isSelected ? '#1890ff' :
                  isAvailable ? '#fff' :
                    isPassed ? '#f5f5f5' : '#fff5f5',
                borderColor: isSelected ? '#1890ff' :
                  isAvailable ? '#d9d9d9' :
                    isPassed ? '#d9d9d9' : '#ffccc7',
                color: isSelected ? '#fff' :
                  isAvailable ? '#262626' :
                    isPassed ? '#999' : '#ff4d4f'
              }}
              onClick={() => {
                if (isAvailable && !isPassed) {
                  setSelectedSlot(slot);
                  setSelectedSlotId(slotData?.slotId || null);
                }
              }}
            >
              <div className="flex flex-col items-center">
                <div className="text-lg">{slot}</div>
              </div>
            </Button>
          </Badge>
        </Tooltip>
      </Col>
    );
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <HeartOutlined style={{ marginRight: 8 }} />
              Đăng ký hiến máu
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
                {currentUser?.username || 'Người dùng'}
              </Text>
            </Space>
          </Col>
        </Row>
      </Header>

      <Content style={{ padding: '24px' }}>
        <div className="max-w-6xl mx-auto">
          {/* Welcome Message */}
          <Alert
            message="Chào mừng bạn đến với hệ thống đăng ký hiến máu!"
            description="Hãy hoàn thành các bước bên dưới để đăng ký lịch hiến máu. Mỗi giọt máu của bạn có thể cứu sống một mạng người."
            type="info"
            icon={<HeartOutlined />}
            showIcon
            className="mb-6"
            style={{
              background: 'linear-gradient(90deg, #e3f2fd 0%, #f3e5f5 100%)',
              border: '1px solid #90caf9'
            }}
          />

          {/* Progress Steps */}
          <Card className="mb-6 shadow-sm">
            <Steps current={currentStep} className="mb-4">
              <Step
                title="Chọn ngày"
                icon={<CalendarOutlined />}
                description="Lựa chọn ngày hiến máu"
              />
              <Step
                title="Chọn giờ"
                icon={<ClockCircleOutlined />}
                description="Chọn khung giờ phù hợp"
              />
              <Step
                title="Xác nhận"
                icon={<CheckCircleOutlined />}
                description="Hoàn tất đăng ký"
              />
            </Steps>

            <Progress
              percent={Math.round(((currentStep + 1) / 3) * 100)}
              strokeColor={{
                '0%': '#ff6b6b',
                '100%': '#ee5a24',
              }}
              className="mb-2"
            />
          </Card>

          <Row gutter={24}>
            {/* Left Column - Form */}
            <Col xs={24} lg={14}>
              <Card
                title={
                  <div className="flex items-center">
                    <ScheduleOutlined className="mr-2 text-red-500" />
                    <span>Thông tin đăng ký</span>
                  </div>
                }
                className="shadow-sm"
                style={{ minHeight: '600px' }}
              >
                <Space direction="vertical" style={{ width: "100%" }} size="large">
                  {/* Location Info */}
                  <div>
                    <Text strong className="block mb-2">
                      <EnvironmentOutlined className="mr-2 text-green-500" />
                      Địa điểm hiến máu
                    </Text>
                    <Input
                      value="FPTU Campus - Khu Công nghệ cao HCM"
                      readOnly
                      size="large"
                      prefix={<EnvironmentOutlined className="text-green-500" />}
                      className="bg-green-50 border-green-200"
                    />
                    <Text type="secondary" className="block mt-1 text-xs">
                      📍 Địa chỉ cố định, không thể thay đổi
                    </Text>
                  </div>

                  {/* Date Selection */}
                  <div>
                    <Text strong className="block mb-2">
                      <CalendarOutlined className="mr-2 text-blue-500" />
                      Chọn ngày hiến máu
                    </Text>
                    <DatePicker
                      style={{ width: "100%" }}
                      size="large"
                      value={selectedDate}
                      onChange={handleDateChange}
                      disabledDate={(current) => current && current < dayjs().startOf("day")}
                      placeholder="Chọn ngày bạn muốn hiến máu"
                      format="DD/MM/YYYY"
                      className="border-blue-200"
                    />
                    {selectedDate && (
                      <div className="mt-2 p-3 bg-blue-50 rounded-lg border-l-4 border-blue-400">
                        <Text className="text-blue-700">
                          <CalendarOutlined className="mr-2" />
                          Ngày đã chọn: <Text strong>{selectedDate.format('dddd, DD/MM/YYYY')}</Text>
                        </Text>
                      </div>
                    )}
                    {selectedDate && lastDonationDate && isTooSoonToDonate() && (
                      <Alert
                        message="⏳ Bạn vừa mới hiến máu!"
                        description={`Bạn cần chờ đủ 12 tuần (84 ngày) giữa 2 lần hiến. Lần gần nhất bạn hiến là ngày ${lastDonationDate.format('DD/MM/YYYY')}.`}
                        type="warning"
                        showIcon
                        className="mt-3"
                      />
                    )}

                  </div>

                  {/* Time Slot Selection */}
                  <div>
                    <div className="flex justify-between items-center mb-3">
                      <Text strong>
                        <ClockCircleOutlined className="mr-2 text-orange-500" />
                        Chọn khung giờ
                      </Text>
                      {selectedDate && (
                        <Space>
                          <Tag color="success">
                            <CheckCircleOutlined /> {getAvailableCount()} còn trống
                          </Tag>
                          <Tag color="error">
                            <WarningOutlined /> {getFullCount()} đã đầy
                          </Tag>
                          {getPassedCount() > 0 && (
                            <Tag color="default">
                              <ClockCircleOutlined /> {getPassedCount()} đã qua
                            </Tag>
                          )}
                        </Space>
                      )}
                    </div>

                    {!selectedDate ? (
                      <div className="text-center py-12">
                        <Empty
                          image={Empty.PRESENTED_IMAGE_SIMPLE}
                          description="Vui lòng chọn ngày trước"
                        />
                      </div>
                    ) : (
                      <Card
                        size="small"
                        className="border-orange-200"
                        style={{ maxHeight: '400px', overflowY: 'auto' }}
                      >
                        <Spin spinning={slotsLoading} tip="Đang tải khung giờ...">
                          <Row gutter={[12, 12]}>
                            {slots.map(renderSlot)}
                          </Row>
                        </Spin>
                      </Card>
                    )}

                    {selectedSlot && (
                      <div className="mt-3 p-3 bg-orange-50 rounded-lg border-l-4 border-orange-400">
                        <Text className="text-orange-700">
                          <ClockCircleOutlined className="mr-2" />
                          Khung giờ đã chọn: <Text strong>{selectedSlot}</Text>
                        </Text>
                      </div>
                    )}
                  </div>

                  {/* Submit Button */}
                  <div className="pt-4">
                    <Button
                      type="primary"
                      size="large"
                      onClick={handleSubmit}
                      loading={loading}
                      disabled={!selectedDate || !selectedSlot || !selectedLocation || isTooSoonToDonate()}

                      block
                      className="h-14 text-lg font-semibold"
                      style={{
                        background: 'linear-gradient(90deg, #ff6b6b 0%, #ee5a24 100%)',
                        border: 'none'
                      }}
                      icon={loading ? <LoadingOutlined /> : <HeartOutlined />}
                    >
                      {loading ? 'Đang xử lý...' : 'Gửi đăng ký hiến máu'}
                    </Button>
                  </div>
                </Space>
              </Card>
            </Col>

            {/* Right Column - Information */}
            <Col xs={24} lg={10}>
              <Space direction="vertical" style={{ width: "100%" }} size="large">
                {/* Selection Summary */}
                <Card
                  title={
                    <div className="flex items-center">
                      <InfoCircleOutlined className="mr-2 text-blue-500" />
                      <span>Thông tin đăng ký</span>
                    </div>
                  }
                  className="shadow-sm"
                >
                  <Space direction="vertical" style={{ width: "100%" }}>
                    <div className="flex justify-between">
                      <Text>Địa điểm: </Text>
                      <Text strong>FPTU Campus - Khu Công nghệ cao HCM</Text>
                    </div>
                    <div className="flex justify-between">
                      <Text>Ngày hiến máu: </Text>
                      <Text strong>
                        {selectedDate ? selectedDate.format('DD/MM/YYYY') : 'Chưa chọn'}
                      </Text>
                    </div>
                    <div className="flex justify-between">
                      <Text>Khung giờ: </Text>
                      <Text strong>
                        {selectedSlot || 'Chưa chọn'}
                      </Text>
                    </div>
                    <Divider style={{ margin: '12px 0' }} />
                    <div className="flex justify-between">
                      <Text>Trạng thái:</Text>
                      <Badge
                        status={selectedDate && selectedSlot ? "success" : "processing"}
                        text={selectedDate && selectedSlot ? "Sẵn sàng" : "Đang chọn"}
                      />
                    </div>
                  </Space>
                </Card>

                {/* Important Notes */}
                <Card
                  title={
                    <div className="flex items-center">
                      <StarOutlined className="mr-2 text-yellow-500" />
                      <span>Lưu ý quan trọng</span>
                    </div>
                  }
                  className="shadow-sm"
                >
                  <Space direction="vertical" size="small">
                    <Text>• Vui lòng đến đúng giờ đã đăng ký</Text>
                    <Text>• Mang theo CCCD/CMND và thẻ sinh viên</Text>
                    <Text>• Không uống rượu bia 24h trước khi hiến máu</Text>
                    <Text>• Ngủ đủ giấc và ăn uống đầy đủ</Text>
                    <Text>• Thông báo nếu đang dùng thuốc</Text>
                    <Text>• Có thể hủy lịch trước 2 giờ</Text>
                  </Space>
                </Card>
              </Space>
            </Col>
          </Row>
        </div>
      </Content>

      <style jsx global>{`
        .ant-steps-item-finish .ant-steps-item-icon {
          background-color: #52c41a;
          border-color: #52c41a;
        }
        
        .ant-steps-item-process .ant-steps-item-icon {
          background-color: #1890ff;
          border-color: #1890ff;
        }
        
        .ant-progress-bg {
          background: linear-gradient(90deg, #ff6b6b 0%, #ee5a24 100%) !important;
        }
        
        .ant-card-head {
          background: #fafafa;
          border-bottom: 2px solid #f0f0f0;
        }
        
        .ant-card-head-title {
          font-weight: 600;
        }
        
        .ant-btn-primary:hover {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
        }
        
        .ant-alert {
          border-radius: 8px;
        }
      `}</style>
    </Layout>
  );
};

export default DonationRegister;