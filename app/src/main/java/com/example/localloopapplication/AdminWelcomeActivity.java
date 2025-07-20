package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.localloopapplication.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * AdminWelcomeActivity displays a welcome message and a list of all users retrieved from Firebase.
 * Admin can tap on a user to see their details.
 */
public class AdminWelcomeActivity extends AppCompatActivity {

    private TextView welcomeText; // Displays welcome message
    private ListView userListView; // ListView to display user data

    // Adapter for displaying users in the ListView
    private com.example.localloopapplication.UserAdapter adapter;

    // Reference to "users" table in Firebase
    private DatabaseReference mDatabase;
    // List to store User objects
    private ArrayList<User> userList;

    private ImageButton backButton;

    /**
     * Called when the activity is starting. Initializes UI, sets up adapter, and loads user data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        //for the backButton , using if just in case the page does not crash if something went wrong
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }


        // Initialize UI components
        welcomeText = findViewById(R.id.welcomeText);
        userListView = findViewById(R.id.userListView);

        // Initialize the user list and adapter
        userList = new ArrayList<>();
        adapter = new com.example.localloopapplication.UserAdapter(this, userList);
        userListView.setAdapter(adapter);

        // Set item click listener to handle user selection
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User clickedUser = userList.get(position);

            // Open UserDetailsActivity, passing selected user's details
            Intent intent = new Intent(AdminWelcomeActivity.this, UserDetailsActivity.class);
            intent.putExtra("email", clickedUser.email);
            intent.putExtra("firstName", clickedUser.firstname);
            intent.putExtra("lastName", clickedUser.lastname);
            intent.putExtra("role", clickedUser.role);

            startActivity(intent);
        });

        // Get reference to Firebase Realtime Database "users" node
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Load user list from Firebase
        loadUsers();
    }

    /**
     * Retrieves user data from Firebase and updates the userList.
     * Listens for any data changes and updates the UI accordingly.
     */
    private void loadUsers() {
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear old data
                userList.clear();

                // Iterate over all users in the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Parse snapshot into User object
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                // Notify adapter that data has changed so ListView refreshes
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
