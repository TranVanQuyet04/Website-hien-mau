package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String addressStreet; // Ví dụ: "12 Nguyễn Huệ"
    private Long wardId;          // chỉ dùng wardId ở phía gửi request
    private String ward;
    private String district;
    private String city;
}
