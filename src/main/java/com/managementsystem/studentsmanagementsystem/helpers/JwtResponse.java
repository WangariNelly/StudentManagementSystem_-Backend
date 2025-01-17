package com.managementsystem.studentsmanagementsystem.helpers;

import java.util.List;

public class JwtResponse {
    private String token;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public JwtResponse() {
    }

    public JwtResponse(String token, String firstName, String lastName, List<String> roles) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
