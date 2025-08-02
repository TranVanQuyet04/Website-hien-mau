import React, { useState, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { message } from "antd";
import {
    Layout,
    Menu,
    Avatar,
    Dropdown,
    Badge,
    Button,
    Space,
    Popover,
    List,
    Typography,
    Divider,
    Empty,
    Tooltip
} from "antd";
import {
    BellOutlined,
    UserOutlined,
    LogoutOutlined,
    LoginOutlined,
    UserAddOutlined,
    HomeOutlined,
    ReadOutlined,
    QuestionCircleOutlined,
    CalendarOutlined,
    HeartOutlined,
    CheckCircleOutlined,
    ClockCircleOutlined,
    ExclamationCircleOutlined,
    SettingOutlined,
    SearchOutlined
} from "@ant-design/icons";
import "../styles/Navbar.css";
import AuthService from "../services/auth.service";
const { Header } = Layout;
const { Text, Title } = Typography;

const Navbar = () => {
    const navigate = useNavigate();

    // Mock notification data - XÓA SAU KHI TÍCH HỢP API THẬT
    

    // Đếm thông báo chưa đọc




const handleLogout = () => {
  AuthService.logout();
  setCurrentUser(null);
  message.success("Đăng xuất thành công");

  setTimeout(() => {
    window.location.href = "/login"; // ✅ hard reload đảm bảo reset app state
  }, 1000);
};

// ⬇️ Tất cả các useState đặt trước
const [currentUser, setCurrentUser] = useState(null);
const [showAdminBoard, setShowAdminBoard] = useState(false);
const [showStaffBoard, setShowStaffBoard] = useState(false);
const [showUserBoard, setShowUserBoard] = useState(false);
const [notifications, setNotifications] = useState([
  
]);

// ⬇️ Chia thông báo theo role sau khi đã có currentUser
let userNotifications = [];
let unreadCount = 0;

if (currentUser) {
  const staffNotifications = notifications.filter(n => n.targetRole === "STAFF");
  const memberNotifications = notifications.filter(n => n.targetRole === "MEMBER");

  if (currentUser.role === "STAFF") {
    userNotifications = staffNotifications;
  } else if (currentUser.role === "MEMBER") {
    userNotifications = memberNotifications;
  } else {
    userNotifications = []; // Nếu role khác (hoặc ADMIN không có thông báo)
  }

  unreadCount = userNotifications.filter(n => !n.isRead).length;
}

useEffect(() => {
  const syncLogin = async () => {
    const user = AuthService.getCurrentUser();
    setCurrentUser(user);

    if (user?.roles) {
      setShowAdminBoard(user.roles.includes("ROLE_ADMIN"));
      setShowStaffBoard(user.roles.includes("ROLE_STAFF"));
      setShowUserBoard(user.roles.includes("ROLE_USER"));
    }
console.log("USER trong syncLogin:", user);
console.log("userId:", user?.userId);

    try {
      let fetchedNotifs = [];

      // 🔴 STAFF: lấy từ API blood-requests/admin
      if (user?.role === "STAFF" && user?.accessToken) {
        const response = await fetch("http://localhost:8080/api/blood-requests/admin", {
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const data = await response.json();

       const staffNotifs = data.map((item, index) => {
  const urgencyMap = {
    "KHAN_CAP": "khẩn cấp",
    "BINH_THUONG": "bình thường",
  };

  const statusMap = {
    "APPROVED": "đã được duyệt",
    "COMPLETED": "đã hoàn thành",
    "PENDING": "đang chờ duyệt",
    "REJECTED": "bị từ chối",
  };

  const urgency = urgencyMap[item.urgencyLevel] || item.urgencyLevel.toLowerCase();
  const status = statusMap[item.status] || item.status.toLowerCase();

  return {
    id: `staff-${index}`,
    title: "Yêu cầu hiến máu",
    message: (
  <>
    Đơn xin máu của bệnh nhân <strong>{item.patientName}</strong> với mức độ <strong>{urgency}</strong> <strong>{status}</strong>.
  </>
),
    time: "", // TODO: add timestamp
    isRead: false,
    type: "blood_request",
    targetRole: "STAFF",
  };
});


        fetchedNotifs = [...fetchedNotifs, ...staffNotifs];
      }

      // 🔵 MEMBER: lấy từ API donation/history/${userId}
      if (user?.role === "MEMBER" && user?.accessToken) {
         const response = await fetch(`http://localhost:8080/api/donation/history/${user.userId}`, {
    headers: {
      Authorization: `Bearer ${user.accessToken}`,
      "Content-Type": "application/json",
          },
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const history = await response.json();

        const memberNotifs = history.map((donation, index) => {
          let message = "";
          if (donation.status === "DONATED") {
            const nextDate = new Date(donation.updatedAt);
            nextDate.setDate(nextDate.getDate() + 84);
            const formattedDate = nextDate.toLocaleDateString("vi-VN");
            message = `Cảm ơn bạn ${donation.fullName} đã hiến máu thành công...(lời cảm ơn Trấn Thành). Nhắc nhở: Bạn cần chăm sóc sức khoẻ cho đến ${formattedDate} để có thể được hiến máu lại.`;
          } else if (donation.status === "CANCELLED") {
            message = `Đơn đăng ký của bạn ${donation.fullName} đã bị huỷ.`;
          } else if (donation.status === "PENDING") {
            message = `Bạn đã đăng ký hiến máu thành công. Vui lòng tới ${donation.location} để được hướng dẫn hiến máu.`;
          }

          return {
            id: `member-${index}`,
            title: "Thông báo hiến máu",
            message,
            time: " ", // TODO: thêm thời gian nếu có
            isRead: false,
            type: donation.status === "DONATED" ? "donation_success" : "reminder",
            targetRole: "MEMBER",
          };
        });

        fetchedNotifs = [...fetchedNotifs, ...memberNotifs];
      }

     setNotifications((prev) => [...fetchedNotifs, ...prev]);
    } catch (error) {
      console.error("Lỗi khi tải thông báo từ API:", error.message);
    }
  };

  syncLogin();
  window.addEventListener("login-success", syncLogin);
  return () => window.removeEventListener("login-success", syncLogin);
}, []);



    // Xử lý click thông báo
    const handleNotificationClick = (notification) => {
        // Đánh dấu đã đọc
        setNotifications(prev =>
            prev.map(n =>
                n.id === notification.id ? { ...n, isRead: true } : n
            )
        );

        // Chuyển hướng nếu có link
        if (notification.link) {
            navigate(notification.link);
        }
    };

    // Đánh dấu tất cả đã đọc
    const markAllAsRead = () => {
        setNotifications(prev =>
            prev.map(n => ({ ...n, isRead: true }))
        );
    };

    // Icon cho từng loại thông báo
    const getNotificationIcon = (type) => {
        switch (type) {
            case 'blood_request':
                return <HeartOutlined style={{ color: '#ff4d4f' }} />;
            case 'donation_success':
                return <CheckCircleOutlined style={{ color: '#52c41a' }} />;
            case 'reminder':
                return <ClockCircleOutlined style={{ color: '#faad14' }} />;
            case 'system':
                return <ExclamationCircleOutlined style={{ color: '#1890ff' }} />;
            default:
                return <BellOutlined />;
        }
    };

    // Menu items cho navigation chính
    const navigationItems = [
        {
            key: 'home',
            icon: <HomeOutlined />,
            label: <NavLink to="/home">Trang chủ</NavLink>,
        },
        {
            key: 'blog',
            icon: <ReadOutlined />,
            label: <NavLink to="/blog">Tin tức</NavLink>,
        },
        {
            key: 'faq',
            icon: <QuestionCircleOutlined />,
            label: <NavLink to="/faq">Hỏi - Đáp</NavLink>,
        },
        {
            key: 'activities',
            icon: <CalendarOutlined />,
            label: <NavLink to="/activities">Hoạt động</NavLink>,
        },
        {
            key: 'donate',
            icon: <HeartOutlined />,
            label: (
                <NavLink to={`/user/${currentUser?.id}/register`}>
                    Đăng ký hiến máu
                </NavLink>
            ),
        }

    ];

    // Notification content - Giống Facebook
    const notificationContent = (
        <div style={{ width: 360, maxHeight: 480, overflowY: 'auto' }}>
            {/* Header thông báo */}
            <div style={{
                padding: '16px 20px 12px',
                borderBottom: '1px solid #f0f0f0',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center'
            }}>
                <Title level={4} style={{ margin: 0, fontWeight: 600 }}>
                    Thông báo
                </Title>
                <Button
                    type="link"
                    size="small"
                    onClick={markAllAsRead}
                    disabled={unreadCount === 0}
                    style={{ padding: 0, fontSize: '13px' }}
                >
                    Đánh dấu tất cả đã đọc
                </Button>
            </div>

            {/* Danh sách thông báo */}
            {notifications.length > 0 ? (
                <List
                    itemLayout="horizontal"
                    dataSource={userNotifications}
                    style={{ padding: 0 }}
                    renderItem={(notification) => (
                        <List.Item
                            style={{
                                padding: '12px 20px',
                                cursor: 'pointer',
                                backgroundColor: notification.isRead ? 'transparent' : '#f6ffed',
                                borderLeft: notification.isRead ? 'none' : '3px solid #52c41a',
                                transition: 'background-color 0.2s',
                                borderBottom: '1px solid #f5f5f5'
                            }}
                            onClick={() => handleNotificationClick(notification)}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.backgroundColor = '#fafafa';
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.backgroundColor =
                                    notification.isRead ? 'transparent' : '#f6ffed';
                            }}
                        >
                            <List.Item.Meta
                                avatar={
                                    <div style={{ position: 'relative' }}>
                                        <Avatar
                                            src={notification.avatar}
                                            icon={!notification.avatar && <UserOutlined />}
                                            size={40}
                                            style={{
                                                backgroundColor: notification.avatar ? undefined : '#f56a00'
                                            }}
                                        />
                                        {/* Icon loại thông báo */}
                                        <div
                                            style={{
                                                position: 'absolute',
                                                bottom: -2,
                                                right: -2,
                                                width: 20,
                                                height: 20,
                                                backgroundColor: 'white',
                                                borderRadius: '50%',
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                                boxShadow: '0 1px 4px rgba(0,0,0,0.15)'
                                            }}
                                        >
                                            {getNotificationIcon(notification.type)}
                                        </div>
                                    </div>
                                }
                                title={
                                    <div>
                                        <Text
                                            strong={!notification.isRead}
                                            style={{
                                                fontSize: '14px',
                                                fontWeight: notification.isRead ? 400 : 600,
                                                lineHeight: '1.4'
                                            }}
                                        >
                                            {notification.title}
                                        </Text>
                                        {!notification.isRead && (
                                            <div
                                                style={{
                                                    width: 8,
                                                    height: 8,
                                                    backgroundColor: '#1890ff',
                                                    borderRadius: '50%',
                                                    display: 'inline-block',
                                                    marginLeft: 8
                                                }}
                                            />
                                        )}
                                    </div>
                                }
                                description={
                                    <div>
                                        <Text
                                            style={{
                                                fontSize: '13px',
                                                color: '#666',
                                                lineHeight: '1.4',
                                                display: 'block',
                                                marginBottom: 4
                                            }}
                                        >
                                            {notification.message}
                                        </Text>
                                        <Text
                                            style={{
                                                fontSize: '12px',
                                                color: '#1890ff',
                                                fontWeight: 500
                                            }}
                                        >
                                            {notification.time}
                                        </Text>
                                    </div>
                                }
                            />
                        </List.Item>
                    )}
                />
            ) : (
                <div style={{ padding: '40px 20px', textAlign: 'center' }}>
                    <Empty
                        description="Không có thông báo nào"
                        image={Empty.PRESENTED_IMAGE_SIMPLE}
                    />
                </div>
            )}

            {/* Footer */}
            {notifications.length > 0 && (
                <div style={{
                    padding: '12px 20px',
                    borderTop: '1px solid #f0f0f0',
                    textAlign: 'center'
                }}>
                    {/* <Button
                        type="link"
                        onClick={() => navigate('/notifications')}
                        style={{ padding: 0, fontSize: '14px', fontWeight: 500 }}
                    >
                        Xem tất cả thông báo
                    </Button> */}
                </div>
            )}
        </div>
    );

    // User menu items
    const userMenuItems = [
        ...(showUserBoard && !showAdminBoard && !showStaffBoard ? [{
            key: 'profile',
            icon: <UserOutlined />,
            label: <NavLink to="/profile">Hồ sơ cá nhân</NavLink>,
        }] : []),
        {
            key: 'logout',
            icon: <LogoutOutlined />,
            label: 'Đăng xuất',
            onClick: handleLogout,
        }
    ];

    return (
        <Header
            style={{
                background: '#d32f2f',
                padding: '0 24px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                boxShadow: '0 2px 8px rgba(0,0,0,0.15)',
                borderBottom: "none",
                position: 'fixed',         // ✅ cố định tuyệt đối
                top: 0,
                width: '100%',             // ✅ full chiều ngang
                zIndex: 10
            }}
        >

            {/* Logo section */}
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <img
                    src="/Logo-Blood-Donation.jpg"
                    alt="Logo"
                    style={{
                        height: '40px',
                        marginRight: '24px',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                    onClick={() => navigate('/home')}
                />
            </div>

            {/* Navigation menu giống 30Shine */}
            <div className="navbar-center-wrapper">
                <div className="navbar-center">
                    {navigationItems.map((item) => {
                        return (
                            <div className="nav-pill-wrapper" key={item.key}>
                                {React.cloneElement(item.label, {
                                    className: ({ isActive }) =>
                                        `nav-pill-custom ${isActive ? 'active' : ''}`,
                                    children: (
                                        <>
                                            {item.icon}
                                            <span>{item.label.props.children}</span>
                                        </>
                                    )
                                })}
                            </div>
                        );
                    })}
                </div>
            </div>

            {/* User section */}
            <Space size="large">

                {/* Notification bell */}
                {currentUser && (
                    <Popover
                        content={notificationContent}
                        title={null}
                        trigger="click"
                        placement="bottomRight"
                        overlayClassName="notification-popover"
                    >
                        <Badge
                            count={unreadCount}
                            size="small"
                            offset={[-2, 2]}
                        >
                            <Button
                                type="text"
                                icon={<BellOutlined />}
                                style={{
                                    color: 'white',
                                    fontSize: '18px',
                                    display: 'flex',
                                    alignItems: 'center'
                                }}
                            />
                        </Badge>
                    </Popover>
                )}

                {currentUser ? (
                    <Dropdown
                        menu={{ items: userMenuItems }}
                        placement="bottomRight"
                        trigger={['click']}
                    >
                        <Space style={{ cursor: 'pointer', color: 'white' }}>
                            <Avatar
                                icon={<UserOutlined />}
                                size="small"
                                style={{ backgroundColor: '#fff', color: '#d32f2f' }}
                            />
                            <Text style={{ color: 'white', fontWeight: 500 }}>
                                {currentUser.username}
                            </Text>
                        </Space>
                    </Dropdown>
                ) : (
                    <Space>
                        <Button
                            type="ghost"
                            icon={<LoginOutlined />}
                            onClick={() => navigate('/login')}
                            style={{
                                borderColor: 'white',
                                color: 'white'
                            }}
                        >
                            Đăng nhập
                        </Button>
                        <Button
                            type="primary"
                            icon={<UserAddOutlined />}
                            onClick={() => navigate('/register/information')}
                            style={{
                                backgroundColor: 'white',
                                borderColor: 'white',
                                color: '#d32f2f'
                            }}
                        >
                            Đăng ký
                        </Button>
                    </Space>
                )}
            </Space>
        </Header>
    );
};

export default Navbar;