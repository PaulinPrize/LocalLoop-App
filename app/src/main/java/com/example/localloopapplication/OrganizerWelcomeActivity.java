package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloopapplication.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganizerWelcomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private ListView eventListView; // ListView to display event data
    //private ArrayList<String> eventList; // List to hold the user data (email, name, etc.)
    private com.example.localloopapplication.EventAdapter adapter;
    // TODO: make eventadapter class
    private DatabaseReference mDatabase;
    private ArrayList<Event> eventList; // Store User objects now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_welcome);

        // Initialize UI components
        welcomeText = findViewById(R.id.welcomeText);
        eventListView = findViewById(R.id.eventListView);

        eventList = new ArrayList<>();
        adapter = new com.example.localloopapplication.EventAdapter(this, eventList);
        eventListView.setAdapter(adapter);

        // Set item click listener here
        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Event clickedEvent = eventList.get(position);

            Intent intent = new Intent(OrganizerWelcomeActivity.this, EventDetailsActivity.class);
            intent.putExtra("name", clickedEvent.name);
            intent.putExtra("description", clickedEvent.description);
            intent.putExtra("category", clickedEvent.category);
            intent.putExtra("fee", clickedEvent.fee);
            intent.putExtra("datTime", clickedEvent.datTime);

            startActivity(intent);
        });

        // Get reference to Firebase Realtime Database
        // this matches the name of the database node from maha's code 
        mDatabase = FirebaseDatabase.getInstance().getReference("events");

        // Load user list from Firebase
        loadEvents();
    }

//This method retrieve data from the database
    private void loadEvents() {
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventList.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show an error message if data retrieval fails
                Toast.makeText(AdminWelcomeActivity.this, "Failed to load event data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
