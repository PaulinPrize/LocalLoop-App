package com.example.localloopapplication;

// Organizer class inherits from User class
public class Organizer extends User {

    // Constructor that sets up an Organizer with the specified attributes
    public Organizer(String firstName, String lastName, String email, String password) {
        // Call the constructor of the User class with role set as "Organizer"
        super(firstName, lastName, email, password, "Organizer");
    }
}
