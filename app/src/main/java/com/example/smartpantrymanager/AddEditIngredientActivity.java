package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etUnit;
    private EditText etExpiryDate;
    private Button btnSaveIngredient;
    private TextView tvFormTitle;

    private DatabaseHelper databaseHelper;

    private boolean isEditMode = false;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        databaseHelper = new DatabaseHelper(this);

        // Check if an ingredient is being edited
        if (getIntent().hasExtra("ingredient_id")) {
            isEditMode = true;

            ingredientId = getIntent().getIntExtra("ingredient_id", -1);
            String name = getIntent().getStringExtra("ingredient_name");
            double quantity = getIntent().getDoubleExtra("ingredient_quantity", 0);
            String unit = getIntent().getStringExtra("ingredient_unit");
            String expiryDate = getIntent().getStringExtra("ingredient_expiry");

            tvFormTitle.setText("Edit Ingredient");
            btnSaveIngredient.setText("Update Ingredient");

            etIngredientName.setText(name);
            etQuantity.setText(String.valueOf(quantity));
            etUnit.setText(unit);
            etExpiryDate.setText(expiryDate);
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = etIngredientName.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();

        if (name.isEmpty()) {
            etIngredientName.setError("Ingredient name is required");
            etIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        if (unit.isEmpty()) {
            etUnit.setError("Unit is required");
            etUnit.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid quantity");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be more than 0");
            etQuantity.requestFocus();
            return;
        }

        if (isEditMode) {
            PantryItem item = new PantryItem(
                    ingredientId,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            int result = databaseHelper.updatePantryItem(item);

            if (result > 0) {
                Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update ingredient", Toast.LENGTH_SHORT).show();
            }

        } else {
            PantryItem item = new PantryItem(
                    0,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );

            long result = databaseHelper.addPantryItem(item);

            if (result != -1) {
                Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save ingredient", Toast.LENGTH_SHORT).show();
            }
        }
    }
}