package com.quyet.superapp.config.jwt;

import com.quyet.superapp.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        // ✅ Bỏ qua kiểm tra token nếu là public endpoint
        String uri = req.getRequestURI();
        if (uri.startsWith("/api/auth/") || uri.startsWith("/api/verify-otp") ||
                uri.startsWith("/api/forgot") || uri.startsWith("/api/change-password") ||
                uri.startsWith("/api/public") || uri.startsWith("/api/blog")) {
            chain.doFilter(req, res);
            return;
        }

        // ✅ Phần xử lý token
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (tokenProvider.validateToken(token)) {
                Long userId = tokenProvider.getUserId(token);
                String username = tokenProvider.getUsername(token);

                var userDetails = userDetailsService.loadUserByUsername(username);

                UserPrincipal principal = new UserPrincipal(
                        userId,
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities(),
                        userDetails.isEnabled()
                );

                var auth = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(req, res);
    }

}
