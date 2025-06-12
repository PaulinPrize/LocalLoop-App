package com.example.localloopapplication;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

// this class is using recyclerView(scrolling vew) to list all existing categories

public class AdminCategoryListActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private DatabaseReference categoryRef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(adapter);

        categoryRef = FirebaseDatabase.getInstance().getReference("categories");

        loadCategoriesFromFirebase();
    }

    // load database to update list

    private void loadCategoriesFromFirebase() {
        categoryRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot categorySnap : snapshot.getChildren()) {
                    Category category = categorySnap.getValue(Category.class);
                }
                adapter.notifyDataSetChanged(); // update UI
            }

            public void onCancelled(DatabaseError error) {

            }

        });
    }
}

