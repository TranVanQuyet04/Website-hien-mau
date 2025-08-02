import React, { useEffect, useState } from "react";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import {
  Layout, Menu, Avatar, Typography, Spin, Button, Result,
  Drawer, Badge, Grid, Tooltip
} from "antd";
import {
  UserOutlined, HeartOutlined, HistoryOutlined,
  ExclamationCircleOutlined, InfoCircleOutlined, ProfileOutlined,
  PhoneOutlined, MenuOutlined, BellOutlined, LogoutOutlined
} from "@ant-design/icons";
import { motion, AnimatePresence } from "framer-motion";
import AuthService from "../services/auth.service";
import UserService from "../services/user.service";
import "../styles/user.css";

const { Sider, Header, Content } = Layout;
const { Title, Text } = Typography;
const { useBreakpoint } = Grid;

const UserLayout = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const screens = useBreakpoint();

  const [collapsed, setCollapsed] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [direction, setDirection] = useState(1);
  const [prevPath, setPrevPath] = useState(location.pathname);

  useEffect(() => {
    const currentUser = AuthService.getCurrentUser();
    if (!currentUser || !currentUser.userId) {
      setError("Vui lòng đăng nhập để truy cập.");
      setLoading(false);
      return;
    }

    UserService.getUserById(currentUser.userId)
      .then(res => setUserInfo(res.data))
      .catch(() => setError("Không thể tải thông tin người dùng."))
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (location.pathname !== prevPath) {
      setDirection(location.pathname > prevPath ? 1 : -1);
      setPrevPath(location.pathname);
    }
  }, [location.pathname]);

  const userId = location.pathname.split("/")[2];

  const menuItems = [
    {
      key: "register",
      icon: <HeartOutlined />,
      label: collapsed ? <Tooltip title="Đăng ký hiến máu">Đăng ký</Tooltip> : "Đăng ký hiến máu"
    },
    {
      key: "urgent",
      icon: <ExclamationCircleOutlined />,
      label: collapsed ? <Tooltip title="Hiến máu khẩn cấp">Khẩn</Tooltip> : "Hiến máu khẩn cấp"
    },
    {
      key: "donation-history",
      icon: <HistoryOutlined />,
      label: collapsed ? <Tooltip title="Lịch sử hiến máu">Lịch sử</Tooltip> : "Lịch sử hiến máu"
    },
    { type: "divider" },
    {
      key: "types",
      icon: <InfoCircleOutlined />,
      label: collapsed ? <Tooltip title="Các loại máu">Máu</Tooltip> : "Các loại máu"
    },
    // {
    //   key: "receive",
    //   icon: <PhoneOutlined />,
    //   label: collapsed ? <Tooltip title="Liên hệ & hỗ trợ hiến máu">Nhận</Tooltip> : "Liên hệ & hỗ trợ hiến máu"
    // },
  ];

  const handleMenuClick = ({ key }) => {
    navigate(`/user/${userId}/${key}`);
  };

  const handleLogout = () => {
    AuthService.logout();
    navigate("/login");
  };

  if (loading) {
    return (
      <div className="loading-spinner">
        <Spin size="large" tip="Đang tải thông tin..." />
      </div>
    );
  }

  if (error) {
    return (
      <Result
        status="403"
        title="Lỗi truy cập"
        subTitle={error}
        extra={<Button type="primary" onClick={() => navigate("/login")}>Đăng nhập</Button>}
      />
    );
  }

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider
        width={280}
        collapsed={collapsed}
        collapsible
        trigger={null}
        style={{
          background: "#b91c1c",
          position: "fixed",
          top: 64,
          left: 0,
          bottom: 0,
          height: "calc(100vh - 64px)",
          display: "flex",
          flexDirection: "column",
        }}
      >
        {/* --- AVATAR / HEADER --- */}
        <div
          style={{
            padding: collapsed ? "16px 0" : 24,       // ↔ khi collapsed đỡ cao
            textAlign: "center",
            transition: "padding 0.2s ease"
          }}
        >
          <Avatar
            size={collapsed ? 40 : 72}                // ↔ size nhỏ khi collapsed
            icon={<UserOutlined />}
            style={{
              backgroundColor: "#fff",
              color: "#b91c1c",
              marginBottom: collapsed ? 0 : 12,       // ↔ bỏ margin-bottom khi collapsed
              transition: "all 0.2s ease"
            }}
          />
          {!collapsed && userInfo && (
            <>
              <Title level={5} style={{ color: "white", marginBottom: 4 }}>
                {userInfo.first_name} {userInfo.last_name}
              </Title>
              <Text style={{ color: "#f0f0f0", fontSize: 12, display: "block" }}>
               {userInfo.blood_type || " "}
              </Text>
            </>
          )}
        </div>


        {/* Menu */}
        <Menu
          mode="inline"
          theme="dark"
          selectedKeys={[location.pathname.split("/").pop()]}
          onClick={handleMenuClick}
          style={{ background: "transparent", border: "none", flex: 1 }}
        >
          {menuItems.map((item) =>
            item.type === "divider" ? (
              <Menu.Divider key="divider" />
            ) : (
              <Menu.Item key={item.key} icon={item.icon} style={{ display: 'flex', alignItems: 'center' }}>
                {!collapsed && <span style={{ marginLeft: 12 }}>{item.label}</span>}
              </Menu.Item>
            )
          )}
        </Menu>

        {/* --- FOOTER: Logout + Collapse tại đáy --- */}
        <div
          style={{
            position: "absolute",
            bottom: 0,
            width: "100%",
            padding: "12px 0 0",   // padding trên để cách logout
            boxSizing: "border-box",
            borderTop: "1px solid rgba(255,255,255,0.2)",
            background: "inherit",
            textAlign: "center",
          }}
        >
          {/* Logout pill */}
          <Button
            type="text"
            danger
            icon={<LogoutOutlined />}
            onClick={handleLogout}
            style={{
              width: collapsed ? 40 : "80%",
              height: 40,
              borderRadius: 20,
              backgroundColor: "#fff0f0",
              margin: "0 auto",
              padding: 0,
            }}
          />

          {/* Collapse toggle */}
          <div
            onClick={() => setCollapsed(!collapsed)}
            style={{
              width: collapsed ? 40 : "100%",
              height: 40,
              margin: collapsed ? "8px auto 0" : "8px 0 0",
              backgroundColor: "#0f172a",
              color: "#fff",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              cursor: "pointer",
              ...(collapsed
                ? { borderRadius: 8 }
                : {
                  borderTopLeftRadius: 0,
                  borderTopRightRadius: 0,
                  borderBottomLeftRadius: 8,
                  borderBottomRightRadius: 8,
                }
              )
            }}
          >
            <MenuOutlined style={{ fontSize: 20 }} />
          </div>
        </div>
      </Sider>

      <Layout style={{ marginLeft: collapsed ? 80 : 280 }}>
      {/* <Layout style={{ marginLeft: collapsed ? 80 : 280, marginTop: 64 }}></Layout> */}
        <Content style={{
          margin: 0, 
          padding: 0,
          background: "#f9f9f9",
          marginTop: 10,
          minHeight: "calc(100vh - 64px)",
          overflowX: 'hidden',       // ✅ CHẶN NGANG
          overflowY: 'auto',
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between"
        }}>

          <AnimatePresence mode="wait">
            <motion.div
              key={location.pathname}
              initial={{ x: direction === 1 ? 300 : -300, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: direction === 1 ? -300 : 300, opacity: 0 }}
              transition={{ duration: 0.4 }}
              style={{ flexGrow: 1 }}
            >
              <div className="page-content">
                <Outlet />
              </div>
            </motion.div>
          </AnimatePresence>
        </Content>
      </Layout>
    </Layout>
  );
};

export default UserLayout;