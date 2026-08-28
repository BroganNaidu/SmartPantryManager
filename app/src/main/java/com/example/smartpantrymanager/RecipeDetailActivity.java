package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeDetailName;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeMethod;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvRecipeDetailName = findViewById(R.id.tvRecipeDetailName);
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        tvRecipeMethod = findViewById(R.id.tvRecipeMethod);

        databaseHelper = new DatabaseHelper(this);

        int recipeId = getIntent().getIntExtra("recipe_id", -1);
        String recipeName = getIntent().getStringExtra("recipe_name");
        String recipeMethod = getIntent().getStringExtra("recipe_method");

        tvRecipeDetailName.setText(recipeName);
        tvRecipeMethod.setText(recipeMethod);

        loadRecipeIngredients(recipeId);
    }

    private void loadRecipeIngredients(int recipeId) {

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText = new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText.append("• ")
                    .append(formatQuantity(ingredient.getQuantity()))
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(capitalizeName(ingredient.getIngredientName()))
                    .append("\n");
        }

        tvRecipeIngredients.setText(ingredientText.toString().trim());
    }

    private String formatQuantity(double quantity) {

        if (quantity == (int) quantity) {
            return String.valueOf((int) quantity);
        }

        return String.valueOf(quantity);
    }

    private String capitalizeName(String name) {

        if (name == null || name.isEmpty()) {
            return "";
        }

        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}