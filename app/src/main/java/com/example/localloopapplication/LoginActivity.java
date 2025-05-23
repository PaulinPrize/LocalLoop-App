package com.example.localloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication to check credentials
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Get the current user from Firebase
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Navigate to the appropriate welcome screen based on role
                            navigateToWelcomeScreen(user);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToWelcomeScreen(FirebaseUser user) {
        // Check if the user is admin or regular user (could be Organizer/Participant)
        String email = user.getEmail();
        if ("admin@example.com".equals(email)) {  // Hardcoded for the admin user
            startActivity(new Intent(LoginActivity.this, AdminWelcomeActivity.class));
        } else {
            startActivity(new Intent(LoginActivity.this, UserWelcomeActivity.class));
        }
        finish(); // Close the login activity
    }
}
