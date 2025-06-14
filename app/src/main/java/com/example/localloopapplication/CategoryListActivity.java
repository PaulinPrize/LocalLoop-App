package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

    private ListView listViewCategories;
    private List<Category> categoryList;
    private CategoryAdapter adapter;
    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_list);

        listViewCategories = findViewById(R.id.listViewCategories);
        Button createButton = findViewById(R.id.btnCreateCategory);

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);
        listViewCategories.setAdapter(adapter);

        categoryRef = FirebaseDatabase.getInstance().getReference("event_categories");

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();  // VERY IMPORTANT: Clears the list before refilling
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        category.setId(categorySnapshot.getKey());
                        categoryList.add(category);
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryListActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });

        // Button to launch AddCategoryActivity
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryListActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });
    }
}
