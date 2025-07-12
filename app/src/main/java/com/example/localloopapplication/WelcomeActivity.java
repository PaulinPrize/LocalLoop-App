package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.content.Intent;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the user's first name from the Intent extras
        String name = getIntent().getStringExtra("firstname");
        // Retrieve the user's role from the Intent extras
        String role = getIntent().getStringExtra("role");

        // Create a TextView to display the welcome message with user's name and role
        TextView textView = new TextView(this);
        textView.setText("Welcome " + name + "! You are logged in as \"" + role + "\".");
        textView.setTextSize(24); // Set text size to 24sp
        textView.setGravity(Gravity.CENTER); // Center the text horizontally



        // Create a button to show the list of all users (visible only to Admins)
        Button showListOfUsersButton = new Button(this);
        showListOfUsersButton.setText("Show all users");
        showListOfUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, UserListActivity.class); // Open UserListActivity
            startActivity(intent);
        });

        // Create a vertical LinearLayout to hold the UI elements
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL); // Arrange children vertically
        layout.setGravity(Gravity.CENTER); // Center all child views horizontally and vertically
        layout.setPadding(50, 50, 50, 50); // Add padding around the layout
        layout.addView(textView); // Add welcome message TextView to layout

        // If the logged-in user is an Admin (case-insensitive check)
        if ("Admin".equalsIgnoreCase(role)) {
            // Create a button for managing events/categories
            Button categoryButton = new Button(this);
            categoryButton.setText("Manage Events");
            categoryButton.setOnClickListener(v -> {
                Intent intent = new Intent(WelcomeActivity.this, CategoryListActivity.class); // Open CategoryListActivity
                startActivity(intent);
            });
            layout.addView(categoryButton); // Add the manage events button to the layout
        }


        // Only add the "Show all users" button if the user is Admin (case-insensitive check)
        if ("admin".equalsIgnoreCase(role)) {
            layout.addView(showListOfUsersButton); // Add the button to the layout so it appears on screen
        }

        if ("Organizer".equalsIgnoreCase(role)) {
            Button createEventButton = new Button(this);
            createEventButton.setText("Create Event");
            createEventButton.setOnClickListener(v -> {
                Intent intent = new Intent(WelcomeActivity.this, AddEventActivity.class); // Open AddEventActivity
                startActivity(intent);
            });
            layout.addView(createEventButton); // Add the button to the layout

            Button viewMyEventsButton = new Button(this);
            viewMyEventsButton.setText("My Events");
            viewMyEventsButton.setOnClickListener(v -> {
                Intent intent = new Intent(WelcomeActivity.this, MyEventsActivity.class);
                startActivity(intent);
            });
            layout.addView(viewMyEventsButton);
        }

        // Create a logout button to sign out the user
        Button logoutButton = new Button(this);
        logoutButton.setText("Logout");
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out the user from Firebase Authentication
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class); // Intent to open LoginActivity
            startActivity(intent); // Start LoginActivity
            finish(); // Close the current activity so user can't go back with back button
        });

        // Add the logout button to the layout (visible for all users)
        layout.addView(logoutButton);

        // Set the LinearLayout as the content view for this activity
        setContentView(layout);
    }
}
