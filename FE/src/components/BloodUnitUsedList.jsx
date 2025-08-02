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
  Row, message,
  Col
} from 'antd';
import {
  ExperimentOutlined,
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


const BloodUnitUsedList = () => {
  const [units, setUnits] = useState([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [selectedRows, setSelectedRows] = useState([]);
  const [bloodBagMap, setBloodBagMap] = useState({});
  const statusMap = {
    AVAILABLE: {
      label: 'Sẵn sàng',
      color: 'green',
    },
    USED: {
      label: 'Đã sử dụng',
      color: 'blue',
    },
    EXPIRED: {
      label: 'Hết hạn',
      color: 'red',
    }
  };
  const fetchBloodBagById = async (bloodId) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/blood-bags/${bloodId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
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

        const filtered = (res.data || []).filter(unit => unit.status !== 'AVAILABLE');
        setUnits(filtered);

        // Gọi song song thông tin túi máu
        const map = {};
        const fetchedBagIds = new Set();

        await Promise.all(filtered.map(async (unit) => {
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

  const columns = [
    {
      title: 'Mã túi máu',
      dataIndex: 'bloodBagId',
      key: 'bloodBagId',
      width: 130,
      render: (id) => bloodBagMap[id] || 'Đang tải...'
    }
    ,
    {
      title: 'ID',
      dataIndex: 'bloodUnitId', // Đảm bảo đúng key từ DTO
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
          if (!Array.isArray(val) || val.length < 6) return 'Không xác định';
          const [year, month, day, hour, minute, second] = val;
          const date = new Date(year, month - 1, day, hour, minute, second); // Lưu ý: month - 1
          return date.toLocaleString('vi-VN');
        } catch {
          return 'Không xác định';
        }
      }
    }
    ,
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: status => {
        const info = statusMap[status] || { label: 'Không xác định', color: 'default' };
        return (
          <Tag color={info.color}>
            {info.label}
          </Tag>
        );
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
              Quản lý  máu Hết hạn sử dụng
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
                 Nhân viên
              </Text>
            </Space>
          </Col>
        </Row>
      </Header>

      <Content style={{ padding: '24px' }}>
        <Card>

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
      </Content>
    </Layout>
  );
};

export default BloodUnitUsedList; 
