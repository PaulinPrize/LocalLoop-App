package com.example.localloopapplication;

// Participant class inherits from User class
public class Participant extends User {

    // Constructor that sets up a Participant with the specified attributes
    public Participant(String firstName, String lastName, String email, String password) {
        // Call the constructor of the User class with role set as "Participant"
        super(firstName, lastName, email, password, "Participant");
    }
}
