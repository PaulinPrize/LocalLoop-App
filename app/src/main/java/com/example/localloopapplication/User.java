package com.example.localloopapplication;

public class User {
    // Public fields for user information
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String role;

    // Default no-argument constructor (required for Firebase deserialization)
    public User() {}

    /**
     * Constructor with all fields including password.
     * Used when creating a full User object with all data.
     */
    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructor without password field.
     * Used when password is not needed (e.g., reading user data without authentication info).
     */
    public User(String firstName, String lastName, String email, String role) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.role = role;
    }
}
