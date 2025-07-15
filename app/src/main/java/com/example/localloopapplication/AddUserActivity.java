package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity to add a new user to the Firebase Realtime Database.
 * Allows input of first name, last name, email, and user role.
 */
public class AddUserActivity extends AppCompatActivity {

    // Input fields for user details
    private EditText edtFirstName, edtLastName, edtEmail;
    private Spinner spinnerRole;
    private Button btnSave;

    // Reference to the "users" table in Firebase
    private DatabaseReference usersRef;
    // Flag to prevent multiple submissions while saving
    private boolean isSubmitting = false;

    /**
     * Called when the activity is starting. Initializes the UI and event handling.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Initialize Firebase reference to "users" node
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Link UI components
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSaveUser);

        // Set up the role spinner with values from resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.add_user_roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        // Set the save button click listener
        btnSave.setOnClickListener(v -> {
            // Prevent double submissions
            if (isSubmitting) return;
            isSubmitting = true;

            // Get user input values
            String firstName = edtFirstName.getText().toString().trim();
            String lastName = edtLastName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            // Check for empty fields
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                isSubmitting = false;
                return;
            }

            // Generate a unique user ID
            String id = usersRef.push().getKey();
            // Create a new user object (status default: "Active")
            User newUser = new User(firstName, lastName, email, role, "Active");

            // Add user to the database if ID generation succeeded
            if (id != null) {
                usersRef.child(id).setValue(newUser)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after successful add
                        })
                        .addOnFailureListener(e -> {
                            isSubmitting = false;
                            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
                        });
            } else {
                isSubmitting = false;
                Toast.makeText(this, "Error generating user ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
