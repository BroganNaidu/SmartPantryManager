package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private TextView tvEmptyPantry;
    private Button btnAddIngredient;
    private BottomNavigationView bottomNavigationView;

    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;
    private ArrayList<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        databaseHelper = new DatabaseHelper(this);

        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));

        // Open the add ingredient screen
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {

        bottomNavigationView.setSelectedItemId(R.id.nav_pantry);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_pantry) {
                return true;
            }

            if (itemId == R.id.nav_recipes) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SuggestedRecipesActivity.class
                );

                startActivity(intent);
                return true;
            }

            if (itemId == R.id.nav_settings) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_pantry);
        }

        loadPantryItems();
    }

    // Load the pantry items from the database
    private void loadPantryItems() {

        pantryItems = databaseHelper.getAllPantryItems();

        pantryAdapter = new PantryAdapter(
                pantryItems,
                this::updateEmptyState
        );

        recyclerViewPantry.setAdapter(pantryAdapter);

        updateEmptyState();
    }

    private void updateEmptyState() {

        if (pantryItems == null || pantryItems.isEmpty()) {
            recyclerViewPantry.setVisibility(View.GONE);
            tvEmptyPantry.setVisibility(View.VISIBLE);
        } else {
            recyclerViewPantry.setVisibility(View.VISIBLE);
            tvEmptyPantry.setVisibility(View.GONE);
        }
    }
}