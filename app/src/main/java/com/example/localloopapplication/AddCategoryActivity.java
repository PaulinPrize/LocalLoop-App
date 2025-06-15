package com.example.localloopapplication;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

// Activity to add or edit an event category
// Contains UI initialization in onCreate, and a method to save or update the category in Firebase
public class AddCategoryActivity extends AppCompatActivity {

    // UI elements for input fields and submit button
    private EditText etCategoryName, etCategoryDescription;
    private Button btnSubmit;

    // Reference to Firebase Realtime Database for categories
    private DatabaseReference categoryRef;

    // Flags and variables to handle edit mode
    private boolean isEditMode = false;
    private String editingCategoryId;

    // Called when the activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_add); // Set layout for this activity

        // Initialize UI elements by finding them by their IDs
        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnSubmit = findViewById(R.id.btnSubmitCategory);

        // Initialize Firebase database reference to the "event_categories" node
        categoryRef = FirebaseDatabase.getInstance().getReference("event_categories");

        // Check if intent contains data indicating this is an edit operation
        Intent intent = getIntent();
        if (intent.hasExtra("categoryId")) {
            isEditMode = true; // Set flag that we are editing an existing category
            editingCategoryId = intent.getStringExtra("categoryId"); // Get the category ID being edited

            // Pre-fill the input fields with existing category data from the intent
            etCategoryName.setText(intent.getStringExtra("name"));
            etCategoryDescription.setText(intent.getStringExtra("description"));
        }

        // Set click listener on submit button to save or update category when clicked
        btnSubmit.setOnClickListener(v -> saveCategory());
    }

    // Method to navigate back to the CategoryListActivity and clear activity stack
    private void goToList() {
        Intent intent = new Intent(AddCategoryActivity.this, CategoryListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity so user cannot go back to it
    }

    // Method to save a new category or update an existing one in Firebase
    private void saveCategory() {
        // Get input text values and trim whitespace
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();

        // Validate inputs are not empty
        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Stop saving if validation fails
        }

        if (isEditMode && editingCategoryId != null) {
            // If editing, create an updated Category object with existing ID
            Category updated = new Category(editingCategoryId, name, description, true);

            // Update the existing category in Firebase under the editingCategoryId node
            categoryRef.child(editingCategoryId).setValue(updated)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show();
                            goToList(); // Navigate back to the list on success
                        } else {
                            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If adding a new category, generate a new unique ID using push()
            String id = categoryRef.push().getKey();

            // Create a new Category object with the generated ID
            Category newCategory = new Category(id, name, description, true);

            // Save the new category in Firebase under the new ID node
            categoryRef.child(id).setValue(newCategory)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
                            goToList(); // Navigate back to list on success
                        } else {
                            Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // NOTE: This block restarts the CategoryListActivity immediately
        // It might cause navigating before async Firebase tasks complete
        // Consider removing this block or moving it inside success listeners above
        Intent intent = new Intent(AddCategoryActivity.this, CategoryListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
