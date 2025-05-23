package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button createAccountButton;
    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// we need to initialize input files and buttons from the layout
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        exitButton = findViewById(R.id.exitButton);
// this par tof the code will handle login button
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
// the base case if the email and the password are correct
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("admin") && password.equals("XPI76SZUqyCjVxgnUjm0")) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                intent.putExtra("firstName", "Admin");
                intent.putExtra("role", "admin");
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        createAccountButton.setOnClickListener(v -> {
            Toast.makeText(this, "Signup form coming soon...", Toast.LENGTH_SHORT).show();
        });


        exitButton.setOnClickListener(v -> {
            finish(); // Exits the app to make it user friendly
        });



    }
}
