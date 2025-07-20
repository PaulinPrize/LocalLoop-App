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

public class OrgNotifAdapter extends RecyclerView.Adapter<OrgNotifAdapter.NotificationViewHolder> {

    private ArrayList<Notification> notifications;
    private String currentUserId;
    public OrgNotifAdapter(ArrayList<Notification> notifications, String currentUserId) {
        this.notifications = notifications;
        this.currentUserId=currentUserId;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_panel_organizer, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position); // where does the list notifications come from?
        // it needs to be the organizer's individual notifications

        holder.notifText.setText(notification.getText());
        // Delete button click listener
        holder.btnAccept.setOnClickListener(v -> {
            // gotta send the participant a notification telling them they are accepted
            
            String text = "You have been approved at the following event: "+notification.getEvent().getName();
            String orgId = currentUserId;
            String participId = notification.getParticipantId();
            
            DatabaseReference notiRef = FirebaseDatabase.getInstance().getReference("users") // go instead into the users node 
                .child(participId)
                .child("notifications");
            String notifId = notiRef.push().getKey();
            Notification newNotification = new Notification(notifId, orgId, participId, text);
            notiRef.child(notifId).setValue(newNotification);
            // and delete the notification from the organizer 
            FirebaseDatabase.getInstance().getReference("users")
                .child(notification.getOrganizerId()) // the organizer user 
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
        
        holder.btnDeny.setOnClickListener(v -> {
            
            // gotta send the participant a notification telling them they are denied
            
            String text = "You have been denied at the following event: "+notification.getEvent().getName();
            String orgId = currentUserId;
            String participId = notification.getParticipantId();
            
            DatabaseReference notiRef = FirebaseDatabase.getInstance().getReference("users") // go instead into the users node 
                .child(participId)
                .child("notifications");
            String notifId = notiRef.push().getKey();
            Notification newNotification = new Notification(notifId, orgId, participId, text);
            notiRef.child(notifId).setValue(newNotification);
            // and delete the notification from the organizer 
            FirebaseDatabase.getInstance().getReference("users")
                .child(notification.getOrganizerId()) // the organizer user 
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
        Button btnAccept, btnDeny;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notifText = itemView.findViewById(R.id.txtNotifText);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDeny = itemView.findViewById(R.id.btnDeny); 
        }
    }
}