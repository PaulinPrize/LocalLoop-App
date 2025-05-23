package com.example.localloop;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminWelcomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private ListView userListView; // ListView to display user data
    private ArrayList<String> userList; // List to hold the user data (email, name, etc.)
    private UserAdapter adapter;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        // Initialize UI components
        welcomeText = findViewById(R.id.welcomeText);
        userListView = findViewById(R.id.userListView);

        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList); // Custom adapter to populate ListView
        userListView.setAdapter(adapter);

        // Get reference to Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Load user list from Firebase
        loadUsers();
    }

    private void loadUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear(); // Clear the previous list

                // Iterate through all users in the "users" node
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    String firstName = userSnapshot.child("firstname").getValue(String.class);
                    String lastName = userSnapshot.child("lastname").getValue(String.class);
                    String role = userSnapshot.child("role").getValue(String.class);

                    // Format the user data (email, first name, last name, and role)
                    String userInfo = firstName + " " + lastName + " (" + email + ") - " + role;

                    // Add the formatted user info to the list
                    userList.add(userInfo);
                }

                // Notify the adapter to update the ListView
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Show an error message if data retrieval fails
                Toast.makeText(AdminWelcomeActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
