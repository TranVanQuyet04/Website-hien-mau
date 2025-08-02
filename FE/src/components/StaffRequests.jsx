
// import React, { useEffect, useState } from "react";
// import axios from "axios";

// const StaffRequests = () => {
//   const [requests, setRequests] = useState([]);

//   useEffect(() => {
//     axios
//       .get("http://localhost:3000/api/requests")
//       .then((res) => setRequests(res.data))
//       .catch((err) => console.error("Lỗi khi tải danh sách yêu cầu:", err));
//   }, []);

//   return (
//     <div className="container mt-4">
//       <h3>📋 Danh sách yêu cầu nhận máu</h3>
//       {requests.length === 0 ? (
//         <p>Chưa có yêu cầu nào.</p>
//       ) : (
//         <table className="table table-bordered table-striped">
//           <thead>
//             <tr>
//               <th>Người yêu cầu</th>
//               <th>Nhóm máu</th>
//               <th>Thành phần</th>
//               <th>Số lượng (ml)</th>
//               <th>Trạng thái</th>
//               <th>Khẩn cấp</th>
//               <th>Thời gian</th>
//             </tr>
//           </thead>
//           <tbody>
//             {requests.map((r) => (
//               <tr key={r.id}>
//                 <td>{r.requester_name || "Ẩn danh"}</td>
//                 <td>{r.blood_type}</td>
//                 <td>{r.component_name}</td>
//                 <td>{r.quantity_ml}</td>
//                 <td>{r.status}</td>
//                 <td>{r.is_urgent ? "✅" : "❌"}</td>
//                 <td>{new Date(r.created_at).toLocaleString()}</td>
//               </tr>
//             ))}
//           </tbody>
//         </table>
//       )}
//     </div>
//   );
// };

// export default StaffRequests;
