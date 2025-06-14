package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

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
    // UI component to display the list
    private ListView listViewUsers;
    // Local list to store user data
    private List<User> userList;
    // Custom adapter to bind data to ListView
    private UserAdapter adapter;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Set the layout file for the activity
        setContentView(R.layout.activity_user_list);

        // Find the ListView in the layout
        listViewUsers = findViewById(R.id.listViewUsers);
        // Initialize the user list
        userList = new ArrayList<>();
        // Create the adapter
        adapter = new UserAdapter(this, userList);
        // Attach the adapter to ListView
        listViewUsers.setAdapter(adapter);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Attach a listener to read the data from database
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing list before loading new data
                userList.clear();

                // Loop through all children of "users" node
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    // Convert to User object
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        // Add user to the local list
                        userList.add(user);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserListActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
