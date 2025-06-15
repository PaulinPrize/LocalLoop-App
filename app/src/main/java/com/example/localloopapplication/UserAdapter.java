package com.example.localloopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    static class ViewHolder {
        TextView tvFullName, tvEmail, tvRole, tvStatus;
        Button btnEdit, btnDelete, btnReactivate, btnYes, btnNo;
        LinearLayout confirmationLayout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();

            holder.tvFullName = convertView.findViewById(R.id.fullName);
            holder.tvEmail = convertView.findViewById(R.id.email);
            holder.tvRole = convertView.findViewById(R.id.role);
            holder.tvStatus = convertView.findViewById(R.id.status);

            holder.btnEdit = convertView.findViewById(R.id.btnEdit);             // Inactivate
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);         // Delete (confirm)
            holder.btnReactivate = convertView.findViewById(R.id.btnReactivate); // Reactivate
            holder.btnYes = convertView.findViewById(R.id.btnYes);
            holder.btnNo = convertView.findViewById(R.id.btnNo);

            holder.confirmationLayout = (LinearLayout) holder.btnYes.getParent().getParent();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set user data
        holder.tvFullName.setText(user.firstname + " " + user.lastname);
        holder.tvEmail.setText(user.email);
        holder.tvRole.setText(user.role);
        holder.tvStatus.setText("Status: " + user.status);

        // Hide confirmation layout initially
        holder.confirmationLayout.setVisibility(View.GONE);

        // Firebase reference for this user
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.id);

        // Delete with confirmation
        holder.btnDelete.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.VISIBLE));
        holder.btnNo.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.GONE));
        holder.btnYes.setOnClickListener(v -> {
            userRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        users.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show()
                    );
        });

        // Inactivate user (btnEdit)
        holder.btnEdit.setOnClickListener(v -> {
            if ("Active".equalsIgnoreCase(user.status)) {
                user.status = "Inactive";
                userRef.child("status").setValue("Inactive")
                        .addOnSuccessListener(aVoid -> {
                            notifyDataSetChanged();
                            Toast.makeText(context, "User inactivated", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(context, "User already inactive", Toast.LENGTH_SHORT).show();
            }
        });

        // Reactivate user (btnReactivate)
        holder.btnReactivate.setOnClickListener(v -> {
            if ("Inactive".equalsIgnoreCase(user.status)) {
                user.status = "Active";
                userRef.child("status").setValue("Active")
                        .addOnSuccessListener(aVoid -> {
                            notifyDataSetChanged();
                            Toast.makeText(context, "User reactivated", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(context, "User already active", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
