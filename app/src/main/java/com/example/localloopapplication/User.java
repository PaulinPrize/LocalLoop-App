package com.example.localloopapplication;

public class User {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String role;
    public String status; // NEW FIELD: "Active", "Inactive", etc.

    public User() {
            this.status = "Active";
    }

    public User(String firstName, String lastName, String email, String password, String role, String status) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(String firstName, String lastName, String email, String role, String status) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
