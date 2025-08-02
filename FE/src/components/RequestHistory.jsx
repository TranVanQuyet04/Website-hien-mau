import React, { useEffect, useState } from "react";
import axios from "axios";
import "../styles/user.css";
import { toast } from "react-toastify";

const RequestHistory = () => {
  const [requests, setRequests] = useState([]);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const currentUser = JSON.parse(localStorage.getItem("user"));
    setUser(currentUser);

    if (!currentUser) return;

    axios
      .get(`/users/requests/history/${currentUser.id}`)
      .then((res) => {
        setRequests(res.data);
        if (res.data.length === 0) {
          setTimeout(() => {
            toast.info("📭 Bạn chưa có yêu cầu nhận máu nào.");
          }, 200);
        }
      })
      .catch((err) => {
        console.error("Lỗi khi tải lịch sử yêu cầu:", err);
        setTimeout(() => {
          toast.error("❌ Không thể tải dữ liệu. Vui lòng thử lại sau.");
        }, 200);
      });
  }, []);

  return (
    <div className="container mt-4">
      <h3 className="text-danger mb-4">🩸 Lịch sử yêu cầu nhận máu</h3>

      {!user ? (
        <div className="alert alert-danger">Người dùng chưa đăng nhập.</div>
      ) : requests.length === 0 ? (
        <p>Chưa có yêu cầu nào.</p>
      ) : (
        <table className="styled-table">
          <thead>
            <tr>
              <th>Nhóm máu</th>
              <th>Thành phần</th>
              <th>Số lượng (ml)</th>
              <th>Khẩn cấp</th>
              <th>Trạng thái</th>
              <th>Ngày tạo</th>
            </tr>
          </thead>
          <tbody>
            {requests.map((req) => (
              <tr key={req.id}>
                <td>{req.blood_type}</td>
                <td>{req.component_name || req.component_id}</td>
                <td>{req.quantity_ml}</td>
                <td>{req.urgency_level}</td>
                <td>{req.status}</td>
                <td>{new Date(req.created_at).toLocaleDateString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default RequestHistory;