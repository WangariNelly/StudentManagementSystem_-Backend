
package com.managementsystem.studentsmanagementsystem.controllers;

import com.managementsystem.studentsmanagementsystem.dtos.LoginRequest;
import com.managementsystem.studentsmanagementsystem.helpers.JwtResponse;
import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.RoleRepository;
import com.managementsystem.studentsmanagementsystem.services.AuthService;
import com.managementsystem.studentsmanagementsystem.services.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    public AuthController(AuthService authService,RoleRepository roleRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        try {
            String responseMessage = authService.registerUser(user, "USER");
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );


            String firstName = loginRequest.getUsername().split(" ")[0];
            String lastName = loginRequest.getUsername().split(" ")[1];

            boolean isAuthenticated = authService.authenticateUser(firstName, lastName,loginRequest.getPassword());


            if (isAuthenticated) {
                String token = jwtUtil.generateToken(firstName, lastName, authentication.getAuthorities());

                JwtResponse jwtResponse = new JwtResponse(
                        token,
                        firstName,
                        lastName,
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                );

                return ResponseEntity.ok().body(jwtResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtResponse("Invalid credentials", null, null, null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JwtResponse("Invalid login", null, null, null));
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
    }
}
