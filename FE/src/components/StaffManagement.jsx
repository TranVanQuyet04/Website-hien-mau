import React, { useState } from "react";
import axios from 'axios';
import { useEffect } from 'react';

import { 
  Form, 
  Input, 
  Button, 
  Select, 
  DatePicker, 
  Radio, 
  Typography, 
  message, 
  Card,
  Row,
  Col,
  Space,
  Divider,
  Alert,
  Table,
  Tag,
  Modal,
  Drawer,
  Avatar,
  Tooltip,
  Popconfirm,
  Switch,
  Badge,
  Tabs,
  Layout
} from "antd";
import { 
  UserOutlined, 
  MedicineBoxOutlined, 
  TeamOutlined, 
  PhoneOutlined, 
  MailOutlined,
  IdcardOutlined,
  CalendarOutlined,
  LockOutlined,
  EditOutlined,
  DeleteOutlined,
  EyeOutlined,
  PlusOutlined,
  UsergroupAddOutlined
} from "@ant-design/icons";
import dayjs from "dayjs";
const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;
const { TextArea } = Input;
const { TabPane } = Tabs;

const StaffManagement = () => {
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  const [selectedRole, setSelectedRole] = useState(null);
  const [activeTab, setActiveTab] = useState("list");
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingStaff, setEditingStaff] = useState(null);
  const [viewingStaff, setViewingStaff] = useState(null);
  const [searchText, setSearchText] = useState("");
  const [filterRole, setFilterRole] = useState("all");
  const [filterStatus, setFilterStatus] = useState("all");
  const [staffList, setStaffList] = useState([]);
