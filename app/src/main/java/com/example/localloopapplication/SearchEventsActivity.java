package com.example.localloopapplication;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchEventsActivity extends AppCompatActivity {
    private SearchView searchView;
    private Spinner categorySpinner;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private ArrayList<Event> fullEventList = new ArrayList<>();
    private ArrayList<Event> filteredEventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_events);

        searchView = findViewById(R.id.searchView);
        categorySpinner = findViewById(R.id.categorySpinner);
        recyclerView = findViewById(R.id.recyclerViewEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(filteredEventList);
        recyclerView.setAdapter(eventAdapter);

        setupCategorySpinner();   // Load categories
        loadEventsFromFirebase(); // Load all public events
        setupSearchFilter();      // Add text search

    }

    private void loadEventsFromFirebase() {
        FirebaseDatabase.getInstance().getReference("events")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fullEventList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Event event = snap.getValue(Event.class);
                            fullEventList.add(event);

                        }
                        applyFilters(); // Apply current search/category
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void setupSearchFilter() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters();
                return false;
            }
        });
    }
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    private void setupCategorySpinner() {
        categories.add("All");  // Default option

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Firebase fetch
        FirebaseDatabase.getInstance().getReference("event_categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot categorySnap : snapshot.getChildren()) {
                            String name = categorySnap.child("name").getValue(String.class);
                            if (name != null && !categories.contains(name)) {
                                categories.add(name);
                            }
                        }
                        categoryAdapter.notifyDataSetChanged(); // Update Spinner
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

        // Handle spinner selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilters() {
        String query = searchView.getQuery().toString().toLowerCase();
        String selectedCategory = categorySpinner.getSelectedItem().toString();

        filteredEventList.clear();
        for (Event event : fullEventList) {
            String eventName = event.getName() != null ? event.getName().toLowerCase() : "";
            String eventCategory = event.getCategory();

            boolean matchesQuery = eventName.contains(query);
            boolean matchesCategory = "All".equalsIgnoreCase(selectedCategory) ||
                    (eventCategory != null && eventCategory.equalsIgnoreCase(selectedCategory));

            if (matchesQuery && matchesCategory) {
                filteredEventList.add(event);
            }
        }

        eventAdapter.notifyDataSetChanged();
    }








}


