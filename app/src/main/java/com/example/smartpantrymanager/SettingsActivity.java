package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerWeightUnit;
    private Spinner spinnerLiquidUnit;
    private Button btnSaveSettings;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "pantry_settings";
    private static final String KEY_WEIGHT_UNIT = "weight_unit";
    private static final String KEY_LIQUID_UNIT = "liquid_unit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerWeightUnit = findViewById(R.id.spinnerWeightUnit);
        spinnerLiquidUnit = findViewById(R.id.spinnerLiquidUnit);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        setupSpinners();
        loadSavedSettings();
        setupBottomNavigation();

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void setupSpinners() {

        String[] weightUnits = {"g", "kg"};

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                weightUnits
        );

        weightAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerWeightUnit.setAdapter(weightAdapter);

        String[] liquidUnits = {"ml", "l"};

        ArrayAdapter<String> liquidAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                liquidUnits
        );

        liquidAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerLiquidUnit.setAdapter(liquidAdapter);
    }

    private void loadSavedSettings() {

        String savedWeightUnit = sharedPreferences.getString(
                KEY_WEIGHT_UNIT,
                "g"
        );

        String savedLiquidUnit = sharedPreferences.getString(
                KEY_LIQUID_UNIT,
                "ml"
        );

        if (savedWeightUnit.equals("kg")) {
            spinnerWeightUnit.setSelection(1);
        } else {
            spinnerWeightUnit.setSelection(0);
        }

        if (savedLiquidUnit.equals("l")) {
            spinnerLiquidUnit.setSelection(1);
        } else {
            spinnerLiquidUnit.setSelection(0);
        }
    }

    private void saveSettings() {

        String weightUnit =
                spinnerWeightUnit.getSelectedItem().toString();

        String liquidUnit =
                spinnerLiquidUnit.getSelectedItem().toString();

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putString(KEY_WEIGHT_UNIT, weightUnit);
        editor.putString(KEY_LIQUID_UNIT, liquidUnit);

        editor.apply();

        Toast.makeText(
                this,
                "Settings saved",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void setupBottomNavigation() {

        bottomNavigationView.setSelectedItemId(R.id.nav_settings);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.nav_settings) {
                return true;
            }

            if (itemId == R.id.nav_pantry) {

                Intent intent = new Intent(
                        SettingsActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            if (itemId == R.id.nav_recipes) {

                Intent intent = new Intent(
                        SettingsActivity.this,
                        SuggestedRecipesActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }
}