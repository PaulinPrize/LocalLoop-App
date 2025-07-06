package com.example.localloopapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private DatabaseReference eventsRef;
    private String organizerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        recyclerView = findViewById(R.id.recyclerEvents);
        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.emptyView);
        
        LinearLayout confirmationlayout = findViewById(R.id.confirmationLayout);
        TextView txtEventName = findViewById(R.id.txtEventName);
        Button btnYes = findViewById(R.id.btnYes);
        Button btnNo = findViewById(R.id.btnNo);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);

        organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        
        fetchEvents();
    
    }

    private void fetchEvents() {
        progressBar.setVisibility(View.VISIBLE);
        eventsRef.orderByChild("organizerId").equalTo(organizerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventList.clear();
                        for (DataSnapshot eventSnap : snapshot.getChildren()) {
                            Event event = eventSnap.getValue(Event.class);
                            eventList.add(event);
                        }
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                        if (eventList.isEmpty()) {
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        emptyView.setText("Failed to load events.");
                        emptyView.setVisibility(View.VISIBLE);
                    }
                });
    }
}