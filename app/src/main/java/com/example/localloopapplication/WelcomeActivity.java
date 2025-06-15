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

        // Get data
        String name = getIntent().getStringExtra("firstname");
        String role = getIntent().getStringExtra("role");

        TextView textView = new TextView(this);
        textView.setText("Welcome " + name + "! You are logged in as \"" + role + "\".");
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);

            // Button lo logout user
        Button logoutButton = new Button(this);
        logoutButton.setText("Logout");
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Firebase deconnection
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Button to redirect to the list of all users (This button is only visible for Admin)
        Button showListOfUsersButton = new Button(this);
        showListOfUsersButton.setText("Show all users");

        // Open the activity that display all users
        showListOfUsersButton.setOnClickListener(v->{
            Intent intent = new Intent(WelcomeActivity.this, UserListActivity.class);
            startActivity(intent);
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(50, 50, 50, 50);
        layout.addView(textView);

        // If the user is admin, show button to go to category list
        if ("Admin".equalsIgnoreCase(role)) {
            Button categoryButton = new Button(this);
            categoryButton.setText("Manage Categories Events");
            categoryButton.setOnClickListener(v -> {
                Intent intent = new Intent(WelcomeActivity.this, CategoryListActivity.class);
                startActivity(intent);
            });
            layout.addView(categoryButton);
        }

        layout.addView(logoutButton);
        // Only add the user list button if the role is admin
        if ("admin".equalsIgnoreCase(role)) {
            // Add the button to the layout so it appears on screen
            layout.addView(showListOfUsersButton);
        }
        setContentView(layout);
    }
}
