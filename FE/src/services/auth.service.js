import axios from 'axios';
import { apiUrl } from "../config/api";

axios.defaults.withCredentials = true;

const API_URL = apiUrl("api/auth");

// ✅ Đăng nhập
const login = async (username, password) => {
  const response = await axios.post(`${API_URL}/login`, { username, password }, {
    headers: { 'Content-Type': 'application/json' }
  });

  const userData = response?.data; // 👈 CHỈ LẤY response.data
  if (!userData || !userData.accessToken) {
    throw new Error("Dữ liệu đăng nhập không hợp lệ.");
  }

  // Lưu token
  localStorage.setItem("user", JSON.stringify(userData));
  localStorage.setItem("token", userData.accessToken);
  axios.defaults.headers.common["Authorization"] = `Bearer ${userData.accessToken}`;

  return userData;
};

// ✅ Đăng ký
const register = (username, email, password, profile) => {
  return axios.post(`${API_URL}/register`, {
    username,
    email,
    password,
    ...profile
  }, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

// ✅ Đăng xuất
const logout = async () => {
  try {
    const token = localStorage.getItem('token');
    if (token) {
      await axios.post(`${API_URL}/logout`, null, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
    }
  } catch (err) {
    console.warn("⚠️ Logout API lỗi (có thể đã hết hạn):", err);
  } finally {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    delete axios.defaults.headers.common["Authorization"];
  }
};

// ✅ Lấy user hiện tại
const getCurrentUser = () => {
  try {
    const userStr = localStorage.getItem('user');
    if (!userStr || userStr === "undefined") {
      return null; // hoặc {} nếu bạn muốn tránh null
    }
    return JSON.parse(userStr);
  } catch (err) {
    console.error("Lỗi parse user từ localStorage:", err);
    return null;
  }
};


// ✅ Tạo Authorization Header
const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : {};
};

// ✅ API mẫu có auth
const getInventory = () => {
  return axios.get(apiUrl("api/blood/inventory"), {
    headers: getAuthHeader()
  });
};

const AuthService = {
  login,
  register,
  logout,
  getInventory,
  getCurrentUser,
  getAuthHeader
};

export default AuthService;