import React, { useState } from 'react';
import { 
  Layout, 
  Menu, 
  Table, 
  Button, 
  Modal, 
  Form, 
  Input, 
  Select, 
  DatePicker, 
  Switch, 
  Tag, 
  Space, 
  Popconfirm, 
  message,
  Card,
  Statistic,
  Row,
  Col,
  Badge,
  Typography,
  Tooltip,
  InputNumber,
  Divider,
  Tabs
} from 'antd';
import { 
  HeartOutlined, 
  ExperimentOutlined, 
  PlusOutlined, 
  EditOutlined, 
  DeleteOutlined,
  CheckCircleOutlined,
  CloseCircleOutlined,
  ExclamationCircleOutlined,
  ReloadOutlined,
  BarChartOutlined,
  UserOutlined,
  EnvironmentOutlined,
  CalendarOutlined,
  FireOutlined,
  BugOutlined
} from '@ant-design/icons';

const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;
const { TabPane } = Tabs;

const BloodManagement = () => {
  const [activeTab, setActiveTab] = useState('1');
  const [bloodGroupModalVisible, setBloodGroupModalVisible] = useState(false);
  const [componentModalVisible, setComponentModalVisible] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();

  // Dữ liệu nhóm máu
  const [bloodGroups, setBloodGroups] = useState([
    {
      id: 1,
      group: 'A',
      rh: '+',
      description: 'Phổ biến',
      status: 'active',
      quantity: 125,
      location: 'Kho A1',
      lastUpdate: '2024-01-15'
    },
    {
      id: 2,
      group: 'AB',
      rh: '-',
      description: 'Rất hiếm',
      status: 'active',
      quantity: 45,
      location: 'Kho B2',
      lastUpdate: '2024-01-14'
    },
    {
      id: 3,
      group: 'O',
      rh: '-',
      description: 'Toàn năng',
      status: 'inactive',
      quantity: 0,
      location: 'Kho C3',
      lastUpdate: '2024-01-13'
    }
  ]);

  // Dữ liệu thành phần máu
  const [bloodComponents, setBloodComponents] = useState([
    {
      id: 1,
      name: 'Hồng cầu',
      code: 'PRC',
      temperature: '2-6°C',
      shelfLife: 42,
      unit: 'ngày',
      status: 'active',
      application: 'Thiếu máu nặng',
      quantity: 89,
      location: 'Tủ lạnh A'
    },
    {
      id: 2,
      name: 'Huyết tương',
      code: 'FFP',
      temperature: '-25°C',
      shelfLife: 365,
      unit: 'ngày',
      status: 'inactive',
      application: 'Sốc, rối loạn đông',
      quantity: 156,
      location: 'Tủ đông B'
    }
  ]);

  // Cột cho bảng nhóm máu
  const bloodGroupColumns = [
    {
      title: '#',
      dataIndex: 'id',
      key: 'id',
      width: 60,
      render: (text) => <Text strong>{text}</Text>
    },
    {
      title: 'Nhóm máu',
      key: 'bloodType',
      render: (_, record) => (
        <Tag color="red" style={{ fontSize: '14px', padding: '4px 8px' }}>
          {record.group}{record.rh}
        </Tag>
      )
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Số lượng',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => (
        <Badge 
          count={quantity} 
          style={{ 
            backgroundColor: quantity > 50 ? '#52c41a' : quantity > 20 ? '#faad14' : '#f5222d' 
          }} 
        />
      )
    },
    {
      title: 'Vị trí',
      dataIndex: 'location',
      key: 'location',
      render: (location) => (
        <Space>
          <EnvironmentOutlined />
          <Text>{location}</Text>
        </Space>
      )
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag 
          color={status === 'active' ? 'green' : 'red'}
          icon={status === 'active' ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
        >
          {status === 'active' ? 'Hoạt động' : 'Vô hiệu'}
        </Tag>
      )
    },
    {
      title: 'Hành động',
      key: 'actions',
      render: (_, record) => (
        <Space>
          <Tooltip title="Chỉnh sửa">
            <Button 
              type="primary" 
              icon={<EditOutlined />} 
              size="small"
              onClick={() => handleEdit('bloodGroup', record)}
            />
          </Tooltip>
          <Tooltip title="Khôi phục">
            <Button 
              type="default" 
              icon={<ReloadOutlined />} 
              size="small"
              onClick={() => handleRestore(record.id)}
            />
          </Tooltip>
          <Popconfirm
            title="Bạn có chắc muốn xóa?"
            onConfirm={() => handleDelete('bloodGroup', record.id)}
            okText="Xóa"
            cancelText="Hủy"
          >
            <Tooltip title="Xóa">
              <Button 
                type="primary" 
                danger 
                icon={<DeleteOutlined />} 
                size="small"
              />
            </Tooltip>
          </Popconfirm>
        </Space>
      )
    }
  ];

  // Cột cho bảng thành phần máu
  const componentColumns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 60,
      render: (text) => <Text strong>{text}</Text>
    },
    {
      title: 'Tên thành phần',
      dataIndex: 'name',
      key: 'name',
      render: (name) => <Text strong style={{ color: '#1890ff' }}>{name}</Text>
    },
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
      render: (code) => <Tag color="blue">{code}</Tag>
    },
    {
      title: 'Nhiệt độ',
      dataIndex: 'temperature',
      key: 'temperature',
      render: (temp) => (
        <Space>
          {temp.includes('-') ? <BugOutlined style={{ color: '#1890ff' }} /> : <FireOutlined style={{ color: '#ff4d4f' }} />}
          <Text>{temp}</Text>
        </Space>
      )
    },
    {
      title: 'Bảo quản',
      key: 'shelfLife',
      render: (_, record) => (
        <Text>
          {record.shelfLife} {record.unit}
        </Text>
      )
    },
    {
      title: 'Số lượng',
      dataIndex: 'quantity',
      key: 'quantity',
      render: (quantity) => (
        <Badge 
          count={quantity} 
          style={{ 
            backgroundColor: quantity > 100 ? '#52c41a' : quantity > 50 ? '#faad14' : '#f5222d' 
          }} 
        />
      )
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag 
          color={status === 'active' ? 'green' : 'red'}
          icon={status === 'active' ? <CheckCircleOutlined /> : <CloseCircleOutlined />}
        >
          {status === 'active' ? 'Hoạt động' : 'Vô hiệu'}
        </Tag>
      )
    },
    {
      title: 'Ứng dụng',
      dataIndex: 'application',
      key: 'application',
      render: (app) => <Text type="secondary">{app}</Text>
    },
    {
      title: 'Hành động',
      key: 'actions',
      render: (_, record) => (
        <Space>
          <Tooltip title="Chỉnh sửa">
            <Button 
              type="primary" 
              icon={<EditOutlined />} 
              size="small"
              onClick={() => handleEdit('component', record)}
            />
          </Tooltip>
          <Tooltip title="Khôi phục">
            <Button 
              type="default" 
              icon={<ReloadOutlined />} 
              size="small"
              onClick={() => handleRestore(record.id)}
            />
          </Tooltip>
          <Popconfirm
            title="Bạn có chắc muốn xóa?"
            onConfirm={() => handleDelete('component', record.id)}
            okText="Xóa"
            cancelText="Hủy"
          >
            <Tooltip title="Xóa">
              <Button 
                type="primary" 
                danger 
                icon={<DeleteOutlined />} 
                size="small"
              />
            </Tooltip>
          </Popconfirm>
        </Space>
      )
    }
  ];

  // Xử lý thêm mới
  const handleAdd = (type) => {
    setEditingRecord(null);
    form.resetFields();
    if (type === 'bloodGroup') {
      setBloodGroupModalVisible(true);
    } else {
      setComponentModalVisible(true);
    }
  };

  // Xử lý chỉnh sửa
  const handleEdit = (type, record) => {
    setEditingRecord(record);
    form.setFieldsValue(record);
    if (type === 'bloodGroup') {
      setBloodGroupModalVisible(true);
    } else {
      setComponentModalVisible(true);
    }
  };

  // Xử lý xóa
  const handleDelete = (type, id) => {
    if (type === 'bloodGroup') {
      setBloodGroups(prev => prev.filter(item => item.id !== id));
    } else {
      setBloodComponents(prev => prev.filter(item => item.id !== id));
    }
    message.success('Xóa thành công!');
  };

  // Xử lý khôi phục
  const handleRestore = (id) => {
    message.success('Khôi phục thành công!');
  };

  // Xử lý lưu
  const handleSave = async (type) => {
    try {
      const values = await form.validateFields();
      
      if (type === 'bloodGroup') {
        if (editingRecord) {
          setBloodGroups(prev => 
            prev.map(item => 
              item.id === editingRecord.id 
                ? { ...item, ...values }
                : item
            )
          );
          message.success('Cập nhật nhóm máu thành công!');
        } else {
          const newId = Math.max(...bloodGroups.map(item => item.id)) + 1;
          setBloodGroups(prev => [...prev, { id: newId, ...values }]);
          message.success('Thêm nhóm máu thành công!');
        }
        setBloodGroupModalVisible(false);
      } else {
        if (editingRecord) {
          setBloodComponents(prev => 
            prev.map(item => 
              item.id === editingRecord.id 
                ? { ...item, ...values }
                : item
            )
          );
          message.success('Cập nhật thành phần máu thành công!');
        } else {
          const newId = Math.max(...bloodComponents.map(item => item.id)) + 1;
          setBloodComponents(prev => [...prev, { id: newId, ...values }]);
          message.success('Thêm thành phần máu thành công!');
        }
        setComponentModalVisible(false);
      }
      
      form.resetFields();
      setEditingRecord(null);
    } catch (error) {
      message.error('Vui lòng kiểm tra lại thông tin!');
    }
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <ExperimentOutlined style={{ marginRight: 8 }} />
               Nhóm máu & Thành phần
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
      
      <Content style={{ margin: '24px', background: '#fff', padding: 24, borderRadius: 8 }}>
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          size="large"
          type="card"
          style={{ marginBottom: 24 }}
        >
          <TabPane
            tab={
              <span>
                <ExperimentOutlined />
                Nhóm Máu
              </span>
            }
            key="1"
          >
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Title level={3} style={{ margin: 0 }}>
                <ExperimentOutlined style={{ marginRight: 8, color: '#ff4d4f' }} />
                Danh sách Nhóm máu
              </Title>
              <Button 
                type="primary" 
                icon={<PlusOutlined />} 
                onClick={() => handleAdd('bloodGroup')}
                size="large"
              >
                Thêm Nhóm máu
              </Button>
            </div>
            <Table 
              columns={bloodGroupColumns} 
              dataSource={bloodGroups}
              rowKey="id"
              pagination={{ pageSize: 10 }}
              size="middle"
            />
          </TabPane>

          <TabPane
            tab={
              <span>
                <HeartOutlined />
                Thành Phần Máu
              </span>
            }
            key="2"
          >
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Title level={3} style={{ margin: 0 }}>
                <HeartOutlined style={{ marginRight: 8, color: '#1890ff' }} />
                Danh sách Thành phần máu
              </Title>
              <Button 
                type="primary" 
                icon={<PlusOutlined />} 
                onClick={() => handleAdd('component')}
                size="large"
              >
                Thêm Thành phần
              </Button>
            </div>
            <Table 
              columns={componentColumns} 
              dataSource={bloodComponents}
              rowKey="id"
              pagination={{ pageSize: 10 }}
              size="middle"
            />
          </TabPane>

          <TabPane
            tab={
              <span>
                <BarChartOutlined />
                Thống Kê
              </span>
            }
            key="3"
          >
            <Title level={3} style={{ marginBottom: 24 }}>
              <BarChartOutlined style={{ marginRight: 8, color: '#52c41a' }} />
              Thống kê tổng quan
            </Title>
            <Row gutter={16}>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Tổng nhóm máu"
                    value={bloodGroups.length}
                    valueStyle={{ color: '#3f8600' }}
                    prefix={<ExperimentOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Thành phần máu"
                    value={bloodComponents.length}
                    valueStyle={{ color: '#cf1322' }}
                    prefix={<HeartOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Đang hoạt động"
                    value={bloodGroups.filter(item => item.status === 'active').length}
                    valueStyle={{ color: '#1890ff' }}
                    prefix={<CheckCircleOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Tổng số lượng"
                    value={bloodGroups.reduce((acc, item) => acc + item.quantity, 0)}
                    valueStyle={{ color: '#722ed1' }}
                    suffix="đơn vị"
                  />
                </Card>
              </Col>
            </Row>
          </TabPane>
        </Tabs>
      </Content>

      {/* Modal Nhóm máu */}
      <Modal
        title={
          <span>
            <ExperimentOutlined style={{ marginRight: 8, color: '#ff4d4f' }} />
            {editingRecord ? 'Chỉnh sửa Nhóm máu' : 'Thêm Nhóm máu mới'}
          </span>
        }
        open={bloodGroupModalVisible}
        onOk={() => handleSave('bloodGroup')}
        onCancel={() => {
          setBloodGroupModalVisible(false);
          setEditingRecord(null);
          form.resetFields();
        }}
        width={600}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical">
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Nhóm máu"
                name="group"
                rules={[{ required: true, message: 'Vui lòng chọn nhóm máu!' }]}
              >
                <Select placeholder="Chọn nhóm máu">
                  <Option value="A">A</Option>
                  <Option value="B">B</Option>
                  <Option value="AB">AB</Option>
                  <Option value="O">O</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Rh"
                name="rh"
                rules={[{ required: true, message: 'Vui lòng chọn Rh!' }]}
              >
                <Select placeholder="Chọn Rh">
                  <Option value="+">Dương (+)</Option>
                  <Option value="-">Âm (-)</Option>
                </Select>
              </Form.Item>
            </Col>
          </Row>
          
          <Form.Item
            label="Mô tả"
            name="description"
            rules={[{ required: true, message: 'Vui lòng nhập mô tả!' }]}
          >
            <Input placeholder="Nhập mô tả nhóm máu" />
          </Form.Item>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Số lượng"
                name="quantity"
                rules={[{ required: true, message: 'Vui lòng nhập số lượng!' }]}
              >
                <InputNumber 
                  min={0} 
                  style={{ width: '100%' }}
                  placeholder="Nhập số lượng"
                />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Vị trí"
                name="location"
                rules={[{ required: true, message: 'Vui lòng nhập vị trí!' }]}
              >
                <Input placeholder="Nhập vị trí lưu trữ" />
              </Form.Item>
            </Col>
          </Row>
          
          <Form.Item
            label="Trạng thái"
            name="status"
            valuePropName="checked"
          >
            <Switch 
              checkedChildren="Hoạt động" 
              unCheckedChildren="Vô hiệu"
              defaultChecked
            />
          </Form.Item>
        </Form>
      </Modal>

      {/* Modal Thành phần máu */}
      <Modal
        title={
          <span>
            <HeartOutlined style={{ marginRight: 8, color: '#1890ff' }} />
            {editingRecord ? 'Chỉnh sửa Thành phần máu' : 'Thêm Thành phần máu mới'}
          </span>
        }
        open={componentModalVisible}
        onOk={() => handleSave('component')}
        onCancel={() => {
          setComponentModalVisible(false);
          setEditingRecord(null);
          form.resetFields();
        }}
        width={700}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical">
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Tên thành phần"
                name="name"
                rules={[{ required: true, message: 'Vui lòng nhập tên!' }]}
              >
                <Input placeholder="Nhập tên thành phần" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Mã Code"
                name="code"
                rules={[{ required: true, message: 'Vui lòng nhập mã!' }]}
              >
                <Input placeholder="Nhập mã code" />
              </Form.Item>
            </Col>
          </Row>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Nhiệt độ bảo quản"
                name="temperature"
                rules={[{ required: true, message: 'Vui lòng nhập nhiệt độ!' }]}
              >
                <Input placeholder="VD: 2-6°C" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Thời hạn bảo quản"
                name="shelfLife"
                rules={[{ required: true, message: 'Vui lòng nhập thời hạn!' }]}
              >
                <InputNumber 
                  min={1} 
                  style={{ width: '100%' }}
                  placeholder="Nhập số ngày"
                />
              </Form.Item>
            </Col>
          </Row>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item
                label="Đơn vị"
                name="unit"
                rules={[{ required: true, message: 'Vui lòng chọn đơn vị!' }]}
              >
                <Select placeholder="Chọn đơn vị">
                  <Option value="ngày">Ngày</Option>
                  <Option value="tháng">Tháng</Option>
                  <Option value="năm">Năm</Option>
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Số lượng"
                name="quantity"
                rules={[{ required: true, message: 'Vui lòng nhập số lượng!' }]}
              >
                <InputNumber 
                  min={0} 
                  style={{ width: '100%' }}
                  placeholder="Nhập số lượng"
                />
              </Form.Item>
            </Col>
          </Row>
          
          <Form.Item
            label="Ứng dụng"
            name="application"
            rules={[{ required: true, message: 'Vui lòng nhập ứng dụng!' }]}
          >
            <Input.TextArea 
              rows={3}
              placeholder="Nhập ứng dụng và công dụng"
            />
          </Form.Item>
          
          <Form.Item
            label="Vị trí lưu trữ"
            name="location"
            rules={[{ required: true, message: 'Vui lòng nhập vị trí!' }]}
          >
            <Input placeholder="Nhập vị trí lưu trữ" />
          </Form.Item>
          
          <Form.Item
            label="Trạng thái"
            name="status"
            valuePropName="checked"
          >
            <Switch 
              checkedChildren="Hoạt động" 
              unCheckedChildren="Vô hiệu"
              defaultChecked
            />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default BloodManagement;