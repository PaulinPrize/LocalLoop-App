package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TextView textView = new TextView(this);
        String name = getIntent().getStringExtra("firstName");
        String role = getIntent().getStringExtra("role");
        textView.setText("Welcome " + name + "! You are logged in as \"" + role + "\".");
        textView.setTextSize(24);

        setContentView(textView);
    }
}
