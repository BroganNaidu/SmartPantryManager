package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private ArrayList<PantryItem> pantryItems;

    public PantryAdapter(ArrayList<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.tvIngredientName.setText(item.getName());
        holder.tvIngredientQuantity.setText(item.getQuantity() + " " + item.getUnit());

        if (item.getExpiryDate() == null || item.getExpiryDate().isEmpty()) {
            holder.tvIngredientExpiry.setText("No expiry date");
        } else {
            holder.tvIngredientExpiry.setText("Expiry: " + item.getExpiryDate());
        }
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientQuantity;
        TextView tvIngredientExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientQuantity = itemView.findViewById(R.id.tvIngredientQuantity);
            tvIngredientExpiry = itemView.findViewById(R.id.tvIngredientExpiry);
        }
    }
}