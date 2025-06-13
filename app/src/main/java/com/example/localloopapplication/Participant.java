package com.example.localloopapplication;

public class Participant extends User {
    public Participant(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, "Participant");
    }
}