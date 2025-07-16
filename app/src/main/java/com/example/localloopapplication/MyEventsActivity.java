package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private ArrayList<Event> eventList = new ArrayList<>();

    private DatabaseReference eventsRef;
    private DatabaseReference requestsRef;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        requestsRef = FirebaseDatabase.getInstance().getReference("event_requests");

        // Passer le rôle "Admin" et la callback pour le clic modifier
        adapter = new EventAdapter(eventList, "Admin", this::onEditEventClicked);
        recyclerView.setAdapter(adapter);

        loadEvents();
    }

    private void loadEvents() {
        eventsRef.orderByChild("organizerId").equalTo(organizerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Event event = ds.getValue(Event.class);
                            if (event != null) {
                                event.setId(ds.getKey());
                                eventList.add(event);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyEventsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Callback appelé quand on clique sur modifier un event
    private void onEditEventClicked(Event event) {
        checkIfCanEditEvent(event.getId());
    }

    private void checkIfCanEditEvent(String eventId) {
        requestsRef.orderByChild("eventId").equalTo(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean canEdit = true;

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String status = ds.child("status").getValue(String.class);
                            if ("pending".equalsIgnoreCase(status) || "accepted".equalsIgnoreCase(status)) {
                                canEdit = false;
                                break;
                            }
                        }

                        if (canEdit) {
                            Intent intent = new Intent(MyEventsActivity.this, EditEventActivity.class);
                            intent.putExtra("eventId", eventId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MyEventsActivity.this,
                                    "Impossible de modifier l'événement : des participants ont une demande en cours ou ont été acceptés.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyEventsActivity.this, "Erreur lors de la vérification", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
