package com.example.localloopapplication;

public class User {
    public String id;           // <-- Firebase unique key
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String role;
    public String status;

    public User() {
        this.status = "Active";
    }

    public User(String id, String firstname, String lastname, String email, String password, String role, String status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(String firstName, String lastName, String email, String role, String status) {
        this.id = null; // or generate later
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = ""; // default blank password
        this.role = role;
        this.status = status;
    }

    public User(String id, String firstname, String lastname, String email, String role, String status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
