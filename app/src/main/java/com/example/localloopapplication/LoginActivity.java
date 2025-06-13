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
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);         // IDs from XML
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerNavButton = findViewById(R.id.registerNavButton);

        //Connecting to the database
        auth = FirebaseAuth.getInstance();

        //Making sure we have the right FireBase path
        userRef = FirebaseDatabase.getInstance().getReference("users");

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            //Checking if the user is the Adimn
            if (email.equals("admin@gmail.com") && password.equals("XPI76SZUqyCjVxgnUjm0")) {

                //Creating a new intent in other to call the WelcomeActivity class after the admin successfully log in
                Intent intent = new Intent(LoginActivity.this, AdminWelcomeActivity.class);

                //Adding extended data to the intent
                intent.putExtra("firstname", "Admin");
                intent.putExtra("role", "Admin");

                //Run the intent
                startActivity(intent);

                //Terminating the activity and set resources free for other intents
                finish();

                //Stop executing and exit the method
                return;
            }

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                String uid = auth.getCurrentUser().getUid();
                userRef.child(uid).get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {

                        String firstname = snapshot.child("firstname").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        //Creating a new intent in other to call the WelcomeActivity class after the admin successfully log in
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);

                        //Adding extended data to the intent
                        intent.putExtra("firstname", firstname);
                        intent.putExtra("role", role);

                        //Run the intent
                        startActivity(intent);

                        //Terminating the activity and set resources free for other intents
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
            //Creating a new intent in other to call the RegisterActivity class after the register button has been clicked
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

            ////Run the intent
            startActivity(intent);
        });
    }
}
