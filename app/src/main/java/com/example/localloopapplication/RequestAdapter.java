package com.example.localloopapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private ArrayList<EventRequest> requests;
    private Context context;

    public RequestAdapter(Context context, ArrayList<EventRequest> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        EventRequest req = requests.get(position);

        holder.txtUserId.setText("User: " + req.getUserId());
        holder.txtEventId.setText("Event ID: " + req.getEventId());
        holder.txtStatus.setText("Status: " + req.getStatus());

        String status = req.getStatus().toLowerCase();

        boolean isPending = status.equals("pending");
        boolean isAccepted = status.equals("accepted");
        boolean isRejected = status.equals("rejected");

        // Gestion bouton Accept
        if (isAccepted) {
            holder.btnAccept.setEnabled(false);
            holder.btnAccept.setText("Request Accepted");
            holder.btnAccept.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        } else {
            holder.btnAccept.setEnabled(isPending);
            holder.btnAccept.setText("Accept");
            holder.btnAccept.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        // Gestion bouton Reject
        if (isRejected) {
            holder.btnReject.setEnabled(false);
            holder.btnReject.setText("Request Rejected");
            holder.btnReject.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
        } else {
            holder.btnReject.setEnabled(isPending);
            holder.btnReject.setText("Reject");
            holder.btnReject.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
        }

        holder.btnAccept.setOnClickListener(v -> updateRequestStatus(req, "accepted", holder));
        holder.btnReject.setOnClickListener(v -> updateRequestStatus(req, "rejected", holder));
    }

    private void updateRequestStatus(EventRequest req, String newStatus, RequestViewHolder holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("event_requests").child(req.getId());
        ref.child("status").setValue(newStatus)
                .addOnSuccessListener(aVoid -> {
                    req.setStatus(newStatus); // mise à jour modèle
                    // Mise à jour UI immédiate en rappelant onBindViewHolder (optionnel)
                    notifyItemChanged(requests.indexOf(req));
                    Toast.makeText(context, "Request " + newStatus, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserId, txtEventId, txtStatus;
        Button btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserId = itemView.findViewById(R.id.txtUserId);
            txtEventId = itemView.findViewById(R.id.txtEventId);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
