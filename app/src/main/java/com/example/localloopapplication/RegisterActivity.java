package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    // UI elements for user input
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private Button registerButton;
    private Spinner roleSpinner;

    // Firebase authentication and database references
    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI components by finding them in the layout
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        roleSpinner = findViewById(R.id.roleInput);
        registerButton = findViewById(R.id.registerButton);

        // Set up the spinner for roles with values from resources (roles_array)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Initialize Firebase Authentication and Realtime Database references
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Set click listener on the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve and trim user inputs
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                // Get role selected from spinner and convert to lowercase for consistency
                String role = roleSpinner.getSelectedItem().toString().toLowerCase();

                // Validate first name (not empty and letters only)
                if (firstName.isEmpty() || !isAlpha(firstName)) {
                    showToast("Enter a valid first name (letters only)");
                    return;
                }
                // Validate last name (not empty and letters only)
                if (lastName.isEmpty() || !isAlpha(lastName)) {
                    showToast("Enter a valid last name (letters only)");
                    return;
                }
                // Validate email format
                if (!isValidEmail(email)) {
                    showToast("Enter a valid email address");
                    return;
                }
                // Validate password complexity
                if (!isValidPassword(password)) {
                    showToast("Password must be 6+ chars, include uppercase, digit, and symbol");
                    return;
                }

                // Create user in Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    // On success, get the UID of the new user
                    String uid = auth.getCurrentUser().getUid();

                    // Prepare user data to store in Firebase Realtime Database
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("firstname", firstName);
                    userMap.put("lastname", lastName);
                    userMap.put("email", email);
                    userMap.put("role", role);

                    // Save user data under "users/{uid}"
                    userRef.child(uid).setValue(userMap).addOnSuccessListener(unused -> {
                        showToast("Registration successful");
                        // Move to LoginActivity after successful registration
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }).addOnFailureListener(e -> {
                        // Show error if saving user data fails
                        showToast("Failed to save user data: " + e.getMessage());
                    });
                }).addOnFailureListener(e -> {
                    // Show error if Firebase user creation fails
                    showToast("Registration failed: " + e.getMessage());
                });
            }
        });
    }

    // Helper method to check if a string contains only letters
    private boolean isAlpha(String input) {
        return input.matches("[a-zA-Z]+");
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Helper method to validate password complexity requirements
    private boolean isValidPassword(String password) {
        return password.length() >= 6
                && password.matches(".*[A-Z].*")   // Contains uppercase letter
                && password.matches(".*[a-z].*")   // Contains lowercase letter
                && password.matches(".*\\d.*")     // Contains digit
                && password.matches(".*[!@#$%^&*+=?-].*"); // Contains special symbol
    }

    // (Unused in code, but here for completeness) Validate role is either organizer or participant
    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("organizer") || role.equalsIgnoreCase("participant");
    }

    // Helper method to show a short Toast message
    private void showToast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
