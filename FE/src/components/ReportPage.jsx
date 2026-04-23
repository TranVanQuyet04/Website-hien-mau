import React from 'react';
import axios from 'axios';
import { useEffect, useState } from 'react';
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
  Layout
} from 'antd';
import {
  BarChartOutlined,
  SearchOutlined,
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
} from '@ant-design/icons';
import { apiUrl } from "../config/api";

const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;
const { RangePicker } = DatePicker;

const ReportPage = () => {
  const [loading, setLoading] = useState(false);
  const [selectedFilters, setSelectedFilters] = useState({
    dateRange: null,
    bloodType: null,
    donorType: null,
    readiness: null,
    hospital: null,
  });
  const token = localStorage.getItem('token');
  const [activeTab, setActiveTab] = useState('dashboard');
  const [selectedDonor, setSelectedDonor] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [inactiveUrgentCount, setInactiveUrgentCount] = useState(0);
  const [userProfileCount, setUserProfileCount] = useState(0);
  const [emergencyDonorCount, setEmergencyDonorCount] = useState(0);
  const [regularDonorCount, setRegularDonorCount] = useState(0);
  const [successfulDonationCount, setSuccessfulDonationCount] = useState(0);
  const [bloodTypeData, setBloodTypeData] = useState([]);
const [donorTypeData, setDonorTypeData] = useState([]);



useEffect(() => {
  const fetchUserProfiles = async () => {
    try {
      const response = await axios.get(apiUrl("api/userprofiles"), {
  headers: {
    Authorization: `Bearer ${token}`, // ✅ Thay token bằng giá trị thực
  }
});
      const countDonors = response.data.length;
      setUserProfileCount(countDonors);
    } catch (error) {
      console.error('Lỗi khi gọi API userprofiles:', error);
    }
  };

  fetchUserProfiles();
}, []);
useEffect(() => {
  const fetchEmergencyDonors = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(apiUrl("api/admin/urgent-donors/list"), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setEmergencyDonorCount(response.data.length); // hoặc response.data.total nếu có
    } catch (error) {
      console.error('Lỗi khi gọi API emergency donors:', error);
    }
  };

  fetchEmergencyDonors();
}, []);
useEffect(() => {
  const fetchRegularDonations = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(apiUrl("api/donation"), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setRegularDonorCount(response.data.length); // hoặc response.data.total nếu là object
    } catch (error) {
      console.error('Lỗi khi gọi API donation:', error);
    }
  };

  fetchRegularDonations();
}, []);
useEffect(() => {
  const fetchSuccessfulDonations = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(apiUrl("api/blood-requests/admin/requests/completed"), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setSuccessfulDonationCount(response.data.length); // hoặc .total nếu là object
    } catch (error) {
      console.error('Lỗi khi gọi API completed donations:', error);
    }
  };

  fetchSuccessfulDonations();
}, []);
useEffect(() => {
  const fetchBloodBags = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(apiUrl("api/blood-units"), {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const bags = response.data;

      // Map ID sang tên nhóm máu và thành phần máu
      const bloodTypeMap = {
        1: 'A+',
        2: 'A-',
        3: 'B+',
        4: 'B-',
        5: 'AB+',
        6: 'AB-',
        7: 'O+',
        8: 'O-',
        13: 'O+',
      };

      const donorTypeMap = {
        1: 'Tiểu cầu',
        2: 'Huyết tương',
        3: 'Hồng cầu',
      };

      const bloodTypeCounts = {};
      const donorTypeCounts = {};

     bags.forEach(bag => {
  if (bag.status !== 'AVAILABLE') return; // 👉 chỉ xử lý khi status là AVAILABLE

  const bloodTypeLabel = bloodTypeMap[bag.bloodTypeId];
  const componentLabel = donorTypeMap[bag.componentId];

  if (bloodTypeLabel) {
    bloodTypeCounts[bloodTypeLabel] = (bloodTypeCounts[bloodTypeLabel] || 0) + 1;
  }

  if (componentLabel) {
    donorTypeCounts[componentLabel] = (donorTypeCounts[componentLabel] || 0) + 1;
  }
});
      const bloodTypeDataFormatted = Object.entries(bloodTypeCounts).map(([bloodType, count]) => ({
        bloodType,
        count,
      }));

      const donorTypeDataFormatted = Object.entries(donorTypeCounts).map(([type, value]) => ({
        type,
        value,
      }));

      setBloodTypeData(bloodTypeDataFormatted);
      setDonorTypeData(donorTypeDataFormatted);

    } catch (error) {
      console.error('Lỗi khi gọi API blood-units:', error);
    }
  };

  fetchBloodBags();
}, []);


