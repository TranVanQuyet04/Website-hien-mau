import React, { useState, useEffect } from "react";
import { Table, Button, message, Tooltip, Tag } from "antd";
import { CheckOutlined, CloseOutlined, InfoCircleOutlined } from "@ant-design/icons";
import axios from "axios";

const UrgentRequests = () => {
  const [donors, setDonors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedDonor, setSelectedDonor] = useState(null);

  // Lấy danh sách người hiến cần xác minh
  const fetchDonors = async () => {
    setLoading(true);
    try {
      const res = await axios.get("http://localhost:8080/api/staff/verify-donors");
      setDonors(res.data);
    } catch (error) {
      message.error("Lỗi tải danh sách người hiến máu");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDonors();
  }, []);

  // Xác minh người hiến
  const verifyDonor = async (donorId) => {
    try {
      await axios.put(`http://localhost:8080/api/staff/verify-donors/${donorId}/verify`);
      message.success("Xác minh thành công");
      fetchDonors();
    } catch (error) {
      message.error("Lỗi khi xác minh người hiến");
      console.error(error);
    }
  };

  // Từ chối người hiến
  const rejectDonor = async (donorId) => {
    try {
      await axios.delete(`http://localhost:8080/api/staff/verify-donors/${donorId}/reject`);
      message.success("Từ chối thành công");
      fetchDonors();
    } catch (error) {
      message.error("Lỗi khi từ chối người hiến");
      console.error(error);
    }
  };

  const columns = [
    { title: "Họ tên", dataIndex: "fullName", key: "fullName" },
    { title: "SĐT", dataIndex: "phone", key: "phone" },
    { title: "Nhóm máu", dataIndex: "bloodType", key: "bloodType" },
    { title: "Giới tính", dataIndex: "gender", key: "gender" },
    { title: "Ngày sinh", dataIndex: "dob", key: "dob" },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        let color = "default";
        if(status === "CHỜ XÁC MINH") color = "orange";
        else if(status === "ĐÃ XÁC MINH") color = "green";
        else if(status === "TỪ CHỐI") color = "red";
        return <Tag color={color}>{status}</Tag>;
      }
    },
    {
      title: "Hành động",
      key: "actions",
      render: (_, record) => (
        <>
          <Tooltip title="Xác minh">
            <Button
              type="primary"
              icon={<CheckOutlined />}
              onClick={() => verifyDonor(record.donorRegistryId)}
              style={{ marginRight: 8 }}
            />
          </Tooltip>
          <Tooltip title="Từ chối">
            <Button
              type="danger"
              icon={<CloseOutlined />}
              onClick={() => rejectDonor(record.donorRegistryId)}
            />
          </Tooltip>
          {/* <Tooltip title="Xem chi tiết">
            <Button
              icon={<InfoCircleOutlined />}
              onClick={() => setSelectedDonor(record)}
              style={{ marginLeft: 8 }}
            />
          </Tooltip> */}
        </>
      ),
    }
  ];

  return (
    <>
      <Table
        dataSource={donors}
        columns={columns}
        loading={loading}
        rowKey="donorRegistryId"
        pagination={{ pageSize: 5 }}
      />
      {/* Modal xem chi tiết */}
      {/* Tùy bạn thêm modal hiện chi tiết người hiến nếu muốn */}
    </>
  );
};

export default UrgentRequests;