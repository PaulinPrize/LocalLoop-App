package com.example.localloopapplication;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

// Activity to add or edit an event
// Contains UI initialization in onCreate, and a method to save or update the event in Firebase
public class AddEventActivity extends AppCompatActivity {

    // UI elements for input fields and submit button
    private EditText etEventName, etEventDescription, etParticipationFee, etYear, etMonth, etDay, etHour, etMinute;
    private Spinner categorySpinner;
    // TODO: add code to assign elements to the spinner 
    //       save and retrieve choice of category
    private Button btnSave;

    // Reference to Firebase Realtime Database for events
    private DatabaseReference eventRef;

    // Flags and variables to handle edit mode
    private boolean isEditMode = false;
    private String editingEventId;

    // Called when the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_add); // Set layout for this activity

        // Initialize UI elements by finding them by their IDs
        etEventName = findViewById(R.id.eventNameTextView);
        etEventDescription = findViewById(R.id.eventDescriptionTextView);
        etParticipationFee = findViewById(R.id.eventParticipationFeeTextView);
        etYear = findViewById(R.id.eventYearTextView);
        etMonth = findViewById(R.id.eventMonthTextView);
        etDay = findViewById(R.id.eventDayTextView);
        etHour = findViewById(R.id.eventHourTextView);
        etMinute = findViewById(R.id.eventMinuteTextView);
        btnSave = findViewById(R.id.btnSaveEvent);

        // Initialize Firebase database reference to the "list_of_events" node
        eventRef = FirebaseDatabase.getInstance().getReference("list_of_events");
        
        // the following code is for making the spinner
        
        // initialize the spinner 
        categorySpinner = findViewById(R.id.categorySpinner);
        //make the list of all categories
        // plan: return a set of key-value pairs or a hashmap of type <eventname, id>
        eventRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot snapshot){
                // process children here 
                List<String> categories = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()){
                    String categoryName = child.child("name").getValue(String.class);
                    categories.add(categoryName);
                }
            
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEventActivity.this, android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error){
                // process error 
            }
        });

        
        

        // Check if intent contains data indicating this is an edit operation
        Intent intent = getIntent();
        if (intent.hasExtra("eventId")) {
            isEditMode = true; // Set flag that we are editing an existing category
            editingEventId = intent.getStringExtra("eventId"); // Get the category ID being edited

            // Pre-fill the input fields with existing category data from the intent
            etEventName.setText(intent.getStringExtra("name"));
            etEventDescription.setText(intent.getStringExtra("description"));
            etParticipationFee.setText(intent.getStringExtra("participation_fee"));
            etYear.setText(intent.getStringExtra("year"));
            etMonth.setText(intent.getStringExtra("month"));
            etDay.setText(intent.getStringExtra("day"));
            etHour.setText(intent.getStringExtra("hour"));
            etMinute.setText(intent.getStringExtra("minute"));
        }

        // Set click listener on submit button to save or update category when clicked
        btnSave.setOnClickListener(v -> saveEvent());
    }

    // Method to navigate back to the CategoryListActivity and clear activity stack
    private void goToList() {
        Intent intent = new Intent(AddEventActivity.this, EventListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity so user cannot go back to it
    }

    // Method to save a new category or update an existing one in Firebase
    private void saveEvent() {
        // Get input text values and trim whitespace
        String name = etEventName.getText().toString().trim();
        String description = etEventDescription.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString().trim();
        float participationFee = Float.parseFloat(etParticipationFee.getText().toString().trim());
        Integer year = Integer.parseInt(etYear.getText().toString().trim());
        Integer month = Integer.parseInt(etMonth.getText().toString().trim());
        Integer day = Integer.parseInt(etDay.getText().toString().trim());
        Integer hour = Integer.parseInt(etHour.getText().toString().trim());
        Integer minute = Integer.parseInt(etMinute.getText().toString().trim());
        
        // IMPORTANT
        // all the following code is missing the category in its implementation
        // none of it will work until this is added 
        
        
        // Validate inputs are not empty
        if (name.isEmpty() || description.isEmpty() || category.isEmpty() || participationFee.isEmpty() || year.isEmpty() || month.isEmpty() || day.isEmpty() || hour.isEmpty() || minute.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Stop saving if validation fails
        }

        if (isEditMode && editingEventId != null) {
            
            // About the following code 
            // it updates the category if we're in editing mode, otherwise creates a new one
            
            
            // If editing, create an updated Event object with existing ID
            Event updated = new Event(editingEventId, name, description, category, participationFee, year, month, day, hour, minute);

            // Update the existing category in Firebase under the editingCategoryId node
            eventRef.child(editingEventId).setValue(updated)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
                            goToList(); // Navigate back to the list on success
                        } else {
                            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If adding a new event, generate a new unique ID using push()
            String id = eventRef.push().getKey();

            // Create a new Category object with the generated ID
            Event newEvent = new Event(editingEventId, name, description, category, participationFee, year, month, day, hour, minute);

            // Save the new event in Firebase under the new ID node
            eventRef.child(id).setValue(newEvent)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show();
                            goToList(); // Navigate back to list on success
                        } else {
                            Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // NOTE: This block restarts the EventListActivity immediately
        // It might cause navigating before async Firebase tasks complete
        // Consider removing this block or moving it inside success listeners above
        Intent intent = new Intent(AddEventActivity.this, EventListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
