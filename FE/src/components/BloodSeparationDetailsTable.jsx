import React, { useState, useEffect } from 'react';
import { Table, Tag, Button, DatePicker, Select, Modal, Descriptions, Space } from 'antd';
import { EyeOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';

const { RangePicker } = DatePicker;
const { Option } = Select;

const mockData = [
  {
    id: 1,
    unitCode: 'U001',
    bagCode: 'B001',
    component: 'Hồng cầu',
    volume: 250,
    qualityRating: 'Tốt',
    createdAt: '2025-07-12',
    note: 'Không có',
  },
  {
    id: 2,
    unitCode: 'U002',
    bagCode: 'B002',
    component: 'Tiểu cầu',
    volume: 70,
    qualityRating: 'Trung bình',
    createdAt: '2025-07-11',
    note: 'Tách bằng máy',
  },
  {
    id: 3,
    unitCode: 'U003',
    bagCode: 'B003',
    component: 'Huyết tương',
    volume: 200,
    qualityRating: 'Kém',
    createdAt: '2025-07-10',
    note: 'Bị đông một phần',
  },
];

const SeparationDetailTable = () => {
  const [data, setData] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [quality, setQuality] = useState(null);
  const [dateRange, setDateRange] = useState([]);
  const [selectedDetail, setSelectedDetail] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);

  useEffect(() => {
    setData(mockData);
    setFiltered(mockData);
  }, []);

  useEffect(() => {
    let result = [...data];

    if (quality) {
      result = result.filter((item) => item.qualityRating === quality);
    }

    if (dateRange && dateRange.length === 2) {
      const [start, end] = dateRange;
      result = result.filter((item) => {
        const date = dayjs(item.createdAt);
        return date.isAfter(start.subtract(1, 'day')) && date.isBefore(end.add(1, 'day'));
      });
    }

    setFiltered(result);
  }, [quality, dateRange, data]);

  const columns = [
    { title: 'Mã đơn vị', dataIndex: 'unitCode' },
    { title: 'Mã túi máu', dataIndex: 'bagCode' },
    { title: 'Thành phần', dataIndex: 'component' },
    { title: 'Thể tích (ml)', dataIndex: 'volume' },
    {
      title: 'Chất lượng',
      dataIndex: 'qualityRating',
      render: (text) => {
        const color = text === 'Tốt' ? 'green' : text === 'Trung bình' ? 'orange' : 'red';
        return <Tag color={color}>{text}</Tag>;
      },
    },
    { title: 'Ngày tách', dataIndex: 'createdAt' },
    {
      title: 'Hành động',
      render: (_, record) => (
        <Button icon={<EyeOutlined />} onClick={() => handleDetail(record)}>
          Xem chi tiết
        </Button>
      ),
    },
  ];

  const handleDetail = (record) => {
    // Gọi API thật tại đây nếu muốn → getDetail(record.id)
    setSelectedDetail(record);
    setModalVisible(true);
  };

  return (
    <>
      <Space style={{ marginBottom: 16 }}>
        <RangePicker onChange={(dates) => setDateRange(dates)} />
        <Select
          placeholder="Lọc theo chất lượng"
          allowClear
          onChange={(value) => setQuality(value)}
          style={{ width: 180 }}
        >
          <Option value="Tốt">Tốt</Option>
          <Option value="Trung bình">Trung bình</Option>
          <Option value="Kém">Kém</Option>
        </Select>
      </Space>

      <Table
        columns={columns}
        dataSource={filtered}
        rowKey="id"
        bordered
        pagination={{ pageSize: 5 }}
      />

      <Modal
        open={modalVisible}
        title="Chi tiết đơn vị máu"
        footer={null}
        onCancel={() => setModalVisible(false)}
      >
        {selectedDetail && (
          <Descriptions column={1}>
            <Descriptions.Item label="Mã đơn vị">{selectedDetail.unitCode}</Descriptions.Item>
            <Descriptions.Item label="Mã túi máu">{selectedDetail.bagCode}</Descriptions.Item>
            <Descriptions.Item label="Thành phần">{selectedDetail.component}</Descriptions.Item>
            <Descriptions.Item label="Thể tích">{selectedDetail.volume} ml</Descriptions.Item>
            <Descriptions.Item label="Chất lượng">{selectedDetail.qualityRating}</Descriptions.Item>
            <Descriptions.Item label="Ngày tách">{selectedDetail.createdAt}</Descriptions.Item>
            <Descriptions.Item label="Ghi chú">{selectedDetail.note}</Descriptions.Item>
          </Descriptions>
        )}
      </Modal>
    </>
  );
};

export default SeparationDetailTable;