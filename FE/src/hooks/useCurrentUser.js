// src/hooks/useCurrentUser.js
import { useState, useEffect } from "react";

/**
 * Custom hook để lấy thông tin user từ localStorage một cách chuẩn hoá.
 * Tự xử lý các trường khác nhau từ backend và cho biết user đã đăng nhập hay chưa.
 */
export const useCurrentUser = () => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const raw = JSON.parse(localStorage.getItem("user") || "{}");

    const mapped = {
      id: raw.userId || raw.id || null,
      username: raw.username || "",
      email: raw.email || "",
      role: raw.role || "",
      first_name: raw.firstName || raw.first_name || "",
      last_name: raw.lastName || raw.last_name || "",
      phone: raw.phone || "",
      blood_type: raw.blood_type || "",
    };

    if (mapped.id) {
      setUser(mapped);
    }
  }, []);

  const isLoggedIn = !!user?.id;

  return { user, isLoggedIn };
};
