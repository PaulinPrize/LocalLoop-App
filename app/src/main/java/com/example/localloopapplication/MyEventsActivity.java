package com.example.localloopapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Activity that displays the events created by the current user (organizer).
 */
public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ArrayList<Event> eventList = new ArrayList<>();
    private DatabaseReference eventsRef;
    private TextView txtNoEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        //for the backButton , using if just in case the page does not crash if something went wrong
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }





        // UI components
        recyclerView = findViewById(R.id.recyclerViewMyEvents);
        txtNoEvents = findViewById(R.id.txtNoEvents);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // Firebase reference
        eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Load events
        fetchEvents();
    }

    private void fetchEvents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String currentUserId = user.getUid();

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();

                for (DataSnapshot eventSnap : snapshot.getChildren()) {
                    Event event = eventSnap.getValue(Event.class);

                    if (event != null && currentUserId.equals(event.getOrganizerId())) {
                        event.setId(eventSnap.getKey()); // important pour pouvoir le supprimer
                        eventList.add(event);
                        Log.d("MyEventsActivity", "Event loaded: " + event.getName());
                    }
                }

                adapter.notifyDataSetChanged();

                // Affichage conditionnel
                if (eventList.isEmpty()) {
                    txtNoEvents.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    txtNoEvents.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyEventsActivity", "Firebase error: " + error.getMessage());
                Toast.makeText(MyEventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
