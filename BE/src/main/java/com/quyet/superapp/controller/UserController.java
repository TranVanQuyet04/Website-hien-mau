package com.quyet.superapp.controller;

import com.quyet.superapp.dto.UserDTO;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.entity.UserProfile;
import com.quyet.superapp.mapper.UserMapper;
import com.quyet.superapp.repository.UserRepository;
import com.quyet.superapp.service.UserService;
import com.quyet.superapp.service.UserService1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;





    @GetMapping("/doctors")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllDoctors() {
        List<UserDTO> doctors = userService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }



}
