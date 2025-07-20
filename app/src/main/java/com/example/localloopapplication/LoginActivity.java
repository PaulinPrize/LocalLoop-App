package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    // UI components
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerNavButton;

    // Firebase components
    private FirebaseAuth auth;                  // Firebase Authentication instance
    private DatabaseReference userRef;          // Reference to "users" node in Firebase Realtime DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Set layout for the login screen

        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Link UI elements from XML
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerNavButton = findViewById(R.id.registerNavButton);

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Login button logic
        loginButton.setOnClickListener(v -> {
            // Get user input and trim spaces
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Check if email and password fields are filled
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // If admin credentials are entered, go directly to Admin Welcome screen
            if (email.equals("admin@gmail.com") && password.equals("XPI76SZUqyCjVxgnUjm0")) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.putExtra("firstname", "Admin");
                intent.putExtra("role", "Admin");
                startActivity(intent);
                finish(); // End current activity
                return;
            }

            // Attempt Firebase authentication with email and password
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                // If login is successful, get user ID
                String uid = auth.getCurrentUser().getUid();

                // Retrieve user data from Realtime Database using UID
                userRef.child(uid).get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        // Extract firstname and role from the database
                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        // Navigate to WelcomeActivity with user info
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.putExtra("firstname", firstname);
                        intent.putExtra("role", role);
                        startActivity(intent);
                        finish(); // End login screen
                    } else {
                        // User record not found in database
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> {
                // Show error if login fails
                Toast.makeText(this, "Connection failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        });

        // Navigate to Register screen when user clicks register button
        registerNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
