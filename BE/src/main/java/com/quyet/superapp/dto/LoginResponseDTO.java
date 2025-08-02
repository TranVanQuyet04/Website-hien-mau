package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private String role;
    private boolean enable;
    private String accessToken;
}
