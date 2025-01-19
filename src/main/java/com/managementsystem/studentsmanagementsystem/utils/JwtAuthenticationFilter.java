package com.managementsystem.studentsmanagementsystem.utils;

import com.managementsystem.studentsmanagementsystem.services.JwtUtil;
import com.managementsystem.studentsmanagementsystem.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    private static final List<String> SKIP_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/error"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return SKIP_PATHS.stream().anyMatch(path::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String firstName = null;
        String lastName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.extractAllClaims(token);
                firstName = claims.get("firstName", String.class);
                lastName = claims.get("lastName", String.class);
            } catch (ExpiredJwtException ex) {
                sendErrorResponse(response, request, "Token is expired. Please login again.");
                return;
            } catch (Exception ex) {
                sendErrorResponse(response, request, "Invalid token.");
                return;
            }
        }

        if (firstName != null && lastName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            String fullName = firstName + " " + lastName;
            UserDetails userDetails = userDetailsService.loadUserByUsername(fullName);

            if (jwtUtil.isTokenValid(token, firstName, lastName)) {
                Collection<? extends GrantedAuthority> roles = jwtUtil.extractRolesFromToken(token);
                System.out.println("User roles from token: " + roles);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpServletRequest request, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        String jsonResponse = String.format("{" + "\"timestamp\":\"%s\"," + "\"status\":%d," + "\"error\":\"%s\"," + "\"path\":\"%s\"" + "}", Instant.now().toString(), HttpServletResponse.SC_FORBIDDEN, errorMessage, request.getRequestURI());

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
