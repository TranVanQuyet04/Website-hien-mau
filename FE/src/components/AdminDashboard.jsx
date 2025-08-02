// src/pages/AdminDashboard.jsx
import React, { useState, useEffect } from 'react';
import { 
  Button, 
  Card, 
  Table, 
  Select, 
  Space, 
  Row, 
  Col, 
  Statistic, 
  Tag, 
  Badge, 
  Tooltip, 
  Typography, 
  Avatar, 
  Alert, 
  Tabs, 
  Modal, 
  Input,
  DatePicker,
  Divider,
  Layout,
  Progress,
  List,
  Timeline,
  notification
} from 'antd';
import {
  DashboardOutlined,
  ReloadOutlined,
  DownloadOutlined,
  UserOutlined,
  HeartOutlined,
  EnvironmentOutlined,
  TeamOutlined,
  BellOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined,
  PlusOutlined,
  CheckCircleOutlined,
  ClockCircleOutlined,
  TrophyOutlined,
  WarningOutlined,
  FireOutlined,
  DropboxOutlined,
  LineChartOutlined,
  CalendarOutlined,
  PhoneOutlined,
  ExclamationCircleOutlined,
  SyncOutlined,
  SettingOutlined
} from '@ant-design/icons';
import { getAllTransfusions } from "../services/transfusion.service";

const { Header, Content, Sider } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;
const { Meta } = Card;

