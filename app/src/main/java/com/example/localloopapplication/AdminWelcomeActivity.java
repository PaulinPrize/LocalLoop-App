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

public class AdminWelcomeActivity extends AppCompatActivity {

    private TextView welcomeText;
    private ListView userListView; // ListView to display user data
    //private ArrayList<String> userList; // List to hold the user data (email, name, etc.)
    private com.example.localloopapplication.UserAdapter adapter;

    private DatabaseReference mDatabase;
    private ArrayList<User> userList; // Store User objects now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        // Initialize UI components
        welcomeText = findViewById(R.id.welcomeText);
        userListView = findViewById(R.id.userListView);

        userList = new ArrayList<>();
        adapter = new com.example.localloopapplication.UserAdapter(this, userList);
        userListView.setAdapter(adapter);

        // Set item click listener here
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User clickedUser = userList.get(position);

            Intent intent = new Intent(AdminWelcomeActivity.this, UserDetailsActivity.class);
            intent.putExtra("email", clickedUser.email);
            intent.putExtra("firstName", clickedUser.firstName);
            intent.putExtra("lastName", clickedUser.lastName);
            intent.putExtra("role", clickedUser.role);

            startActivity(intent);
        });

        // Get reference to Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Load user list from Firebase
        loadUsers();
    }

//This method retrieve data from the database
    private void loadUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
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
