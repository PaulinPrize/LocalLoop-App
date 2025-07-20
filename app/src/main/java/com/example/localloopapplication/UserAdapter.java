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

/**
 * Adapter for displaying User objects in a ListView.
 * Handles user inactivation, deletion (with confirmation), and reactivation.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Context context;      // The activity context
    private List<User> users;     // List of users to display

    /**
     * Constructor for the adapter.
     *
     * @param context The activity context
     * @param users   The list of users to display
     */
    public UserAdapter(Context context, List<User> users) {
        super(context, 0, users);
        this.context = context;
        this.users = users;
    }

    /**
     * Holds references to the views for each user row, for performance (ViewHolder pattern).
     */
    static class ViewHolder {
        TextView tvFullName, tvEmail, tvRole, tvStatus;
        Button btnEdit, btnDelete, btnReactivate, btnYes, btnNo;
        LinearLayout confirmationLayout;
    }

    /**
     * Returns the view for a user row in the ListView.
     * Handles all button actions: inactivate, delete (with confirmation), and reactivate.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);  // Get current user
        ViewHolder holder;

        // Inflate the view and set up the holder if not recycled
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();

            // Link UI components
            holder.tvFullName = convertView.findViewById(R.id.fullName);
            holder.tvEmail = convertView.findViewById(R.id.email);
            holder.tvRole = convertView.findViewById(R.id.role);
            holder.tvStatus = convertView.findViewById(R.id.status);

            holder.btnEdit = convertView.findViewById(R.id.btnEdit);             // Inactivate
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);         // Delete (confirm)
            holder.btnReactivate = convertView.findViewById(R.id.btnReactivate); // Reactivate
            holder.btnYes = convertView.findViewById(R.id.btnYes);
            holder.btnNo = convertView.findViewById(R.id.btnNo);

            // Get confirmation layout (for delete)
            holder.confirmationLayout = (LinearLayout) holder.btnYes.getParent().getParent();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set user data to views
        holder.tvFullName.setText(user.firstname + " " + user.lastname);
        holder.tvEmail.setText(user.email);
        holder.tvRole.setText(user.role);
        holder.tvStatus.setText("Status: " + user.status);

        // Hide the delete confirmation layout initially
        holder.confirmationLayout.setVisibility(View.GONE);

        // Firebase reference to this user
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.id);

        // --- Delete functionality with confirmation ---
        holder.btnDelete.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.VISIBLE));
        holder.btnNo.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.GONE));
        holder.btnYes.setOnClickListener(v -> {
            userRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        users.remove(position);    // Remove user from local list
                        notifyDataSetChanged();   // Update ListView
                        Toast.makeText(context, "User deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to delete user", Toast.LENGTH_SHORT).show()
                    );
        });

        // --- Inactivate user (set status to Inactive) ---
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

        // --- Reactivate user (set status to Active) ---
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
