package com.managementsystem.studentsmanagementsystem.services;

import com.managementsystem.studentsmanagementsystem.models.Role;
import com.managementsystem.studentsmanagementsystem.models.User;
import com.managementsystem.studentsmanagementsystem.repos.RoleRepository;
import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initRoles() {
        if (roleRepository.findByName("USER") == null) {
            roleRepository.save(new Role("USER"));
        }
        if (roleRepository.findByName("ADMIN") == null) {
            roleRepository.save(new Role("ADMIN"));
        }
    }


    public String registerUser(User user, String... roles) {
        // Validate input
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty() ||
                user.getLastName() == null || user.getLastName().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getDob() == null || user.getClassName() == null) {
            throw new IllegalArgumentException("All required fields (firstName, lastName, password, dob, className) must be provided.");
        }
        if (roles == null || roles.length == 0) {
            throw new IllegalArgumentException("At least one role must be provided.");
        }

        Set<Role> assignedRoles = Arrays.stream(roles)
                .map(roleRepository::findByName)
                .peek(role -> {
                    if (role == null) {
                        throw new IllegalStateException("Role not found. Ensure roles are initialized.");
                    }
                })
                .collect(Collectors.toSet());

        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setDob(user.getDob());
        newUser.setClassName(user.getClassName());
        newUser.setScore(user.getScore());
        newUser.setPhotoPath(user.getPhotoPath() != null ? user.getPhotoPath() : "");
        newUser.setStatus(user.getStatus());
        newUser.setRoles(assignedRoles);

        // Save the user
        userRepository.save(newUser);

        return "User registered successfully";
    }

    public boolean authenticateUser(String firstName, String lastName, String password) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if (user == null) {
            return false;
        }

        return passwordEncoder.matches(password, user.getPassword());
    }
}
