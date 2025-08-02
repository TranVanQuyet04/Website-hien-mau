// src/utils/auth.utils.js

/**
 * Chuẩn hoá thông tin người dùng từ localStorage
 * Dù backend trả về userId hay id, firstName hay first_name, hàm này sẽ xử lý chuẩn.
 */
export const getMappedUser = () => {
  const raw = JSON.parse(localStorage.getItem("user") || "{}");

  return {
    id: raw.userId || raw.id || null,
    username: raw.username || "",
    email: raw.email || "",
    role: raw.role || "",
    first_name: raw.firstName || raw.first_name || "",
    last_name: raw.lastName || raw.last_name || "",
    phone: raw.phone || "",
    blood_type: raw.blood_type || ""
  };
};
