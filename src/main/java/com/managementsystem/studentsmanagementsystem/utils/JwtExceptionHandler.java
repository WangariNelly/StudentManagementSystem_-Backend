package com.managementsystem.studentsmanagementsystem.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

import java.security.SignatureException;

@Component
public class JwtExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        try {
            if (authException instanceof ExpiredJwtException) {
                handleExpiredJwtException(response);
            } else if (authException instanceof UnsupportedJwtException) {
                handleUnsupportedJwtException(response);
            } else if (authException instanceof MalformedJwtException) {
                handleMalformedJwtException(response);
            } else if (authException instanceof SignatureException) {
                handleSignatureException(response);
            } else if (authException instanceof BadCredentialsException) {
                handleBadCredentialsException(response);
            } else {
                handleGenericAuthenticationException(response);
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal server error.\"}");
        }
    }

    private void handleExpiredJwtException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"JWT token expired\"}");
    }

    private void handleUnsupportedJwtException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unsupported JWT token\"}");
    }

    private void handleMalformedJwtException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Malformed JWT token\"}");
    }

    private void handleSignatureException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid JWT signature\"}");
    }

    private void handleBadCredentialsException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid credentials\"}");
    }

    private void handleGenericAuthenticationException(HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Authentication failed\"}");
    }
}