useEffect(() => {
  const fetchStaffs = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/api/admin/staffs", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const data = response.data;

const mapped = data.map((staff, index) => ({
  id: staff.userId,
  staffId: `${staff.staffPosition === 'Doctor' ? 'BS' : 'NV'}${(index + 1).toString().padStart(3, '0')}`,
  username: staff.username,
  fullName: staff.fullName,
  email: staff.email,
  role: staff.staffPosition === 'Doctor' ? 'DOCTOR' : 'STAFF',
  position: staff.staffPosition === 'Staff' ? 'Nhân viên' : undefined,
  specialty: staff.staffPosition === 'Doctor' ? 'Chuyên khoa chưa rõ' : undefined,
  department: undefined,
  status: staff.enable ? 'ACTIVE' : 'INACTIVE',
}));


      setStaffList(mapped);
    } catch (err) {
      console.error("Lỗi khi gọi API /staffs:", err);
    }
  };

  fetchStaffs();
}, []);

  // Dữ liệu mẫu nhân viên
  

  // Danh sách chuyên khoa và phòng ban
  const specialties = [
    "Nội tổng hợp", "Ngoại tổng hợp", "Sản phụ khoa", "Nhi khoa", "Mắt", 
    "Tai mũi họng", "Da liễu", "Thần kinh", "Tim mạch", "Tiêu hóa", 
    "Hô hấp", "Thận - Tiết niệu", "Nội tiết", "Chấn thương chỉnh hình", 
    "Gây mê hồi sức", "Chuẩn đoán hình ảnh", "Xét nghiệm", "Dược"
  ];

  const departments = [
    "Khoa Nội", "Khoa Ngoại", "Khoa Sản", "Khoa Nhi", "Khoa Cấp cứu",
    "Phòng Xét nghiệm", "Phòng Chẩn đoán hình ảnh", "Phòng Dược", 
    "Phòng Hành chính", "Phòng Kế toán", "Phòng Nhân sự", "Phòng Kỹ thuật"
  ];

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      const newStaff = {
        id: staffList.length + 1,
        staffId: generateStaffId(values.role),
        ...values,
        dateOfBirth: values.dateOfBirth.format("YYYY-MM-DD"),
        role: values.role.toUpperCase(),
        status: "ACTIVE",
        joinDate: dayjs().format("YYYY-MM-DD"),
        avatar: null
      };

      if (editingStaff) {
        // Cập nhật nhân viên
        setStaffList(prev => prev.map(staff => 
          staff.id === editingStaff.id ? { ...staff, ...newStaff, id: editingStaff.id } : staff
        ));
        message.success("Cập nhật thông tin nhân viên thành công!");
        setEditingStaff(null);
      } else {
        // Thêm nhân viên mới
        setStaffList(prev => [...prev, newStaff]);
        message.success(`Tạo tài khoản ${values.role === 'DOCTOR' ? 'bác sĩ' : 'nhân viên'} thành công!`);
      }
      
      form.resetFields();
      setSelectedRole(null);
      setShowCreateForm(false);
      setActiveTab("list");
    } catch (err) {
      message.error("Có lỗi xảy ra! Vui lòng thử lại.");
    } finally {
      setLoading(false);
    }
  };

  const generateStaffId = (role) => {
    const prefix = role === 'DOCTOR' ? 'BS' : 'NV';
    const count = staffList.filter(s => s.role === role.toUpperCase()).length + 1;
    return `${prefix}${count.toString().padStart(3, '0')}`;
  };

  const handleRoleChange = (value) => {
    setSelectedRole(value);
    form.setFieldsValue({ department: undefined, specialty: undefined });
  };

  const handleEdit = (record) => {
    setEditingStaff(record);
    form.setFieldsValue({
      ...record,
      dateOfBirth: dayjs(record.dateOfBirth)
    });
    setSelectedRole(record.role);
    setShowCreateForm(true);
  };

  const handleDelete = (id) => {
    setStaffList(prev => prev.filter(staff => staff.id !== id));
    message.success("Xóa nhân viên thành công!");
  };

  const toggleStatus = (id) => {
    setStaffList(prev => prev.map(staff => 
      staff.id === id ? { ...staff, status: staff.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' } : staff
    ));
    message.success("Cập nhật trạng thái thành công!");
  };

  const filteredStaff = staffList.filter(staff => {
    const matchesSearch = staff.fullName.toLowerCase().includes(searchText.toLowerCase()) ||
                         staff.staffId.toLowerCase().includes(searchText.toLowerCase()) ||
                         staff.email.toLowerCase().includes(searchText.toLowerCase());
    const matchesRole = filterRole === 'all' || staff.role === filterRole;
    const matchesStatus = filterStatus === 'all' || staff.status === filterStatus;
    return matchesSearch && matchesRole && matchesStatus;
  });

  const columns = [
    {
      title: 'Avatar',
      dataIndex: 'avatar',
      width: 60,
      render: (avatar, record) => (
        <Avatar 
          size={40} 
          src={avatar} 
          icon={<UserOutlined />}
          style={{ backgroundColor: record.role === 'DOCTOR' ? '#52c41a' : '#1890ff' }}
        />
      )
    },
    {
      title: 'Mã NV',
      dataIndex: 'staffId',
      width: 80,
      render: (text, record) => (
        <Text strong style={{ color: record.role === 'DOCTOR' ? '#52c41a' : '#1890ff' }}>
          {text}
        </Text>
      )
    },
    {
      title: 'Họ tên',
      dataIndex: 'fullName',
      width: 200,
      render: (text, record) => (
        <div>
          <Text strong>{text}</Text>
          <br />
          <Text type="secondary" style={{ fontSize: '12px' }}>
            {record.role === 'DOCTOR' ? record.specialty : record.position}
          </Text>
        </div>
      )
    },
    {
      title: 'Vai trò',
      dataIndex: 'role',
      width: 100,
      render: (role) => (
        <Tag color={role === 'DOCTOR' ? 'green' : 'blue'} icon={role === 'DOCTOR' ? <MedicineBoxOutlined /> : <TeamOutlined />}>
          {role === 'DOCTOR' ? 'Bác sĩ' : 'Nhân viên'}
        </Tag>
      )
    },
    // {
    //   title: 'Phòng ban',
    //   dataIndex: 'department',
    //   width: 150
    // },
    {
      title: 'Liên hệ',
      width: 200,
      render: (_, record) => (
        <div>
          <div style={{ marginBottom: '4px' }}>
            <PhoneOutlined style={{ color: '#1890ff', marginRight: '4px' }} />
            <Text>{record.phone}</Text>
          </div>
          <div>
            <MailOutlined style={{ color: '#1890ff', marginRight: '4px' }} />
            <Text>{record.email}</Text>
          </div>
        </div>
      )
    },
    // {
    //   title: 'Trạng thái',
    //   dataIndex: 'status',
    //   width: 100,
    //   render: (status, record) => (
    //     <div>
    //       <Badge 
    //         status={status === 'ACTIVE' ? 'success' : 'error'} 
    //         text={status === 'ACTIVE' ? 'Đang làm' : 'Nghỉ việc'} 
    //       />
    //       <br />
    //       <Switch
    //         size="small"
    //         checked={status === 'ACTIVE'}
    //         onChange={() => toggleStatus(record.id)}
    //         style={{ marginTop: '4px' }}
    //       />
    //     </div>
    //   )
    // },
    // {
    //   title: 'Thao tác',
    //   width: 120,
    //   render: (_, record) => (
    //     <Space>
    //       <Tooltip title="Xem chi tiết">
    //         <Button 
    //           type="text" 
    //           icon={<EyeOutlined />} 
    //           onClick={() => setViewingStaff(record)}
    //         />
    //       </Tooltip>
    //       <Tooltip title="Chỉnh sửa">
    //         <Button 
    //           type="text" 
    //           icon={<EditOutlined />} 
    //           onClick={() => handleEdit(record)}
    //         />
    //       </Tooltip>
    //       <Tooltip title="Xóa">
    //         <Popconfirm
    //           title="Bạn có chắc chắn muốn xóa nhân viên này?"
    //           onConfirm={() => handleDelete(record.id)}
    //           okText="Xóa"
    //           cancelText="Hủy"
    //         >
    //           <Button type="text" icon={<DeleteOutlined />} danger />
    //         </Popconfirm>
    //       </Tooltip>
    //     </Space>
    //   )
    // }
  ];

  const statsData = [
    { title: 'Tổng nhân viên', value: staffList.length, color: '#1890ff' },
    { title: 'Bác sĩ', value: staffList.filter(s => s.role === 'DOCTOR').length, color: '#52c41a' },
    { title: 'Nhân viên', value: staffList.filter(s => s.role === 'STAFF').length, color: '#722ed1' },
    { title: 'Đang làm việc', value: staffList.filter(s => s.status === 'ACTIVE').length, color: '#fa8c16' }
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <UsergroupAddOutlined style={{ marginRight: 8 }} />
              Quản lý Nhân viên Y tế
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
          <div style={{ marginBottom: '24px' }}>          
          {/* Thống kê */}
          <Row gutter={16} style={{ marginBottom: '24px' }}>
            {statsData.map((stat, index) => (
              <Col span={6} key={index}>
                <Card size="small" style={{ textAlign: 'center' }}>
                  <Text style={{ color: stat.color, fontSize: '24px', fontWeight: 'bold' }}>
                    {stat.value}
                  </Text>
                  <br />
                  <Text type="secondary">{stat.title}</Text>
                </Card>
              </Col>
            ))}
          </Row>

          {/* Thanh tìm kiếm và bộ lọc */}
          <Row gutter={16} style={{ marginBottom: '16px' }}>
            <Col span={8}>
              <Input.Search
                placeholder="Tìm kiếm nhân viên..."
                allowClear
                value={searchText}
                onChange={(e) => setSearchText(e.target.value)}
                style={{ width: '100%' }}
              />
            </Col>
            <Col span={4}>
              <Select
                value={filterRole}
                onChange={setFilterRole}
                style={{ width: '100%' }}
                placeholder="Lọc theo vai trò"
              >
                <Option value="all">Tất cả vai trò</Option>
                <Option value="DOCTOR">Bác sĩ</Option>
                <Option value="STAFF">Nhân viên</Option>
              </Select>
            </Col>
            <Col span={4}>
              <Select
                value={filterStatus}
                onChange={setFilterStatus}
                style={{ width: '100%' }}
                placeholder="Lọc theo trạng thái"
              >
                <Option value="all">Tất cả trạng thái</Option>
                <Option value="ACTIVE">Đang làm việc</Option>
                <Option value="INACTIVE">Nghỉ việc</Option>
              </Select>
            </Col>
            <Col span={8} style={{ textAlign: 'right' }}>
              <Button 
                type="primary" 
                icon={<PlusOutlined />}
                onClick={() => setShowCreateForm(true)}
                size="large"
              >
                Thêm nhân viên mới
              </Button>
            </Col>
          </Row>

          {/* Bảng danh sách nhân viên */}
          <Table
            columns={columns}
            dataSource={filteredStaff}
            rowKey="id"
            pagination={{
              pageSize: 10,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} nhân viên`
            }}
            scroll={{ x: 1200 }}
          />
        </div>
      </Card>
      </Content>


      {/* Drawer tạo/sửa nhân viên */}
      <Drawer
        title={editingStaff ? "Chỉnh sửa nhân viên" : "Thêm nhân viên mới"}
        width={700}
        onClose={() => {
          setShowCreateForm(false);
          setEditingStaff(null);
          form.resetFields();
          setSelectedRole(null);
        }}
        open={showCreateForm}
        footer={
          <div style={{ textAlign: 'right' }}>
            <Space>
              <Button onClick={() => setShowCreateForm(false)}>Hủy</Button>
              <Button type="primary" onClick={() => form.submit()} loading={loading}>
                {editingStaff ? 'Cập nhật' : 'Tạo mới'}
              </Button>
            </Space>
          </div>
        }
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          requiredMark={false}
        >
          <Alert
            message="Lưu ý"
            description="Mật khẩu mặc định sẽ được gửi qua email. Nhân viên cần đổi mật khẩu khi đăng nhập lần đầu."
            type="info"
            showIcon
            style={{ marginBottom: '24px' }}
          />

          <Divider orientation="left">Thông tin cơ bản</Divider>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item 
                label="Chức vụ" 
                name="role" 
                rules={[{ required: true, message: 'Vui lòng chọn chức vụ!' }]}
              >
                <Select 
                  placeholder="Chọn chức vụ"
                  onChange={handleRoleChange}
                >
                  <Option value="DOCTOR">
                    <Space>
                      <MedicineBoxOutlined />
                      Bác sĩ
                    </Space>
                  </Option>
                  <Option value="STAFF">
                    <Space>
                      <TeamOutlined />
                      Nhân viên
                    </Space>
                  </Option>
                </Select>
              </Form.Item>
            </Col>
            
            <Col span={12}>
              <Form.Item 
                label="Họ và tên" 
                name="fullName" 
                rules={[{ required: true, message: 'Vui lòng nhập họ tên!' }]}
              >
                <Input 
                  prefix={<UserOutlined />}
                  placeholder="VD: Nguyễn Văn A"
                />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={8}>
              <Form.Item 
                label="Ngày sinh" 
                name="dateOfBirth" 
                rules={[{ required: true, message: 'Vui lòng chọn ngày sinh!' }]}
              >
                <DatePicker 
                  format="DD/MM/YYYY" 
                  style={{ width: "100%" }}
                  placeholder="Chọn ngày sinh"
                />
              </Form.Item>
            </Col>
            
            <Col span={8}>
              <Form.Item 
                label="Giới tính" 
                name="gender" 
                rules={[{ required: true, message: 'Vui lòng chọn giới tính!' }]}
              >
                <Radio.Group>
                  <Radio value="Nam">Nam</Radio>
                  <Radio value="Nữ">Nữ</Radio>
                  <Radio value="Khác">Khác</Radio>
                </Radio.Group>
              </Form.Item>
            </Col>
            
            <Col span={8}>
              <Form.Item 
                label="CCCD/CMND" 
                name="citizenId" 
                rules={[{ required: true, message: 'Vui lòng nhập CCCD/CMND!' }]}
              >
                <Input 
                  prefix={<IdcardOutlined />}
                  placeholder="VD: 123456789012"
                />
              </Form.Item>
            </Col>
          </Row>

          <Divider orientation="left">Phân công công việc</Divider>
          
          {selectedRole === 'DOCTOR' && (
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item 
                  label="Chuyên khoa" 
                  name="specialty"
                  rules={[{ required: true, message: 'Vui lòng chọn chuyên khoa!' }]}
                >
                  <Select 
                    placeholder="Chọn chuyên khoa"
                    showSearch
                    filterOption={(input, option) =>
                      option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                    }
                  >
                    {specialties.map(specialty => (
                      <Option key={specialty} value={specialty}>{specialty}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item 
                  label="Phòng ban" 
                  name="department"
                  rules={[{ required: true, message: 'Vui lòng chọn phòng ban!' }]}
                >
                  <Select placeholder="Chọn phòng ban">
                    {departments.map(dept => (
                      <Option key={dept} value={dept}>{dept}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
            </Row>
          )}

          {selectedRole === 'STAFF' && (
            <Row gutter={16}>
              <Col span={12}>
                <Form.Item 
                  label="Chức danh" 
                  name="position"
                  rules={[{ required: true, message: 'Vui lòng nhập chức danh!' }]}
                >
                  <Select placeholder="Chọn chức danh">
                    <Option value="Điều dưỡng">Điều dưỡng</Option>
                    <Option value="Kỹ thuật viên">Kỹ thuật viên</Option>
                    <Option value="Dược sĩ">Dược sĩ</Option>
                    <Option value="Nhân viên hành chính">Nhân viên hành chính</Option>
                    <Option value="Nhân viên kế toán">Nhân viên kế toán</Option>
                    <Option value="Bảo vệ">Bảo vệ</Option>
                    <Option value="Lao công">Lao công</Option>
                    <Option value="Lái xe">Lái xe</Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item 
                  label="Phòng ban" 
                  name="department"
                  rules={[{ required: true, message: 'Vui lòng chọn phòng ban!' }]}
                >
                  <Select placeholder="Chọn phòng ban">
                    {departments.map(dept => (
                      <Option key={dept} value={dept}>{dept}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
            </Row>
          )}

          <Divider orientation="left">Thông tin liên hệ</Divider>
          
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item 
                label="Email" 
                name="email" 
                rules={[
                  { required: true, message: 'Vui lòng nhập email!' },
                  { type: "email", message: 'Email không hợp lệ!' }
                ]}
              >
                <Input 
                  prefix={<MailOutlined />}
                  placeholder="VD: bs.nguyen@hospital.com"
                />
              </Form.Item>
            </Col>
            
            <Col span={12}>
              <Form.Item 
                label="Số điện thoại" 
                name="phone" 
                rules={[{ required: true, message: 'Vui lòng nhập số điện thoại!' }]}
              >
                <Input 
                  prefix={<PhoneOutlined />}
                  placeholder="VD: 0909 123 456"
                />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item 
            label="Địa chỉ" 
            name="address"
            rules={[{ required: true, message: 'Vui lòng nhập địa chỉ!' }]}
          >
            <TextArea 
              placeholder="VD: 123 Trần Hưng Đạo, Phường Bến Thành, Quận 1, TP.HCM" 
              rows={2}
            />
          </Form.Item>

          {!editingStaff && (
            <>
              <Divider orientation="left">Thông tin tài khoản</Divider>
              
              <Row gutter={16}>
                <Col span={12}>
                  <Form.Item 
                    label="Tên đăng nhập" 
                    name="username" 
                    rules={[{ required: true, message: 'Vui lòng nhập tên đăng nhập!' }]}
                  >
                    <Input 
                      prefix={<UserOutlined />}
                      placeholder="VD: bs.nguyen2024"
                    />
                  </Form.Item>
                </Col>
                
                <Col span={12}>
                  <Form.Item 
                    label="Mật khẩu tạm thời" 
                    name="password" 
                    rules={[{ required: true, message: 'Vui lòng nhập mật khẩu!' }]}
                  >
                    <Input.Password 
                      prefix={<LockOutlined />}
                      placeholder="Ít nhất 8 ký tự"
                    />
                  </Form.Item>
                </Col>
              </Row>
            </>
          )}

          <Form.Item label="Ghi chú" name="note">
            <TextArea 
              placeholder="Ghi chú thêm về nhân viên (nếu có)..." 
              rows={3}
            />
          </Form.Item>
        </Form>
      </Drawer>

      {/* Modal xem chi tiết nhân viên */}
      <Modal
        title={`Thông tin chi tiết - ${viewingStaff?.fullName}`}
        open={!!viewingStaff}
        onCancel={() => setViewingStaff(null)}
        footer={[
          <Button key="close" onClick={() => setViewingStaff(null)}>
            Đóng
          </Button>
        ]}
        width={600}
      >
        {viewingStaff && (
          <div>
            <Row gutter={16}>
              <Col span={6}>
                <Avatar 
                  size={80} 
                  src={viewingStaff.avatar} 
                  icon={<UserOutlined />}
                  style={{ backgroundColor: viewingStaff.role === 'DOCTOR' ? '#52c41a' : '#1890ff' }}
                />
              </Col>
              <Col span={18}>
                <Title level={4}>{viewingStaff.fullName}</Title>
                <Tag color={viewingStaff.role === 'DOCTOR' ? 'green' : 'blue'}>
                  {viewingStaff.role === 'DOCTOR' ? 'Bác sĩ' : 'Nhân viên'}
                </Tag>
                <Tag color={viewingStaff.status === 'ACTIVE' ? 'success' : 'error'}>
                  {viewingStaff.status === 'ACTIVE' ? 'Đang làm việc' : 'Nghỉ việc'}
                </Tag>
              </Col>
            </Row>
            
            <Divider />
            
            <Row gutter={16}>
              <Col span={12}>
                <Text strong>Mã nhân viên:</Text> {viewingStaff.staffId}
              </Col>
              <Col span={12}>
                <Text strong>Tên đăng nhập:</Text> {viewingStaff.username}
              </Col>
            </Row>
            
            <Row gutter={16} style={{ marginTop: '16px' }}>
              <Col span={12}>
                <Text strong>Ngày sinh:</Text> {dayjs(viewingStaff.dateOfBirth).format('DD/MM/YYYY')}
              </Col>
              <Col span={12}>
                <Text strong>Giới tính:</Text> {viewingStaff.gender}
              </Col>
            </Row>
            
            <Row gutter={16} style={{ marginTop: '16px' }}>
              <Col span={12}>
                <Text strong>CCCD/CMND:</Text> {viewingStaff.citizenId}
              </Col>
              <Col span={12}>
                <Text strong>Ngày vào làm
                    :</Text> {dayjs(viewingStaff.joinDate).format('DD/MM/YYYY')}
              </Col>
            </Row>
            
            <Row gutter={16} style={{ marginTop: '16px' }}>
              <Col span={12}>
                <Text strong>Email:</Text> {viewingStaff.email}
              </Col>
              <Col span={12}>
                <Text strong>Số điện thoại:</Text> {viewingStaff.phone}
              </Col>
            </Row>
            
            <Row gutter={16} style={{ marginTop: '16px' }}>
              <Col span={12}>
                <Text strong>Phòng ban:</Text> {viewingStaff.department}
              </Col>
              <Col span={12}>
                <Text strong>
                  {viewingStaff.role === 'DOCTOR' ? 'Chuyên khoa:' : 'Chức danh:'}
                </Text> {viewingStaff.specialty || viewingStaff.position}
              </Col>
            </Row>
            
            <Row gutter={16} style={{ marginTop: '16px' }}>
              <Col span={24}>
                <Text strong>Địa chỉ:</Text><br />
                {viewingStaff.address}
              </Col>
            </Row>
            
            {viewingStaff.note && (
              <Row gutter={16} style={{ marginTop: '16px' }}>
                <Col span={24}>
                  <Text strong>Ghi chú:</Text><br />
                  {viewingStaff.note}
                </Col>
              </Row>
            )}
          </div>
        )}
      </Modal>
    </Layout>
  );
};

export default StaffManagement;