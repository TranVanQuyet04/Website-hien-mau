// src/routes/userRoutes.js

// 📘 Cấu hình danh sách route dùng cho UserLayout
// 🎯 Mỗi route tương ứng với 1 trang và có tiêu đề (title) hiển thị ở Header
// 👉 Lợi ích:
//   - Dễ mở rộng, dễ tái sử dụng
//   - Tránh hard-code tiêu đề trong component
//   - Có thể mở rộng thêm: icon, breadcrumb, permission...

export const userRoutes = [
  {
    path: "urgent-register",     // 🩸 Đăng ký hiến máu khẩn cấp
    title: "Hiến máu khẩn cấp",
  },
  {
    path: "register",            // 🩺 Đăng ký hiến máu thường
    title: "Đăng ký hiến máu",
  },
  {
    path: "donation-history",   // 📜 Xem lịch sử hiến máu
    title: "Lịch sử hiến máu",
  },
  {
    path: "types",              // 🧬 Tìm hiểu các nhóm máu
    title: "Các loại máu",
  },
  {
    path: "receive",            // 🩹 Cách thức nhận máu
    title: "Cách nhận máu",
  },
  {
    path: "roles",              // 👥 Chủ & phụ trong truyền máu
    title: "Vai trò trong truyền máu",
  },
];