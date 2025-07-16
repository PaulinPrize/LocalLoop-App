package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private ArrayList<EventRequest> requestList = new ArrayList<>();
    private DatabaseReference requestsRef;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        recyclerView = findViewById(R.id.recyclerViewRequests);
        txtEmpty = findViewById(R.id.txtEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAdapter(this, requestList);
        recyclerView.setAdapter(adapter);

        requestsRef = FirebaseDatabase.getInstance().getReference("event_requests");

        loadRequests();
    }

    private void loadRequests() {
        String organizerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

        eventsRef.orderByChild("organizerId").equalTo(organizerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> organizerEventIds = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            organizerEventIds.add(ds.getKey());
                        }
                        loadFilteredRequests(organizerEventIds);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ManageRequestsActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadFilteredRequests(ArrayList<String> organizerEventIds) {
        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    EventRequest req = ds.getValue(EventRequest.class);
                    if (req != null && organizerEventIds.contains(req.getEventId())) {
                        req.setId(ds.getKey());
                        requestList.add(req);
                    }
                }
                adapter.notifyDataSetChanged();

                //  Gérer l'état vide
                if (requestList.isEmpty()) {
                    txtEmpty.setVisibility(TextView.VISIBLE);
                    recyclerView.setVisibility(RecyclerView.GONE);
                } else {
                    txtEmpty.setVisibility(TextView.GONE);
                    recyclerView.setVisibility(RecyclerView.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageRequestsActivity.this, "Failed to load requests", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
