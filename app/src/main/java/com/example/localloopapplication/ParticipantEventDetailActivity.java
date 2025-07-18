package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParticipantEventDetailActivity extends AppCompatActivity {

    TextView txtName, txtDateTime, txtCategory, txtFee, txtDescription;
    Button btnRequestToJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participant_item_event);

        txtName = findViewById(R.id.txtEventName);
        txtDateTime = findViewById(R.id.txtEventDateTime);
        txtCategory = findViewById(R.id.txtEventCategory);
        txtFee = findViewById(R.id.txtEventFee);
        txtDescription = findViewById(R.id.txtEventDescription);
        btnRequestToJoin = findViewById(R.id.btnRequestToJoin);


        String name = getIntent().getStringExtra("name");
        String dateTime = getIntent().getStringExtra("dateTime");
        String category = getIntent().getStringExtra("category");
        String fee = String.valueOf(getIntent().getDoubleExtra("fee", 0.0));
        String description = getIntent().getStringExtra("description");


        txtName.setText(name);
        txtDateTime.setText("Date/Time: " + dateTime);
        txtCategory.setText("Category: " + category);
        txtFee.setText("Fee: $" + fee);
        txtDescription.setText(description);

    }
}
