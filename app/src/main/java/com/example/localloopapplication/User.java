package com.example.localloop;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String role;

    // Default constructor (required by Firebase)
    public User() {
        // This constructor is needed by Firebase to deserialize the object.
    }

    // Constructor with parameters
    public User(String firstName, String lastName, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }
}