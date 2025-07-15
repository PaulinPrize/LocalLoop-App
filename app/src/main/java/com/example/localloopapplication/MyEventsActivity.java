package com.example.localloopapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays the events created by the current user (organizer).
 */
public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;        // RecyclerView to display events
    private EventAdapter adapter;             // Adapter for RecyclerView
    private ArrayList<Event> eventList = new ArrayList<>(); // List holding events for this user
    private DatabaseReference eventsRef;      // Firebase reference to "events" node

    /**
     * Called when the activity is starting. Initializes UI components and loads user's events.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // Set up RecyclerView with linear layout and adapter
        recyclerView = findViewById(R.id.recyclerViewMyEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        // Add vertical dividers between event items
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Get reference to the "events" node in Firebase
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Load the current user's events from Firebase
        fetchEvents();
    }

    /**
     * Fetches events from Firebase for the current user and updates the RecyclerView.
     */
    private void fetchEvents() {
        // Get the currently authenticated user's UID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Listen for changes in the "events" node
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear(); // Clear old data before updating
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    // Only include events organized by the current user
                    if (event != null && event.getOrganizerId().equals(currentUserId)) {
                        event.setId(eventSnapshot.getKey()); // Set event's Firebase key as ID
                        eventList.add(event);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show error message if data retrieval fails
                Toast.makeText(MyEventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