useEffect(() => {
  setKpiData(prev => ({
    ...prev,
    totalDonors: userProfileCount,
  }));
}, [userProfileCount]);

useEffect(() => {
  setKpiData(prev => ({
    ...prev,
    emergencyDonors: emergencyDonorCount,
  }));
}, [emergencyDonorCount]);
useEffect(() => {
  setKpiData(prev => ({
    ...prev,
    regularDonors: regularDonorCount,
  }));
}, [regularDonorCount]);
useEffect(() => {
  setKpiData(prev => ({
    ...prev,
    successfulDonations: successfulDonationCount,
  }));
}, [successfulDonationCount]);


  // Mock data
  
  const [kpiData, setKpiData] = useState({
  totalDonors: 0, // sẽ được gán lại
  emergencyDonors: 0,
  regularDonors: 0,
  responseRate: 14.5,
  avgResponseTime: 84,
  successfulDonations: 0,
});

  // const bloodTypeData = [
  //   { bloodType: 'O+', count: 213 },
  //   { bloodType: 'A+', count: 182 },
  //   { bloodType: 'B+', count: 140 },
  //   { bloodType: 'AB+', count: 92 },
  //   { bloodType: 'O-', count: 78 },
  //   { bloodType: 'A-', count: 65 },
  //   { bloodType: 'B-', count: 48 },
  //   { bloodType: 'AB-', count: 35 },
  // ];

  // const donorTypeData = [
  //   { type: 'Hồng cầu', value: 469 },
  //   { type: 'Tiểu cầu', value: 256 },
  //   { type: 'Huyết tương', value: 128 },
  // ];

  const getReadinessColor = (readiness) => {
    switch (readiness) {
      case 'NOW':
        return '#ff4d4f';
      case 'FLEXIBLE':
        return '#faad14';
      case 'REGULAR':
        return '#1890ff';
      default:
        return '#d9d9d9';
    }
  };

  const getReadinessText = (readiness) => {
    switch (readiness) {
      case 'NOW':
        return 'Khẩn cấp';
      case 'FLEXIBLE':
        return 'Linh hoạt';
      case 'REGULAR':
        return 'Thường quy';
      default:
        return 'Không xác định';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'Đã phục hồi':
        return 'success';
      case 'Đang chờ':
        return 'warning';
      case 'Không liên hệ':
        return 'error';
      default:
        return 'default';
    }
  };

  const columns = [
    {
      title: '#',
      dataIndex: 'id',
      key: 'id',
      width: 60,
    },
    {
      title: 'Họ tên',
      dataIndex: 'name',
      key: 'name',
      render: (text, record) => (
        <Space>
          <Avatar icon={<UserOutlined />} size="small" />
          {text}
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
      title: 'Thành phần',
      dataIndex: 'donorType',
      key: 'donorType',
      render: (type) => (
        <Tag color="blue">{type}</Tag>
      ),
    },
    {
      title: 'Sẵn sàng',
      dataIndex: 'readiness',
      key: 'readiness',
      render: (readiness) => (
        <Badge
          color={getReadinessColor(readiness)}
          text={getReadinessText(readiness)}
        />
      ),
    },
    {
      title: 'Lần hiến',
      dataIndex: 'donationCount',
      key: 'donationCount',
      render: (count) => (
        <Text strong>{count}</Text>
      ),
    },
    {
      title: 'Lần gần nhất',
      dataIndex: 'lastDonation',
      key: 'lastDonation',
      render: (date) => (
        <Text type="secondary">{date}</Text>
      ),
    },
    {
      title: 'Khoảng cách',
      dataIndex: 'distance',
      key: 'distance',
      render: (distance) => (
        <Text type="secondary">
          <EnvironmentOutlined /> {distance}
        </Text>
      ),
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={getStatusColor(status)}>
          {status}
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
                setSelectedDonor(record);
                setModalVisible(true);
              }}
            />
          </Tooltip>
          <Tooltip title="Chỉnh sửa">
            <Button type="text" icon={<EditOutlined />} />
          </Tooltip>
          <Tooltip title="Xóa">
            <Button type="text" danger icon={<DeleteOutlined />} />
          </Tooltip>
        </Space>
      ),
    },
  ];

  const handleFilterChange = (key, value) => {
    setSelectedFilters(prev => ({ ...prev, [key]: value }));
  };

  const handleRefresh = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
    }, 1000);
  };

  const handleExport = () => {
    alert('Đang xuất báo cáo...');
  };

  const renderKPICards = () => (
    <Row gutter={[16, 16]}>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Tổng số người hiến máu"
            value={kpiData.totalDonors}
            prefix={<TeamOutlined style={{ color: '#1890ff' }} />}
            suffix="người"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Số lượt hiến máu khẩn cấp"
            value={kpiData.emergencyDonors}
            prefix={<BellOutlined style={{ color: '#ff4d4f' }} />}
            suffix="lượt"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Số lượt hiến máu thường"
            value={kpiData.regularDonors}
            prefix={<HeartOutlined style={{ color: '#52c41a' }} />}
            suffix="lượt"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Tỉ lệ từ chối hiến máu"
            value={kpiData.responseRate}
            prefix={<TrophyOutlined style={{ color: '#faad14' }} />}
            suffix="%"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Trung bình thời gian phục hồi"
            value={kpiData.avgResponseTime}
            prefix={<ClockCircleOutlined style={{ color: '#722ed1' }} />}
            suffix="ngày"
          />
        </Card>
      </Col>
      <Col xs={24} sm={12} md={8} lg={4}>
        <Card>
          <Statistic
            title="Tổng số ca truyền máu thành công"
            value={kpiData.successfulDonations}
            prefix={<CheckCircleOutlined style={{ color: '#13c2c2' }} />}
            suffix="ca"
          />
        </Card>
      </Col>
    </Row>
  );

  const renderFilters = () => (
    <Card title="Bộ lọc dữ liệu" size="small" style={{ marginBottom: 16 }}>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} md={6}>
          <Text strong>Khoảng thời gian:</Text>
          <RangePicker
            style={{ width: '100%', marginTop: 4 }}
            placeholder={['Từ ngày', 'Đến ngày']}
            onChange={(dates) => handleFilterChange('dateRange', dates)}
          />
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Text strong>Nhóm máu:</Text>
          <Select
            style={{ width: '100%', marginTop: 4 }}
            placeholder="Tất cả"
            allowClear
            onChange={(value) => handleFilterChange('bloodType', value)}
          >
            <Option value="O+">O+</Option>
            <Option value="O-">O-</Option>
            <Option value="A+">A+</Option>
            <Option value="A-">A-</Option>
            <Option value="B+">B+</Option>
            <Option value="B-">B-</Option>
            <Option value="AB+">AB+</Option>
            <Option value="AB-">AB-</Option>
          </Select>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Text strong>Thành phần máu:</Text>
          <Select
            style={{ width: '100%', marginTop: 4 }}
            placeholder="Tất cả"
            allowClear
            onChange={(value) => handleFilterChange('donorType', value)}
          >
            <Option value="Hồng cầu">Hồng cầu</Option>
            <Option value="Tiểu cầu">Tiểu cầu</Option>
            <Option value="Huyết tương">Huyết tương</Option>
          </Select>
        </Col>
        <Col xs={24} sm={12} md={6}>
          <Text strong>Mức độ sẵn sàng:</Text>
          <Select
            style={{ width: '100%', marginTop: 4 }}
            placeholder="Tất cả"
            allowClear
            onChange={(value) => handleFilterChange('readiness', value)}
          >
            <Option value="NOW">Khẩn cấp</Option>
            <Option value="FLEXIBLE">Linh hoạt</Option>
            <Option value="REGULAR">Thường quy</Option>
          </Select>
        </Col>
      </Row>
      <Row style={{ marginTop: 16 }}>
        <Col>
          <Space>
            <Button
              icon={<SearchOutlined />}
              type="primary"
              onClick={() => alert('Đã áp dụng bộ lọc')}
            >
              Tìm kiếm
            </Button>
            <Button
              icon={<ReloadOutlined />}
              onClick={handleRefresh}
              loading={loading}
            >
              Làm mới
            </Button>
            {/* <Button
              icon={<DownloadOutlined />}
              onClick={handleExport}
            >
              Xuất báo cáo
            </Button> */}
          </Space>
        </Col>
      </Row>
    </Card>
  );

  // Simple chart replacements using CSS
  const renderSimpleBarChart = () => (
  <div style={{ padding: '20px' }}>
    <Title level={5} style={{ marginBottom: 20 }}>Thống kê nhóm máu</Title>

    {bloodTypeData.length === 0 ? (
      <Text type="secondary">Không có dữ liệu nhóm máu</Text>
    ) : (
      bloodTypeData.map((item, index) => (
        <div key={index} style={{ marginBottom: 12 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
            <Text strong>{item.bloodType}</Text>
            <Text>{item.count}</Text>
          </div>
          <div style={{
            width: '100%',
            height: 8,
            backgroundColor: '#f0f0f0',
            borderRadius: 4,
            overflow: 'hidden'
          }}>
            <div style={{
              width: `${(item.count / Math.max(...bloodTypeData.map(i => i.count), 1)) * 100}%`,
              height: '100%',
              backgroundColor: '#1890ff',
              borderRadius: 4
            }} />
          </div>
        </div>
      ))
    )}
  </div>
);

  const renderSimplePieChart = () => (
    <div style={{ padding: '20px' }}>
      <Title level={5} style={{ marginBottom: 20 }}>Thống kê thành phần máu</Title>
      {donorTypeData.map((item, index) => (
        <div key={index} style={{ 
          display: 'flex', 
          justifyContent: 'space-between', 
          alignItems: 'center',
          marginBottom: 12,
          padding: 8,
          backgroundColor: '#f5f5f5',
          borderRadius: 4
        }}>
          <Space>
            <div style={{ 
              width: 12, 
              height: 12, 
              backgroundColor: ['#ff4d4f', '#faad14', '#52c41a'][index],
              borderRadius: 2
            }} />
            <Text>{item.type}</Text>
          </Space>
          <Text strong>{item.value}</Text>
        </div>
      ))}
    </div>
  );

  const renderCharts = () => (
    <Row gutter={[16, 16]}>
      <Col xs={24} lg={12}>
        <Card title="Biểu đồ nhóm máu được hiến" size="small">
          {renderSimpleBarChart()}
        </Card>
      </Col>
      <Col xs={24} lg={12}>
        <Card title="Biểu đồ thành phần máu" size="small">
          {renderSimplePieChart()}
        </Card>
      </Col>
      <Col xs={24}>
        {/* <Card title="Thống kê theo mức độ sẵn sàng" size="small">
          <div style={{ padding: '20px' }}>
            <Row gutter={[16, 16]}>
              <Col span={8}>
                <Card style={{ textAlign: 'center', backgroundColor: '#fff2f0' }}>
                  <Statistic
                    title="Khẩn cấp"
                    value={158}
                    valueStyle={{ color: '#ff4d4f' }}
                    prefix={<BellOutlined />}
                  />
                </Card>
              </Col>
              <Col span={8}>
                <Card style={{ textAlign: 'center', backgroundColor: '#fffbe6' }}>
                  <Statistic
                    title="Linh hoạt"
                    value={313}
                    valueStyle={{ color: '#faad14' }}
                    prefix={<ClockCircleOutlined />}
                  />
                </Card>
              </Col>
              <Col span={8}>
                <Card style={{ textAlign: 'center', backgroundColor: '#f6ffed' }}>
                  <Statistic
                    title="Thường quy"
                    value={382}
                    valueStyle={{ color: '#52c41a' }}
                    prefix={<HeartOutlined />}
                  />
                </Card>
              </Col>
            </Row>
          </div>
        </Card> */}
      </Col>
    </Row>
  );

  const renderDataTable = () => (
    <Card
      title="Bảng dữ liệu chi tiết"
      size="small"
      extra={
        <Space>
          <Button
            icon={<PlusOutlined />}
            type="primary"
            onClick={() => alert('Thêm người hiến máu mới')}
          >
            Thêm mới
          </Button>
        </Space>
      }
    >
      <Table
        columns={columns}
        dataSource={donorData}
        rowKey="id"
        loading={loading}
        pagination={{
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total, range) =>
            `${range[0]}-${range[1]} của ${total} người hiến máu`,
        }}
        scroll={{ x: 1000 }}
      />
    </Card>
  );

  const renderDonorModal = () => (
    <Modal
      title="Thông tin chi tiết người hiến máu"
      open={modalVisible}
      onCancel={() => setModalVisible(false)}
      footer={[
        <Button key="close" onClick={() => setModalVisible(false)}>
          Đóng
        </Button>,
        <Button key="edit" type="primary">
          Chỉnh sửa
        </Button>,
      ]}
      width={600}
    >
      {selectedDonor && (
        <div>
          <Row gutter={[16, 16]}>
            <Col span={6}>
              <Avatar size={80} icon={<UserOutlined />} />
            </Col>
            <Col span={18}>
              <Title level={4}>{selectedDonor.name}</Title>
              <Space direction="vertical" size={4}>
                <Text>
                  <strong>Nhóm máu:</strong> 
                  <Tag color="red" style={{ marginLeft: 8 }}>
                    {selectedDonor.bloodType}
                  </Tag>
                </Text>
                <Text>
                  <strong>Số điện thoại:</strong> {selectedDonor.phone}
                </Text>
                <Text>
                  <strong>Địa chỉ:</strong> {selectedDonor.address}
                </Text>
              </Space>
            </Col>
          </Row>
          <Divider />
          <Row gutter={[16, 16]}>
            <Col span={12}>
              <Statistic
                title="Số lần hiến máu"
                value={selectedDonor.donationCount}
                prefix={<HeartOutlined />}
              />
            </Col>
            <Col span={12}>
              <Statistic
                title="Khoảng cách"
                value={selectedDonor.distance}
                prefix={<EnvironmentOutlined />}
              />
            </Col>
          </Row>
          <Divider />
          <Space direction="vertical" size={8} style={{ width: '100%' }}>
            <Text>
              <strong>Thành phần máu:</strong> 
              <Tag color="blue" style={{ marginLeft: 8 }}>
                {selectedDonor.donorType}
              </Tag>
            </Text>
            <Text>
              <strong>Mức độ sẵn sàng:</strong> 
              <Badge
                color={getReadinessColor(selectedDonor.readiness)}
                text={getReadinessText(selectedDonor.readiness)}
                style={{ marginLeft: 8 }}
              />
            </Text>
            <Text>
              <strong>Trạng thái:</strong> 
              <Tag color={getStatusColor(selectedDonor.status)} style={{ marginLeft: 8 }}>
                {selectedDonor.status}
              </Tag>
            </Text>
            <Text>
              <strong>Lần hiến gần nhất:</strong> {selectedDonor.lastDonation}
            </Text>
            <Text>
              <strong>Bệnh viện:</strong> {selectedDonor.hospital}
            </Text>
          </Space>
        </div>
      )}
    </Modal>
  );

  const tabItems = [
    {
      key: 'dashboard',
      label: 'Tổng quan',
      children: (
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          {renderKPICards()}
          {renderFilters()}
          {renderCharts()}
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
              <BarChartOutlined style={{ marginRight: 8 }} />
              Báo cáo & Thống kê
            </Title>
          </Col>
          <Col>
            <Space>
              <Text type="secondary">
                Cơ sở tiếp nhận: <strong>BV FPTU</strong>
              </Text>
              <Text type="secondary">
                Số cơ sở y tế tham gia: <strong>5 bệnh viện</strong>
              </Text>
            </Space>
          </Col>
        </Row>
      </Header>

      <Content style={{ padding: '24px' }}>
        <Alert
          message="Ghi chú: Số liệu dựa theo bộ lọc ở trên."
          type="info"
          showIcon
          style={{ marginBottom: 24 }}
        />

        <Tabs activeKey={activeTab} onChange={setActiveTab} items={tabItems} />

        {renderDonorModal()}
      </Content>
    </Layout>
  );
};

export default ReportPage;