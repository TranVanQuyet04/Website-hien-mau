package com.quyet.superapp.service;

import com.quyet.superapp.dto.UserDTO;
import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.mapper.UserMapper;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService1 {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public List<UserDTO> getAll() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public Optional<UserDTO> getById(Long id) {

        return userRepository.findById(id)
                .map(UserMapper::toDTO);
    }

    public UserDTO create(UserDTO dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        User user = UserMapper.toEntity(dto, role);
        return UserMapper.toDTO(userRepository.save(user));
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setEnable(dto.isEnable());
        user.setRole(role);

        return UserMapper.toDTO(userRepository.save(user));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
