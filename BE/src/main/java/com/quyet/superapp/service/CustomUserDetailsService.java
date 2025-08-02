package com.quyet.superapp.service;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        String rawRole = user.getRole().getName();
        String roleName = rawRole.toUpperCase().trim();

        System.out.println("🧠 Role granted: ROLE_" + roleName);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnable(), true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName))
        );
    }


}
