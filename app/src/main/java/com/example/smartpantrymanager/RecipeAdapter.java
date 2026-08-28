package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private DatabaseHelper databaseHelper;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        databaseHelper = new DatabaseHelper(parent.getContext());

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.tvRecipeName.setText(recipe.getName());

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipe.getId());

        int ingredientCount = ingredients.size();

        if (ingredientCount == 1) {
            holder.tvRecipeIngredientCount.setText("1 ingredient");
        } else {
            holder.tvRecipeIngredientCount.setText(
                    ingredientCount + " ingredients"
            );
        }

        holder.btnViewRecipe.setOnClickListener(v -> {
            // Recipe details screen will be added later
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeIngredientCount;
        Button btnViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeIngredientCount =
                    itemView.findViewById(R.id.tvRecipeIngredientCount);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }
    }
}