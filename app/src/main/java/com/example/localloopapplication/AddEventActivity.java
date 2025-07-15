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

public class AddEventActivity extends AppCompatActivity {

    private EditText etName, etDescription, etFee, etDateTime;
    private Spinner categorySpinner;
    private Button btnSave;

    private DatabaseReference eventsRef, categoriesRef;

    private ArrayList<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    private boolean isEditing = false;
    private String editingEventId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEditing = getIntent().getBooleanExtra("isEditing", false);
        
        setContentView(R.layout.activity_add_event); // We'll build this layout next

        // Firebase reference to "events" table
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        categoriesRef = FirebaseDatabase.getInstance().getReference("event_categories");

        // flag block of code 
        // Link input fields
        etName = findViewById(R.id.etEventName);
        etDescription = findViewById(R.id.etEventDescription);
        categorySpinner = findViewById(R.id.spinnerEventCategory);
        etFee = findViewById(R.id.etEventFee);
        etDateTime = findViewById(R.id.etEventDateTime);
        btnSave = findViewById(R.id.btnSaveEvent);
        // end flag 
        
        if (isEditing) {
            String eventId = getIntent().getStringExtra("eventId");
            String name = getIntent().getStringExtra("name");
            String description = getIntent().getStringExtra("description");
            String category = getIntent().getStringExtra("category");
            double fee = getIntent().getDoubleExtra("fee", 0);
            String dateTime = getIntent().getStringExtra("dateTime");

            etName.setText(name);
            etDescription.setText(description);
            etFee.setText(String.valueOf(fee));
            etDateTime.setText(dateTime);

            categorySpinner.post(() -> {
                int index = categoryList.indexOf(category);
                if (index >= 0) {
                    categorySpinner.setSelection(index);
                }
            });
            this.editingEventId = eventId;
        }
        
        // Initialize category spinner adapter
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        categorySpinner.setAdapter(categoryAdapter);

        // Load categories from Firebase
        loadCategories();

        etDateTime.setOnClickListener(v -> showDateTimePicker());

        btnSave.setOnClickListener(v -> saveEvent());
    }

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

    private void loadCategories() {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                categoryList.add("Select Category");  // Default placeholder
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

    private void saveEvent() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "";
        String feeText = etFee.getText().toString().trim();
        String dateTime = etDateTime.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Event name is required");
            return;
        }

        if (category.equals("Select Category") || category.isEmpty()) {
            Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show();
            return;
        }

        double fee = 0;
        try {
            if (!feeText.isEmpty()) fee = Double.parseDouble(feeText);
        } catch (NumberFormatException e) {
            etFee.setError("Invalid fee");
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current logged-in organizer's UID
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // flag block of code 
        //String eventId = UUID.randomUUID().toString();
        // end flag
        String eventId;
        if(isEditing && editingEventId != null){
            eventId = editingEventId;
        } else {
            eventId = UUID.randomUUID().toString();
        }
        Event event = new Event(organizerId, name, description, category, fee, dateTime);
        event.setId(eventId);
        eventsRef.child(eventId).setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}