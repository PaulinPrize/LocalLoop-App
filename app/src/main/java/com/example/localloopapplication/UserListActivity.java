package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {
    // UI component to display the list of users
    private ListView listViewUsers;

    // Local list to hold User objects
    private List<User> userList;

    // Adapter that bridges userList data with ListView UI
    private UserAdapter adapter;

    // Reference to Firebase Realtime Database "users" node
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Set the activity layout to display user list
        setContentView(R.layout.activity_user_list);

        Button btnAddUser = findViewById(R.id.btnAddUser);

        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(UserListActivity.this, AddUserActivity.class);
            startActivity(intent);
        });

        // Find ListView in the layout by its ID
        listViewUsers = findViewById(R.id.listViewUsers);

        // Initialize user list
        userList = new ArrayList<>();

        // Create adapter with context and data list
        adapter = new UserAdapter(this, userList);

        // Set adapter for the ListView
        listViewUsers.setAdapter(adapter);

        // Get Firebase database reference to "users" node
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Attach a listener to get realtime updates from database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear old list to avoid duplicates before adding fresh data
                userList.clear();

                // Loop through each child (user) in the "users" node
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    // Deserialize each snapshot into a User object
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        //Assign Firebase ID to user
                        user.id = userSnapshot.getKey();
                        // Add user to the local list
                        userList.add(user);
                    }
                }

                // Notify adapter that data changed, so UI updates
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Show error message if data loading fails
                Toast.makeText(UserListActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
