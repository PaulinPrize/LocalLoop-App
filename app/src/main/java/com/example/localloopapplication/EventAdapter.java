package com.example.localloopapplication;

import android.app.AlertDialog;
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
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> events;

    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
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

        // Bouton Supprimer avec confirmation
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("events")
                                .child(event.getId())  // Assure-toi que Event contient l’ID Firebase
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(holder.itemView.getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                                    events.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, events.size());
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(holder.itemView.getContext(), "Failed to delete", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEventActivity.class);
            intent.putExtra("isEditing", true);
            intent.putExtra("eventId", event.getId());
            intent.putExtra("name", event.getName());
            intent.putExtra("description", event.getDescription());
            intent.putExtra("category", event.getCategory());
            intent.putExtra("fee", event.getFee());
            intent.putExtra("dateTime", event.getDateTime());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate;
        Button btnDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.txtEventName);
            eventDate = itemView.findViewById(R.id.txtEventDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}