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

    //Instance variables
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    private Button registerButton;
    private Spinner roleSpinner;
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
        roleSpinner = findViewById(R.id.roleInput);
        registerButton = findViewById(R.id.registerButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Initializer Firebase
        auth = FirebaseAuth.getInstance();

        //Checking the firebase path
        userRef = FirebaseDatabase.getInstance().getReference("users");

        //Listener fot the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String role = roleSpinner.getSelectedItem().toString().toLowerCase();

                //Validating the firstName with only letter
                if (firstName.isEmpty() || !isAlpha(firstName)) {
                    showToast("Enter a valid first name (letters only)");
                    return;
                }
                //Validating the lastName with only letter
                if (lastName.isEmpty() || !isAlpha(lastName)) {
                    showToast("Enter a valid last name (letters only)");
                    return;
                }
                //Validating the email with valid email address
                if (!isValidEmail(email)) {
                    showToast("Enter a valid email address");
                    return;
                }

                //Validating the passWord
                if (!isValidPassword(password)) {
                    showToast("Password must be 6+ chars, include uppercase, digit, and symbol");
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

                    //Handling difference errors cases realated to registration
                    userRef.child(uid).setValue(userMap).addOnSuccessListener(unused -> {
                        showToast("Registration successful"); //Successfullly registered
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }).addOnFailureListener(e -> {
                        showToast("Failed to save user data: " + e.getMessage()); //Failed to save the user
                    });
                }).addOnFailureListener(e -> {
                    showToast("Registration failed: " + e.getMessage()); //Registration error
                });
            }
        });
    }

    //This method verify if the entered information are only letters | Use for the firstName and the lastName validation
    private boolean isAlpha(String input) {
        return input.matches("[a-zA-Z]+");
    }

    //This method verify if the entered email address is valid(Contain a @, contain a domain...)
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //This method verify if the entered password has lower, upperCase, special characters...
    private boolean isValidPassword(String password) {
        return password.length() >= 6 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*\\d.*") && password.matches(".*[!@#$%^&*+=?-].*");
    }

    ////This method verify if the entered role valid(either Organizer or Participant)
    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("organizer") || role.equalsIgnoreCase("participant");
    }

    private void showToast(String msg) {
        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
