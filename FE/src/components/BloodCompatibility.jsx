import React, { useState, useEffect } from 'react';
import { 
  Layout, 
  Card, 
  Select, 
  Button, 
  Table, 
  Tag, 
  Space, 
  Alert, 
  Typography, 
  Row, 
  Col, 
  Divider, 
  Badge,
  Tooltip,
  Modal,
  Form,
  Input,
  DatePicker,
  Switch,
  message,
  Statistic,
  Progress,
  Timeline,
  Tabs,
  Steps,
  Result,
  Descriptions,
  List,
  Avatar,
  Checkbox
} from 'antd';
import { 
  HeartOutlined, 
  ShareAltOutlined, 
  CheckCircleOutlined, 
  CloseCircleOutlined, 
  ExclamationCircleOutlined,
  SearchOutlined,
  UserOutlined,
  MedicineBoxOutlined,
  WarningOutlined,
  SafetyOutlined,
  FileTextOutlined,
  ClockCircleOutlined,
  TeamOutlined,
  BarChartOutlined,
  FilterOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  EyeOutlined,
  PrinterOutlined,
  DownloadOutlined,
  ReloadOutlined,
  TableOutlined
} from '@ant-design/icons';

const { Header, Content, Sider } = Layout;
const { Title, Text, Paragraph } = Typography;
const { Option } = Select;
const { Step } = Steps;
const { TabPane } = Tabs;

