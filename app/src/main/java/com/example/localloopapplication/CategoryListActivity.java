package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    private ListView listViewCategories;         // ListView to display the categories
    private List<Category> categoryList;         // Local list to hold Category objects
    private CategoryAdapter adapter;             // Adapter to populate ListView with category data
    private DatabaseReference categoryRef;       // Firebase reference to "event_categories" node

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_list); // Set layout for the activity


        //for the backButton , using if just in case the page does not crash if something went wrong
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }




        // Initialize views
        listViewCategories = findViewById(R.id.listViewCategories);
        Button createButton = findViewById(R.id.btnCreateCategory); // Button to add new category

        // Initialize list and adapter
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);
        listViewCategories.setAdapter(adapter);

        // Reference to the "event_categories" node in Firebase
        categoryRef = FirebaseDatabase.getInstance().getReference("event_categories");

        // Set up a listener to load categories from Firebase
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();  // Clear the list before loading to avoid duplicates
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class); // Convert snapshot to Category object
                    if (category != null) {
                        category.setId(categorySnapshot.getKey()); // Store Firebase key as ID
                        categoryList.add(category); // Add to local list
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible error when reading from Firebase
                Toast.makeText(CategoryListActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });

        // Set onClick listener for the Create button to launch AddCategoryActivity
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryListActivity.this, AddCategoryActivity.class);
            startActivity(intent); // Open the AddCategoryActivity screen
        });
    }
}
