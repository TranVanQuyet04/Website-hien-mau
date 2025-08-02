package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private boolean enable;   // nên giữ là enable
    private Long roleId;

    // Bổ sung để nhận dữ liệu từ profile
    private String fullName;
    private String staffPosition;
}
