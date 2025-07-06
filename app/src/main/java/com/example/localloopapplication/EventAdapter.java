package com.example.localloopapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final int pos = position;
        Event event = eventList.get(pos); 
        holder.txtEventName.setText(event.getName());
        //holder.txtEventDate.setText(event.getDate());
        // Add more if needed
        holder.txtCategoryName.setText(event.getName());
        holder.txtCategoryDescription.setText(event.getDescription());
        holder.btnDelete.setOnClickListener(v -> {
            if(context instanceof MyEventsActivity){
                MyEventsActivity myeventsactivity = (MyEventsActivity) context;
                View confirmationLayout = myeventsactivity.findViewById(R.id.confirmationLayout);
                TextView txtEventName = myeventsactivity.findViewById(R.id.txtEventName);
                Button btnYes = myeventsactivity.findViewById(R.id.btnYes);
                Button btnNo = myeventsactivity.findViewById(R.id.btnNo);
                
                confirmationLayout.setVisibility(View.VISIBLE);
                txtEventName.setText(event.getName());
                
                btnYes.setOnClickListener(null);
                btnYes.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        // perform delete action 
                            FirebaseDatabase.getInstance().getReference("events")
                                .child(event.getId()).removeValue()
                                // TODO: add event.getId method 
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                                    eventList.remove(pos); 
                                    notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                                });
                        confirmationLayout.setVisibility(View.GONE);
                    }
                });
                
                btnNo.setOnClickListener(null);
                btnNo.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        confirmationLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
        
        
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView txtEventName, txtEventDate, txtCategoryName, txtCategoryDescription;
        Button btnDelete, btnEdit;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtEventDate = itemView.findViewById(R.id.txtEventDate);
            txtCategoryName = itemView.findViewById(R.id.txtCategoryName);
            txtCategoryDescription = itemView.findViewById(R.id.txtCategoryDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}