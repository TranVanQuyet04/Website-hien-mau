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
  ExclamationCircleOutlined,
  HeartOutlined,
  ExperimentOutlined,
  BarChartOutlined,
  AlertOutlined,
  CheckCircleOutlined,
  MenuOutlined,
  HistoryOutlined,
} from "@ant-design/icons";
import { DeploymentUnitOutlined } from '@ant-design/icons'; // icon gợi ý liên quan đến tách máu
const { Sider, Content } = Layout;
const { Title } = Typography;

const StaffLayout = () => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  const [selectedKey, setSelectedKey] = useState("");
  const [collapsed, setCollapsed] = useState(false);

  const staffMenuItems = [
    { key: 'requests', icon: <ExclamationCircleOutlined />, label: 'Yêu cầu truyền máu', path: '/staff/requests' },
    { key: 'blood-requests', icon: <ExclamationCircleOutlined />, label: 'Danh sách yêu cầu máu', path: '/staff/blood-requests' },
    { key: 'donation-confirm', icon: <HeartOutlined />, label: 'Xác nhận hiến máu', path: '/staff/donation-confirm' },
    // { key: 'inventory', icon: <ExperimentOutlined />, label: 'Quản lý kho máu', path: '/staff/inventory' },
    { key: 'donation-history', icon: <HistoryOutlined />, label: 'Lịch sử hiến máu', path: '/staff/donation-history' },
    { key: 'unit', icon: <ExperimentOutlined />, label: 'Quản lý kho máu', path: '/staff/unit' },
    { key: 'unitused', icon: <ExperimentOutlined />, label: 'Quản lý kho máu Hết hạn sử dụng', path: '/staff/unitused' },
    // { key: 'separation-dashboard', icon: <DeploymentUnitOutlined />, label: 'Dashboard tách máu', path: '/staff/separation-dashboard' },
    // { key: 'statistics', icon: <BarChartOutlined />, label: 'Thống kê', path: '/staff/statistics' },
    { key: 'urgent-requests', icon: <AlertOutlined />, label: 'Duyệt danh sách khẩn cấp', path: '/staff/urgent-requests' },
    { key: 'vnpay', icon: <CheckCircleOutlined />, label: 'Thanh toán', path: '/staff/vnpay' },
  ];

  // keep selectedKey in sync with current URL
  useEffect(() => {
    const active = staffMenuItems.find(item => pathname === item.path);

    setSelectedKey(active?.key || '');
  }, [pathname]);

  // build the items array for Menu, injecting Tooltip on icon when collapsed
  const menuItems = staffMenuItems.map(({ key, icon, label, path }) => ({
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
              {collapsed ? 'STAFF' : 'BẢNG NHÂN VIÊN'}
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

export default StaffLayout;