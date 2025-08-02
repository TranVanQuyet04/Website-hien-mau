package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.UserDTO;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;



public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnable(user.isEnable());
        dto.setRoleId(user.getRole().getRoleId());

        if (user.getUserProfile() != null) {
            dto.setFullName(user.getUserProfile().getFullName());
            dto.setStaffPosition(user.getUserProfile().getStaffPosition());
        }

        return dto;
    }

    public static User toEntity(UserDTO dto, Role role) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setEnable(dto.isEnable());
        user.setRole(role);
        return user;
    }
}
