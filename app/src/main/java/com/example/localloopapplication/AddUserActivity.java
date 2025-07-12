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

public class AddUserActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtEmail;
    private Spinner spinnerRole;
    private Button btnSave;

    private DatabaseReference usersRef;
    private boolean isSubmitting = false; // prevent multiple adds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnSave = findViewById(R.id.btnSaveUser);

        // Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.add_user_roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            if (isSubmitting) return;
            isSubmitting = true;

            String firstName = edtFirstName.getText().toString().trim();
            String lastName = edtLastName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                isSubmitting = false;
                return;
            }

            String id = usersRef.push().getKey();
            User newUser = new User(firstName, lastName, email, role, "Active");

            if (id != null) {
                usersRef.child(id).setValue(newUser)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
                            finish(); // Exit activity after adding
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
