import React, { useEffect, useState } from "react";
import {
  Layout,
  Table,
  Modal,
  Typography,
  Tag,
  Descriptions,
  message,
  Button,
  Row,
  Col,
  Card,
  Statistic,
  Space,
  Empty,
  Badge,
  Tooltip,
  Alert,
  Progress,
  Avatar,
  Divider,
  Spin
} from "antd";
import {
  DownloadOutlined,
  HistoryOutlined,
  HeartOutlined,
  CalendarOutlined,
  EnvironmentOutlined,
  ExperimentOutlined,
  FileTextOutlined,
  TrophyOutlined,
  StarOutlined,
  UserOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
  SyncOutlined,
  DropboxOutlined
} from "@ant-design/icons";
import axios from "axios";
import dayjs from "dayjs";
import * as XLSX from "xlsx";
import { useParams } from "react-router-dom";
import AuthService from "../services/auth.service";

const { Title, Text, Paragraph } = Typography;
const { Header, Content } = Layout;

const MemberDonationHistory = () => {
  const { id } = useParams();
  const [userId, setUserId] = useState(null);
  const [history, setHistory] = useState([]);
  const [selected, setSelected] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [loading, setLoading] = useState(false);
  const [exportLoading, setExportLoading] = useState(false);

  const currentUser = AuthService.getCurrentUser();

  useEffect(() => {
    const currentUser = AuthService.getCurrentUser();
    setUserId(id || currentUser?.userId);
  }, [id]);

  useEffect(() => {
    const fetchHistory = async () => {
      const token = localStorage.getItem("token");
      if (!token || !userId) {
        console.warn("❗ Thiếu token hoặc userId.");
        return;
      }

      try {
        setLoading(true);
        const res = await axios.get(`http://localhost:8080/api/donation/history/${userId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setHistory(Array.isArray(res.data) ? res.data : []);
      } catch (err) {
        console.error("❌ Lỗi API:", err);
        message.error("Không thể tải lịch sử hiến máu.");
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();
  }, [userId]);

  const handleExportExcel = async () => {
    try {
      setExportLoading(true);

      const exportData = history.map((h, index) => ({
        "STT": index + 1,
        "Ngày hiến": dayjs(h.donation_date).format("DD/MM/YYYY"),
        "Địa điểm": h.location,
        "Thể tích": `${h.volume_ml} ml`,
        "Nhóm máu": h.blood_type,
        "Trạng thái": h.status === "DONATED" ? "Đã hiến" : h.status,
        "Người hiến": h.donor_name || "—",
        "Ghi chú": h.note || "Không có",
        "Đơn vị máu": (h.blood_units && h.blood_units.length > 0) ? h.blood_units.join(", ") : "Chưa có"
      }));

      const worksheet = XLSX.utils.json_to_sheet(exportData);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "LichSuHienMau");

      // Set column widths
      const colWidths = [
        { wch: 5 }, { wch: 12 }, { wch: 25 }, { wch: 10 },
        { wch: 12 }, { wch: 15 }, { wch: 20 }, { wch: 25 }, { wch: 30 }
      ];
      worksheet['!cols'] = colWidths;

      XLSX.writeFile(workbook, `lich_su_hien_mau_${dayjs().format('YYYY_MM_DD')}.xlsx`);
      message.success("📄 Xuất file Excel thành công!");
    } catch (error) {
      message.error("❌ Lỗi khi xuất file Excel");
    } finally {
      setExportLoading(false);
    }
  };

  // Statistics calculations
  const getStats = () => {
    const total = history.length;
    const donated = history.filter(h => h.status === "DONATED" || h.status === "CONFIRMED").length;
    const totalVolume = history.reduce((sum, h) => sum + (h.volume_ml || 0), 0);
    const thisYear = history.filter(h => dayjs(h.donation_date).year() === dayjs().year()).length;

    return { total, donated, totalVolume, thisYear };
  };

  const stats = getStats();

  // Get status info
  const getStatusInfo = (status) => {
    const statusMap = {
      "DONATED": { color: "success", icon: <CheckCircleOutlined />, text: "Đã hiến" },
      "CONFIRMED": { color: "success", icon: <CheckCircleOutlined />, text: "Đã xác nhận" },
      "PENDING": { color: "processing", icon: <SyncOutlined spin />, text: "Đang xử lý" },
      "CANCELLED": { color: "error", icon: <ClockCircleOutlined />, text: "Đã hủy" },
      "SCHEDULED": { color: "warning", icon: <CalendarOutlined />, text: "Đã lên lịch" }
    };
    return statusMap[status] || { color: "default", icon: <ClockCircleOutlined />, text: status };
  };

  // Achievement level based on donation count
  const getAchievementLevel = (count) => {
    if (count >= 20) return { level: "Anh hùng", color: "#722ed1", icon: "🏆" };
    if (count >= 10) return { level: "Chuyên gia", color: "#eb2f96", icon: "⭐" };
    if (count >= 5) return { level: "Tích cực", color: "#1890ff", icon: "🎖️" };
    if (count >= 1) return { level: "Người tốt", color: "#52c41a", icon: "💚" };
    return { level: "Mới bắt đầu", color: "#faad14", icon: "🌟" };
  };

  const achievement = getAchievementLevel(stats.donated);

  const columns = [
    {
      title: (
        <div className="flex items-center">
          <CalendarOutlined className="mr-1 text-blue-500" />
          <span>Ngày hiến</span>
        </div>
      ),
      dataIndex: "donation_date",
      key: "donation_date",
      render: (date) => (
        <div>
          <Text strong>{dayjs(date).format("DD/MM/YYYY")}</Text>
          <br />
          <Text type="secondary" className="text-xs">
            {dayjs(date).format("dddd")}
          </Text>
        </div>
      ),
      width: 120,
    },
    {
      title: (
        <div className="flex items-center">
          <EnvironmentOutlined className="mr-1 text-green-500" />
          <span>Địa điểm</span>
        </div>
      ),
      dataIndex: "location",
      key: "location",
      render: (location) => (
        <Tooltip title={location}>
          <Text className="max-w-xs truncate block">{location}</Text>
        </Tooltip>
      ),
      width: 180,
    },
    {
      title: (
        <div className="flex items-center">
          <CheckCircleOutlined className="mr-1 text-orange-500" />
          <span>Trạng thái</span>
        </div>
      ),
      dataIndex: "status",
      key: "status",
      render: (status) => {
        const statusInfo = getStatusInfo(status);
        return (
          <Tag color={statusInfo.color} icon={statusInfo.icon} className="font-medium">
            {statusInfo.text}
          </Tag>
        );
      },
      width: 130,
    },
    // {
    //   title: "Chi tiết",
    //   key: "action",
    //   render: (_, record) => (
    //     <Button
    //       type="link"
    //       size="small"
    //       icon={<FileTextOutlined />}
    //       onClick={(e) => {
    //         e.stopPropagation();
    //         setSelected(record);
    //         setModalVisible(true);
    //       }}
    //     >
    //       Xem chi tiết
    //     </Button>
    //   ),
    //   width: 100,
    // },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <HistoryOutlined style={{ marginRight: 8 }} />
              Lịch sử hiến máu
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
        <div className="max-w-7xl mx-auto">
          {/* Welcome & Achievement Section */}
          <Alert
            message={
              <div className="flex items-center">
                <HeartOutlined className="mr-2 text-red-500" />
                <span>Cảm ơn bạn đã đóng góp cho cộng đồng!</span>
              </div>
            }
            description={`Bạn đã hiến máu ${stats.donated} lần, góp phần cứu sống nhiều mạng người. Hãy tiếp tục duy trì tinh thần tương trợ tốt đẹp này.`}
            type="success"
            showIcon={false}
            className="mb-6"
            style={{
              background: 'linear-gradient(90deg, #f6ffed 0%, #fff2e8 100%)',
              border: '1px solid #b7eb8f'
            }}
          />

          {/* Achievement Badge */}
          <Card className="mb-6 shadow-sm">
            <Row align="middle" gutter={16}>
              <Col flex="none">
                <Avatar
                  size={64}
                  style={{
                    backgroundColor: achievement.color,
                    fontSize: '24px'
                  }}
                >
                  {achievement.icon}
                </Avatar>
              </Col>
              <Col flex="auto">
                <Title level={4} style={{ margin: 0, color: achievement.color }}>
                  Cấp độ: {achievement.level}
                </Title>
                <Text type="secondary">
                  Bạn đã hiến máu {stats.donated} lần thành công
                </Text>
                <div className="mt-2">
                  <Progress
                    percent={Math.min((stats.donated / 20) * 100, 100)}
                    strokeColor={achievement.color}
                    size="small"
                  />
                  <Text className="text-xs text-gray-500 mt-1 block">
                    {stats.donated < 20 && `Còn ${20 - stats.donated} lượt để đạt cấp "Anh hùng"`}
                    {stats.donated >= 20 && "🎉 Bạn đã đạt cấp độ cao nhất!"}
                  </Text>
                </div>
              </Col>
            </Row>
          </Card>

          {/* History Table */}
          <Card
            title={
              <div className="flex items-center">
                <HistoryOutlined className="mr-2 text-blue-500" />
                <span>Chi tiết lịch sử hiến máu</span>
                <Badge
                  count={history.length}
                  style={{ marginLeft: 8, backgroundColor: '#1890ff' }}
                />
              </div>
            }
            className="shadow-sm"
          >
            <Spin spinning={loading} tip="Đang tải dữ liệu...">
              <Table
                columns={columns}
                dataSource={history}
                rowKey={(record) => record.id}
                onRow={(record) => ({
                  onClick: () => {
                    setSelected(record);
                    setModalVisible(true);
                  },
                  className: "cursor-pointer hover:bg-blue-50 transition-colors"
                })}
                locale={{
                  emptyText: (
                    <Empty
                      image={Empty.PRESENTED_IMAGE_SIMPLE}
                      description={
                        <div>
                          <Text>Chưa có lịch sử hiến máu</Text>
                          <br />
                          <Text type="secondary" className="text-sm">
                            Hãy đăng ký hiến máu để bắt đầu hành trình ý nghĩa
                          </Text>
                        </div>
                      }
                    />
                  )
                }}
                scroll={{ x: 800 }}
              />
            </Spin>
          </Card>

          {/* Detail Modal */}
          <Modal
            title={
              <div className="flex items-center">
                <FileTextOutlined className="mr-2 text-blue-500" />
                <span>Chi tiết lần hiến máu</span>
              </div>
            }
            open={modalVisible}
            onCancel={() => setModalVisible(false)}
            footer={
              <Button
                type="primary"
                onClick={() => setModalVisible(false)}
                style={{
                  background: 'linear-gradient(90deg, #ff6b6b 0%, #ee5a24 100%)',
                  border: 'none'
                }}
              >
                Đóng
              </Button>
            }
            width={600}
          >
            {selected && (
              <div>
                <Alert
                  message={`Trạng thái: ${getStatusInfo(selected.status).text}`}
                  type={getStatusInfo(selected.status).color === 'success' ? 'success' : 'info'}
                  icon={getStatusInfo(selected.status).icon}
                  className="mb-4"
                  showIcon
                />

                <Descriptions
                  column={1}
                  bordered
                  size="middle"
                  labelStyle={{ fontWeight: 600, width: '140px' }}
                >
                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <UserOutlined className="mr-2 text-blue-500" />
                        <span>Người hiến</span>
                      </div>
                    }
                  >
                    <Text strong>{selected.donor_name || "—"}</Text>
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <CalendarOutlined className="mr-2 text-green-500" />
                        <span>Ngày hiến</span>
                      </div>
                    }
                  >
                    <Space>
                      <Text strong>{dayjs(selected.donation_date).format("DD/MM/YYYY")}</Text>
                      <Tag color="blue">
                        {dayjs(selected.donation_date).format("dddd")}
                      </Tag>
                    </Space>
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <EnvironmentOutlined className="mr-2 text-orange-500" />
                        <span>Địa điểm</span>
                      </div>
                    }
                  >
                    {selected.location}
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <ExperimentOutlined className="mr-2 text-purple-500" />
                        <span>Nhóm máu</span>
                      </div>
                    }
                  >
                    <Badge
                      count={selected.blood_type}
                      style={{
                        backgroundColor: '#f50',
                        fontWeight: 'bold'
                      }}
                    />
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <DropboxOutlined className="mr-2 text-red-500" />
                        <span>Thể tích</span>
                      </div>
                    }
                  >
                    <Tag color="red" className="text-lg font-bold px-3 py-1">
                      {selected.volume_ml} ml
                    </Tag>
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <FileTextOutlined className="mr-2 text-gray-500" />
                        <span>Ghi chú</span>
                      </div>
                    }
                  >
                    <Text>{selected.note || "Không có ghi chú"}</Text>
                  </Descriptions.Item>

                  <Descriptions.Item
                    label={
                      <div className="flex items-center">
                        <ExperimentOutlined className="mr-2 text-pink-500" />
                        <span>Đơn vị máu</span>
                      </div>
                    }
                  >
                    {(selected.blood_units && selected.blood_units.length > 0) ? (
                      <Space wrap>
                        {selected.blood_units.map((unit, index) => (
                          <Tag key={index} color="magenta">
                            {unit}
                          </Tag>
                        ))}
                      </Space>
                    ) : (
                      <Text type="secondary">Chưa có đơn vị máu được tạo</Text>
                    )}
                  </Descriptions.Item>
                </Descriptions>
              </div>
            )}
          </Modal>
        </div>
      </Content>

      <style jsx global>{`
        .ant-card {
          border-radius: 8px;
          transition: all 0.3s ease;
        }
        
        .ant-card:hover {
          transform: translateY(-2px);
        }
        
        .ant-table-tbody > tr:hover > td {
          background: #e6f7ff !important;
        }
        
        .ant-descriptions-bordered .ant-descriptions-item-label {
          background: #fafafa;
        }
        
        .ant-modal-header {
          background: linear-gradient(90deg, #f0f9ff 0%, #fef7f0 100%);
          border-bottom: 2px solid #f0f0f0;
        }
        
        .ant-progress-bg {
          transition: all 0.3s ease;
        }
        
        .ant-statistic-content {
          display: flex;
          align-items: center;
          justify-content: center;
        }
        
        .ant-alert {
          border-radius: 8px;
        }
        
        .cursor-pointer {
          cursor: pointer;
        }
        
        .transition-colors {
          transition: background-color 0.2s ease;
        }
        
        .transition-shadow {
          transition: box-shadow 0.3s ease;
        }
      `}</style>
    </Layout>
  );
};

export default MemberDonationHistory;