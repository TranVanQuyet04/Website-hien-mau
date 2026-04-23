import axios from 'axios';
import { apiUrl } from "../config/api";

// ✅ Gửi cookie nếu backend yêu cầu (ví dụ: Spring Security dùng JSESSIONID)
axios.defaults.withCredentials = true;

// ✅ Base URL cho mọi request
const API_URL = apiUrl("api/");

// ✅ Tạo Authorization Header từ JWT
export const getAuthHeader = () => {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
};

// =======================
// 🔓 API công khai
// =======================
const getPublicContent = () => {
  return axios.get(`${API_URL}auth/public/content`);
};

// =======================
// 👤 API người dùng
// =======================
const getUserProfile = () => {
  return axios.get(`${API_URL}user/profile`, {
    headers: getAuthHeader()
  });
};

const getUserById = (id) => {
  return axios.get(`${API_URL}users/${id}`, {
    headers: getAuthHeader()
  });
};

const getDonationHistory = () => {
  return axios.get(`${API_URL}donation/history`, {
    headers: getAuthHeader()
  });
};

const getBloodRequest = () => {
  return axios.get(`${API_URL}request/list`, {
    headers: getAuthHeader()
  });
};

const getTransfusionHistory = () => {
  return axios.get(`${API_URL}transfusion/history`, {
    headers: getAuthHeader()
  });
};

// =======================
// 🧑‍⚕️ API nhân viên
// =======================
const getStaffDashboard = () => {
  return axios.get(`${API_URL}staff/dashboard`, {
    headers: getAuthHeader()
  });
};

// ✅ 🧪 API tồn kho máu


// =======================
// 🛠️ API admin
// =======================
const getAdminDashboard = () => {
  return axios.get(`${API_URL}admin`, {
    headers: getAuthHeader()
  });
};

const getAllUsers = () => {
  return axios.get(`${API_URL}users/list`, {
    headers: getAuthHeader()
  });
};

const getAllRoles = () => {
  return axios.get(`${API_URL}roles/list`, {
    headers: getAuthHeader()
  });
};

const getNotifications = () => {
  return axios.get(`${API_URL}notifications/list`, {
    headers: getAuthHeader()
  });
};
const getInventory = () => {
  return axios.get(apiUrl("api/blood-inventory"), {
    headers: {
      ...getAuthHeader(),
      "Cache-Control": "no-cache"
    }
  });
};

// =======================
// 📦 Export tất cả
// =======================
const UserService = {
  getPublicContent,
  getUserProfile,
  getUserById,
  getDonationHistory,
  getBloodRequest,
  getTransfusionHistory,
  getStaffDashboard,
  getAdminDashboard,
  getAllUsers,
  getAllRoles,
  getNotifications,
  getInventory,         // ✅ THÊM DÒNG NÀY
  getAuthHeader
};

export default UserService;
