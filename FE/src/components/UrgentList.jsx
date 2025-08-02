import React, { useState, useEffect } from 'react';
import { 
  Layout,
  Table, 
  Tag, 
  Button, 
  Modal, 
  Descriptions, 
  Space, 
  Input, 
  Select, 
  Card,
  Avatar,
  Typography,
  Badge,
  Tooltip,
  Alert,
  Divider,
  Progress,
  Row,message ,
  Col
} from 'antd';
import { 
  SearchOutlined, 
  PhoneOutlined, 
  UnorderedListOutlined,
  ClockCircleOutlined,
  UserOutlined,
  HeartOutlined,
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  CalendarOutlined,
  FireOutlined
} from '@ant-design/icons';
import axios from 'axios';

const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;

const UrgentList = () => {
  const [data, setData] = useState([]);  // Dữ liệu từ API
  const [selected, setSelected] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchText, setSearchText] = useState('');
  const [bloodTypeFilter, setBloodTypeFilter] = useState('all');
  const [levelFilter, setLevelFilter] = useState('all');
  const [filteredData, setFilteredData] = useState([]);
  const [loading, setLoading] = useState(false);

// Hàm chuẩn hóa địa chỉ từ các field detail
const getFullAddress = (donor) => {
  const detail = donor.detail || {};
  const parts = [
    detail.street || '',
    detail.ward || '',
    detail.district || '',
    detail.city || '',
  ];
  return parts.filter(Boolean).join(', ');
};


