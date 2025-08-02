const sql = require("mssql");
const config = require("./db.config");

const poolPromise = sql.connect(config)
  .then(pool => {
    console.log("✅ Đã kết nối SQL Server");
    return pool;
  })
  .catch(err => {
    console.error("❌ Kết nối thất bại:", err);
    throw err;
  });

module.exports = {
  sql,
  poolPromise
};

