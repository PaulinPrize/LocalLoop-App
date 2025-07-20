package com.example.localloopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;



import com.google.firebase.auth.FirebaseAuth;

public class OrganizerActivity extends AppCompatActivity {

    private Button addEventBtn, viewEventsBtn, btnLogout;
    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);


        //for the backButton , using if just in case the page does not crash if something went wrong
        ImageButton backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }



        // Link UI elements
        addEventBtn = findViewById(R.id.btnAddEvent);         // Add Event
        viewEventsBtn = findViewById(R.id.btnMyEvents);       // View My Events
        btnLogout = findViewById(R.id.btnLogout);             // Logout button
        txtWelcome = findViewById(R.id.txtWelcome);           // Welcome Text

        // Set welcome message
        txtWelcome.setText("Welcome! You are logged in as an Organizer.");

        // Navigate to AddEventActivity
        addEventBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerActivity.this, AddEventActivity.class);
            startActivity(intent);
        });

        // Navigate to MyEventsActivity
        viewEventsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizerActivity.this, MyEventsActivity.class);
            startActivity(intent);
        });

        // Logout logic
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(OrganizerActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
