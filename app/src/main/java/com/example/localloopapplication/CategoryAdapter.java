package com.example.localloopapplication;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//  takes your data (like a list of event categories)
//and binds it to views (like rows in a RecyclerView).

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private List<Category> categoryList;


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_admin_category_list, parent, false);
        return new CategoryViewHolder(view);
    }

    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        holder.tvCategoryName.setText(category.getName());
        holder.tvCategoryDescription.setText(category.getDescription());

        // allows you to click and edit
        holder.btnEdit.setOnClickListener(v ->  {
                Toast.makeText(context, "Edit " + category.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, AddCategoryActivity.class);
            intent.putExtra("eventId", Category.getId());
            intent.putExtra("name", Category.getName());
            intent.putExtra("description", Category.getDescription());
            context.startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        });
        //alows you to click delete
        holder.btnDelete.setOnClickListener(v -> {
            Toast.makeText(context, "Delete " + category.getName(), Toast.LENGTH_SHORT).show();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("events");
            db.child(Category.getId()).removeValue();
        });
    }
    // keeps track of category list
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvCategoryDescription;
        Button btnEdit, btnDelete;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvCategoryDescription = itemView.findViewById(R.id.tvCategoryDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

    }
}
