package com.example.localloopapplication;
//Admin should be a class that inherits from the User class and methods
public class Admin extends User {
    public Admin(String email, String password) {
        super("Admin", "", email, password, "admin");
    }
}
