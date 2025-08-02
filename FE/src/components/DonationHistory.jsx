import React, { useEffect, useState } from "react";
import {
  Table,
  Modal,
  Typography,
  Tag,
  Descriptions,
  message,
  Layout,
  Row,
  Col,
  Space,
} from "antd";
import { HistoryOutlined,CalendarOutlined,UserOutlined } from "@ant-design/icons";
import axios from "axios";
import dayjs from "dayjs";
import * as XLSX from "xlsx";

const { Title,Text } = Typography;
const { Header, Content } = Layout;


const DonationHistory = () => {
  const [history, setHistory] = useState([]);
  const [selected, setSelected] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
const donatedHistory = history.filter((item) => item.status === "DONATED");
  useEffect(() => {
  const token = localStorage.getItem("token"); // hoặc nơi bạn lưu token
  axios
    .get(`http://localhost:8080/api/donation`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    .then((res) => {
      console.log("✅ Data từ API:", res.data);
      setHistory(Array.isArray(res.data) ? res.data : []);
    })
    .catch((err) => {
      console.error("❌ Lỗi API:", err);
      message.error("Không thể tải danh sách hiến máu.");
    });
}, []);



  const handleExportExcel = () => {
    const exportData = history.map((h) => ({
      "Ngày hiến": dayjs(h.donation_date).format("DD/MM/YYYY"),
      "Địa điểm": h.location,
      "Thể tích": `${h.volume_ml} ml`,
      "Nhóm máu": h.blood_type,
      "Trạng thái": h.status,
    }));

    const worksheet = XLSX.utils.json_to_sheet(exportData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "LichSuHienMau");

    XLSX.writeFile(workbook, "lich_su_hien_mau.xlsx");
  };

  const columns = [
    {
      title: "🗓 Ngày",
      dataIndex: "donation_date",
      render: (date) => dayjs(date).format("DD/MM/YYYY"),
    },
    {
      title: "🏥 Địa điểm",
      dataIndex: "location",
    },
    // {
    //   title: "🩸 Thể tích",
    //   dataIndex: "volume_ml",
    //   render: (v) => `${v}ml`,
    // },
    // {
    //   title: "🧬 Nhóm máu",
    //   dataIndex: "blood_type",
    // },
    {
      title: "📦 Trạng thái",
      dataIndex: "status",
      render: (status) => (
        <Tag color={status === "DONATED" || status === "CONFIRMED" ? "green" : "orange"}>
          {status === "DONATED" ? "Đã hiến" : status}
        </Tag>
      ),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header style={{ background: '#fff', padding: '0 24px', borderBottom: '1px solid #f0f0f0' }}>
        <Row justify="space-between" align="middle">
          <Col>
            <Title level={3} style={{ margin: 0, color: '#1890ff' }}>
              <HistoryOutlined style={{ marginRight: 8 }} />
              Lịch sử Hiến máu
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
      <Table
        columns={columns}
        dataSource={history.filter((item) => item.status === "DONATED")}
        rowKey={(record) => record.id}
        onRow={(record) => ({
          onClick: () => {
            setSelected(record);
            setModalVisible(true);
          },
        })}
        pagination={{ pageSize: 5 }}
        locale={{ emptyText: "Không có lịch sử hiến máu." }}
      />

      <Modal
        title="📋 Chi tiết lần hiến máu"
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        {selected && (
          <Descriptions column={1} bordered size="small">
            <Descriptions.Item label="👤 Người hiến">
  {selected.fullName || "—"}
</Descriptions.Item>
            <Descriptions.Item label="🗓 Ngày hiến">
              {dayjs(selected.donation_date).format("DD/MM/YYYY")}
            </Descriptions.Item>
            <Descriptions.Item label="🏥 Địa điểm">{selected.location}</Descriptions.Item>
            {/* <Descriptions.Item label="🧬 Nhóm máu">{selected.blood_type}</Descriptions.Item>
            <Descriptions.Item label="🩸 Thể tích">{selected.volume_ml} ml</Descriptions.Item> */}
            <Descriptions.Item label="📝 Ghi chú">{selected.note || "Không có"}</Descriptions.Item>
            {/* <Descriptions.Item label="🧪 Đơn vị máu sinh ra">
              {(selected.blood_units && selected.blood_units.length > 0)
                ? selected.blood_units.join(", ")
                : "Chưa có"}
            </Descriptions.Item> */}
          </Descriptions>
        )}
      </Modal>
      </Content>
</Layout>
  );
};

export default DonationHistory;
