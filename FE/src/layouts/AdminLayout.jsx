import React, { useEffect, useState, useMemo } from "react";
import { useLocation, useNavigate, Outlet } from "react-router-dom";
import {
  ConfigProvider,
  Layout,
  Menu,
  Typography,
  Tooltip,
} from "antd";
import {
  DashboardOutlined,
  ExclamationCircleOutlined,
  HistoryOutlined,
  TeamOutlined,
  ExperimentOutlined,
  ShareAltOutlined,
  BarChartOutlined,
  MedicineBoxOutlined,
  MenuOutlined,
  UnorderedListOutlined,
} from "@ant-design/icons";

const { Sider, Content } = Layout;
const { Title } = Typography;

const AdminLayout = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const [selectedKey, setSelectedKey] = useState("");
  const [collapsed, setCollapsed] = useState(false);

  const adminMenuItems = [
    // { key: 'dashboard', icon: <DashboardOutlined />, label: 'Tổng quan', path: '/admin/dashboard' },
    { key: 'blood-requests', icon: <ExclamationCircleOutlined />, label: 'Yêu cầu máu', path: '/admin/blood-requests' },
    { key: 'donation-history', icon: <HistoryOutlined />, label: 'Lịch sử hiến máu', path: '/admin/donation-history' },
    { key: 'transfusion-history', icon: <MedicineBoxOutlined />, label: 'Lịch sử truyền máu', path: '/admin/transfusion-history' },
    { key: 'urgent-list', icon: <UnorderedListOutlined />, label: 'DS người hiến máu khẩn cấp', path: '/admin/urgent-list' },
    { key: 'staff&doctor', icon: <TeamOutlined />, label: 'Nhân viên y tế', path: '/admin/staff&doctor' },
    // { key: 'blood', icon: <ExperimentOutlined />, label: 'Nhóm máu & Thành phần', path: '/admin/blood' },
    { key: 'compatibility', icon: <ShareAltOutlined />, label: 'Quy tắc tương thích', path: '/admin/compatibility' },
    { key: 'report', icon: <BarChartOutlined />, label: 'Báo cáo & Thống kê', path: '/admin/report' },
  ];

  // keep selectedKey in sync with current URL
  useEffect(() => {
    const active = adminMenuItems.find(item => pathname.startsWith(item.path));
    setSelectedKey(active?.key || '');
  }, [pathname]);

  // build the items array for Menu, injecting Tooltip on icon when collapsed
  const menuItems = adminMenuItems.map(({ key, icon, label, path }) => ({
    key,
    icon: collapsed
      ? (
        <Tooltip placement="right" title={label}>
          {icon}
        </Tooltip>
      )
      : icon,
    label: !collapsed ? label : '',
    // override title để Menu không tự gán tooltip
    title: collapsed ? null : label,
    onClick: () => navigate(path),
  }));
  return (
    <ConfigProvider theme={{ token: { colorPrimary: '#1890ff' } }}>
      <Layout style={{ minHeight: '100vh' }}>
        <Sider
          collapsible
          collapsed={collapsed}
          onCollapse={setCollapsed}
          width={280}
          trigger={(
            <MenuOutlined
              onClick={() => setCollapsed(!collapsed)}
              aria-label={collapsed ? 'Mở sidebar' : 'Đóng sidebar'}
              style={{ color: 'white', fontSize: 18, padding: '0 24px', cursor: 'pointer' }}
            />
          )}
          style={{
            background: '#b91c1c',
            position: 'sticky',
            height: '100vh',
            left: 0,
            top: 0,
            zIndex: 100,
          }}
        >
          <div
            style={{
              padding: '20px 16px',
              textAlign: 'center',
              borderBottom: '1px solid rgba(255,255,255,0.1)',
            }}
          >
            <Title
              level={4}
              style={{ color: 'white', margin: 0, fontSize: collapsed ? 14 : 16 }}
            >
              {collapsed ? 'ADMIN' : 'BẢNG QUẢN TRỊ'}
            </Title>
          </div>

          <Menu
            theme="dark"
            mode="inline"
            selectedKeys={[selectedKey]}
            items={menuItems}
            style={{ background: 'transparent', border: 'none' }}
          />
        </Sider>

        <Layout
          style={{
            transition: 'margin-left 0.2s',
          }}
        >
          <Content style={{ padding: 24, background: '#f5f5f5', minHeight: '100vh' }}>
            <Outlet />
          </Content>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
};

export default AdminLayout;