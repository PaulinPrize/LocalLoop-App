package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParticipNotifActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private ParticipNotifAdapter adapter;
    private ArrayList<Notification> notifications;
    private String currentUserId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particip_notif);
        
        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notifications = new ArrayList<>();
        
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        adapter = new ParticipNotifAdapter(notifications, currentUserId);
        recyclerView.setAdapter(adapter);
        
        loadNotifications();
    }
    
    private void loadNotifications(){
        FirebaseDatabase.getInstance().getReference("users")
            .child(currentUserId)
            .child("notifications")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notifications.clear();
                    for (DataSnapshot notifSnapshot : snapshot.getChildren()) {
                        Notification notif = notifSnapshot.getValue(Notification.class);
                        if (notif != null) {
                            notifications.add(notif);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ParticipNotifActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                }
            });
    }
}