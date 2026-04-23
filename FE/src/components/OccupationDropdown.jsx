import React, { useEffect, useState } from "react";
import { Select, message } from "antd";

const { Option } = Select;

const OccupationDropdown = ({ value, onChange }) => {
  const [options, setOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchOccupations = async () => {
      try {
        setLoading(true);
        const response = await fetch("http://localhost:8080/api/occupations");
 // <-- Đổi URL nếu cần
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();
        console.log("Occupation data:", data);
        setOptions(data);
      } catch (error) {
        console.error("Lỗi khi load nghề nghiệp:", error);
        message.error("Không thể tải danh sách nghề nghiệp");
      } finally {
        setLoading(false);
      }
    };

    fetchOccupations();
  }, []);

  return (
    <Select
      size="large"
      placeholder="Chọn nghề nghiệp"
      value={value}
      onChange={onChange}
      loading={loading}
      showSearch
      allowClear
      filterOption={(input, option) =>
        option?.children?.toLowerCase().includes(input.toLowerCase())
      }
    >
      {options.map((item) => (
        <Option key={item.id} value={item.id}>
          {item.name}
        </Option>
      ))}
    </Select>
  );
};

export default OccupationDropdown;
