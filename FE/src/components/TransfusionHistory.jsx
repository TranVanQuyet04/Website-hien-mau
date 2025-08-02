import React, { useState, useEffect } from 'react';
import {
  Table,
  Tag,
  Button,
  Modal,
  Descriptions,
  Card,
  Input,
  Select,
  DatePicker,
  Space,
  Typography,
  Divider,
  Badge,
  Layout,
  Row,
  Col,
  message 
} from 'antd';
import {
  MedicineBoxOutlined,
  SearchOutlined,
  FilterOutlined,
  UserOutlined,
  CalendarOutlined,
  AlertOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  ClockCircleOutlined,
  StarOutlined,
  WarningOutlined
} from '@ant-design/icons';
import dayjs from 'dayjs';
import axios from 'axios';
const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;

const AdminBloodRequests = () => {
  const [selectedRecord, setSelectedRecord] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  const [dateRange, setDateRange] = useState(null);
  const [timePeriod, setTimePeriod] = useState('custom');

  // Cấu hình hiển thị trạng thái
  const statusConfig = {
    PENDING: { color: 'warning', text: 'ĐÃ HOÀN THÀNH', icon: <ExclamationCircleOutlined /> },
    APPROVED: { color: 'success', text: 'ĐÃ DUYỆT', icon: <CheckCircleOutlined /> },
    REJECTED: { color: 'error', text: 'TỪ CHỐI', icon: <ExclamationCircleOutlined /> },
    WAITING: { color: 'processing', text: 'CHỜ MÁU', icon: <ClockCircleOutlined /> }
  };

  // TODO: MOCK DATA -- XÓA KHI TÍCH HỢP API
  // Dữ liệu mẫu: Đơn từ staff gửi lên, mặc định là ĐÃ HOÀN THÀNH
  const [bloodRequests, setBloodRequests] = useState([]);
  const [loading, setLoading] = useState(false);
useEffect(() => {
  fetchBloodRequests();
}, []);

const fetchBloodRequests = async () => {
  try {
    setLoading(true);
    const token = localStorage.getItem("token");
    const res = await axios.get("http://localhost:8080/api/blood-requests/admin/requests/completed", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
const formatDateTime = (dateString) => {
  if (!dateString) return '';
  return dayjs(dateString).format('HH:mm DD/MM/YYYY');
};
const formatDate = (dateStr) => {
  if (!dateStr) return "--";
  return dayjs(dateStr).format("DD/MM/YYYY HH:mm");
};

const mapTriageToColor = {
  RED: { color: 'red', label: 'KHẨN CẤP' },
  YELLOW: { color: 'orange', label: 'GẤP' },
  GREEN: { color: 'green', label: 'BÌNH THƯỜNG' },
};

const mapStatusToTag = {
  PENDING: { color: 'warning', label: 'ĐÃ HOÀN THÀNH' },
  APPROVED: { color: 'success', label: 'ĐÃ DUYỆT' },
  REJECTED: { color: 'error', label: 'TỪ CHỐI' },
  COMPLETED: { color: 'cyan', label: 'ĐÃ HOÀN THÀNH' },
};
const bloodTypeMap = {
  1: 'A+',
  2: 'A-',
  3: 'B+',
  4: 'B-',
  5: 'AB+',
  6: 'AB-',
  7: 'O+',
  8: 'O-',
};
const bloodComponentMap = {
  3: 'Hồng cầu',
  2: 'Huyết tương',
  1: 'Tiểu cầu',
};

    // Chuẩn hoá dữ liệu
const mapped = res.data.map((item) => ({
        id: item.bloodRequestId,
        patientName: item.patientName || '—',
        bloodTypeName: item.bloodTypeName || 'Không rõ', // ✅ Thêm dòng này
        componentName: item.componentName || 'Không rõ', // ✅ Thêm dòng này
        age: item.patientAge || '—',
        volume: `${item.quantityMl}ml`,
        priority: mapTriageToColor[item.triageLevel]?.label || 'Không rõ',
        status: item.status,
        createdDate: formatDate(item.createdAt),
        requester: {
          name: `#${item.requesterId}`,
          phone: item.requesterPhone || '—',
        },
        reason: item.reason || '—',
        bagCount: item.quantityBag || 1,
        notes: {
          warning: item.warningNote || '',
          special: item.specialNote || '',
          emergency: item.emergencyNote || '',
        },
        processingTime: {
          requestTime: formatDateTime(item.createdAt),
          createdTime: formatDateTime(item.createdAt),
          approvedTime: item.approvedAt ? formatDateTime(item.approvedAt) : '',
          processingDuration: '—',
        },
        patientInfo: {
          weight: item.patientWeight || 0,
          phone: item.patientPhone || '',
          donnorId: item.patientRecordCode || '',
        },
      }));


    setBloodRequests(mapped);
  } catch (err) {
    console.error("❌ Lỗi khi tải danh sách yêu cầu máu:", err);
  } finally {
    setLoading(false);
  }
};

const approveBloodRequest = async (data) => {
  try {
    const token = localStorage.getItem("token");
    const res = await axios.put(
      "http://localhost:8080/api/blood-requests/admin/requests/completed",
      data,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    message.success("✅ Đã cập nhật trạng thái yêu cầu.");
    return res.data;
  } catch (err) {
    console.error("❌ Lỗi khi duyệt yêu cầu:", err);
    message.error("Lỗi khi duyệt yêu cầu.");
    return null;
  }
};

const handleApprove = async () => {
  const payload = {
  bloodRequestId: recordId,
  status: newStatus,
  approvedBy: currentUser.userId,
  confirmedVolumeMl: 400, // phải có, không được null
};


  const updated = await approveBloodRequest(payload);
  if (updated) {
    setModalVisible(false);
    fetchBloodRequests(); // load lại danh sách
  }
};


  // Hàm xử lý chọn khoảng thời gian nhanh
  const handleTimePeriodChange = (value) => {
    setTimePeriod(value);

    const now = dayjs();
    let startDate, endDate;

    switch (value) {
      case '1week':
        startDate = now.subtract(1, 'week');
        endDate = now;
        setDateRange([startDate, endDate]);
        break;
      case '1month':
        startDate = now.subtract(1, 'month');
        endDate = now;
        setDateRange([startDate, endDate]);
        break;
      case '1year':
        startDate = now.subtract(1, 'year');
        endDate = now;
        setDateRange([startDate, endDate]);
        break;
      case 'custom':
        setDateRange(null);
        break;
      default:
        setDateRange(null);
    }
  };

  // Hàm xử lý thay đổi date range picker
  const handleDateRangeChange = (dates) => {
    setDateRange(dates);
    if (dates) {
      setTimePeriod('custom');
    }
  };

  // Hàm reset filters
  const handleReset = () => {
    setSearchText('');
    setStatusFilter('all');
    setDateRange(null);
    setTimePeriod('custom');
  };

  // Hàm xử lý tìm kiếm
  const handleSearch = () => {
    console.log({
      searchText,
      statusFilter,
      dateRange,
      timePeriod
    });
    // Thực hiện logic tìm kiếm ở đây
  };

  // Hàm xử lý thay đổi trạng thái
const handleStatusChange = async (recordId, newStatus) => {
  const currentUser = JSON.parse(localStorage.getItem("user") || "{}");
  if (!recordId || !currentUser.userId) {
    message.error("Thiếu thông tin yêu cầu hoặc người duyệt.");
    return;
  }

  // ✅ Tìm bản ghi đang xử lý để lấy confirmedVolumeMl
  const record = bloodRequests.find(r => r.id === recordId);
  if (!record) {
    message.error("Không tìm thấy bản ghi yêu cầu máu.");
    return;
  }

  const payload = {
    bloodRequestId: recordId,
    status: newStatus,
    approvedBy: currentUser.userId,
    confirmedVolumeMl: record.quantityMl || parseInt(record.volume), // Ưu tiên dùng quantityMl nếu có
  };

  try {
    const updated = await approveBloodRequest(payload);
    if (!updated) return;

    const now = dayjs();

    // ✅ Cập nhật danh sách
    setBloodRequests(prevData =>
      prevData.map(r =>
        r.id === recordId
          ? {
              ...r,
              status: updated.status || newStatus,
              processingTime: {
                ...r.processingTime,
                approvedTime: updated.approvedAt
                  ? dayjs(updated.approvedAt).format("DD/MM/YYYY HH:mm")
                  : now.format("DD/MM/YYYY HH:mm"),
                processingDuration:
                  newStatus !== "PENDING"
                    ? now.diff(dayjs(r.processingTime.createdTime, "DD/MM/YYYY HH:mm"), "minute") + "m"
                    : "—",
              },
            }
          : r
      )
    );

    // ✅ Cập nhật modal nếu đang mở
    if (selectedRecord?.id === recordId) {
      setSelectedRecord(prev => ({
        ...prev,
        status: updated.status || newStatus,
        processingTime: {
          ...prev.processingTime,
          approvedTime: updated.approvedAt
            ? dayjs(updated.approvedAt).format("DD/MM/YYYY HH:mm")
            : now.format("DD/MM/YYYY HH:mm"),
          processingDuration:
            newStatus !== "PENDING"
              ? now.diff(dayjs(prev.processingTime.createdTime, "DD/MM/YYYY HH:mm"), "minute") + "m"
              : "—",
        },
      }));
    }

    message.success("✅ Đã cập nhật trạng thái thành công.");
  } catch (error) {
    console.error("❌ Lỗi khi cập nhật trạng thái:", error);
    message.error("Đã xảy ra lỗi khi cập nhật trạng thái.");
  }
};



  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 60,
      align: 'center',
    },
    {
      title: 'Bệnh nhân',
      dataIndex: 'patientName',
      key: 'patientName',
      width: 200,
      filteredValue: searchText ? [searchText] : null,
      onFilter: (value, record) =>
        record.patientName.toLowerCase().includes(value.toLowerCase()),
      render: (text) => (
        <div className="flex items-center">
          <UserOutlined className="mr-2 text-blue-500" />
          <Text strong>{text}</Text>
        </div>
      ),
    },
    {
      title: 'Nhóm máu',
      dataIndex: 'bloodTypeName',
      key: 'bloodTypeName',
      align: 'center',
      render: (text) => (
        <Tag color="red" className="font-semibold">
          {text || 'Không rõ'}
        </Tag>
      ),
    },
    {
          title: 'Thành phần máu',
          dataIndex: 'componentName',
          key: 'componentName',
          align: 'center',
          render: (text) => (
            <Tag color="blue" className="font-semibold">
              {text || 'Không rõ'}
            </Tag>
          ),
        },
    {
      title: 'Tuổi',
      dataIndex: 'age',
      key: 'age',
      width: 80,
      align: 'center',
    },
    {
      title: 'Lượng',
      dataIndex: 'volume',
      key: 'volume',
      width: 100,
      align: 'center',
      render: (text) => <Text strong className="text-red-600">{text}</Text>,
    },
    {
      title: 'Ưu tiên',
      dataIndex: 'priority',
      key: 'priority',
      width: 100,
      align: 'center',
      render: (priority) => {
        const config = {
          RED: { color: 'red', icon: <AlertOutlined />, text: 'KHẨN CẤP' },
          YELLOW: { color: 'orange', icon: <WarningOutlined />, text: 'GẤP' },
          GREEN: { color: 'green', icon: <CheckCircleOutlined />, text: 'BÌNH THƯỜNG' }
        };
        const { color, icon, text } = config[priority] || config.GREEN;
        return (
          <Badge status="processing" color={color}>
            <Tag color={color} icon={icon} className="font-semibold">
              {text}
            </Tag>
          </Badge>
        );
      },
    },
   {
  title: 'Trạng thái',
  dataIndex: 'status',
  key: 'status',
  width: 160,
  filteredValue: statusFilter === 'all' ? null : [statusFilter],
  onFilter: (value, record) => record.status === value,
  render: (status) => {
    const { color, text, icon } = statusConfig[status] || statusConfig.PENDING;
    return (
      <Tag color={color} icon={icon} className="font-semibold">
        {text}
      </Tag>
    );
  }
}
,
    {
      title: 'Ngày tạo',
      dataIndex: 'createdDate',
      key: 'createdDate',
      width: 120,
      render: (text) => (
        <div className="flex items-center">
          <CalendarOutlined className="mr-1 text-gray-500" />
          {text}
        </div>
      ),
    },
    // {
    //   title: 'Thao tác',
    //   key: 'action',
    //   width: 100,
    //   align: 'center',
    //   render: (_, record) => (
    //     <Tooltip title="Xem chi tiết">
    //       <Button
    //         type="primary"
    //         icon={<EyeOutlined />}
    //         size="small"
    //         onClick={() => handleViewDetail(record)}
    //         className="bg-blue-500 hover:bg-blue-600"
    //       />
    //     </Tooltip>
    //   ),
    // },
  ];

  const handleViewDetail = (record) => {
    setSelectedRecord(record);
    setModalVisible(true);
  };

  const handleCloseModal = () => {
    setModalVisible(false);
    setSelectedRecord(null);
  };

  // Hàm render footer của modal
 const renderModalFooter = () => {
  if (!selectedRecord) return null;

  const footerButtons = [];

  // Hàm tạo nút hành động nhanh gọn
  const createActionButton = (key, text, icon, status, type = 'default', danger = false, className = '') => (
    <Button
      key={key}
      type={type}
      danger={danger}
      icon={icon}
      onClick={() => handleStatusChange(selectedRecord.id, status)}
      className={className}
    >
      {text}
    </Button>
  );

  // Tuỳ theo trạng thái hiện tại
  switch (selectedRecord.status) {
    case 'PENDING':
      footerButtons.push(
        createActionButton('approve', 'Duyệt', <CheckCircleOutlined />, 'APPROVED', 'primary', false, 'bg-green-500 hover:bg-green-600 border-green-500'),
        createActionButton('waiting', 'Chờ máu', <ClockCircleOutlined />, 'WAITING', 'primary', false, 'bg-blue-500 hover:bg-blue-600 border-blue-500'),
        createActionButton('reject', 'Từ chối', <ExclamationCircleOutlined />, 'REJECTED', 'primary', true)
      );
      break;

    case 'WAITING':
      footerButtons.push(
        createActionButton('approve', 'Duyệt', <CheckCircleOutlined />, 'APPROVED', 'primary', false, 'bg-green-500 hover:bg-green-600 border-green-500'),
        createActionButton('reject', 'Từ chối', <ExclamationCircleOutlined />, 'REJECTED', 'primary', true)
      );
      break;

    // Có thể mở rộng cho COMPLETED, CANCELLED v.v. nếu cần
    default:
      break;
  }

  // Luôn có nút Đóng
  footerButtons.push(
    <Button key="close" onClick={handleCloseModal} className="ml-2">
      Đóng
    </Button>
  );

  return footerButtons;
};


  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <MedicineBoxOutlined style={{ marginRight: 8 }} />
              Lịch sử truyền máu
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
        <div className="bg-gray-50 min-h-screen">
      {/* Filters */}
      <Card className="mb-6 shadow-sm">
        <Row gutter={16} align="middle">
          <Col span={8}>
            <Input
              placeholder="Tìm kiếm theo tên bệnh nhân..."
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              allowClear
            />
          </Col>
          <Col span={4}>
            <Select
              placeholder="Lọc theo trạng thái"
              value={statusFilter}
              onChange={setStatusFilter}
              className="w-full"
              suffixIcon={<FilterOutlined />}
            >
              <Option value="all">Tất cả trạng thái</Option>
              <Option value="PENDING">Chờ duyệt</Option>
              <Option value="APPROVED">Đã duyệt</Option>
              <Option value="REJECTED">Từ chối</Option>
              <Option value="WAITING">Chờ máu</Option>
            </Select>
          </Col>
          <Col span={4}>
            <Select
              placeholder="Lọc theo thời gian"
              value={timePeriod}
              onChange={handleTimePeriodChange}
              className="w-full"
              suffixIcon={<CalendarOutlined />}
            >
              <Option value="custom">Tùy chỉnh</Option>
              <Option value="1week">1 tuần qua</Option>
              <Option value="1month">1 tháng qua</Option>
              <Option value="1year">1 năm qua</Option>
            </Select>
          </Col>
          <Col span={4}>
            <DatePicker placeholder="Chọn ngày" className="w-full" />
          </Col>
          <Col span={2}>
            <Button type="primary" icon={<SearchOutlined />} className="w-full">
              Tìm kiếm
            </Button>
          </Col>
        </Row>
        {/* Hàng thứ hai - Date Range Picker */}
        <Row gutter={16} align="middle" className="mt-2">
          <Col span={7}>
            <RangePicker
              placeholder={['Từ ngày', 'Đến ngày']}
              className="w-full"
              value={dateRange}
              onChange={handleDateRangeChange}
              format="DD/MM/YYYY"
              allowClear
            />
          </Col>
          <Col span={5}>
            <Button
              onClick={handleReset}
              className="w-full"
            >
              Đặt lại
            </Button>
          </Col>
        </Row>

        {/* Hiển thị khoảng thời gian đã chọn */}
        {dateRange && (
          <Row>
            <Col span={24}>
              <div className="text-sm text-gray-600 bg-blue-50 p-3 rounded-lg border-l-4 border-blue-400">
                <CalendarOutlined className="mr-2 text-blue-600" />
                <Text strong>Lọc từ ngày:</Text> {dateRange[0].format('DD/MM/YYYY')}
                <Text strong className="mx-2">đến</Text> {dateRange[1].format('DD/MM/YYYY')}
                {timePeriod !== 'custom' && (
                  <Tag color="blue" className="ml-3">
                    {timePeriod === '1week' ? '1 tuần qua' :
                      timePeriod === '1month' ? '1 tháng qua' :
                        timePeriod === '1year' ? '1 năm qua' : ''}
                  </Tag>
                )}
              </div>
            </Col>
          </Row>
        )}
      </Card>

     {/* Table */}
<Card className="shadow-sm">
  <Table
    columns={columns}
    dataSource={bloodRequests}  // ✅ Dữ liệu thật từ API
    rowKey="id"
    loading={loading}           // ✅ Hiển thị loading khi đang tải
    pagination={{
      total: bloodRequests.length,
      pageSize: 10,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total, range) =>
        `${range[0]}-${range[1]} trong ${total} bản ghi`,
    }}
    scroll={{ x: 1000 }}
    className="custom-table"
  />
