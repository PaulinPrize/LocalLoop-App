package com.example.localloopapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ParticipNotifAdapter extends RecyclerView.Adapter<ParticipNotifAdapter.NotificationViewHolder> {

    private ArrayList<Notification> notifications;
    private String currentUserId;
    public ParticipNotifAdapter(ArrayList<Notification> notifications, String currentUserId) {
        this.notifications = notifications;
        this.currentUserId=currentUserId;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_panel_participant, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position); // where does the list notifications come from?
        // it needs to be the organizer's individual notifications

        holder.notifText.setText(notification.getText());
        // Delete button click listener
        holder.btnDismiss.setOnClickListener(v -> {
            // delete the notification from the participant
            FirebaseDatabase.getInstance().getReference("users")
                .child(notification.getParticipantId()) // the organizer user 
                .child("notifications")
                .child(notification.getNotifId())
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Deletion successful
                    Toast.makeText(holder.itemView.getContext(), "Notification deleted", Toast.LENGTH_SHORT).show();
                    int currentPosition = holder.getAdapterPosition();
                    notifications.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                })
                .addOnFailureListener(e -> {
                    // Deletion failed
                    Toast.makeText(holder.itemView.getContext(), "Failed to delete notification", Toast.LENGTH_SHORT).show();
                    int currentPosition = holder.getAdapterPosition();
                    notifications.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                });
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notifText;
        Button btnDismiss;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notifText = itemView.findViewById(R.id.txtNotifText);
            btnDismiss = itemView.findViewById(R.id.btnDismiss);
        }
    }
}