package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private EditText etName, etDescription, etCategory, etFee, etDateTime;
    private Button btnSave;

    private DatabaseReference eventsRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event); // We'll build this layout next

        // Firebase reference to "events" table
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Link input fields
        etName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etEventDescription);
        etCategory = findViewById(R.id.etEventCategory);
        etFee = findViewById(R.id.etEventFee);
        etDateTime = findViewById(R.id.etEventDateTime);
        btnSave = findViewById(R.id.btnSaveEvent);

        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void saveEvent() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String feeText = etFee.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Event name is required");
            return;
        }

        double fee = 0;
        try {
            if (!feeText.isEmpty()) fee = Double.parseDouble(feeText);
        } catch (NumberFormatException e) {
            etFee.setError("Invalid fee");
            return;
        }

        // Get current logged-in organizer's UID
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String eventId = UUID.randomUUID().toString();
        Event event = new Event(eventId, organizerId, name, description, category, fee, dateTime);

        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
