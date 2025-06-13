package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button registerNavButton;

    private FirebaseAuth auth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Make sure this file exists

        emailInput = findViewById(R.id.emailInput);         // IDs from XML
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerNavButton = findViewById(R.id.registerNavButton);

        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.equals("admin@gmail.com") && password.equals("XPI76SZUqyCjVxgnUjm0")) {
                Intent intent = new Intent(LoginActivity.this, AdminWelcomeActivity.class);
                intent.putExtra("firstname", "Admin");
                intent.putExtra("role", "Admin");
                startActivity(intent);
                finish();
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                String uid = auth.getCurrentUser().getUid();
                userRef.child(uid).get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.putExtra("firstname", firstname);
                        intent.putExtra("role", role);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Connection failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        });

        registerNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
