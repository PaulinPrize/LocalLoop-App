package com.example.localloopapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.localloopapplication.Category;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
// Delete button click listener shows confirmation dialog
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete from Firebase and update RecyclerView
                        deleteCategory(category.getId(), holder.getAdapterPosition());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddCategoryActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("name", category.getName());
            intent.putExtra("description", category.getDescription());
            context.startActivity(intent);
        });



    }




    // Returns total number of items in the list
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;
        Button deleteButton, editButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.category_name);
            descriptionTextView = itemView.findViewById(R.id.category_description);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);
        }
    }

    private void deleteCategory(String categoryId, int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("event_categories");
        ref.child(categoryId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    categoryList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
