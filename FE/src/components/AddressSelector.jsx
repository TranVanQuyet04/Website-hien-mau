// src/components/AddressSelector.jsx
import React, { useEffect, useState } from "react";
import { Form, Select } from "antd";
import { HomeOutlined } from "@ant-design/icons";
import axios from "axios";
import { apiUrl } from "../config/api";

const { Option } = Select;

const AddressSelector = ({ form }) => {
  const [districts, setDistricts] = useState([]);
  const [wards, setWards] = useState([]);
  const [loadingDistricts, setLoadingDistricts] = useState(false);
  const [loadingWards, setLoadingWards] = useState(false);

  const defaultCity = { id: 1, name: "TP Hồ Chí Minh" };

  useEffect(() => {
    let isMounted = true;

    const init = async () => {
      form.setFieldsValue({ province: defaultCity.name });
      setLoadingDistricts(true);
      try {
        const res = await axios.get(
          apiUrl(`api/districts/by-city?cityId=${defaultCity.id}&t=${Date.now()}`)
        );
        console.log("📦 Districts API response:", res.data);
        console.log(
          "📦 Đang gọi API:",
          `/api/districts/by-city?cityId=${defaultCity.id}&t=${Date.now()}`
        );
console.log("📦 Districts API response (raw):", res);
console.log("📦 Districts API response (data):", res.data);


        const result = Array.isArray(res.data) ? res.data : res.data?.data || [];
        if (isMounted) setDistricts(result);
      } catch (error) {
        console.error("❌ Lỗi khi load quận/huyện:", error);
        if (isMounted) setDistricts([]);
      } finally {
        if (isMounted) setLoadingDistricts(false);
      }
    };

    init();
    return () => {
      isMounted = false;
    };
  }, [form]);

  const fetchWards = async (districtId) => {
    setWards([]); // Clear danh sách cũ
    setLoadingWards(true);
    try {
      const res = await axios.get(
        apiUrl(`api/wards/by-district?districtId=${districtId}&t=${Date.now()}`)
      );
      console.log("🏘️ Wards API response:", res.data);

      const result = Array.isArray(res.data) ? res.data : res.data?.data || [];
      setWards(result);
    } catch (error) {
      console.error("❌ Lỗi khi load phường/xã:", error);
      setWards([]);
    } finally {
      setLoadingWards(false);
    }
  };

  return (
    <>
      <Form.Item
        label="Tỉnh/Thành phố"
        name="province"
        initialValue={defaultCity.name}
      >
        <Select disabled size="large" prefix={<HomeOutlined />}>
          <Option value={defaultCity.name}>{defaultCity.name}</Option>
        </Select>
      </Form.Item>

      <Form.Item
        label="Quận/Huyện"
        name="district"
        rules={[{ required: true, message: "Vui lòng chọn quận/huyện" }]}
        hasFeedback
      >
        <Select
          size="large"
          placeholder="Chọn quận/huyện"
          loading={loadingDistricts}
           notFoundContent={loadingDistricts ? "Đang tải..." : "Không có quận nào"}
          onChange={(districtId) => {
            form.setFieldsValue({ ward: undefined });
            fetchWards(districtId);
          }}
        >
          {districts.map((d) => (
            <Option key={d.districtId} value={d.districtId}>
            {d.districtName}
            </Option>
          ))}
        </Select>
      </Form.Item>

      <Form.Item
        label="Phường/Xã"
        name="ward"
        rules={[{ required: true, message: "Vui lòng chọn phường/xã" }]}
        hasFeedback
      >
        <Select
          size="large"
          placeholder="Chọn phường/xã"
          loading={loadingWards}
           notFoundContent={loadingWards ? "Đang tải..." : "Không có phường nào"}
        >
          {wards.map((w) => (
            <Option key={w.wardId} value={w.wardId}>
                {w.wardName}
            </Option>

          ))}
        </Select>
      </Form.Item>
    </>
  );
};

export default AddressSelector;
