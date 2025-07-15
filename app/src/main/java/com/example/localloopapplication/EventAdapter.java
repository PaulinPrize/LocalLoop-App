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
        holder.eventDescription.setText(event.getDescription());
        holder.eventFee.setText("Fee: $" + event.getFee());

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

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventDescription, eventFee;
        Button btnDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.txtEventName);
            eventDate = itemView.findViewById(R.id.txtEventDate);
            eventDescription = itemView.findViewById(R.id.txtEventDescription);
            eventFee = itemView.findViewById(R.id.txtEventFee);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
