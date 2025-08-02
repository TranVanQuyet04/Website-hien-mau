import React, { useState, useEffect } from 'react';
import {
  Layout,
  Table,
  Tag,
  Button,
  Modal,
  Space,
  Card,
  Typography,
  Row,
  message,
  Col
} from 'antd';
import {
  ExperimentOutlined,
  UserOutlined,
  CalendarOutlined
} from '@ant-design/icons';
import axios from 'axios';

const { Header, Content } = Layout;
const { Title, Text } = Typography;

const BloodUnitList = () => {
  const [units, setUnits] = useState([]);
  const [expiredUnits, setExpiredUnits] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [bloodBagMap, setBloodBagMap] = useState({});

  const statusMap = {
    AVAILABLE: { label: 'Sẵn sàng', color: 'green' },
    USED: { label: 'Đã sử dụng', color: 'blue' },
    EXPIRED: { label: 'Hết hạn', color: 'red' }
  };

  const isExpired = (component, createdAt) => {
    try {
      const [year, month, day, hour, minute, second] = createdAt;
      const createdDate = new Date(year, month - 1, day, hour, minute, second);
      const now = new Date();
      const days = (now - createdDate) / (1000 * 60 * 60 * 24);
      switch (component) {
        case 'Hồng cầu': return days > 35;
        case 'Huyết tương': return days > 365;
        case 'Tiểu cầu': return days > 5;
        default: return false;
      }
    } catch {
      return false;
    }
  };

  const fetchBloodBagById = async (bloodId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/blood-bags/${bloodId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      return response.data;
    } catch (error) {
      console.error(`❌ Lỗi khi lấy túi máu ID ${bloodId}:`, error);
      return null;
    }
  };

  useEffect(() => {
    const fetchUnits = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(`http://localhost:8080/api/blood-units`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        const allUnits = res.data || [];
        const available = allUnits.filter(unit => unit.status === 'AVAILABLE');
        const expired = available.filter(unit => isExpired(unit.componentName, unit.createdAt));

        setUnits(available);
        setExpiredUnits(expired);

        const map = {};
        const fetchedBagIds = new Set();
        await Promise.all(available.map(async (unit) => {
          const bagId = unit.bloodBagId;
          if (bagId && !fetchedBagIds.has(bagId)) {
            const bag = await fetchBloodBagById(bagId);
            if (bag) {
              map[bagId] = bag.bagCode;
              fetchedBagIds.add(bagId);
            }
          }
        }));
        setBloodBagMap(map);

      } catch (error) {
        console.error("❌ Lỗi khi load đơn vị máu:", error);
        message.error("Không thể tải đơn vị máu.");
      }
    };

    fetchUnits();
  }, []);

  const markExpired = (id) => {
    Modal.confirm({
      title: 'Bạn có chắc muốn đánh dấu đơn vị máu này là hết hạn?',
      content: 'Thao tác này sẽ cập nhật trạng thái của đơn vị máu.',
      okText: 'Đồng ý',
      cancelText: 'Không',
      onOk: async () => {
        try {
          const token = localStorage.getItem('token');
          await axios.put(`http://localhost:8080/api/blood-units/${id}/mark-expired`, {}, {
            headers: { Authorization: `Bearer ${token}` }
          });
          message.success(`✅ Đã đánh dấu đơn vị ${id} là hết hạn.`);
        } catch (error) {
          message.error("❌ Lỗi khi đánh dấu hết hạn.");
        }
      }
    });
  };

  const expiredColumns = [
    {
      title: 'Mã túi máu',
      dataIndex: 'bloodBagId',
      key: 'bloodBagId',
      width: 130,
      render: (id) => bloodBagMap[id] || 'Đang tải...'
    },
    { title: 'ID', dataIndex: 'bloodUnitId', key: 'id' },
    { title: 'Mã đơn vị', dataIndex: 'unitCode', key: 'unitCode' },
    { title: 'Thành phần', dataIndex: 'componentName', key: 'componentName' },
    { title: 'Ngày tạo', dataIndex: 'createdAt', key: 'createdAt',
      render: (val) => {
        try {
          const [y, m, d, h, min, s] = val;
          return new Date(y, m - 1, d, h, min, s).toLocaleString('vi-VN');
        } catch {
          return 'Không xác định';
        }
      }
    },
    {
      title: 'Thao tác',
      render: (_, record) => (
        <Button type="link" danger onClick={() => markExpired(record.bloodUnitId)}>
          Đánh dấu hết hạn
        </Button>
      )
    }
  ];

  const columns = [
    {
      title: 'Mã túi máu',
      dataIndex: 'bloodBagId',
      key: 'bloodBagId',
      width: 130,
      render: (id) => bloodBagMap[id] || 'Đang tải...'
    },
    {
      title: 'ID',
      dataIndex: 'bloodUnitId',
      key: 'bloodUnitId',
      width: 80,
    },
    {
      title: 'Mã đơn vị',
      dataIndex: 'unitCode',
      key: 'unitCode',
      width: 140,
      ellipsis: true,
    },
    {
      title: 'Nhóm máu',
      dataIndex: 'bloodTypeName',
      key: 'bloodTypeName',
      width: 80,
    },
    {
      title: 'Thành phần',
      dataIndex: 'componentName',
      key: 'componentName',
      width: 100,
      ellipsis: true,
    },
    {
      title: 'Dung tích (ml)',
      dataIndex: 'quantityMl',
      key: 'quantityMl',
      width: 100,
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 180,
      render: (val) => {
        try {
          const [year, month, day, hour, minute, second] = val;
          return new Date(year, month - 1, day, hour, minute, second).toLocaleString('vi-VN');
        } catch {
          return 'Không xác định';
        }
      }
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: status => {
        const info = statusMap[status] || { label: 'Không xác định', color: 'default' };
        return <Tag color={info.color}>{info.label}</Tag>;
      }
    }
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <ExperimentOutlined style={{ marginRight: 8 }} />
              Quản lý kho máu
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
        <Card
          title={
            <Space>
              <span>Đơn vị máu sẵn sàng</span>
              <Button type="primary" onClick={() => setModalVisible(true)}>
                Xem đơn vị máu hết hạn
              </Button>
            </Space>
          }
        >
          <Table
            rowKey={record => record.bloodUnitId ?? record.unitCode}
            dataSource={units}
            columns={columns}
            pagination={{ pageSize: 10 }}
            bordered
            scroll={{ x: 'max-content' }}
            locale={{ emptyText: 'Không có dữ liệu đơn vị máu' }}
          />
        </Card>

        <Modal
          title="Đơn vị máu đã hết hạn"
          open={modalVisible}
          onCancel={() => setModalVisible(false)}
          footer={null}
          width={800}
        >
          <Table
            rowKey={(r) => r.bloodUnitId}
            dataSource={expiredUnits}
            columns={expiredColumns}
            pagination={false}
            bordered
          />
        </Modal>
      </Content>
    </Layout>
  );
};

export default BloodUnitList;
