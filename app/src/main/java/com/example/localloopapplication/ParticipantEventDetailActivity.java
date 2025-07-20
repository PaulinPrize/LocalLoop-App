package com.example.localloopapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ParticipantEventDetailActivity extends AppCompatActivity {

    TextView txtName, txtDateTime, txtCategory, txtFee, txtDescription;
    Button btnRequestToJoin;
    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        
        btnRequestToJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendRequestToOrganizer(event);
            }
        })

    }
    
    public void sendRequestToOrganizer(Event event){
        // each organizer type object and each user type object needs
        // to have a node in the firebase called notifications with a bunch of child nodes
        // which are each individual notifications 
        // a notification type object for an organizer should have an accept and reject button 
        // and for a participant it should have just the text that shows you have been accepted/rejected to this event 
        // - create the layout of the notification screen for each type (organizer/participant)
        // - create the NotificationsActivity for each type 
        
        DatabaseReference notiRef = FirebaseDatabase.getInstance().getReference("users") // go instead into the users node 
            .child(event.getOrganizerId())
            .child("notifications");
            
        String notifId = notiRef.push().getKey();
        String theText = getFullName()+"wants to join your event : "+event.getName();
        // create the notification type object 
        Notification notification = new Notification(notifId, event.getOrganizerId(), currentUserId, theText, event);
        // Save the notification under /users/{organizerId}/notifications/{notifId}
        notiRef.child(notifId).setValue(notification);
        
    }
}