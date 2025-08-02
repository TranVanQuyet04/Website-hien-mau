import axios from "axios";

export const getAllTransfusions = async () => {
  return await axios.get("/api/transfusions");
};

/**
 * Lấy lịch sử truyền máu của 1 người dùng cụ thể (dành cho user).
 * @param {string|number} userId - ID người dùng.
 */
export const getUserTransfusions = (userId) => {
  return axios.get(`/api/transfusions/user/${userId}`);
};

/**
 * Gửi yêu cầu xác nhận truyền máu.
 * @param {object} data - Dữ liệu truyền máu { recipientName, bloodType, units }.
 */
export const confirmTransfusion = (data) => {
  return axios.post("/api/transfusions", data);
};
