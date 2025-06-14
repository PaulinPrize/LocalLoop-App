package com.example.localloopapplication;

import android.os.Bundle;
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
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);  // CategoryAdapter with button logic
        listViewCategories.setAdapter(adapter);

        categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        categoryList.add(category);
                    }
                }
                adapter.notifyDataSetChanged();  // refresh ListView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CategoryListActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
