package com.example.localloopapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Context context;
    private List<Category> categories;

    // Constructor to initialize adapter with context and category list
    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
    }

    // Called for each item to create and return the row view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position); // Get current category object

        // Inflate layout if it's not already created
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_row, parent, false);
        }

        // Find views in the row layout
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvDesc = convertView.findViewById(R.id.tvCategoryDescription);
        Button btnEdit = convertView.findViewById(R.id.btnCatEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        // Set text values from the category object
        tvName.setText(category.getName());
        tvDesc.setText(category.getDescription());

        // Delete button: removes the category from Firebase and updates UI ( pop up made to confirm)
        btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // User confirmed deletion
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
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Edit button: opens AddCategoryActivity with existing data passed via Intent
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddCategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("name", category.getName());
            intent.putExtra("description", category.getDescription());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required when using context in adapter
            context.startActivity(intent);
        });

        return convertView; // Return the populated view for display
    }
}
