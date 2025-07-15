package com.example.localloopapplication;

/**
 * User class represents a user in the application.
 * Stores user details such as name, email, password, role, and status.
 */
public class User {
    public String id;           // Firebase unique key (can be null until generated)
    public String firstname;    // User's first name
    public String lastname;     // User's last name
    public String email;        // User's email address
    public String password;     // User's password (can be blank, may not be used)
    public String role;         // User's role (e.g., Admin, Organizer, etc.)
    public String status;       // User's status (e.g., Active, Inactive)

    /**
     * Default constructor required for Firebase data mapping.
     * Sets status to "Active" by default.
     */
    public User() {
        this.status = "Active";
    }

    /**
     * Constructor with all fields, including password.
     * @param id        Firebase unique ID
     * @param firstname User's first name
     * @param lastname  User's last name
     * @param email     User's email
     * @param password  User's password
     * @param role      User's role
     * @param status    User's status
     */
    public User(String id, String firstname, String lastname, String email, String password, String role, String status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    /**
     * Constructor used when password is not needed, for quick creation.
     * @param firstName User's first name
     * @param lastName  User's last name
     * @param email     User's email
     * @param role      User's role
     * @param status    User's status
     */
    public User(String firstName, String lastName, String email, String role, String status) {
        this.id = null; // or generate later
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.password = ""; // default blank password
        this.role = role;
        this.status = status;
    }

    /**
     * Alternative constructor without password field (sometimes used in app logic).
     * @param id        Firebase unique ID
     * @param firstname User's first name
     * @param lastname  User's last name
     * @param email     User's email
     * @param role      User's role
     * @param status    User's status
     */
    public User(String id, String firstname, String lastname, String email, String role, String status) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    /**
     * Returns the full name of the user.
     * @return first name + last name
     */
    public String getFullName() {
        return firstname + " " + lastname;
    }
}
