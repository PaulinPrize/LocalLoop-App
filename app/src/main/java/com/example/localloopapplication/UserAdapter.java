package com.example.localloopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    // Context of the activity
    private Context context;
    // List of users to display
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get current user
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        }

        // Find views
        TextView tvFullName = convertView.findViewById(R.id.fullName);
        TextView tvEmail = convertView.findViewById(R.id.email);
        TextView tvRole = convertView.findViewById(R.id.role);

        // initializing the edit and delete buttons
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);

        // below is the onClick for the delete button
        btnDelete.setOnClickListener(new View.OnClickListener{
            @Override
            public void onClick(View v){
                // opens a connection with firebase
                // the variable database represents our specific database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // usersRef is the node with all users
                DatabaseReference usersRef = database.getReference("users");

                DatabaseReference specificUsersRef = usersRef.child(user.id);
                specificUsersRef.removeValue().addOnSuccessListener(unused -> {
                    // Successfully deleted user
                    Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    // Failed to delete user
                    Toast.makeText(context, "Failed to delete user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

                // i have gone into the user class and added an attribute user.id
                // i have gone into the registeractivity class and made the hashmap add the uid into the hashmap as id
                // i modified both user constructors to include id
                // i haven't made sure that in the list of users being referenced here, that the user constructor is called correctly
                     // with the id thing in the constructor
            }
        })


        // Set data to TextView
        tvFullName.setText(user.firstname + " " + user.lastname);
        tvEmail.setText(user.email);
        tvRole.setText(user.role);

        return convertView;
    }
}
