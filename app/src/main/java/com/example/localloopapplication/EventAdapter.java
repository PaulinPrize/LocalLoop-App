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

/**
 * RecyclerView Adapter for displaying Event objects in a list.
 * Supports event deletion with confirmation.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // List of events to display
    private ArrayList<Event> events;

    /**
     * Constructor for EventAdapter.
     * @param events The list of events to display in the RecyclerView.
     */
    public EventAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each event row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * Binds data to a ViewHolder at the given position.
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        // Get current event object
        Event event = events.get(position);
        // Set event name and date in the view
        holder.eventName.setText(event.getName());
        holder.eventDate.setText(event.getDateTime());

        // Set up delete button with confirmation dialog
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete event from Firebase (using its unique ID)
                        FirebaseDatabase.getInstance().getReference("events")
                                .child(event.getId())  // Make sure Event contains its Firebase ID!
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    // Remove from local list and update RecyclerView
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
    }

    /**
     * Returns the number of events in the list.
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * ViewHolder for an Event item in the RecyclerView.
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate;
        Button btnDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link UI elements from layout
            eventName = itemView.findViewById(R.id.txtEventName);
            eventDate = itemView.findViewById(R.id.txtEventDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
