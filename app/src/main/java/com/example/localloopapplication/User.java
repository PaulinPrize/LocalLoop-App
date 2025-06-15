package com.example.localloopapplication;

public class User {
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String role;
    public String id;
    public boolean active;

    public User(){}

    public User(String firstName, String lastName, String email, String password, String role, String id) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.id=id;
        this.active=true;
    }

    public User(String firstName, String lastName, String email, String role, String id) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.role = role;
        this.id=id;
        this.active=true;
    }
}
