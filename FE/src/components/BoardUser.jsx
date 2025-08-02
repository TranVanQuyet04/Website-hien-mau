import React, { useEffect, useState } from "react";
import { Card, Descriptions, Typography, Alert, Spin } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { getAuthHeader } from "../services/user.service";
import axios from "axios";

const { Title } = Typography;

const BoardUser = () => {
  const [userDetail, setUserDetail] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await axios.get("http://localhost:8080/api/userprofiles/me", {
          headers: getAuthHeader(),
        });
        setUserDetail(res.data);
      } catch (err) {
        console.error("❌ Không thể tải hồ sơ:", err);
        setError("Không thể tải thông tin người dùng.");
      }
    };

    fetchProfile();
  }, []);

  return (
    <div className="p-6 min-h-[calc(100vh-64px)] bg-gray-50 flex justify-center overflow-y-auto overflow-x-hidden">
      <div className="w-full max-w-[800px]">
        <Card
          style={{ width: "100%" }}
          bordered={false}
          className="shadow-md rounded-xl"
        >
          <Title level={3} className="mb-4">
            <UserOutlined /> Thông tin cá nhân
          </Title>

          {error && <Alert message={error} type="error" showIcon />}

          {!userDetail && !error && (
            <div className="text-center mt-4">
              <Spin tip="Đang tải thông tin người dùng..." />
            </div>
          )}

          {userDetail && (
            <Descriptions
              bordered
              column={1}
              size="middle"
              labelStyle={{ fontWeight: 600, width: "30%" }}
            >
              <Descriptions.Item label="Họ tên">
                {userDetail.fullName}
              </Descriptions.Item>
              <Descriptions.Item label="Email">{userDetail.email}</Descriptions.Item>
              <Descriptions.Item label="Nhóm máu">
                {userDetail.bloodType || "Chưa cập nhật"}
              </Descriptions.Item>
              <Descriptions.Item label="Địa chỉ">{userDetail.addressFull}</Descriptions.Item>
              <Descriptions.Item label="Số điện thoại">{userDetail.phone}</Descriptions.Item>
            </Descriptions>
          )}
        </Card>
      </div>
    </div>
  );
};

export default BoardUser;
