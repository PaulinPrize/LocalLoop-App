package com.example.localloopapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Activity for adding a new event to the database.
 * Handles input for event name, description, category, fee, and date/time.
 * Loads categories from Firebase and saves events under "events".
 */
public class AddEventActivity extends AppCompatActivity {

    // Input fields for event information
    private EditText etName, etDescription, etFee, etDateTime;
    private Spinner categorySpinner;
    private Button btnSave;

    // Firebase database references for events and categories
    private DatabaseReference eventsRef, categoriesRef;

    // List and adapter for categories spinner
    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    /**
     * onCreate initializes the activity, sets up UI and loads event categories.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event); // We'll build this layout next

        // Firebase reference to "events" table
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        categoriesRef = FirebaseDatabase.getInstance().getReference("event_categories");

        // Link input fields to layout views
        etName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etEventDescription);
        categorySpinner = findViewById(R.id.spinnerEventCategory);
        etFee = findViewById(R.id.etEventFee);
        etDateTime = findViewById(R.id.etEventDateTime);
        btnSave = findViewById(R.id.btnSaveEvent);

        // Initialize category spinner adapter
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        // Load categories from Firebase
        loadCategories();

        // Set up date/time picker dialog on date/time field click
        etDateTime.setOnClickListener(v -> showDateTimePicker());

        // Save event when button is clicked
        btnSave.setOnClickListener(v -> saveEvent());
    }

    /**
     * Displays dialogs for picking date and time, and sets the selected value in the date/time field.
     */
    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        // Show date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Show time picker dialog after date is selected
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Format and set the date/time in the EditText
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                etDateTime.setText(sdf.format(calendar.getTime()));
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

    /**
     * Loads event categories from Firebase and populates the spinner.
     */
    private void loadCategories() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                categoryList.add("Select Category");  // Default placeholder
                // Loop through categories in database and add them to the list
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("name").getValue(String.class);
                    if (categoryName != null) {
                        categoryList.add(categoryName);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddEventActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates input fields and saves the event to Firebase database.
     */
    private void saveEvent() {
        // Retrieve input values
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "";
        String feeText = etFee.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();

        // Validate event name
        if (name.isEmpty()) {
            etName.setError("Event name is required");
            return;
        }

        // Validate category selection
        if (category.equals("Select Category") || category.isEmpty()) {
            Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate and parse event fee
        double fee = 0;
        try {
            if (!feeText.isEmpty()) fee = Double.parseDouble(feeText);
        } catch (NumberFormatException e) {
            etFee.setError("Invalid fee");
            return;
        }

        // Check if user is authenticated
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current logged-in organizer's UID
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Generate a unique event ID
        String eventId = UUID.randomUUID().toString();
        // Create a new Event object
        Event event = new Event(organizerId, name, description, category, fee, dateTime);

        // Save event to Firebase under the generated event ID
        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after successful save
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
