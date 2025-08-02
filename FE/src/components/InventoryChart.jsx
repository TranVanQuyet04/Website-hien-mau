import React, { useEffect, useState } from "react";
import { Bar, Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
} from "chart.js";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import {
  Row,
  Col,
  Select,
  Card,
  Typography,
  Button,
  Modal,
  Table,
  message,
  theme,
} from "antd";
import {
  FileExcelOutlined,
  InfoCircleOutlined,
  ExclamationCircleOutlined,
  WarningOutlined,
} from "@ant-design/icons";
import "../styles/staff.css";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend
);

const { Title, Text } = Typography;

const InventoryChart = () => {
  const [rawData, setRawData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [historyData, setHistoryData] = useState([]);
  const [bloodType, setBloodType] = useState("");
  const [component, setComponent] = useState("");
  const [orientation, setOrientation] = useState("y");
  const [summary, setSummary] = useState({ totalBlood: 0, lowStockTypes: [] });
  const [modalOpen, setModalOpen] = useState(false);
  const [modalContent, setModalContent] = useState([]);
  const [expiredBloodUnits, setExpiredBloodUnits] = useState([]);

  const { token } = theme.useToken();

  const isExpired = (component, createdAt) => {
    const createdDate = new Date(createdAt);
    const now = new Date();
    const diffDays = (now - createdDate) / (1000 * 60 * 60 * 24);
    switch (component) {
      case "Hồng cầu":
        return diffDays > 35;
      case "Huyết tương":
        return diffDays > 365;
      case "Tiểu cầu":
        return diffDays > 5;
      default:
        return false;
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const bloodUnitsRes = await fetch("/api/blood-units");
        const bloodUnits = await bloodUnitsRes.json();
        setRawData(bloodUnits);
        setModalContent(bloodUnits);
        setModalOpen(true);

        const expired = bloodUnits.filter((unit) =>
          isExpired(unit.component, unit.createdAt)
        );
        setExpiredBloodUnits(expired);
      } catch (error) {
        message.error("Không thể tải dữ liệu chi tiết tồn kho máu.");
      }
    };
    fetchData();
  }, []);

  const openBloodUnitsDetails = async () => {
    try {
      const res = await fetch("/api/blood-units");
      const data = await res.json();
      setModalContent(data);
      setModalOpen(true);
    } catch (error) {
      message.error("Không thể tải dữ liệu chi tiết tồn kho máu.");
    }
  };

  useEffect(() => {
    const filtered = rawData.filter(
      (item) =>
        (!bloodType || item.blood_type === bloodType) &&
        (!component || item.component === component)
    );
    setFilteredData(filtered);
    updateSummary(filtered);
  }, [bloodType, component, rawData]);

  const updateSummary = (data) => {
    let total = 0;
    const lowStock = [];
    data.forEach((item) => {
      if (item.total_quantity_ml != null) {
        total += item.total_quantity_ml;
        if (item.total_quantity_ml < 500) lowStock.push(item);
      }
    });
    setSummary({ totalBlood: total, lowStockTypes: lowStock });
  };

  const exportToExcel = () => {
    const ws = XLSX.utils.json_to_sheet(filteredData);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Tồn kho máu");
    const buffer = XLSX.write(wb, { bookType: "xlsx", type: "array" });
    saveAs(
      new Blob([buffer], { type: "application/octet-stream" }),
      "bao_cao_ton_kho_mau.xlsx"
    );
  };

  const openDetails = (data) => {
    setModalContent(data);
    setModalOpen(true);
  };

  const columns = [
    {
      title: "Nhóm máu",
      dataIndex: "blood_type",
    },
    {
      title: "Thành phần",
      dataIndex: "component",
    },
    {
      title: "Tổng lượng (ml)",
      dataIndex: "total_quantity_ml",
      render: (value) => <Text strong>{value}</Text>,
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <Title level={3}>🔬 Kiểm tra tồn kho máu</Title>

      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={6}>
          <Select
            placeholder="Chọn nhóm máu"
            value={bloodType || undefined}
            onChange={(val) => setBloodType(val)}
            style={{ width: "100%" }}
            allowClear
          >
            {["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"].map((type) => (
              <Select.Option key={type} value={type}>
                {type}
              </Select.Option>
            ))}
          </Select>
        </Col>
        <Col span={6}>
          <Select
            placeholder="Chọn thành phần máu"
            value={component || undefined}
            onChange={(val) => setComponent(val)}
            style={{ width: "100%" }}
            allowClear
          >
            {["Hồng cầu", "Tiểu cầu", "Huyết tương"].map((c) => (
              <Select.Option key={c} value={c}>
                {c}
              </Select.Option>
            ))}
          </Select>
        </Col>
        <Col span={6}>
          <Select
            value={orientation}
            onChange={setOrientation}
            style={{ width: "100%" }}
          >
            <Select.Option value="y">🔄 Biểu đồ ngang</Select.Option>
            <Select.Option value="x">⬆️ Biểu đồ dọc</Select.Option>
          </Select>
        </Col>
        <Col span={6}>
          <Button
            icon={<FileExcelOutlined />}
            type="primary"
            block
            onClick={exportToExcel}
          >
            Xuất Excel
          </Button>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={8}>
          <Card title="Tổng lượng máu trong kho" bordered>
            <Text strong>{summary.totalBlood} ml</Text>
            <Button
              type="link"
              icon={<InfoCircleOutlined />}
              onClick={openBloodUnitsDetails}
            >
              Nhập chi tiết tồn kho máu
            </Button>
          </Card>
        </Col>
        <Col span={8}>
          <Card title="Nhóm máu thiếu hụt" bordered>
            <Text type="danger">{summary.lowStockTypes.length} nhóm</Text>
            <Button
              type="link"
              icon={<ExclamationCircleOutlined />}
              onClick={() => openDetails(summary.lowStockTypes)}
            >
              Xem chi tiết
            </Button>
          </Card>
        </Col>
        <Col span={8}>
          <Card title="Đơn vị máu hết hạn" bordered>
            <Text type="danger">{expiredBloodUnits.length} đơn vị</Text>
            <Button
              type="link"
              icon={<WarningOutlined />}
              onClick={() => openDetails(expiredBloodUnits)}
            >
              Xem chi tiết
            </Button>
          </Card>
        </Col>
      </Row>

      <div style={{ height: 400, marginTop: 32 }}>
        <Bar
          data={{
            labels: filteredData.map((item) => `${item.blood_type} - ${item.component}`),
            datasets: [
              {
                label: "Tồn kho (ml)",
                data: filteredData.map((item) => item.total_quantity_ml),
                backgroundColor: filteredData.map((item) =>
                  item.total_quantity_ml < 500
                    ? "#ff4d4f"
                    : item.total_quantity_ml < 2000
                    ? "#faad14"
                    : "#52c41a"
                ),
              },
            ],
          }}
          options={{
            indexAxis: orientation,
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              tooltip: {
                callbacks: {
                  label: (ctx) => `Tồn kho: ${ctx.raw} ml`,
                },
              },
            },
          }}
        />
      </div>

      {historyData.length > 0 && (
        <div style={{ marginTop: 48 }}>
          <Title level={4}>📈 Biến động tồn kho theo ngày</Title>
          <Line
            data={{
              labels: historyData.map((h) => h.date),
              datasets: [
                {
                  label: "Hồng cầu",
                  data: historyData.map((h) => h.red_cells || 0),
                  borderColor: "#ff4d4f",
                  fill: false,
                },
                {
                  label: "Tiểu cầu",
                  data: historyData.map((h) => h.platelets || 0),
                  borderColor: "#1890ff",
                  fill: false,
                },
                {
                  label: "Huyết tương",
                  data: historyData.map((h) => h.plasma || 0),
                  borderColor: "#52c41a",
                  fill: false,
                },
              ],
            }}
          />
        </div>
      )}

      <Modal
        title="Chi tiết tồn kho máu"
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        footer={null}
        width={700}
      >
        <Table
          rowKey={(record, index) => index}
          columns={columns}
          dataSource={modalContent.sort((a, b) => a.total_quantity_ml - b.total_quantity_ml)}
          pagination={false}
          bordered
        />
      </Modal>
    </div>
  );
};

export default InventoryChart;
