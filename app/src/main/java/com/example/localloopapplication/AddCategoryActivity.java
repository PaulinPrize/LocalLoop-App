package com.example.localloopapplication;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;

// code to add event
// this contains a onCreate for buttons, and a saveCategory method to update and add the category
// to the list

public class AddCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName, etCategoryDescription;
    private Button btnSubmit;

    private DatabaseReference categoryRef;
    private boolean isEditMode = false;
    private String editingCategoryId;

    protected void onCreate(Bundle savedInstanceState) { // connecting to interface buttons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_add);

        etCategoryName = findViewById(R.id.etCategoryName);
        etCategoryDescription = findViewById(R.id.etCategoryDescription);
        btnSubmit = findViewById(R.id.btnSubmitCategory);

        categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        // Check if we're editing!?
        Intent intent = getIntent();
        if (intent.hasExtra("categoryId")) {
            isEditMode = true;
            editingCategoryId = intent.getStringExtra("categoryId");
            etCategoryName.setText(intent.getStringExtra("name"));
            etCategoryDescription.setText(intent.getStringExtra("description"));
        }

        btnSubmit.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {// creating a new category
        String name = etCategoryName.getText().toString().trim();
        String description = etCategoryDescription.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            // Update existing category
            Category updated = new Category(editingCategoryId, name, description, true);
            categoryRef.child(editingCategoryId).setValue(updated).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Category updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {
            // Add new category
            String id = categoryRef.push().getKey();
            Category newCategory = new Category(id, name, description, true);
            categoryRef.child(id).setValue(newCategory).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }
}