// Hàm fetch danh sách người hiến máu khẩn cấp và tính khoảng cách
const fetchUrgentDonors = async () => {
  try {
    setLoading(true);

    // B1: Gọi danh sách người hiến
    const res = await axios.get("http://localhost:8080/api/admin/urgent-donors/list");

    const enriched = await Promise.all(
      res.data.map(async (donor) => {
        // B2: Lấy profile theo donorId
        let profile = {};
        try {
          const profileRes = await axios.get(`http://localhost:8080/api/userprofiles/${donor.donorId}`);
          profile = profileRes.data;
        } catch (e) {
          console.warn(`⚠️ Không tìm thấy profile của donorId=${donor.donorId}`);
        }

        // B3: Tính khoảng cách theo profile.addressId
        const addressId = profile.addressId;
        const distanceKm = distanceMap[addressId?.toString()];
        const distanceText = distanceKm ? `${distanceKm} km` : '--';

        return {
          ...donor,
          profile,
          distanceToFpt: distanceText,
        };
      })
    );

    console.log("✅ Dữ liệu enriched:", enriched);
    setData(enriched);
  } catch (err) {
    console.error("❌ Lỗi khi lấy người hiến:", err);
    message.error("Không thể tải dữ liệu người hiến máu khẩn cấp.");
  } finally {
    setLoading(false);
  }
};

  useEffect(() => {
    fetchUrgentDonors();
  }, []);

  // Lọc dữ liệu khi thay đổi tìm kiếm hoặc filter hoặc data
  useEffect(() => {
    let filtered = data;

    if (searchText) {
      filtered = filtered.filter(item =>
        (item.fullName || '').toLowerCase().includes(searchText.toLowerCase()) ||
        (item.phone || '').includes(searchText) ||
        (item.location || '').toLowerCase().includes(searchText.toLowerCase())
      );
    }

    if (bloodTypeFilter !== 'all') {
      filtered = filtered.filter(item => item.bloodType === bloodTypeFilter);
    }

    if (levelFilter !== 'all') {
      filtered = filtered.filter(item => item.readinessLevel === levelFilter);
    }

    setFilteredData(filtered);
  }, [searchText, bloodTypeFilter, levelFilter, data]);

  // Các hàm helper
  const getLevelConfig = (level) => {
    const configs = {
      'URGENT': { color: 'red', text: 'Khẩn cấp', icon: <ExclamationCircleOutlined /> },
      'FLEXIBLE': { color: 'orange', text: 'Linh hoạt', icon: <ClockCircleOutlined /> }
    };
    return configs[level] || { color: 'gray', text: level || '', icon: null };
  };

  const getBloodTypeColor = (bloodType) => {
    const colors = {
      'A+': 'blue', 'A-': 'cyan', 'B+': 'green', 'B-': 'lime',
      'AB+': 'purple', 'AB-': 'magenta', 'O+': 'orange', 'O-': 'red'
    };
    return colors[bloodType] || 'default';
  };

  const calculateDaysSinceLastDonation = (lastDonate) => {
    if (!lastDonate || lastDonate === '--') return '--';
    const today = new Date();
    const lastDonateDate = new Date(lastDonate);
    const diffTime = Math.abs(today - lastDonateDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
  };

  const handleCall = (phone) => {
    window.open(`tel:${phone}`);
  };
  const distanceMap = {
  "1": 16,"2": 16,"3": 16,"4": 16,"5": 16,"6": 16,"7": 16,"8": 16,"9": 16,"10": 14,
  "11": 14,"12": 14,"13": 14,"14": 14,"15": 14,"16": 14,"17": 14,"18": 14,"19": 14,"20": 14,
  "21": 17,"22": 17,"23": 17,"24": 17,"25": 17,"26": 17,"27": 17,"28": 17,"29": 17,"30": 17,
  "31": 17,"32": 17,"33": 17,"34": 17,"35": 17,"36": 17,"37": 17,"38": 17,"39": 17,"40": 17,
  "41": 17,"42": 17,"43": 17,"44": 17,"45": 17,"46": 17,"47": 17,"48": 17,"49": 17,"50": 20,
  "51": 20,"52": 20,"53": 20,"54": 20,"55": 20,"56": 20,"57": 20,"58": 20,"59": 20,"60": 20,
  "61": 20,"62": 20,"63": 20,"64": 18,"65": 18,"66": 18,"67": 18,"68": 18,"69": 18,"70": 18,
  "71": 18,"72": 18,"73": 19,"74": 19,"75": 19,"76": 19,"77": 19,"78": 19,"79": 19,"80": 19,
  "81": 19,"82": 19,"83": 19,"84": 19,"85": 19,"86": 19,"87": 19,"88": 19,"89": 15,"90": 15,
  "91": 15,"92": 15,"93": 15,"94": 15,"95": 15, "96": 15,"97": 15,"98": 15,"99": 15,"100": 15,
  "101": 15,"102": 15,"103": 15,"104": 17,"105": 17,"106": 17,"107": 17,"108": 17,"109": 17,"110": 17,
  "111": 17,"112": 17,"113": 17,"114": 17,"115": 17,"116": 17,"117": 17,"118": 17,"119": 17,"120": 10,
  "121": 10,"122": 10,"123": 10,"124": 10,"125": 10,"126": 10,"127": 10,"128": 10,"129": 10,"130": 12,
  "131": 12,"132": 12,"133": 12,"134": 12,"135": 12,"136": 12,"137": 12,"138": 12,"139": 12,"140": 12,
  "141": 12,"142": 12,"143": 12,"144": 12,"145": 12,"146": 9,"147": 9,"148": 9,"149": 9,"150": 9,
  "151": 9,"152": 9,"153": 9,"154": 9,"155": 9,"156": 9,"157": 9,"158": 9,"159": 9,"160": 9,
  "161": 9,"162": 9,"163": 9,"164": 9,"165": 9,"166": 11,"167": 11,"168": 11,"169": 11,"170": 11,
  "171": 11,"172": 11,"173": 11,"174": 11,"175": 11,"176": 11,"177": 11,"178": 11,"179": 16,"180": 16,
  "181": 16,"182": 16,"183": 16,"184": 16,"185": 16,"186": 16,"187": 16,"188": 16,"189": 16,"190": 16,
  "191": 16,"192": 16,"193": 16,"194": 18,"195": 18,"196": 18,"197": 18,"198": 18,"199": 18,"200": 18,
  "201": 18,"202": 18,"203": 18,"204": 18,"205": 21,"206": 21,"207": 21,"208": 21,"209": 21,"210": 21,
  "211": 21,"212": 21,"213": 21,"214": 21,"215": 3,"216": 3,"217": 3,"218": 3,"219": 3,"220": 3,
  "221": 3,"222": 3,"223": 3,"224": 3,"225": 3,"226": 25,"227": 25,"228": 25,"229": 25,"230": 25,
  "231": 25,"232": 25,"233": 25,"234": 25,"235": 25,"236": 25,"237": 25,"238": 25,"239": 25,"240": 35,
  "241": 35,"242": 35, "243": 35,"244": 35,"245": 35,"246": 35,"247": 35,"248": 35,"249": 35,"250": 35,
  "251": 35,"252": 35,"253": 35,"254": 35,"255": 35,"256": 35,"257": 35,"258": 35,"259": 35,"260": 30,
  "261": 30,"262": 30,"263": 30,"264": 30,"265": 30,"266": 30,"267": 30,"268": 30,"269": 30,"270": 30,
  "271": 24,"272": 24,"273": 24,"274": 24,"275": 24,"276": 24,"277": 60,"278": 60,"279": 60,"280": 60,
  "281": 60,"282": 60,"283": 60
  };

 const columns = [
  {
    title: 'STT',
    width: 60,
    align: 'center',
    render: (_, __, index) => index + 1,
  },
  {
    title: 'Thông tin người hiến',
    dataIndex: 'fullName',
    render: (name, record) => (
      <Space>
        <Avatar 
          size="large" 
          icon={<UserOutlined />}
          style={{ backgroundColor: '#52c41a' }}
        />
        <div>
          <div style={{ fontWeight: 'bold' }}>{name}</div>
          <div style={{ fontSize: '12px', color: '#666' }}>
            <PhoneOutlined style={{ marginRight: 4 }} />
            {record.phone}
          </div>
        </div>
      </Space>
    ),
    width: 200,
  },
  {
    title: 'Nhóm máu',
    dataIndex: 'bloodType',
    render: (bloodType) => (
      <Tag color={getBloodTypeColor(bloodType)} style={{ fontSize: '14px', fontWeight: 'bold' }}>
        {bloodType}
      </Tag>
    ),
    width: 100,
    align: 'center',
  },
  {
    title: 'Mức sẵn sàng',
    dataIndex: 'readinessDescription',
    width: 150,
    align: 'center',
  },
  {
    title: 'Ngày hiến gần nhất',
    dataIndex: 'lastDonationDate',
    width: 120,
    align: 'center',
    render: (date) => date === '--' ? '--' : date,
  },
{
  title: 'Khoảng cách đến FPT',
  dataIndex: 'distanceToFpt',
  render: (distance) => (
    <Tooltip title={distance}>
      <div style={{
        maxWidth: '150px',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap'
      }}>
        {distance || '--'}
      </div>
    </Tooltip>
  ),
  width: 150,
}

,
  {
    title: 'Hành động',
    width: 100,
    align: 'center',
    render: (_, record) => (
      <Button
        type="primary"
        size="small"
        onClick={async () => {
          try {
            // Gọi API lấy chi tiết người hiến
            const res = await axios.get(`http://localhost:8080/api/urgent-donors/detail/${record.donorId}`);
            // Set dữ liệu chi tiết trả về
            setSelected(res.data);
            setIsModalOpen(true);
          } catch (error) {
            console.error("Lỗi khi lấy chi tiết:", error);
            message.error("Không thể tải chi tiết người hiến");
          }
        }}
      >
        Chi tiết
      </Button>
    ),
  },
];


  return (
  <Layout style={{ minHeight: '100vh' }}>
    <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
      <Row justify="space-between" align="middle">
        <Col>
          <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
            <UnorderedListOutlined style={{ marginRight: 8 }} />
            Danh sách người hiến máu khẩn cấp
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
      <Card>
        <Text type="secondary" style={{ display: 'block', marginBottom: '16px' }}>
          Cập nhật lần cuối: {new Date().toLocaleString('vi-VN')}
        </Text>

        <Alert
          message="Lưu ý quan trọng"
          description="Vui lòng xác minh thông tin sức khỏe trước khi liên hệ. Ưu tiên những người có mức sẵn sàng 'Khẩn cấp' và tình trạng sức khỏe tốt."
          type="warning"
          showIcon
          style={{ margin: '16px 0' }}
        />

        <div style={{ marginBottom: '16px' }}>
          <Space wrap>
            <Input
              placeholder="Tìm kiếm theo tên, số điện thoại, địa điểm..."
              prefix={<SearchOutlined />}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              style={{ width: 300 }}
            />
            <Select
              placeholder="Lọc theo nhóm máu"
              value={bloodTypeFilter}
              onChange={setBloodTypeFilter}
              style={{ width: 150 }}
            >
              <Option value="all">Tất cả nhóm máu</Option>
              <Option value="A+">A+</Option>
              <Option value="A-">A-</Option>
              <Option value="B+">B+</Option>
              <Option value="B-">B-</Option>
              <Option value="AB+">AB+</Option>
              <Option value="AB-">AB-</Option>
              <Option value="O+">O+</Option>
              <Option value="O-">O-</Option>
            </Select>
            <Select
              placeholder="Lọc theo mức độ"
              value={levelFilter}
              onChange={setLevelFilter}
              style={{ width: 150 }}
            >
              <Option value="all">Tất cả mức độ</Option>
              <Option value="URGENT">Khẩn cấp</Option>
              <Option value="FLEXIBLE">Linh hoạt</Option>
            </Select>
          </Space>
        </div>

        <Table 
          columns={columns} 
          dataSource={filteredData} 
          loading={loading}
          pagination={{
            showSizeChanger: true,
            showQuickJumper: true,
            showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} người hiến`,
            pageSizeOptions: ['5', '10', '20', '50'],
            defaultPageSize: 10,
          }}
          scroll={{ x: 1200 }}
          size="middle"
          rowKey="donorId"
        />

        {selected && (
          <Modal
  open={isModalOpen}
  title={
    <Space>
      <Avatar 
        size="large" 
        icon={<UserOutlined />}
        style={{ backgroundColor: selected.verified ? '#52c41a' : '#faad14' }}
      />
      <div>
        <div style={{ fontSize: '18px', fontWeight: 'bold' }}>
          {selected.fullName}
        </div>
        <div style={{ fontSize: '12px', color: '#666' }}>
          {selected.verified ? (
            <Badge status="success" text="Đã xác minh" />
          ) : (
            <Badge status="warning" text="Chưa xác minh" />
          )}
        </div>
      </div>
    </Space>
  }
  onCancel={() => setIsModalOpen(false)}
  width={700}
  footer={[
    <Button key="cancel" onClick={() => setIsModalOpen(false)}>
      Đóng
    </Button>,
    <Button 
      key="call" 
      type="primary" 
      icon={<PhoneOutlined />}
      onClick={() => handleCall(selected.phone)}
    >
      Gọi ngay: {selected.phone}
    </Button>,
  ]}
>
  <div style={{ padding: '16px 0' }}>
    <Alert
      message={`Mức sẵn sàng: ${getLevelConfig(selected.readinessLevel).text}`}
      description={`Thời gian phản hồi dự kiến: ${selected.readinessDescription || ''}`}
      type={selected.readinessLevel === 'URGENT' ? 'error' : 'warning'}
      showIcon
      style={{ marginBottom: '16px' }}
    />

    <Descriptions column={2} bordered size="small">
      <Descriptions.Item label="Họ tên" span={2}>
        <strong>{selected.fullName}</strong>
      </Descriptions.Item>
      <Descriptions.Item label="Ngày sinh">
        {selected.dateOfBirth || selected.detail?.dob || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Giới tính">
        {selected.gender || selected.detail?.gender || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Số CCCD">
        {selected.citizenId || selected.detail?.cccd || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Nghề nghiệp">
        {selected.occupation || selected.detail?.job || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Cân nặng">
        {selected.detail?.weight || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Chiều cao">
        {selected.detail?.height || '--'}
      </Descriptions.Item>
    </Descriptions>

    <Divider orientation="left">Thông tin liên hệ</Divider>
    <Descriptions column={1} bordered size="small">
      <Descriptions.Item label="Số điện thoại">
        <Space>
          {selected.phone}
          <Button 
            type="link" 
            icon={<PhoneOutlined />}
            onClick={() => handleCall(selected.phone)}
          >
            Gọi
          </Button>
        </Space>
      </Descriptions.Item>
      <Descriptions.Item label="Email">
        {selected.email || selected.detail?.email || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Địa chỉ">
        {selected.address || selected.detail?.address || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Liên hệ khẩn cấp">
        {selected.detail?.emergencyContact || '--'}
      </Descriptions.Item>
    </Descriptions>

    <Divider orientation="left">Thông tin y tế</Divider>
    <Descriptions column={2} bordered size="small">
      <Descriptions.Item label="Nhóm máu">
        <Tag color={getBloodTypeColor(selected.bloodType)} style={{ fontSize: '14px' }}>
          {selected.bloodType}
        </Tag>
      </Descriptions.Item>
      <Descriptions.Item label="Lần hiến gần nhất">
        {selected.lastDonationDate || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Tình trạng hồi phục">
        <Progress
          percent={selected.recovery || 0}
          size="small"
          status={selected.recovery >= 90 ? 'success' : selected.recovery >= 70 ? 'active' : 'exception'}
        />
      </Descriptions.Item>
      <Descriptions.Item label="Số lần hiến">
        <Badge count={selected.donationCount || 0} style={{ backgroundColor: '#52c41a' }} />
      </Descriptions.Item>
      <Descriptions.Item label="Tiền sử bệnh" span={2}>
        {selected.detail?.medicalHistory || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Khám sức khỏe gần nhất">
        {selected.detail?.lastHealthCheck || '--'}
      </Descriptions.Item>
      <Descriptions.Item label="Vị trí hiện tại">
        {(selected.location || '--') + (selected.distance ? ` (Cách ${selected.distance})` : '')}
      </Descriptions.Item>
    </Descriptions>

    <Divider orientation="left">Ghi chú</Divider>
    <div style={{ 
      padding: '12px', 
      backgroundColor: '#f5f5f5', 
      borderRadius: '6px',
      border: '1px solid #d9d9d9'
    }}>
      {selected.note || '--'}
    </div>
  </div>
</Modal>

        )}
      </Card>
    </Content>
  </Layout>
);

};

export default UrgentList;
