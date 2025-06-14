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

        // Set data to TextView
        tvFullName.setText(user.firstname + " " + user.lastname);
        tvEmail.setText(user.email);
        tvRole.setText(user.role);

        return convertView;
    }
}
