import React, { useState } from "react";
import "./EmergencyDonorRegistration.css";

const bloodGroups = ["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"];
const genders = ["Nam", "Nữ", "Khác"];

const EmergencyDonorRegistration = () => {
  const [form, setForm] = useState({
    fullName: "",
    dob: "",
    gender: "",
    bloodType: "",
    phone: "",
    email: "",
    address: "",
    expectedDate: "",
    expectedTime: "",
    agree: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!form.agree) {
      alert("Bạn phải đồng ý với điều kiện hiến máu.");
      return;
    }

    // Gửi API thật nếu cần:
    // axios.post("/api/emergency-donation", form).then(...)

    console.log("Dữ liệu gửi:", form);
    alert("Đăng ký hiến máu khẩn cấp thành công!");
  };

  return (
    <div className="emergency-register-page">
      <div className="emergency-register-card">
        <h2>Đăng Ký Hiến Máu Khẩn Cấp</h2>
        <form onSubmit={handleSubmit}>
          <div className="grid grid-2">
            <div>
              <label className="emergency-label">Họ và tên</label>
              <input
                type="text"
                name="fullName"
                value={form.fullName}
                onChange={handleChange}
                className="emergency-input"
                required
              />
            </div>
            <div>
              <label className="emergency-label">Ngày sinh</label>
              <input
                type="date"
                name="dob"
                value={form.dob}
                onChange={handleChange}
                className="emergency-input"
                required
              />
            </div>
            <div>
              <label className="emergency-label">Giới tính</label>
              <select
                name="gender"
                value={form.gender}
                onChange={handleChange}
                className="emergency-select"
                required
              >
                <option value="">-- Chọn --</option>
                {genders.map((g) => (
                  <option key={g} value={g}>
                    {g}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="emergency-label">Nhóm máu</label>
              <select
                name="bloodType"
                value={form.bloodType}
                onChange={handleChange}
                className="emergency-select"
                required
              >
                <option value="">-- Chọn nhóm --</option>
                {bloodGroups.map((bg) => (
                  <option key={bg} value={bg}>
                    {bg}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="emergency-label">Số điện thoại</label>
              <input
                name="phone"
                value={form.phone}
                onChange={handleChange}
                className="emergency-input"
                required
              />
            </div>
            <div>
              <label className="emergency-label">Email</label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                className="emergency-input"
              />
            </div>
          </div>

          <div>
            <label className="emergency-label">Địa chỉ hiện tại</label>
            <textarea
              name="address"
              value={form.address}
              onChange={handleChange}
              className="emergency-textarea"
              rows={2}
              required
            />
          </div>

          <div className="grid grid-2">
            <div>
              <label className="emergency-label">Ngày sẵn sàng hiến</label>
              <input
                type="date"
                name="expectedDate"
                value={form.expectedDate}
                onChange={handleChange}
                className="emergency-input"
                required
              />
            </div>
            <div>
              <label className="emergency-label">Thời gian sẵn sàng</label>
              <input
                type="time"
                name="expectedTime"
                value={form.expectedTime}
                onChange={handleChange}
                className="emergency-input"
              />
            </div>
          </div>

          <div className="emergency-checkbox-container">
            <input
              type="checkbox"
              name="agree"
              checked={form.agree}
              onChange={handleChange}
              required
            />
            <span>
              Tôi đồng ý với{" "}
              <a href="/donation-terms" target="_blank" rel="noopener noreferrer">
                điều kiện hiến máu và quy định của hệ thống
              </a>
              .
            </span>
          </div>

          <button type="submit" className="emergency-submit-btn">
            Đăng Ký Ngay
          </button>
        </form>
      </div>
    </div>
  );
};

export default EmergencyDonorRegistration;
