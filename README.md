# 💉 Blood Donation Management System

Hệ thống quản lý hiến và truyền máu, hỗ trợ các vai trò: Admin, Staff, Doctor và Member (người hiến máu). Hệ thống đảm bảo luồng xử lý rõ ràng, đầy đủ thông tin về nhóm máu, kho máu, yêu cầu truyền máu và tình trạng sức khỏe người hiến.

## 📌 Chức năng chính theo vai trò

👤 Member (Người hiến máu)

- Đăng ký tài khoản và đăng nhập.
- Cập nhật thông tin cá nhân (không bao gồm nhóm máu).
- Đăng ký sẵn sàng hiến máu.
- Theo dõi lịch sử hiến máu.
- Nhận thông báo nếu được chọn là người hiến khẩn cấp.
- Nhận thông báo sau khi hiến máu thành công.
- Không được chỉnh sửa nhóm máu — nhóm máu sẽ được xác định bởi Staff sau khi kiểm tra sức khỏe.

 🧪 Staff (Nhân viên y tế)

- Kiểm tra thông tin người hiến máu khi đến địa điểm.
- Điền vào phiếu kiểm tra sức khỏe.
- **Xác định nhóm máu của người hiến** (chỉ định một lần và không cho sửa).
- Đánh giá điều kiện hiến máu.
- Cập nhật trạng thái hiến máu.
- Xử lý đơn đăng ký hiến máu (phê duyệt hoặc từ chối).
- Gửi thông báo cho người hiến máu nếu cần.
- Nhận thông báo khi đơn truyền máu đã được admin xử lý xong.
- Tạo thanh toán (nếu có quy trình bồi dưỡng người hiến máu).

 🛡 Admin (Quản trị viên)

- Tạo tài khoản cho Staff và Doctor.
- Quản lý kho máu (BloodUnit): thể tích, nhóm máu, tình trạng, hạn sử dụng.
- Xử lý đơn yêu cầu truyền máu:
  - Nếu đủ máu: cập nhật kho máu, ghi lại bản ghi truyền máu.
  - Nếu **không đủ máu**: tìm người hiến máu khẩn cấp.
- Nhận phản hồi từ member khẩn cấp:
  - Nếu member đồng ý hiến máu, quay lại luồng truyền máu.
- Gửi thông báo đến các bên liên quan.
- Quản lý thanh toán, thống kê và báo cáo.


## 🔄 Luồng hoạt động chính

✅ Hiến máu:

1. Member đăng ký sẵn sàng hiến máu.
2. Đến địa điểm hiến máu theo lịch.
3. Staff kiểm tra sức khỏe và **xác nhận nhóm máu**.
4. Nếu đạt điều kiện → hệ thống cập nhật hồ sơ và kho máu.
5. Member được ghi nhận lịch sử hiến máu.


🩸 Truyền máu:

1. Staff gửi yêu cầu truyền máu đến Admin.
2. Admin kiểm tra kho máu:
   - Nếu đủ → ghi nhận lịch sử truyền máu.
   - Nếu không đủ:
     - Gửi thông báo đến danh sách hiến máu khẩn cấp (đúng nhóm máu).
     - Khi có member xác nhận, quay lại bước 2.
3. Sau khi truyền thành công, Staff nhận thông báo và tạo thanh toán nếu cần.

## 📊 Dashboard & Báo cáo

- Thống kê số lượng máu từng nhóm.
- Lịch sử truyền máu và hiến máu.
- Danh sách người hiến máu theo khu vực, nhóm máu.
- Tình trạng khẩn cấp và phản hồi.

## 🚧 Ghi chú kỹ thuật

 Backend:
 - Framework: Spring Boot
 - Bảo mật: Spring Security + JWT Authentication
 - ORM: JPA (Java Persistence API)
 - Cơ sở dữ liệu: MySQL
   
 Frontend:
 - Thư viện chính: React.js
 - Ngôn ngữ: HTML / CSS / JavaScript
   
Xác thực & Phân quyền:
 - Phương thức xác thực: JWT (JSON Web Token)
 - Phân quyền theo vai trò (Role-based Access Control):
 - ADMIN: Toàn quyền hệ thống
 - STAFF: Nhân viên xử lý yêu cầu
 - DOCTOR: Bác sĩ tiếp nhận / đánh giá chuyên môn
 - MEMBER: Người dùng hệ thống (bệnh nhân, người hiến máu, staff, admin)
   
## 🛡 Logic bảo mật

- Chỉ admin được tạo tài khoản staff/doctor.
- Member không thể tự chỉnh nhóm máu.
- Mọi thao tác nhạy cảm (xử lý đơn, cập nhật kho máu) đều yêu cầu role phù hợp.
