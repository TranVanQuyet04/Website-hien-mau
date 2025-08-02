module.exports = {
  user: "sa",
  password: "12345",
  server: "localhost",  // KHÔNG có \\BINH nếu TCP/IP + cổng chuẩn
  database: "Blood_Donation",
  options: {
    encrypt: false,
    trustServerCertificate: true,
    port: 1433,
  },
};