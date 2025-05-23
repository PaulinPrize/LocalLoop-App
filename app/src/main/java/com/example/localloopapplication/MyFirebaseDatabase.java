package com.example.localloopapplication;

import android.util.Log;

import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseDatabase {
    // these arguments are entered into the database upon user account creation
    // note that the java team needs to make a function that 
    // automatically generates the integer id to be passed into this function 
    
    /**
    * Creates an entry in the database
    *
    * Typically used for creation of user accounts in LocalLoop
    * 
    *
    * @param email the user's email
    * @param firstname the user's first name
    * @param id the user's unique id in the database 
    * @param lastname the user's last name
    * @param password the user's chosen password
    * @param role the user's role, either an admin, an organizer or a participant
    */
    
    public void createRow(String email, String firstname, int id, String lastname, String password, String role){
        // opens a connection with firebase
        // the variable database represents our specific database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // this creates a node "users" inside the json tree
        // can be thought of as an individual sheet in google sheets or excel
        // in it we have different child keys for individuals using the app
        DatabaseReference usersRef = database.getReference("users");

        // makes a unique child key under "users" for one user
        String userId = usersRef.push().getKey();

        if (userId!=null){ // nullcheck
            Map<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("firstname", firstname);
            //userData.put("id", id);
            userData.put("lastname", lastname);
            userData.put("password", password);
            userData.put("role", role);
            // these three lines create a hashmap with the user's data
            Task<Void> voidTask = usersRef.child(userId).setValue(userData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "User created successfully");
                    }) 
                    .addOnFailureListener(e -> {
                        Log.d("Firebase", "Failed to create user", e);
                    }); // messages on the android logcat console to help in debugging
            ;
        }

    }
}
