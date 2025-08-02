import React, { useState, useEffect } from 'react';
import {
  Table,
  Input,
  Button,
  Modal,
  Descriptions,
  Empty,
  DatePicker,
  Space,
  Card,
  Typography,
  Tag,
  Select,
  Row,
  Col,
  Statistic,
  Alert,
  Spin,
  message,
  Tooltip,
  Avatar,
  Badge,
} from 'antd';
import {
  SearchOutlined,
  FilterOutlined,
  EyeOutlined,
  ReloadOutlined,
  UserOutlined,
  CalendarOutlined,
  EnvironmentOutlined,
  HeartOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons';
import dayjs from 'dayjs';
import axios from 'axios';
const { RangePicker } = DatePicker;
const { Title, Text } = Typography;
const { Option } = Select;

// API service functions
const donationService = {
  // Get donation history by user ID
  async getHistoryByUserId(userId) {
    try {
      const response = await fetch(`/api/donations/history?userId=${userId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch donation history');
      }

      const data = await response.json();
      return data.data || [];
    } catch (error) {
      console.error('Error fetching donation history:', error);
      throw error;
    }
  },

  // Get all donations for staff overview
  async getAllDonations() {
    try {
      const response = await fetch('/api/donations', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch donations');
      }

      const data = await response.json();
      return data || [];
    } catch (error) {
      console.error('Error fetching donations:', error);
      throw error;
    }
  },

  // Get donations by user ID
  async getDonationsByUserId(userId) {
    try {
      const response = await fetch(`/api/donations/by-user?userId=${userId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch user donations');
      }

      const data = await response.json();
      return data || [];
    } catch (error) {
      console.error('Error fetching user donations:', error);
      throw error;
    }
  }
};

const StaffDonationHistory = () => {
  const [loading, setLoading] = useState(false);
  const [donations, setDonations] = useState([]);
  const [filteredDonations, setFilteredDonations] = useState([]);
  const [selectedRecord, setSelectedRecord] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [dateRange, setDateRange] = useState([]);
  const [statusFilter, setStatusFilter] = useState('all');
  const [locationFilter, setLocationFilter] = useState('all');
  const [statistics, setStatistics] = useState({
    total: 0,
    completed: 0,
    pending: 0,
    cancelled: 0
  });

  // Load data on component mount
  useEffect(() => {
    loadDonations();
  }, []);

  // Filter donations when filters change
  useEffect(() => {
    applyFilters();
  }, [donations, searchText, dateRange, statusFilter, locationFilter]);

  const loadDonations = async () => {
    setLoading(true);
    try {
      const data = await donationService.getAllDonations();

      // Transform data to match table structure
      const transformedData = data.map((donation, index) => ({
        key: donation.donationId || index + 1,
        id: donation.donationId,
        userId: donation.userId,
        name: donation.user?.fullName || 'Chưa có thông tin',
        bloodType: donation.bloodType?.description || 'Chưa xác định',
        status: donation.status || 'PENDING',
        location: donation.location || 'Chưa rõ',
        phone: donation.user?.phone || 'Chưa có SĐT',
        donationDate: donation.donationDate,
        volume: donation.volumeMl,
        component: donation.component?.name || 'Chưa tách',
        note: donation.note || '',
        registration: donation.registration,
        separationStatus: donation.component?.code ? 'Đã tách' : 'Chưa tách'
      }));

      setDonations(transformedData);
      calculateStatistics(transformedData);
    } catch (error) {
      message.error('Không thể tải dữ liệu lịch sử hiến máu');
    } finally {
      setLoading(false);
    }
  };

  const calculateStatistics = (data) => {
    const stats = {
      total: data.length,
      completed: data.filter(d => d.status === 'COMPLETED').length,
      pending: data.filter(d => d.status === 'PENDING').length,
      cancelled: data.filter(d => d.status === 'CANCELLED').length
    };
    setStatistics(stats);
  };

  const applyFilters = () => {
    let filtered = [...donations];

    // Search filter
    if (searchText) {
      filtered = filtered.filter(item =>
        item.name.toLowerCase().includes(searchText.toLowerCase()) ||
        item.location.toLowerCase().includes(searchText.toLowerCase()) ||
        item.phone.includes(searchText)
      );
    }

    // Date range filter
    if (dateRange.length === 2) {
      filtered = filtered.filter(item => {
        const itemDate = dayjs(item.donationDate);
        return itemDate.isAfter(dateRange[0]) && itemDate.isBefore(dateRange[1]);
      });
    }

    // Status filter
    if (statusFilter !== 'all') {
      filtered = filtered.filter(item => item.status === statusFilter);
    }

    // Location filter
    if (locationFilter !== 'all') {
      filtered = filtered.filter(item =>
        item.location.toLowerCase().includes(locationFilter.toLowerCase())
      );
    }

    setFilteredDonations(filtered);
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'COMPLETED': return 'success';
      case 'PENDING': return 'processing';
      case 'CANCELLED': return 'error';
      default: return 'default';
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 'COMPLETED': return 'Đã hoàn thành';
      case 'PENDING': return 'Đang xử lý';
      case 'CANCELLED': return 'Đã hủy';
      default: return 'Không rõ';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'COMPLETED': return <CheckCircleOutlined />;
      case 'PENDING': return <ClockCircleOutlined />;
      case 'CANCELLED': return <CloseCircleOutlined />;
      default: return <ExclamationCircleOutlined />;
    }
  };

  const showDetail = async (record) => {
    if (record.userId) {
      setLoading(true);
      try {
        const history = await donationService.getHistoryByUserId(record.userId);
        setSelectedRecord({
          ...record,
          history: history
        });
        setIsModalOpen(true);
      } catch (error) {
        message.error('Không thể tải chi tiết lịch sử hiến máu');
      } finally {
        setLoading(false);
      }
    } else {
      setSelectedRecord(record);
      setIsModalOpen(true);
    }
  };

  const resetFilters = () => {
    setSearchText('');
    setDateRange([]);
    setStatusFilter('all');
    setLocationFilter('all');
  };

  const columns = [
    {
      title: 'STT',
      dataIndex: 'key',
      width: 60,
      align: 'center',
    },
    {
      title: 'Người hiến',
      dataIndex: 'name',
      width: 150,
      render: (text, record) => (
        <Space>
          <Avatar size="small" icon={<UserOutlined />} />
          <div>
            <Text strong>{text}</Text>
            <br />
            <Text type="secondary" style={{ fontSize: 12 }}>{record.phone}</Text>
          </div>
        </Space>
      ),
    },
    {
      title: 'Nhóm máu',
      dataIndex: 'bloodType',
      width: 100,
      align: 'center',
      render: (text) => (
        <Tag color="red" icon={<DropletOutlined />}>
          {text}
        </Tag>
      ),
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      width: 120,
      align: 'center',
      render: (status) => (
        <Tag color={getStatusColor(status)} icon={getStatusIcon(status)}>
          {getStatusText(status)}
        </Tag>
      ),
    },
    {
      title: 'Địa điểm',
      dataIndex: 'location',
      width: 180,
      render: (text) => (
        <Space>
          <EnvironmentOutlined style={{ color: '#666' }} />
          <Text>{text}</Text>
        </Space>
      ),
    },
    {
      title: 'Thời gian hiến',
      dataIndex: 'donationDate',
      width: 150,
      render: (date) => (
        <Space>
          <CalendarOutlined style={{ color: '#666' }} />
          <Text>{date ? dayjs(date).format('DD/MM/YYYY HH:mm') : 'Chưa có'}</Text>
        </Space>
      ),
    },
    {
      title: 'Thể tích',
      dataIndex: 'volume',
      width: 100,
      align: 'center',
      render: (volume) => (
        <Badge
          count={volume ? `${volume}ml` : 'Chưa có'}
          color={volume ? '#52c41a' : '#d9d9d9'}
        />
      ),
    },
    {
      title: 'Thành phần',
      dataIndex: 'component',
      width: 120,
      render: (text) => (
        <Text type={text === 'Chưa tách' ? 'secondary' : 'primary'}>
          {text}
        </Text>
      ),
    },
    {
      title: 'Hành động',
      width: 100,
      align: 'center',
      render: (_, record) => (
        <Tooltip title="Xem chi tiết">
          <Button
            type="primary"
            ghost
            icon={<EyeOutlined />}
            onClick={() => showDetail(record)}
            size="small"
          />
        </Tooltip>
      ),
    },
  ];

  return (
    <div style={{ padding: '0' }}>
      {/* Statistics Cards */}
      <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Tổng số lần hiến"
              value={statistics.total}
              prefix={<HeartOutlined style={{ color: '#1890ff' }} />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Đã hoàn thành"
              value={statistics.completed}
              prefix={<CheckCircleOutlined style={{ color: '#52c41a' }} />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Đang xử lý"
              value={statistics.pending}
              prefix={<ClockCircleOutlined style={{ color: '#faad14' }} />}
              valueStyle={{ color: '#faad14' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Card>
            <Statistic
              title="Đã hủy"
              value={statistics.cancelled}
              prefix={<CloseCircleOutlined style={{ color: '#f5222d' }} />}
              valueStyle={{ color: '#f5222d' }}
            />
          </Card>
        </Col>
      </Row>

      {/* Filter Section */}
      <Card style={{ marginBottom: 24 }}>
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12} md={6}>
            <Input
              placeholder="Tìm theo tên, địa điểm, SĐT..."
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              allowClear
            />
          </Col>
          <Col xs={24} sm={12} md={6}>
            <RangePicker
              value={dateRange}
              onChange={setDateRange}
              placeholder={['Từ ngày', 'Đến ngày']}
              style={{ width: '100%' }}
            />
          </Col>
          <Col xs={24} sm={12} md={4}>
            <Select
              value={statusFilter}
              onChange={setStatusFilter}
              style={{ width: '100%' }}
              placeholder="Trạng thái"
            >
              <Option value="all">Tất cả trạng thái</Option>
              <Option value="COMPLETED">Đã hoàn thành</Option>
              <Option value="PENDING">Đang xử lý</Option>
              <Option value="CANCELLED">Đã hủy</Option>
            </Select>
          </Col>
          <Col xs={24} sm={12} md={4}>
            <Select
              value={locationFilter}
              onChange={setLocationFilter}
              style={{ width: '100%' }}
              placeholder="Khu vực"
            >
              <Option value="all">Tất cả khu vực</Option>
              <Option value="quận 1">Quận 1</Option>
              <Option value="quận 3">Quận 3</Option>
              <Option value="quận 10">Quận 10</Option>
            </Select>
          </Col>
          <Col xs={24} sm={24} md={4}>
            <Space>
              <Button
                type="primary"
                icon={<FilterOutlined />}
                onClick={applyFilters}
              >
                Lọc
              </Button>
              <Button
                icon={<ReloadOutlined />}
                onClick={() => {
                  resetFilters();
                  loadDonations();
                }}
              >
                Reset
              </Button>
            </Space>
          </Col>
        </Row>
      </Card>

      {/* Main Table */}
      <Card>
        <div style={{ marginBottom: 16 }}>
          <Title level={4} style={{ margin: 0 }}>
            Danh sách lịch sử hiến máu
          </Title>
          <Text type="secondary">
            Hiển thị {filteredDonations.length} / {donations.length} bản ghi
          </Text>
        </div>

        <Spin spinning={loading}>
          {filteredDonations.length > 0 ? (
            <Table
              columns={columns}
              dataSource={filteredDonations}
              pagination={{
                total: filteredDonations.length,
                pageSize: 10,
                showSizeChanger: true,
                showQuickJumper: true,
                showTotal: (total, range) =>
                  `${range[0]}-${range[1]} trong ${total} bản ghi`,
              }}
              scroll={{ x: 1200 }}
              size="middle"
            />
          ) : (
            <Empty
              description={
                <div>
                  <p>Không tìm thấy lịch sử hiến máu</p>
                  <Button type="primary" onClick={loadDonations}>
                    Tải lại dữ liệu
                  </Button>
                </div>
              }
            />
          )}
        </Spin>
      </Card>

      {/* Detail Modal */}
      {selectedRecord && (
        <Modal
          open={isModalOpen}
          title={
            <Space>
              <HeartOutlined style={{ color: '#dc2626' }} />
              <span>Chi tiết lần hiến máu - {selectedRecord.name}</span>
            </Space>
          }
          onCancel={() => setIsModalOpen(false)}
          footer={null}
          width={800}
        >
          <Descriptions column={2} bordered size="small">
            <Descriptions.Item label="Người hiến máu" span={2}>
              <Space>
                <Avatar icon={<UserOutlined />} />
                <div>
                  <Text strong>{selectedRecord.name}</Text>
                  <br />
                  <Text type="secondary">{selectedRecord.phone}</Text>
                </div>
              </Space>
            </Descriptions.Item>
            <Descriptions.Item label="Ngày hiến">
              {selectedRecord.donationDate
                ? dayjs(selectedRecord.donationDate).format('DD/MM/YYYY HH:mm')
                : 'Chưa có thông tin'
              }
            </Descriptions.Item>
            <Descriptions.Item label="Địa điểm">
              {selectedRecord.location}
            </Descriptions.Item>
            <Descriptions.Item label="Thể tích">
              <Badge
                count={selectedRecord.volume ? `${selectedRecord.volume}ml` : 'Chưa có'}
                color="#52c41a"
              />
            </Descriptions.Item>
            <Descriptions.Item label="Nhóm máu">
              <Tag color="red" icon={<DropletOutlined />}>
                {selectedRecord.bloodType}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="Thành phần">
              {selectedRecord.component}
            </Descriptions.Item>
            <Descriptions.Item label="Trạng thái tách">
              <Tag color={selectedRecord.separationStatus === 'Đã tách' ? 'green' : 'default'}>
                {selectedRecord.separationStatus}
              </Tag>
            </Descriptions.Item>
            <Descriptions.Item label="Ghi chú" span={2}>
              {selectedRecord.note || 'Không có ghi chú'}
            </Descriptions.Item>
          </Descriptions>

          {selectedRecord.history && (
            <>
              <Divider />
              <Title level={5}>Lịch sử hiến máu khác</Title>
              <ul style={{ paddingLeft: 20 }}>
                {selectedRecord.history
                  .filter(h => h.donationId !== selectedRecord.id)
                  .map((item, index) => (
                    <li key={index}>
                      {dayjs(item.donationDate).format('DD/MM/YYYY')} - {item.volumeMl}ml - {item.component?.name || 'Chưa tách'}
                    </li>
                  ))}
              </ul>
            </>
          )}
        </Modal>
      )}
    </div>
  );
};

export default StaffDonationHistory;