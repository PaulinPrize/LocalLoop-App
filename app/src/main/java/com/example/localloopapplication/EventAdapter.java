package com.example.localloopapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnEditClickListener {
        void onEditClicked(Event event);
    }

    private ArrayList<Event> events;
    private String userRole;
    private OnEditClickListener editClickListener;

    public EventAdapter(ArrayList<Event> events, String userRole, OnEditClickListener listener) {
        this.events = events;
        this.userRole = userRole;
        this.editClickListener = listener;
    }

    // Constructeur simplifié (pas d'édition possible)
    public EventAdapter(ArrayList<Event> events, String userRole) {
        this(events, userRole, null);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventName.setText(event.getName());
        holder.eventDate.setText(event.getDateTime());
        holder.eventDescription.setText(event.getDescription());
        holder.eventFee.setText(String.format("$%.2f", event.getFee()));

        if ("Participant".equalsIgnoreCase(userRole)) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnRequestAccess.setVisibility(View.VISIBLE);

            // Important: reset du listener pour éviter doublons
            holder.btnRequestAccess.setOnClickListener(null);

            // Set couleur de base et texte par défaut
            holder.btnRequestAccess.setEnabled(true);
            holder.btnRequestAccess.setText("Request Access");
            holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                    .getResources().getColor(android.R.color.holo_blue_light));

            // Check si une demande existe et adapter bouton
            checkRequestStatus(event.getId(), holder);

        } else {
            // Vue Admin (ou Organizer)
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnRequestAccess.setVisibility(View.GONE);

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), EditEventActivity.class);
                intent.putExtra("eventId", event.getId());
                intent.putExtra("eventName", event.getName());
                intent.putExtra("eventDateTime", event.getDateTime());
                intent.putExtra("eventDescription", event.getDescription());
                intent.putExtra("eventFee", event.getFee());
                holder.itemView.getContext().startActivity(intent);
            });

            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            int currentPos = holder.getAdapterPosition();
                            if (currentPos == RecyclerView.NO_POSITION || currentPos >= events.size()) return;

                            Event eventToDelete = events.get(currentPos);
                            FirebaseDatabase.getInstance().getReference("events")
                                    .child(eventToDelete.getId())
                                    .removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        if (currentPos < events.size()) {
                                            events.remove(currentPos);
                                            notifyItemRemoved(currentPos);
                                            Toast.makeText(holder.itemView.getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    // Vérifie l'état de la demande et ajuste le bouton
    private void checkRequestStatus(String eventId, EventViewHolder holder) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference requestsRef = FirebaseDatabase.getInstance().getReference("event_requests");

        requestsRef.orderByChild("eventId").equalTo(eventId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean requestFound = false;
                        String requestStatus = "";

                        for (DataSnapshot child : snapshot.getChildren()) {
                            EventRequest request = child.getValue(EventRequest.class);
                            if (request != null && currentUserId.equals(request.getUserId())) {
                                requestFound = true;
                                requestStatus = request.getStatus();
                                break;
                            }
                        }

                        if (requestFound) {
                            holder.btnRequestAccess.setEnabled(false);

                            switch (requestStatus.toLowerCase()) {
                                case "pending":
                                    holder.btnRequestAccess.setText("Request Pending");
                                    holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                            .getResources().getColor(android.R.color.darker_gray));
                                    break;
                                case "accepted":
                                    holder.btnRequestAccess.setText("Request Accepted");
                                    holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                            .getResources().getColor(android.R.color.holo_green_dark));
                                    break;
                                case "rejected":
                                    holder.btnRequestAccess.setText("Request Rejected");
                                    holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                            .getResources().getColor(android.R.color.holo_red_dark));
                                    break;
                                default:
                                    holder.btnRequestAccess.setText("Request Sent");
                                    holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                            .getResources().getColor(android.R.color.darker_gray));
                                    break;
                            }
                        } else {
                            // Pas de demande existante, bouton actif
                            holder.btnRequestAccess.setEnabled(true);
                            holder.btnRequestAccess.setText("Request Access");
                            holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                    .getResources().getColor(android.R.color.holo_blue_light));

                            // Nettoyer avant d'ajouter listener pour éviter doublons
                            holder.btnRequestAccess.setOnClickListener(null);

                            holder.btnRequestAccess.setOnClickListener(v -> {
                                DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference("event_requests");
                                String requestId = reqRef.push().getKey();
                                EventRequest newRequest = new EventRequest(currentUserId, eventId, "pending");
                                reqRef.child(requestId).setValue(newRequest)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(holder.itemView.getContext(), "Request sent!", Toast.LENGTH_SHORT).show();
                                            holder.btnRequestAccess.setText("Request Pending");
                                            holder.btnRequestAccess.setEnabled(false);
                                            holder.btnRequestAccess.setBackgroundColor(holder.itemView.getContext()
                                                    .getResources().getColor(android.R.color.darker_gray));
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(holder.itemView.getContext(), "Failed to send request", Toast.LENGTH_SHORT).show();
                                        });
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(holder.itemView.getContext(), "Failed to load requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventDescription, eventFee;
        Button btnDelete, btnEdit, btnRequestAccess;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.txtEventName);
            eventDate = itemView.findViewById(R.id.txtEventDate);
            eventDescription = itemView.findViewById(R.id.txtEventDescription);
            eventFee = itemView.findViewById(R.id.txtEventFee);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnRequestAccess = itemView.findViewById(R.id.btnRequestAccess);
        }
    }
}
