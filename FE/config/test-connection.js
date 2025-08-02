const { poolPromise } = require("./database");

poolPromise
  .then(() => {
    console.log("✅ Kết nối test OK");
    setTimeout(() => process.exit(), 2000);
  })
  .catch((err) => {
    console.error("❌ Lỗi khi test kết nối:", err);
    process.exit(1);
  });

