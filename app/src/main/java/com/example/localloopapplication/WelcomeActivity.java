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

        Button logoutButton = new Button(this);
        logoutButton.setText("Logout");
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Firebase deconnection
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(50, 50, 50, 50);
        layout.addView(textView);
        layout.addView(logoutButton);

        setContentView(layout);
    }
}
