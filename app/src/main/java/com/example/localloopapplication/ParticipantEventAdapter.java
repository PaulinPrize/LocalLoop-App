package com.example.localloopapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParticipantEventAdapter extends RecyclerView.Adapter<ParticipantEventAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> eventList;

    public ParticipantEventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.participant_searchitem_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.txtEventName.setText(event.getName());
        holder.txtEventDate.setText(event.getDateTime());


        holder.btnViewEvent.setOnClickListener(v -> {
            Intent intent = new Intent(context, ParticipantEventDetailActivity.class);
            intent.putExtra("eventId", event.getId());
            intent.putExtra("name", event.getName());
            intent.putExtra("dateTime", event.getDateTime());
            intent.putExtra("category", event.getCategory());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventName, txtEventDate;
        Button btnViewEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventDate = itemView.findViewById(R.id.txtEventDate);
            btnViewEvent = itemView.findViewById(R.id.btnViewEvent);
        }
    }
}
