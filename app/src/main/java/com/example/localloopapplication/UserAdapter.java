package com.example.localloop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> users;

    public UserAdapter(Context context, ArrayList<String> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false);
        }

        String userInfo = getItem(position);
        TextView userTextView = convertView.findViewById(R.id.userTextView);
        userTextView.setText(userInfo);

        return convertView;
    }
}
