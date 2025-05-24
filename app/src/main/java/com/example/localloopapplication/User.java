package com.example.localloopapplication;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String role;

    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }
}
