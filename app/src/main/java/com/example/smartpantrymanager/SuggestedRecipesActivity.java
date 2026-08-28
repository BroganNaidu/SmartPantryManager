package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView tvNoRecipes;
    private BottomNavigationView bottomNavigationView;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        tvNoRecipes = findViewById(R.id.tvNoRecipes);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNavigation();
        loadSuggestedRecipes();
    }

    private void setupBottomNavigation() {

        bottomNavigationView.setSelectedItemId(R.id.nav_recipes);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_recipes) {
                return true;
            }

            if (itemId == R.id.nav_pantry) {

                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            if (itemId == R.id.nav_settings) {

                Intent intent = new Intent(
                        SuggestedRecipesActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }

    private void loadSuggestedRecipes() {

        ArrayList<Recipe> allRecipes = databaseHelper.getAllRecipes();
        ArrayList<PantryItem> pantryItems = databaseHelper.getAllPantryItems();

        ArrayList<Recipe> matchedRecipes = new ArrayList<>();

        for (Recipe recipe : allRecipes) {

            ArrayList<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredients(recipe.getId());

            boolean canMakeRecipe = true;

            for (RecipeIngredient requiredIngredient : requiredIngredients) {

                boolean ingredientFound = false;

                for (PantryItem pantryItem : pantryItems) {

                    if (normalizeName(pantryItem.getName())
                            .equals(normalizeName(requiredIngredient.getIngredientName()))) {

                        if (hasEnoughQuantity(pantryItem, requiredIngredient)) {
                            ingredientFound = true;
                            break;
                        }
                    }
                }

                if (!ingredientFound) {
                    canMakeRecipe = false;
                    break;
                }
            }

            if (canMakeRecipe) {
                matchedRecipes.add(recipe);
            }
        }

        recipeAdapter = new RecipeAdapter(matchedRecipes);
        recyclerViewRecipes.setAdapter(recipeAdapter);

        if (matchedRecipes.isEmpty()) {
            recyclerViewRecipes.setVisibility(View.GONE);
            tvNoRecipes.setVisibility(View.VISIBLE);
        } else {
            recyclerViewRecipes.setVisibility(View.VISIBLE);
            tvNoRecipes.setVisibility(View.GONE);
        }
    }

    private String normalizeName(String name) {

        String normalized = name.toLowerCase().trim();

        if (normalized.endsWith("ies")) {
            normalized = normalized.substring(0, normalized.length() - 3) + "y";
        } else if (normalized.endsWith("oes")) {
            normalized = normalized.substring(0, normalized.length() - 2);
        } else if (normalized.endsWith("s") && !normalized.endsWith("ss")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        return normalized;
    }

    private boolean hasEnoughQuantity(PantryItem pantryItem,
                                      RecipeIngredient requiredIngredient) {

        double pantryQuantity = pantryItem.getQuantity();
        double requiredQuantity = requiredIngredient.getQuantity();

        String pantryUnit = normalizeUnit(pantryItem.getUnit());
        String requiredUnit = normalizeUnit(requiredIngredient.getUnit());

        if (pantryUnit.equals(requiredUnit)) {
            return pantryQuantity >= requiredQuantity;
        }

        double convertedPantryQuantity =
                convertQuantity(pantryQuantity, pantryUnit, requiredUnit);

        if (convertedPantryQuantity == -1) {
            return false;
        }

        return convertedPantryQuantity >= requiredQuantity;
    }

    private String normalizeUnit(String unit) {

        String normalized = unit.toLowerCase().trim();

        if (normalized.equals("grams") || normalized.equals("gram")) {
            return "g";
        }

        if (normalized.equals("kilograms")
                || normalized.equals("kilogram")
                || normalized.equals("kgs")) {
            return "kg";
        }

        if (normalized.equals("millilitres")
                || normalized.equals("milliliters")
                || normalized.equals("millilitre")
                || normalized.equals("milliliter")) {
            return "ml";
        }

        if (normalized.equals("litres")
                || normalized.equals("liters")
                || normalized.equals("litre")
                || normalized.equals("liter")) {
            return "l";
        }

        if (normalized.equals("piece")
                || normalized.equals("pieces")
                || normalized.equals("pcs")
                || normalized.equals("pc")) {
            return "pieces";
        }

        return normalized;
    }

    private double convertQuantity(double quantity,
                                   String fromUnit,
                                   String toUnit) {

        if (fromUnit.equals("kg") && toUnit.equals("g")) {
            return quantity * 1000;
        }

        if (fromUnit.equals("g") && toUnit.equals("kg")) {
            return quantity / 1000;
        }

        if (fromUnit.equals("l") && toUnit.equals("ml")) {
            return quantity * 1000;
        }

        if (fromUnit.equals("ml") && toUnit.equals("l")) {
            return quantity / 1000;
        }

        return -1;
    }
}