</Card>


      {/* Detail Modal */}
      <Modal
        title={
          <div className="flex items-center">
            <Badge status="processing" color="green" />
            <Text strong className="text-lg ml-2">
              Yêu cầu #{selectedRecord?.id} - Trạng thái: {statusConfig[selectedRecord?.status]?.text || selectedRecord?.status}
            </Text>
          </div>
        }
        open={modalVisible}
        onCancel={handleCloseModal}
        footer={renderModalFooter()}
        width={800}
        className="detail-modal"
      >
        {selectedRecord && (
          <div className="space-y-6">
            {/* Request Header */}
            <Card size="small" className="bg-red-50 border-red-200">
              <Title level={4} className="text-red-700 mb-3">
                <AlertOutlined className="mr-2" />
                Yêu cầu máu khẩn cấp cho bệnh nhân {selectedRecord.patientName} ({selectedRecord.bloodType})
              </Title>
            </Card>

            {/* Request Details */}
            <Card title="Chi tiết yêu cầu" size="small">
              <Row gutter={[16, 16]}>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="Mã yêu cầu">
                      <Text strong>#{selectedRecord.id}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Trạng thái">
                      {(() => {
                        const { color, text, icon } = statusConfig[selectedRecord.status] || statusConfig.PENDING;
                        return (
                          <Tag color={color} icon={icon}>
                            {text}
                          </Tag>
                        );
                      })()}
                    </Descriptions.Item>
                    <Descriptions.Item label="Mức độ khẩn cấp">
                      <Tag color="red" icon={<AlertOutlined />}>
                        KHẨN CẤP
                      </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="Mức ưu tiên làm sàng">
                      <Badge status="error" text="RED" />
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="Lý do">
                      {selectedRecord.reason}
                    </Descriptions.Item>
                    <Descriptions.Item label="Số túi yêu cầu">
                      <Text strong>{selectedRecord.bagCount} túi (200ml)</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Thành phần máu">
                      {selectedRecord.bloodComponent}
                    </Descriptions.Item>
                    <Descriptions.Item label="Loại máu">
                      {selectedRecord.bloodType}
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
              </Row>

              <Divider />

              <Row gutter={16}>
                <Col span={12}>
                  <Text strong>Cần định nhóm chéo:</Text>
                  <Tag color="success" className="ml-2">Có</Tag>
                </Col>
                <Col span={12}>
                  <Text strong>Có phù hợp nhóm máu?:</Text>
                  <Tag color="success" className="ml-2">Có</Tag>
                </Col>
              </Row>
            </Card>

            {/* Requester Information */}
            <Card title="Thông tin người phụ trách" size="small">
              <Row gutter={[16, 16]}>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="Họ tên">
                      <Text strong>{selectedRecord.requester?.name}</Text>
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="SĐT người phụ trách">
                      <Text copyable className="text-blue-600">
                        {selectedRecord.requester?.phone}
                      </Text>
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
              </Row>
            </Card>

            {/* Patient Information */}
            <Card title={<><UserOutlined className="mr-2" />Thông tin bệnh nhân</>} size="small">
              <Row gutter={[16, 16]}>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="Họ tên">
                      <Text strong>{selectedRecord.patientName}</Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Tuổi/Giới tính">
                      {selectedRecord.age} / Nam
                    </Descriptions.Item>
                    <Descriptions.Item label="Cân nặng">
                      {selectedRecord.weight} kg
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
                <Col span={12}>
                  <Descriptions column={1} size="small">
                    <Descriptions.Item label="Nhóm máu">
                      <Tag color="red" className="font-semibold">
                        {selectedRecord.bloodType}
                      </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="SĐT bệnh nhân">
                      <Text copyable className="text-blue-600">
                        {selectedRecord.phone}
                      </Text>
                    </Descriptions.Item>
                    <Descriptions.Item label="Mã bệnh nhân">
                      <Text strong>#{selectedRecord.donnorId}</Text>
                    </Descriptions.Item>
                  </Descriptions>
                </Col>
              </Row>
            </Card>

            {/* Notes and Alerts */}
            <Card title="Ghi chú và tiền sử" size="small">
              <Space direction="vertical" className="w-full">
                <div>
                  <Tag color="orange" icon={<WarningOutlined />}>Warning Note:</Tag>
                  <Text className="ml-2">{selectedRecord.notes?.warning}</Text>
                </div>
                <div>
                  <Tag color="gold" icon={<StarOutlined />}>Special Note:</Tag>
                  <Text className="ml-2">{selectedRecord.notes?.special}</Text>
                </div>
                <div>
                  <Tag color="red" icon={<AlertOutlined />}>Emergency Note:</Tag>
                  <Text className="ml-2">{selectedRecord.notes?.emergency}</Text>
                </div>
              </Space>

              <Divider />

              <Row gutter={16}>
                <Col span={12}>
                  <Text strong>Tiền sử truyền máu:</Text>
                  <Tag color="red" className="ml-2">Không</Tag>
                </Col>
                <Col span={12}>
                  <Text strong>Phản ứng truyền máu:</Text>
                  <Tag color="red" className="ml-2">Không</Tag>
                </Col>
              </Row>
              <Row gutter={16} className="mt-2">
                <Col span={12}>
                  <Text strong>Kháng thể bất thường:</Text>
                  <Tag color="red" className="ml-2">Không</Tag>
                </Col>
                <Col span={12}>
                  <Text strong>Đang mang thai:</Text>
                  <Tag color="red" className="ml-2">Không</Tag>
                </Col>
              </Row>
            </Card>

            {/* Processing Timeline */}
            <Card title={<><CalendarOutlined className="mr-2" />Thời gian xử lý</>} size="small">
              <Descriptions column={1} size="small">
                <Descriptions.Item label="Thời điểm cấp máu">
                  {selectedRecord.processingTime?.requestTime}
                </Descriptions.Item>
                <Descriptions.Item label="Ngày tạo yêu cầu">
                  {selectedRecord.processingTime?.createdTime}
                </Descriptions.Item>
                <Descriptions.Item label="Ngày được phê duyệt">
                  {selectedRecord.processingTime?.approvedTime}
                </Descriptions.Item>
                <Descriptions.Item label="Thời gian xử lý">
                  <Text strong className="text-green-600">
                    {selectedRecord.processingTime?.processingDuration}
                  </Text>
                </Descriptions.Item>
              </Descriptions>
            </Card>
          </div>
        )}
      </Modal>

      <style jsx global>{`
        .custom-table .ant-table-thead > tr > th {
          background-color: #f8fafc;
          font-weight: 600;
          border-bottom: 2px solid #e2e8f0;
        }
        
        .custom-table .ant-table-tbody > tr:hover > td {
          background-color: #f1f5f9;
        }
        
        .detail-modal .ant-modal-header {
          background: linear-gradient(90deg, #f8fafc 0%, #e2e8f0 100%);
          border-bottom: 2px solid #cbd5e1;
        }
        
        .ant-descriptions-item-label {
          font-weight: 600;
          color: #374151;
        }
      `}</style>
</div>
      </Content>
    </Layout>  );
};

export default AdminBloodRequests;