const BloodCompatibility = () => {
  const [donorBlood, setDonorBlood] = useState('');
  const [donorRh, setDonorRh] = useState('');
  const [donorComponent, setDonorComponent] = useState('');
  const [recipientBlood, setRecipientBlood] = useState('');
  const [recipientRh, setRecipientRh] = useState('');
  const [compatibilityResult, setCompatibilityResult] = useState(null);
  const [activeTab, setActiveTab] = useState('1');
  const [checkHistory, setCheckHistory] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [currentStep, setCurrentStep] = useState(0);
  const [form] = Form.useForm();

  // Dữ liệu mẫu lịch sử kiểm tra (giảm bớt)
  const [historyData, setHistoryData] = useState([
    {
      id: 1,
      donor: 'A+',
      recipient: 'A+',
      component: 'PRC',
      result: 'compatible',
      timestamp: '2024-01-15 10:30:00',
      technician: 'Nguyễn Văn A',
      notes: 'Kiểm tra thường quy'
    },
    {
      id: 2,
      donor: 'O-',
      recipient: 'AB+',
      component: 'Plasma',
      result: 'compatible',
      timestamp: '2024-01-15 11:45:00',
      technician: 'Trần Thị B',
      notes: 'Trường hợp cấp cứu'
    }
  ]);

  // Ma trận tương thích cho từng nhóm máu
  const compatibilityMatrix = {
    'PRC': {
      'A+': ['A+', 'A-', 'O+', 'O-'],
      'A-': ['A-', 'O-'],
      'B+': ['B+', 'B-', 'O+', 'O-'],
      'B-': ['B-', 'O-'],
      'AB+': ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'],
      'AB-': ['A-', 'B-', 'AB-', 'O-'],
      'O+': ['O+', 'O-'],
      'O-': ['O-']
    },
    'Plasma': {
      'A+': ['A+', 'A-', 'AB+', 'AB-'],
      'A-': ['A+', 'A-', 'AB+', 'AB-'],
      'B+': ['B+', 'B-', 'AB+', 'AB-'],
      'B-': ['B+', 'B-', 'AB+', 'AB-'],
      'AB+': ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'],
      'AB-': ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'],
      'O+': ['O+', 'O-'],
      'O-': ['O+', 'O-']
    }
  };

  // Thông tin chi tiết về từng nhóm máu (giảm bớt)
  const bloodGroupInfo = {
    'A+': { frequency: '34%', canGiveTo: ['A+', 'AB+'], canReceiveFrom: ['A+', 'A-', 'O+', 'O-'] },
    'A-': { frequency: '6%', canGiveTo: ['A+', 'A-', 'AB+', 'AB-'], canReceiveFrom: ['A-', 'O-'] },
    'B+': { frequency: '9%', canGiveTo: ['B+', 'AB+'], canReceiveFrom: ['B+', 'B-', 'O+', 'O-'] },
    'B-': { frequency: '2%', canGiveTo: ['B+', 'B-', 'AB+', 'AB-'], canReceiveFrom: ['B-', 'O-'] },
    'AB+': { frequency: '3%', canGiveTo: ['AB+'], canReceiveFrom: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'] },
    'AB-': { frequency: '1%', canGiveTo: ['AB+', 'AB-'], canReceiveFrom: ['A-', 'B-', 'AB-', 'O-'] },
    'O+': { frequency: '38%', canGiveTo: ['A+', 'B+', 'AB+', 'O+'], canReceiveFrom: ['O+', 'O-'] },
    'O-': { frequency: '7%', canGiveTo: ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'], canReceiveFrom: ['O-'] }
  };

  // Kiểm tra tương thích
  const checkCompatibility = () => {
    if (!donorBlood || !donorRh || !recipientBlood || !recipientRh || !donorComponent) {
      message.warning('Vui lòng chọn đầy đủ thông tin!');
      return;
    }

    const donorType = donorBlood + donorRh;
    const recipientType = recipientBlood + recipientRh;
    
    const isCompatible = compatibilityMatrix[donorComponent][recipientType]?.includes(donorType);
    
    const result = {
      donor: donorType,
      recipient: recipientType,
      component: donorComponent,
      compatible: isCompatible,
      timestamp: new Date().toLocaleString('vi-VN'),
      riskLevel: isCompatible ? 'low' : 'high',
      recommendation: isCompatible ? 'Có thể truyền máu' : 'Không được truyền máu',
      notes: isCompatible ? 'Tương thích hoàn toàn' : 'Có nguy cơ phản ứng miễn dịch'
    };

    setCompatibilityResult(result);
    
    // Thêm vào lịch sử
    const newHistory = {
      id: historyData.length + 1,
      donor: donorType,
      recipient: recipientType,
      component: donorComponent,
      result: isCompatible ? 'compatible' : 'incompatible',
      timestamp: new Date().toLocaleString('vi-VN'),
      technician: 'Người dùng hệ thống',
      notes: result.notes
    };
    
    setHistoryData(prev => [newHistory, ...prev]);
    message.success('Kiểm tra tương thích thành công!');
  };

  // Reset form
  const resetForm = () => {
    setDonorBlood('');
    setDonorRh('');
    setDonorComponent('');
    setRecipientBlood('');
    setRecipientRh('');
    setCompatibilityResult(null);
    setCurrentStep(0);
  };

  // Cấu hình cột cho bảng lịch sử
  const historyColumns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 60,
      render: (text) => <Text strong>#{text}</Text>
    },
    {
      title: 'Người cho',
      dataIndex: 'donor',
      key: 'donor',
      render: (donor) => <Tag color="blue">{donor}</Tag>
    },
    {
      title: 'Người nhận',
      dataIndex: 'recipient',
      key: 'recipient',
      render: (recipient) => <Tag color="green">{recipient}</Tag>
    },
    {
      title: 'Thành phần',
      dataIndex: 'component',
      key: 'component',
      render: (component) => <Tag color="purple">{component}</Tag>
    },
    {
      title: 'Kết quả',
      dataIndex: 'result',
      key: 'result',
      render: (result) => (
        <Tag 
          color={result === 'compatible' ? 'green' : 'red'}
          icon={result === 'compatible' ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
        >
          {result === 'compatible' ? 'Tương thích' : 'Không tương thích'}
        </Tag>
      )
    },
    {
      title: 'Thời gian',
      dataIndex: 'timestamp',
      key: 'timestamp',
      render: (time) => (
        <Space>
          <ClockCircleOutlined />
          <Text type="secondary">{time}</Text>
        </Space>
      )
    },
    {
      title: 'Kỹ thuật viên',
      dataIndex: 'technician',
      key: 'technician',
      render: (tech) => (
        <Space>
          <UserOutlined />
          <Text>{tech}</Text>
        </Space>
      )
    },
    {
      title: 'Hành động',
      key: 'actions',
      render: (_, record) => (
        <Space>
          <Tooltip title="Xem chi tiết">
            <Button type="primary" icon={<EyeOutlined />} size="small" />
          </Tooltip>
          <Tooltip title="In báo cáo">
            <Button type="default" icon={<PrinterOutlined />} size="small" />
          </Tooltip>
          <Tooltip title="Xóa">
            <Button type="primary" danger icon={<DeleteOutlined />} size="small" />
          </Tooltip>
        </Space>
      )
    }
  ];

  // Render kết quả tương thích
  const renderCompatibilityResult = () => {
    if (!compatibilityResult) return null;

    const { compatible, donor, recipient, component, recommendation, notes, riskLevel } = compatibilityResult;

    return (
      <Card 
        title={
          <Space>
            <MedicineBoxOutlined />
            <Text strong>Kết quả kiểm tra tương thích</Text>
          </Space>
        }
        style={{ marginTop: 16 }}
      >
        <Result
          status={compatible ? 'success' : 'error'}
          title={compatible ? 'TƯƠNG THÍCH' : 'KHÔNG TƯƠNG THÍCH'}
          subTitle={recommendation}
          extra={
            <Space direction="vertical" size="middle" style={{ width: '100%' }}>
              <Descriptions bordered column={2}>
                <Descriptions.Item label="Người cho" span={1}>
                  <Tag color="blue" style={{ fontSize: '14px' }}>{donor}</Tag>
                </Descriptions.Item>
                <Descriptions.Item label="Người nhận" span={1}>
                  <Tag color="green" style={{ fontSize: '14px' }}>{recipient}</Tag>
                </Descriptions.Item>
                <Descriptions.Item label="Thành phần máu" span={1}>
                  <Tag color="purple" style={{ fontSize: '14px' }}>{component}</Tag>
                </Descriptions.Item>
                <Descriptions.Item label="Mức độ rủi ro" span={1}>
                  <Tag color={riskLevel === 'low' ? 'green' : 'red'}>
                    {riskLevel === 'low' ? 'Thấp' : 'Cao'}
                  </Tag>
                </Descriptions.Item>
                <Descriptions.Item label="Ghi chú" span={2}>
                  {notes}
                </Descriptions.Item>
              </Descriptions>
              
              <Space>
                <Button type="primary" icon={<PrinterOutlined />}>
                  In báo cáo
                </Button>
                <Button type="default" icon={<DownloadOutlined />}>
                  Tải xuống
                </Button>
                <Button type="default" icon={<ReloadOutlined />} onClick={resetForm}>
                  Kiểm tra mới
                </Button>
              </Space>
            </Space>
          }
        />
      </Card>
    );
  };

  // Render bảng tương thích (giảm bớt dữ liệu)
  const renderCompatibilityTable = () => {
    const tableData = [
      { key: 'A+', donor: 'A+', component: 'PRC', recipients: 'O-, O+, A-, A+', status: 'compatible' },
      { key: 'AB-', donor: 'AB-', component: 'Plasma', recipients: 'AB+, AB-', status: 'compatible' }
    ];

    const tableColumns = [
      {
        title: 'Người nhận',
        dataIndex: 'donor',
        key: 'donor',
        render: (donor) => <Tag color="blue">{donor}</Tag>
      },
      {
        title: 'Thành phần',
        dataIndex: 'component',
        key: 'component',
        render: (component) => <Tag color="purple">{component}</Tag>
      },
      {
        title: 'Có thể nhận từ nhóm máu',
        dataIndex: 'recipients',
        key: 'recipients',
        render: (recipients) => (
          <Text>{recipients}</Text>
        )
      },
      {
        title: 'Hành động',
        key: 'actions',
        render: () => (
          <Space>
            <Button type="link" icon={<EditOutlined />}>Sửa</Button>
            <Button type="link" icon={<DeleteOutlined />} danger>Xóa</Button>
          </Space>
        )
      }
    ];

    return (
      <Table 
        columns={tableColumns}
        dataSource={tableData}
        pagination={{ pageSize: 8 }}
        size="middle"
      />
    );
  };

  // Render thống kê
  const renderStatistics = () => {
    const compatibleCount = historyData.filter(item => item.result === 'compatible').length;
    const incompatibleCount = historyData.filter(item => item.result === 'incompatible').length;
    const totalTests = historyData.length;
    const successRate = totalTests > 0 ? (compatibleCount / totalTests * 100).toFixed(1) : 0;

    return (
      <div>
        <Row gutter={16} style={{ marginBottom: 24 }}>
          <Col span={6}>
            <Card>
              <Statistic
                title="Tổng số kiểm tra"
                value={totalTests}
                valueStyle={{ color: '#3f8600' }}
                prefix={<FileTextOutlined />}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="Tương thích"
                value={compatibleCount}
                valueStyle={{ color: '#52c41a' }}
                prefix={<CheckCircleOutlined />}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="Không tương thích"
                value={incompatibleCount}
                valueStyle={{ color: '#f5222d' }}
                prefix={<CloseCircleOutlined />}
              />
            </Card>
          </Col>
          <Col span={6}>
            <Card>
              <Statistic
                title="Tỷ lệ thành công"
                value={successRate}
                valueStyle={{ color: '#1890ff' }}
                suffix="%"
                prefix={<BarChartOutlined />}
              />
            </Card>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col span={12}>
            <Card title="Xu hướng kiểm tra">
              <Progress 
                type="circle" 
                percent={successRate} 
                format={(percent) => `${percent}%`}
                strokeColor={{
                  '0%': '#108ee9',
                  '100%': '#87d068',
                }}
              />
              <div style={{ textAlign: 'center', marginTop: 16 }}>
                <Text type="secondary">Tỷ lệ tương thích</Text>
              </div>
            </Card>
          </Col>
          <Col span={12}>
            <Card title="Hoạt động gần đây">
              <Timeline>
                {historyData.slice(0, 2).map((item) => (
                  <Timeline.Item
                    key={item.id}
                    color={item.result === 'compatible' ? 'green' : 'red'}
                    dot={item.result === 'compatible' ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
                  >
                    <Text strong>{item.donor} → {item.recipient}</Text>
                    <br />
                    <Text type="secondary">{item.timestamp}</Text>
                  </Timeline.Item>
                ))}
              </Timeline>
            </Card>
          </Col>
        </Row>
      </div>
    );
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <ShareAltOutlined style={{ marginRight: 8 }} />
              Quy tắc tương thích
            </Title>
          </Col>
          <Col>
            <Space>
              <Text type="secondary">
                <ClockCircleOutlined style={{ marginRight: 4 }} />
                {new Date().toLocaleString('vi-VN')}
              </Text>
              <Text type="secondary">
                <UserOutlined style={{ marginRight: 4 }} />
                Kỹ thuật viên
              </Text>
            </Space>
          </Col>
        </Row>
      </Header>

      <Content style={{ padding: '24px' }}>
        <Tabs activeKey={activeTab} onChange={setActiveTab} type="card" size="large">
          <TabPane
            tab={
              <Space>
                <SearchOutlined />
                Kiểm tra tương thích
              </Space>
            }
            key="1"
          >
            <Card 
              title={
                <Space>
                  <MedicineBoxOutlined />
                  <Text strong>Quy tắc tương thích truyền máu</Text>
                </Space>
              }
              style={{ marginBottom: 16 }}
            >
              <Steps current={currentStep} style={{ marginBottom: 24 }}>
                <Step title="Chọn thông tin" description="Nhóm máu và thành phần" />
                <Step title="Kiểm tra" description="Xác minh tương thích" />
                <Step title="Kết quả" description="Báo cáo chi tiết" />
              </Steps>

              <Alert
                message="Thông tin quan trọng"
                description="Người A+ (cần PRC) có thể nhận từ: O-, O+, A-, A+"
                type="info"
                showIcon
                style={{ marginBottom: 16 }}
              />

              <Row gutter={16}>
                <Col span={12}>
                  <Card title="Thông tin người cho" size="small">
                    <Form layout="vertical">
                      <Row gutter={16}>
                        <Col span={12}>
                          <Form.Item label="Nhóm máu người cho">
                            <Select
                              placeholder="Chọn nhóm máu"
                              value={donorBlood}
                              onChange={setDonorBlood}
                              size="large"
                            >
                              <Option value="A">A</Option>
                              <Option value="B">B</Option>
                              <Option value="AB">AB</Option>
                              <Option value="O">O</Option>
                            </Select>
                          </Form.Item>
                        </Col>
                        <Col span={12}>
                          <Form.Item label="Rh">
                            <Select
                              placeholder="Chọn Rh"
                              value={donorRh}
                              onChange={setDonorRh}
                              size="large"
                            >
                              <Option value="+">Dương (+)</Option>
                              <Option value="-">Âm (-)</Option>
                            </Select>
                          </Form.Item>
                        </Col>
                      </Row>
                      <Form.Item label="Thành phần máu">
                        <Select
                          placeholder="Chọn thành phần"
                          value={donorComponent}
                          onChange={setDonorComponent}
                          size="large"
                        >
                          <Option value="PRC">PRC (Hồng cầu)</Option>
                          <Option value="Plasma">Plasma (Huyết tương)</Option>
                        </Select>
                      </Form.Item>
                    </Form>
                  </Card>
                </Col>

                <Col span={12}>
                  <Card title="Thông tin người nhận" size="small">
                    <Form layout="vertical">
                      <Row gutter={16}>
                        <Col span={12}>
                          <Form.Item label="Nhóm máu người nhận">
                            <Select
                              placeholder="Chọn nhóm máu"
                              value={recipientBlood}
                              onChange={setRecipientBlood}
                              size="large"
                            >
                              <Option value="A">A</Option>
                              <Option value="B">B</Option>
                              <Option value="AB">AB</Option>
                              <Option value="O">O</Option>
                            </Select>
                          </Form.Item>
                        </Col>
                        <Col span={12}>
                          <Form.Item label="Rh">
                            <Select
                              placeholder="Chọn Rh"
                              value={recipientRh}
                              onChange={setRecipientRh}
                              size="large"
                            >
                              <Option value="+">Dương (+)</Option>
                              <Option value="-">Âm (-)</Option>
                            </Select>
                          </Form.Item>
                        </Col>
                      </Row>
                    </Form>
                  </Card>
                </Col>
              </Row>

              <div style={{ textAlign: 'center', marginTop: 24 }}>
                <Space size="large">
                  <Button 
                    type="primary" 
                    size="large" 
                    icon={<SearchOutlined />}
                    onClick={checkCompatibility}
                    disabled={!donorBlood || !donorRh || !recipientBlood || !recipientRh || !donorComponent}
                  >
                    Kiểm tra tương thích
                  </Button>
                  <Button 
                    size="large" 
                    icon={<ReloadOutlined />}
                    onClick={resetForm}
                  >
                    Làm mới
                  </Button>
                </Space>
              </div>

              {renderCompatibilityResult()}
            </Card>
          </TabPane>

          <TabPane
            tab={
              <Space>
                <FileTextOutlined />
                Lịch sử kiểm tra
              </Space>
            }
            key="2"
          >
            <Card 
              title={
                <Space>
                  <ClockCircleOutlined />
                  <Text strong>Lịch sử kiểm tra tương thích</Text>
                </Space>
              }
              extra={
                <Space>
                  <Button type="primary" icon={<FilterOutlined />}>
                    Lọc
                  </Button>
                  <Button type="default" icon={<DownloadOutlined />}>
                    Xuất Excel
                  </Button>
                </Space>
              }
            >
              <Table
                columns={historyColumns}
                dataSource={historyData}
                rowKey="id"
                pagination={{ pageSize: 10 }}
                size="middle"
              />
            </Card>
          </TabPane>

          <TabPane
            tab={
              <Space>
                <BarChartOutlined />
                Thống kê
              </Space>
            }
            key="3"
          >
            <Card title="Thống kê tổng quan">
              {renderStatistics()}
            </Card>
          </TabPane>

          <TabPane
            tab={
              <Space>
                <TableOutlined />
                Bảng tương thích
              </Space>
            }
            key="4"
          >
            <Card 
              title={
                <Space>
                  <SafetyOutlined />
                  <Text strong>Bảng quy tắc tương thích</Text>
                </Space>
              }
              extra={
                <Button type="primary" icon={<PlusOutlined />}>
                  Thêm quy tắc
                </Button>
              }
            >
              {renderCompatibilityTable()}
            </Card>
          </TabPane>
        </Tabs>
      </Content>
    </Layout>
  );
};

export default BloodCompatibility;