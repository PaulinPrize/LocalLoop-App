package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput, passwordInput, roleInput;
    private Button registerButton;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        roleInput = findViewById(R.id.roleInput);
        registerButton = findViewById(R.id.registerButton);

        // Initialiser Firebase
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String role = roleInput.getText().toString().trim().toLowerCase();

                if (firstName.isEmpty() || !isAlpha(firstName)) {
                    showToast("Enter a valid first name (letters only)");
                    return;
                }
                if (lastName.isEmpty() || !isAlpha(lastName)) {
                    showToast("Enter a valid last name (letters only)");
                    return;
                }
                if (!isValidEmail(email)) {
                    showToast("Enter a valid email address");
                    return;
                }
                if (!isValidPassword(password)) {
                    showToast("Password must be 6+ chars, include uppercase, digit, and symbol");
                    return;
                }
                if (!isValidRole(role)) {
                    showToast("Role must be 'organizer' or 'participant'");
                    return;
                }

                // Créer un compte Firebase
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                    String uid = auth.getCurrentUser().getUid();

                    // Enregistrer les données dans la Realtime Database
                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("firstname", firstName);
                    userMap.put("lastname", lastName);
                    userMap.put("email", email);
                    userMap.put("role", role);

                    userRef.child(uid).setValue(userMap).addOnSuccessListener(unused -> {
                        showToast("Registration successful");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }).addOnFailureListener(e -> {
                        showToast("Failed to save user data: " + e.getMessage());
                    });
                }).addOnFailureListener(e -> {
                    showToast("Registration failed: " + e.getMessage());
                });
            }
        });
    }
    private boolean isAlpha(String input) {
        return input.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*+=?-].*");
    }

    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("organizer") || role.equalsIgnoreCase("participant");
    }

    private void showToast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
