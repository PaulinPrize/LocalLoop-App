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

/*
THIS CLASS IS USE FOR EACH USERS AND GOES ALONG WITH user_list_item.
SO WHEN THE ADMIN ACCESS HIS WELCOME PAGE AND SELECT ANY USER,
THIS IS THE CLASS THAT IS CALLED TO DISPLAY USER INFORMATION.
 */

public class UserDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_item); //user layout

        TextView fullNameText = findViewById(R.id.fullName);
        TextView emailText = findViewById(R.id.email);
        TextView roleText = findViewById(R.id.role);

        // Get data from intents
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String role = getIntent().getStringExtra("role");

        //Concatenating the firstName and to lastName
        fullNameText.setText(firstName + " " + lastName);

        //Set email address to email
        emailText.setText(email);

        //Set role address to role
        roleText.setText(role);
    }
}
