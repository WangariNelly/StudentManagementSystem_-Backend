package com.managementsystem.studentsmanagementsystem.dtos;

public class LoginRequest {

    private String firstName;
    private String lastName;
    private String password;

    public LoginRequest(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    public String getUsername() {
        return firstName + " " + lastName;
    }
    public void setUsername(String username) {
        String[] names = username.split(" ", 2);
        this.firstName = names.length > 0 ? names[0] : "";
        this.lastName = names.length > 1 ? names[1] : "";
    }

    public LoginRequest() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
