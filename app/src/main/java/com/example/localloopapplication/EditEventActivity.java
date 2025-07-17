package com.example.localloopapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    private EditText editName, editDateTime, editDescription, editFee;
    private Button btnConfirm;
    private String eventId;
    private String organizerId;  // <-- new field

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Initialize views
        editName = findViewById(R.id.editEventName);
        editDateTime = findViewById(R.id.editEventDateTime);
        editDescription = findViewById(R.id.editEventDescription);
        editFee = findViewById(R.id.editEventFee);
        btnConfirm = findViewById(R.id.btnConfirmEdit);

        // Get data from Intent
        eventId = getIntent().getStringExtra("eventId");
        organizerId = getIntent().getStringExtra("organizerId");  // <-- get organizerId here
        String name = getIntent().getStringExtra("name");
        String dateTime = getIntent().getStringExtra("dateTime");
        String description = getIntent().getStringExtra("description");
        String feeStr = getIntent().getStringExtra("fee");

        // Set data to input fields
        editName.setText(name);
        editDateTime.setText(dateTime);
        editDescription.setText(description);
        editFee.setText(feeStr);

        // Date and time picker on click
        editDateTime.setOnClickListener(v -> showDateTimePicker());

        // Confirm button click listener
        btnConfirm.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newDateTime = editDateTime.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();
            String feeInput = editFee.getText().toString().trim();

            if (newName.isEmpty() || newDateTime.isEmpty() || feeInput.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double parsedFee;
            try {
                parsedFee = Double.parseDouble(feeInput);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid fee format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create updated event object using the real organizerId
            Event updatedEvent = new Event(
                    organizerId,  // <-- use real organizerId here
                    newName,
                    newDescription,
                    "Unchanged",  // change if you have categories to update
                    parsedFee,
                    newDateTime
            );

            // Set the event ID separately
            updatedEvent.setId(eventId);

            // Update event in Firebase
            FirebaseDatabase.getInstance().getReference("events")
                    .child(eventId)
                    .setValue(updatedEvent)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();  // close this activity and return
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Show DatePicker followed by TimePicker
    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                String formattedDateTime = String.format(Locale.getDefault(),
                                        "%04d-%02d-%02d %02d:%02d",
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH) + 1,
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE));

                                editDateTime.setText(formattedDateTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);

                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
