package com.managementsystem.studentsmanagementsystem.services;

import com.managementsystem.studentsmanagementsystem.repos.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserDetailsServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        String[] names = fullName.split(" ", 2);
        if (names.length != 2) {
            throw new UsernameNotFoundException("Invalid username format. Expected 'firstName lastName'");
        }
        String firstName = names[0];
        String lastName = names[1];


        com.managementsystem.studentsmanagementsystem.models.User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + fullName);
        }

        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        String token = jwtUtil.generateToken(user.getFirstName(), user.getLastName(), authorities);
        System.out.println("Generated token: " + token);

        return User.builder()
                .username(user.getFirstName() + " " + user.getLastName())
                .password(user.getPassword())

                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toArray(GrantedAuthority[]::new))
                .build();
    }
}
