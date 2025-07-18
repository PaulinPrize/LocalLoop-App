package com.example.localloopapplication;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class ParticipantEventDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_item_event); // Create this XML layout next!

        // Get data passed from the intent
        String name = getIntent().getStringExtra("name");
        String dateTime = getIntent().getStringExtra("dateTime");
        String description = getIntent().getStringExtra("description");
        double fee = getIntent().getDoubleExtra("fee", 0);


    }

}