const AdminDashboard = () => {
  const [loading, setLoading] = useState(false);
  const [transfusions, setTransfusions] = useState([]);
  const [selectedFilters, setSelectedFilters] = useState({
    dateRange: null,
    bloodType: null,
    status: null,
    priority: null,
  });
  const [activeTab, setActiveTab] = useState('overview');
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [refreshTime, setRefreshTime] = useState(new Date());

  // Fetch data
  useEffect(() => {
    loadTransfusions();
  }, []);

  const loadTransfusions = async () => {
    setLoading(true);
    try {
      const res = await getAllTransfusions();
      setTransfusions(Array.isArray(res.data) ? res.data : []);
    } catch (error) {
      console.error('Error loading transfusions:', error);
      setTransfusions([]);
      notification.error({
        message: 'Lỗi tải dữ liệu',
        description: 'Không thể tải dữ liệu truyền máu. Vui lòng thử lại.',
      });
    } finally {
      setLoading(false);
    }
  };

  // Mock data for demo - replace with real API data
  const dashboardStats = {
    donorsToday: 25,
    bloodUnits: 450,
    urgentRequests: 3,
    totalDonors: 1205,
    successfulTransfusions: 342,
    pendingRequests: 15,
    avgResponseTime: 35,
    bloodBankCapacity: 85.5,
  };

  const bloodInventory = [
    { bloodType: 'O+', available: 120, required: 80, status: 'sufficient' },
    { bloodType: 'A+', available: 85, required: 70, status: 'sufficient' },
    { bloodType: 'B+', available: 45, required: 60, status: 'low' },
    { bloodType: 'AB+', available: 25, required: 30, status: 'low' },
    { bloodType: 'O-', available: 15, required: 40, status: 'critical' },
    { bloodType: 'A-', available: 30, required: 35, status: 'low' },
    { bloodType: 'B-', available: 20, required: 25, status: 'low' },
    { bloodType: 'AB-', available: 8, required: 15, status: 'critical' },
  ];

  const recentRequests = [
    {
      id: 1,
      patientName: 'Nguyễn Văn A',
      bloodType: 'O-',
      unitsNeeded: 3,
      priority: 'critical',
      hospital: 'BV Chợ Rẫy',
      requestTime: '2025-07-14 08:30',
      status: 'pending',
      phone: '0123456789',
      notes: 'Bệnh nhân tai nạn giao thông, cần truyền máu khẩn cấp',
    },
    {
      id: 2,
      patientName: 'Trần Thị B',
      bloodType: 'AB+',
      unitsNeeded: 2,
      priority: 'high',
      hospital: 'BV Bình Dân',
      requestTime: '2025-07-14 09:15',
      status: 'processing',
      phone: '0987654321',
      notes: 'Phẫu thuật tim, cần chuẩn bị máu dự phòng',
    },
    {
      id: 3,
      patientName: 'Lê Văn C',
      bloodType: 'A+',
      unitsNeeded: 1,
      priority: 'normal',
      hospital: 'BV Đại học Y Dược',
      requestTime: '2025-07-14 10:00',
      status: 'completed',
      phone: '0345678901',
      notes: 'Điều trị thiếu máu, truyền máu định kỳ',
    },
    {
      id: 4,
      patientName: 'Phạm Thị D',
      bloodType: 'B-',
      unitsNeeded: 2,
      priority: 'high',
      hospital: 'BV Nhi Đồng 1',
      requestTime: '2025-07-14 11:30',
      status: 'pending',
      phone: '0456789012',
      notes: 'Bệnh nhân nhi, cần máu hiếm',
    },
  ];

  const todayActivities = [
    {
      time: '08:00',
      type: 'donation',
      description: '5 người hiến máu tình nguyện tại BV Chợ Rẫy',
      status: 'completed',
    },
    {
      time: '09:30',
      type: 'request',
      description: 'Yêu cầu khẩn cấp máu O- từ BV Chợ Rẫy',
      status: 'urgent',
    },
    {
      time: '10:15',
      type: 'transfer',
      description: 'Chuyển 10 đơn vị máu A+ đến BV Bình Dân',
      status: 'completed',
    },
    {
      time: '11:00',
      type: 'donation',
      description: '3 người hiến máu định kỳ tại BV Đại học Y Dược',
      status: 'completed',
    },
    {
      time: '12:30',
      type: 'alert',
      description: 'Cảnh báo: Lượng máu B- dưới ngưỡng tối thiểu',
      status: 'warning',
    },
  ];

  const getPriorityColor = (priority) => {
    switch (priority) {
      case 'critical':
        return '#ff4d4f';
      case 'high':
        return '#faad14';
      case 'normal':
        return '#52c41a';
      default:
        return '#d9d9d9';
    }
  };

  const getPriorityText = (priority) => {
    switch (priority) {
      case 'critical':
        return 'Khẩn cấp';
      case 'high':
        return 'Cao';
      case 'normal':
        return 'Thường';
      default:
        return 'Không xác định';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'completed':
        return 'success';
      case 'processing':
        return 'processing';
      case 'pending':
        return 'warning';
      case 'cancelled':
        return 'error';
      default:
        return 'default';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'completed':
        return 'Hoàn thành';
      case 'processing':
        return 'Đang xử lý';
      case 'pending':
        return 'Chờ xử lý';
      case 'cancelled':
        return 'Đã hủy';
      default:
        return 'Không xác định';
    }
  };

  const getBloodStatusColor = (status) => {
    switch (status) {
      case 'sufficient':
        return '#52c41a';
      case 'low':
        return '#faad14';
      case 'critical':
        return '#ff4d4f';
      default:
        return '#d9d9d9';
    }
  };

  const getBloodStatusText = (status) => {
    switch (status) {
      case 'sufficient':
        return 'Đủ';
      case 'low':
        return 'Thấp';
      case 'critical':
        return 'Nguy hiểm';
      default:
        return 'Không xác định';
    }
  };

  const handleRefresh = () => {
    setLoading(true);
    setRefreshTime(new Date());
    loadTransfusions();
    setTimeout(() => {
      setLoading(false);
      notification.success({
        message: 'Đã cập nhật',
        description: 'Dữ liệu đã được làm mới thành công.',
      });
    }, 1000);
  };

  const handleExport = () => {
    notification.info({
      message: 'Đang xuất báo cáo',
      description: 'Báo cáo sẽ được tải xuống trong giây lát.',
    });
  };

  const handleRequestAction = (action, record) => {
    switch (action) {
      case 'approve':
        notification.success({
          message: 'Đã phê duyệt',
          description: `Yêu cầu ${record.id} đã được phê duyệt.`,
        });
        break;
      case 'reject':
        notification.error({
          message: 'Đã từ chối',
          description: `Yêu cầu ${record.id} đã bị từ chối.`,
        });
        break;
      case 'process':
        notification.info({
          message: 'Đang xử lý',
          description: `Yêu cầu ${record.id} đang được xử lý.`,
        });
        break;
      default:
        break;
    }
  };

  const requestColumns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 60,
    },
    {
      title: 'Bệnh nhân',
      dataIndex: 'patientName',
      key: 'patientName',
      render: (text, record) => (
        <Space>
          <Avatar icon={<UserOutlined />} size="small" />
          <div>
            <div>{text}</div>
            <Text type="secondary" style={{ fontSize: '12px' }}>
              {record.hospital}
            </Text>
          </div>
        </Space>
      ),
    },
    {
      title: 'Nhóm máu',
      dataIndex: 'bloodType',
      key: 'bloodType',
      render: (bloodType) => (
        <Tag color="red" style={{ fontWeight: 'bold' }}>
          {bloodType}
        </Tag>
      ),
    },
    {
      title: 'Số lượng',
      dataIndex: 'unitsNeeded',
      key: 'unitsNeeded',
      render: (units) => (
        <Text strong>{units} đơn vị</Text>
      ),
    },
    {
      title: 'Ưu tiên',
      dataIndex: 'priority',
      key: 'priority',
      render: (priority) => (
        <Badge
          color={getPriorityColor(priority)}
          text={getPriorityText(priority)}
        />
      ),
    },
    {
      title: 'Thời gian',
      dataIndex: 'requestTime',
      key: 'requestTime',
      render: (time) => (
        <Text type="secondary">{time}</Text>
      ),
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={getStatusColor(status)}>
          {getStatusText(status)}
        </Tag>
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Tooltip title="Xem chi tiết">
            <Button
              type="text"
              icon={<EyeOutlined />}
              onClick={() => {
                setSelectedRequest(record);
                setModalVisible(true);
              }}
            />
          </Tooltip>
          {record.status === 'pending' && (
            <>
              <Tooltip title="Phê duyệt">
                <Button
                  type="text"
                  icon={<CheckCircleOutlined />}
                  style={{ color: '#52c41a' }}
                  onClick={() => handleRequestAction('approve', record)}
                />
              </Tooltip>
              <Tooltip title="Từ chối">
                <Button
                  type="text"
                  icon={<ExclamationCircleOutlined />}
                  danger
                  onClick={() => handleRequestAction('reject', record)}
                />
              </Tooltip>
            </>
          )}
          {record.status === 'processing' && (
            <Tooltip title="Xử lý">
              <Button
                type="text"
                icon={<SyncOutlined />}
                onClick={() => handleRequestAction('process', record)}
              />
            </Tooltip>
          )}
        </Space>
      ),
    },
  ];

  const renderOverviewCards = () => (
    <Row gutter={[16, 16]}>
      <Col xs={24} sm={12} md={8} lg={6}>
        <Card>
          <Statistic
            title="Người hiến máu hôm nay"
            value={dashboardStats.donorsToday}
            prefix={<UserOutlined style={{ color: '#52c41a' }} />}
            suffix="người"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={6}>
        <Card>
          <Statistic
            title="Đơn vị máu tồn kho"
            value={dashboardStats.bloodUnits}
            prefix={<DropboxOutlined style={{ color: '#1890ff' }} />}
            suffix="đơn vị"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={6}>
        <Card>
          <Statistic
            title="Yêu cầu khẩn cấp"
            value={dashboardStats.urgentRequests}
            prefix={<FireOutlined style={{ color: '#ff4d4f' }} />}
            suffix="yêu cầu"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={6}>
        <Card>
          <Statistic
            title="Yêu cầu chờ xử lý"
            value={dashboardStats.pendingRequests}
            prefix={<ClockCircleOutlined style={{ color: '#faad14' }} />}
            suffix="yêu cầu"
          />
        </Card>
      </Col>
    </Row>
  );

  const renderBloodInventory = () => (
    <Card title="Tình trạng kho máu" size="small">
      <Row gutter={[16, 16]}>
        {bloodInventory.map((item, index) => (
          <Col xs={24} sm={12} md={8} lg={6} key={index}>
            <Card size="small" style={{ textAlign: 'center' }}>
              <div style={{ marginBottom: 8 }}>
                <Tag color="red" style={{ fontWeight: 'bold', fontSize: '16px' }}>
                  {item.bloodType}
                </Tag>
              </div>
              <Progress
                type="circle"
                percent={Math.round((item.available / item.required) * 100)}
                size={80}
                strokeColor={getBloodStatusColor(item.status)}
                format={() => item.available}
              />
              <div style={{ marginTop: 8 }}>
                <Text strong>{item.available}</Text>
                <Text type="secondary"> / {item.required}</Text>
              </div>
              <div style={{ marginTop: 4 }}>
                <Badge
                  color={getBloodStatusColor(item.status)}
                  text={getBloodStatusText(item.status)}
                />
              </div>
            </Card>
          </Col>
        ))}
      </Row>
    </Card>
  );

  const renderRecentRequests = () => (
    <Card
      title="Yêu cầu mới nhất"
      size="small"
      extra={
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => notification.info({ message: 'Thêm yêu cầu mới' })}
        >
          Thêm yêu cầu
        </Button>
      }
    >
      <Table
        columns={requestColumns}
        dataSource={recentRequests}
        rowKey="id"
        pagination={false}
        scroll={{ x: 800 }}
        loading={loading}
      />
    </Card>
  );

  const renderTodayActivities = () => (
    <Card title="Hoạt động hôm nay" size="small">
      <Timeline>
        {todayActivities.map((activity, index) => (
          <Timeline.Item
            key={index}
            color={
              activity.status === 'urgent' ? 'red' :
              activity.status === 'warning' ? 'orange' :
              activity.status === 'completed' ? 'green' : 'blue'
            }
          >
            <div>
              <Text strong>{activity.time}</Text>
              <br />
              <Text>{activity.description}</Text>
            </div>
          </Timeline.Item>
        ))}
      </Timeline>
    </Card>
  );

  const renderQuickStats = () => (
    <Row gutter={[16, 16]}>
      <Col xs={24} sm={12} md={8}>
        <Card>
          <Statistic
            title="Tổng số người hiến máu"
            value={dashboardStats.totalDonors}
            prefix={<TeamOutlined style={{ color: '#1890ff' }} />}
            suffix="người"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8}>
        <Card>
          <Statistic
            title="Ca truyền máu thành công"
            value={dashboardStats.successfulTransfusions}
            prefix={<CheckCircleOutlined style={{ color: '#52c41a' }} />}
            suffix="ca"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8}>
        <Card>
          <Statistic
            title="Thời gian phản hồi trung bình"
            value={dashboardStats.avgResponseTime}
            prefix={<ClockCircleOutlined style={{ color: '#faad14' }} />}
            suffix="phút"
          />
        </Card>
      </Col>
    </Row>
  );

  const renderRequestModal = () => (
    <Modal
      title="Chi tiết yêu cầu truyền máu"
      open={modalVisible}
      onCancel={() => setModalVisible(false)}
      width={700}
      footer={[
        <Button key="close" onClick={() => setModalVisible(false)}>
          Đóng
        </Button>,
        selectedRequest?.status === 'pending' && (
          <Button
            key="approve"
            type="primary"
            onClick={() => {
              handleRequestAction('approve', selectedRequest);
              setModalVisible(false);
            }}
          >
            Phê duyệt
          </Button>
        ),
      ]}
    >
      {selectedRequest && (
        <div>
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Alert
                message={`Yêu cầu ${getPriorityText(selectedRequest.priority).toLowerCase()}`}
                type={selectedRequest.priority === 'critical' ? 'error' : 
                      selectedRequest.priority === 'high' ? 'warning' : 'info'}
                showIcon
                style={{ marginBottom: 16 }}
              />
            </Col>
          </Row>
          
          <Row gutter={[16, 16]}>
            <Col span={12}>
              <Space direction="vertical" style={{ width: '100%' }}>
                <div>
                  <Text strong>Bệnh nhân:</Text>
                  <br />
                  <Text>{selectedRequest.patientName}</Text>
                </div>
                <div>
                  <Text strong>Nhóm máu:</Text>
                  <br />
                  <Tag color="red" style={{ fontWeight: 'bold' }}>
                    {selectedRequest.bloodType}
                  </Tag>
                </div>
                <div>
                  <Text strong>Số lượng cần:</Text>
                  <br />
                  <Text>{selectedRequest.unitsNeeded} đơn vị</Text>
                </div>
              </Space>
            </Col>
            <Col span={12}>
              <Space direction="vertical" style={{ width: '100%' }}>
                <div>
                  <Text strong>Bệnh viện:</Text>
                  <br />
                  <Text>{selectedRequest.hospital}</Text>
                </div>
                <div>
                  <Text strong>Thời gian yêu cầu:</Text>
                  <br />
                  <Text>{selectedRequest.requestTime}</Text>
                </div>
                <div>
                  <Text strong>Số điện thoại:</Text>
                  <br />
                  <Text>{selectedRequest.phone}</Text>
                </div>
              </Space>
            </Col>
          </Row>

          <Divider />

          <div>
            <Text strong>Ghi chú:</Text>
            <br />
            <Text>{selectedRequest.notes}</Text>
          </div>

          <Divider />

          <div>
            <Text strong>Trạng thái:</Text>
            <br />
            <Tag color={getStatusColor(selectedRequest.status)} style={{ marginTop: 4 }}>
              {getStatusText(selectedRequest.status)}
            </Tag>
          </div>
        </div>
      )}
    </Modal>
  );

  const tabItems = [
    {
      key: 'overview',
      label: 'Tổng quan',
      children: (
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          {renderOverviewCards()}
          <Row gutter={[16, 16]}>
            <Col xs={24} lg={16}>
              {renderBloodInventory()}
            </Col>
            <Col xs={24} lg={8}>
              {renderTodayActivities()}
            </Col>
          </Row>
          {renderQuickStats()}
        </Space>
      ),
    },
    {
      key: 'requests',
      label: 'Yêu cầu truyền máu',
      children: (
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          {renderRecentRequests()}
        </Space>
      ),
    },
    {
      key: 'inventory',
      label: 'Quản lý kho máu',
      children: (
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          {renderBloodInventory()}
          <Card title="Thống kê chi tiết kho máu" size="small">
            <Row gutter={[16, 16]}>
              <Col span={24}>
                <div style={{ padding: '20px' }}>
                  <Title level={5}>Tình trạng tồn kho theo nhóm máu</Title>
                  {bloodInventory.map((item, index) => (
                    <div key={index} style={{ marginBottom: 16 }}>
                      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
                        <div>
                          <Tag color="red" style={{ fontWeight: 'bold' }}>
                            {item.bloodType}
                          </Tag>
                          <Text>{item.available} / {item.required} đơn vị</Text>
                        </div>
                        <Badge
                          color={getBloodStatusColor(item.status)}
                          text={getBloodStatusText(item.status)}
                        />
                      </div>
                      <Progress
                        percent={Math.round((item.available / item.required) * 100)}
                        strokeColor={getBloodStatusColor(item.status)}
                        size="small"
                      />
                    </div>
                  ))}
                </div>
              </Col>
            </Row>
          </Card>
        </Space>
      ),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <DashboardOutlined style={{ marginRight: 8 }} />
              Tổng quan
            </Title>
          </Col>
          <Col>
            <Space>
              <Text type="secondary">
                Cập nhật lúc: <strong>{refreshTime.toLocaleTimeString()}</strong>
              </Text>
              <Button
                icon={<ReloadOutlined />}
                onClick={handleRefresh}
                loading={loading}
                type="text"
              />
            </Space>
          </Col>
        </Row>
      </Header>

      <Content style={{ padding: '24px' }}>
        <Alert
          message="Hệ thống đang hoạt động bình thường"
          description="Tất cả các dịch vụ và kết nối cơ sở dữ liệu đang hoạt động ổn định."
          type="success"
          showIcon
          style={{ marginBottom: 24 }}
        />

        {dashboardStats.urgentRequests > 0 && (
          <Alert
            message={`Có ${dashboardStats.urgentRequests} yêu cầu khẩn cấp cần xử lý`}
            description="Vui lòng kiểm tra và xử lý các yêu cầu khẩn cấp trong tab 'Yêu cầu truyền máu'."
            type="warning"
            showIcon
            style={{ marginBottom: 24 }}
            action={
              <Button
                size="small"
                type="primary"
                onClick={() => setActiveTab('requests')}
              >
                Xem ngay
              </Button>
            }
          />
        )}

        <Tabs activeKey={activeTab} onChange={setActiveTab} items={tabItems} />

        {renderRequestModal()}
      </Content>
    </Layout>
  );
};

export default AdminDashboard;