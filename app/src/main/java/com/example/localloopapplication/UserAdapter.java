package com.example.localloopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    // Context of the activity where adapter is used
    private Context context;
    // List of User objects to be displayed in the list
    private List<User> users;

    // Constructor to initialize context and user list, passing 0 as layout resource to super
    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    // Method to provide a view for an AdapterView (ListView, GridView, etc.)
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the User object for the current position in the list
        User user = getItem(position);

        // If convertView is null, inflate a new view from the user_item layout resource
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        }

        // Find the TextViews in the inflated layout to set user data
        TextView tvFullName = convertView.findViewById(R.id.fullName);
        TextView tvEmail = convertView.findViewById(R.id.email);
        TextView tvRole = convertView.findViewById(R.id.role);

        // Set the full name by concatenating first name and last name of the user
        tvFullName.setText(user.firstname + " " + user.lastname);
        // Set the user's email
        tvEmail.setText(user.email);
        // Set the user's role
        tvRole.setText(user.role);

        // Return the fully populated view for display in the list
        return convertView;
    }
}
