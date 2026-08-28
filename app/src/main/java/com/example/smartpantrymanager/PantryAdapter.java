package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private ArrayList<PantryItem> pantryItems;
    private DatabaseHelper databaseHelper;
    private OnPantryChangedListener listener;

    public interface OnPantryChangedListener {
        void onPantryChanged();
    }

    public PantryAdapter(ArrayList<PantryItem> pantryItems,
                         OnPantryChangedListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        databaseHelper = new DatabaseHelper(parent.getContext());

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.tvIngredientName.setText(item.getName());

        if (item.getQuantity() == (int) item.getQuantity()) {
            holder.tvIngredientQuantity.setText(
                    (int) item.getQuantity() + " " + item.getUnit()
            );
        } else {
            holder.tvIngredientQuantity.setText(
                    item.getQuantity() + " " + item.getUnit()
            );
        }

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            holder.tvIngredientExpiry.setText("No expiry date");
        } else {
            holder.tvIngredientExpiry.setText("Expiry: " + item.getExpiryDate());
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(
                    v.getContext(),
                    AddEditIngredientActivity.class
            );

            intent.putExtra("ingredient_id", item.getId());
            intent.putExtra("ingredient_name", item.getName());
            intent.putExtra("ingredient_quantity", item.getQuantity());
            intent.putExtra("ingredient_unit", item.getUnit());
            intent.putExtra("ingredient_expiry", item.getExpiryDate());

            v.getContext().startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Ingredient")
                    .setMessage("Are you sure you want to delete " + item.getName() + "?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {

                        int result = databaseHelper.deletePantryItem(item.getId());

                        if (result > 0) {
                            int currentPosition = holder.getBindingAdapterPosition();

                            if (currentPosition != RecyclerView.NO_POSITION) {
                                pantryItems.remove(currentPosition);
                                notifyItemRemoved(currentPosition);

                                if (listener != null) {
                                    listener.onPantryChanged();
                                }
                            }

                            Toast.makeText(
                                    v.getContext(),
                                    "Ingredient deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {
                            Toast.makeText(
                                    v.getContext(),
                                    "Failed to delete ingredient",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;
        TextView tvIngredientExpiry;
        Button btnEdit;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
            tvIngredientExpiry = itemView.findViewById(R.id.tvIngredientExpiry);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}