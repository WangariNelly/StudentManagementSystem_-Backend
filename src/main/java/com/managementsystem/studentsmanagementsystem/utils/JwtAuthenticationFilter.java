package com.managementsystem.studentsmanagementsystem.utils;

import com.managementsystem.studentsmanagementsystem.services.JwtUtil;
import com.managementsystem.studentsmanagementsystem.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String firstName = null;
        String lastName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            Claims claims = jwtUtil.extractAllClaims(token);
            firstName = claims.get("firstName", String.class);
            lastName = claims.get("lastName", String.class);
        }

        if (firstName != null && lastName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String fullName = firstName + " " + lastName;
            UserDetails userDetails = userDetailsService.loadUserByUsername(fullName);

            if (jwtUtil.isTokenValid(token, firstName, lastName)) {
                Collection<? extends GrantedAuthority> roles = jwtUtil.extractRolesFromToken(token);
                System.out.println("User roles from token: " + roles);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()

                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
