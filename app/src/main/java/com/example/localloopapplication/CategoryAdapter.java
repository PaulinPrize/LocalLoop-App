package com.example.localloopapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_row, parent, false);
            holder = new ViewHolder();

            holder.tvName = convertView.findViewById(R.id.tvCategoryName);
            holder.tvDesc = convertView.findViewById(R.id.tvCategoryDescription);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            holder.btnYes = convertView.findViewById(R.id.btnYes);
            holder.btnNo = convertView.findViewById(R.id.btnNo);
            holder.confirmationLayout = convertView.findViewById(R.id.confirmationLayout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(category.getName());
        holder.tvDesc.setText(category.getDescription());

        // Hide confirmation layout by default
        holder.confirmationLayout.setVisibility(View.GONE);

        // Show confirmation layout
        holder.btnDelete.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.VISIBLE));

        // Cancel deletion
        holder.btnNo.setOnClickListener(v -> holder.confirmationLayout.setVisibility(View.GONE));

        // Confirm deletion
        holder.btnYes.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("event_categories")
                    .child(category.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show();
                        categories.remove(category);
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                    });
        });

        // Edit category
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddCategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("name", category.getName());
            intent.putExtra("description", category.getDescription());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvName, tvDesc;
        Button btnEdit, btnDelete, btnYes, btnNo;
        LinearLayout confirmationLayout;
    }
}
