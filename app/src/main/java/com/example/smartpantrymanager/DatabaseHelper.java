package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 3;

    // Pantry table
    private static final String TABLE_PANTRY = "pantry_items";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiry_date";

    // Recipes table
    private static final String TABLE_RECIPES = "recipes";

    private static final String COLUMN_RECIPE_ID = "id";
    private static final String COLUMN_RECIPE_NAME = "name";
    private static final String COLUMN_RECIPE_METHOD = "method";

    // Recipe ingredients table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    private static final String COLUMN_RECIPE_INGREDIENT_ID = "id";
    private static final String COLUMN_RECIPE_ID_FK = "recipe_id";
    private static final String COLUMN_INGREDIENT_NAME = "ingredient_name";
    private static final String COLUMN_REQUIRED_QUANTITY = "quantity";
    private static final String COLUMN_REQUIRED_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPantryTable(db);
        createRecipeTables(db);
        seedRecipes(db);
    }

    // Create the pantry table
    private void createPantryTable(SQLiteDatabase db) {

        String createPantryTable =
                "CREATE TABLE " + TABLE_PANTRY + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY + " TEXT)";

        db.execSQL(createPantryTable);
    }

    // Create recipe tables
    private void createRecipeTables(SQLiteDatabase db) {

        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                        COLUMN_RECIPE_METHOD + " TEXT NOT NULL)";

        db.execSQL(createRecipesTable);

        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COLUMN_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_ID_FK + " INTEGER NOT NULL, " +
                        COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COLUMN_REQUIRED_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_REQUIRED_UNIT + " TEXT NOT NULL, " +
                        "FOREIGN KEY(" + COLUMN_RECIPE_ID_FK + ") REFERENCES " +
                        TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";

        db.execSQL(createRecipeIngredientsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Version 2 added the recipe tables
        if (oldVersion < 2) {
            createRecipeTables(db);
        }

        // Version 3 rebuilds the recipe tables and adds the starting recipes
        if (oldVersion < 3) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);

            createRecipeTables(db);
            seedRecipes(db);
        }
    }

    // Add the starting recipes
    private void seedRecipes(SQLiteDatabase db) {

        // 1. Scrambled Eggs
        long recipeId = addRecipe(
                db,
                "Scrambled Eggs",
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add the milk and mix well.\n" +
                        "3. Melt the butter in a pan.\n" +
                        "4. Add the eggs and stir until cooked."
        );

        addRecipeIngredient(db, recipeId, "egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "milk", 30, "ml");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 2. Cheese Omelette
        recipeId = addRecipe(
                db,
                "Cheese Omelette",
                "1. Beat the eggs in a bowl.\n" +
                        "2. Melt the butter in a pan.\n" +
                        "3. Add the eggs and cook gently.\n" +
                        "4. Add the cheese and fold the omelette."
        );

        addRecipeIngredient(db, recipeId, "egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 30, "g");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 3. Fried Egg on Toast
        recipeId = addRecipe(
                db,
                "Fried Egg on Toast",
                "1. Toast the bread.\n" +
                        "2. Melt the butter in a pan.\n" +
                        "3. Fry the egg until cooked.\n" +
                        "4. Place the egg on the toast."
        );

        addRecipeIngredient(db, recipeId, "egg", 1, "pieces");
        addRecipeIngredient(db, recipeId, "bread", 2, "pieces");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 4. Grilled Cheese Sandwich
        recipeId = addRecipe(
                db,
                "Grilled Cheese Sandwich",
                "1. Butter the bread.\n" +
                        "2. Place cheese between the bread slices.\n" +
                        "3. Cook in a pan until golden brown.\n" +
                        "4. Serve while warm."
        );

        addRecipeIngredient(db, recipeId, "bread", 2, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 40, "g");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 5. Tomato and Cheese Sandwich
        recipeId = addRecipe(
                db,
                "Tomato and Cheese Sandwich",
                "1. Slice the tomato.\n" +
                        "2. Place the tomato and cheese onto the bread.\n" +
                        "3. Close the sandwich and serve."
        );

        addRecipeIngredient(db, recipeId, "bread", 2, "pieces");
        addRecipeIngredient(db, recipeId, "tomato", 1, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 30, "g");


        // 6. French Toast
        recipeId = addRecipe(
                db,
                "French Toast",
                "1. Beat the egg and milk together.\n" +
                        "2. Dip the bread into the mixture.\n" +
                        "3. Melt butter in a pan.\n" +
                        "4. Cook the bread on both sides."
        );

        addRecipeIngredient(db, recipeId, "bread", 2, "pieces");
        addRecipeIngredient(db, recipeId, "egg", 1, "pieces");
        addRecipeIngredient(db, recipeId, "milk", 50, "ml");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 7. Pancakes
        recipeId = addRecipe(
                db,
                "Pancakes",
                "1. Mix the flour, egg and milk together.\n" +
                        "2. Stir until the batter is smooth.\n" +
                        "3. Melt butter in a pan.\n" +
                        "4. Cook each pancake on both sides."
        );

        addRecipeIngredient(db, recipeId, "flour", 150, "g");
        addRecipeIngredient(db, recipeId, "egg", 1, "pieces");
        addRecipeIngredient(db, recipeId, "milk", 200, "ml");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 8. Tomato Pasta
        recipeId = addRecipe(
                db,
                "Tomato Pasta",
                "1. Boil the pasta until cooked.\n" +
                        "2. Chop the tomato.\n" +
                        "3. Cook the tomato in a pan.\n" +
                        "4. Mix the pasta and tomato together."
        );

        addRecipeIngredient(db, recipeId, "pasta", 150, "g");
        addRecipeIngredient(db, recipeId, "tomato", 2, "pieces");


        // 9. Garlic Butter Pasta
        recipeId = addRecipe(
                db,
                "Garlic Butter Pasta",
                "1. Boil the pasta until cooked.\n" +
                        "2. Melt the butter in a pan.\n" +
                        "3. Add the garlic and cook briefly.\n" +
                        "4. Mix in the cooked pasta."
        );

        addRecipeIngredient(db, recipeId, "pasta", 150, "g");
        addRecipeIngredient(db, recipeId, "garlic", 2, "pieces");
        addRecipeIngredient(db, recipeId, "butter", 20, "g");


        // 10. Cheese Pasta
        recipeId = addRecipe(
                db,
                "Cheese Pasta",
                "1. Boil the pasta until cooked.\n" +
                        "2. Drain the pasta.\n" +
                        "3. Add cheese while the pasta is still hot.\n" +
                        "4. Mix until the cheese melts."
        );

        addRecipeIngredient(db, recipeId, "pasta", 150, "g");
        addRecipeIngredient(db, recipeId, "cheese", 50, "g");


        // 11. Egg Fried Rice
        recipeId = addRecipe(
                db,
                "Egg Fried Rice",
                "1. Cook the rice if needed.\n" +
                        "2. Fry the egg in a pan.\n" +
                        "3. Add the rice.\n" +
                        "4. Mix everything together."
        );

        addRecipeIngredient(db, recipeId, "rice", 200, "g");
        addRecipeIngredient(db, recipeId, "egg", 2, "pieces");


        // 12. Tomato Rice
        recipeId = addRecipe(
                db,
                "Tomato Rice",
                "1. Cook the rice.\n" +
                        "2. Chop the tomato.\n" +
                        "3. Cook the tomato in a pan.\n" +
                        "4. Add the rice and mix together."
        );

        addRecipeIngredient(db, recipeId, "rice", 200, "g");
        addRecipeIngredient(db, recipeId, "tomato", 2, "pieces");


        // 13. Cheese and Tomato Omelette
        recipeId = addRecipe(
                db,
                "Cheese and Tomato Omelette",
                "1. Beat the eggs.\n" +
                        "2. Chop the tomato.\n" +
                        "3. Cook the eggs in a pan.\n" +
                        "4. Add the tomato and cheese, then fold."
        );

        addRecipeIngredient(db, recipeId, "egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "tomato", 1, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 30, "g");


        // 14. Mashed Potatoes
        recipeId = addRecipe(
                db,
                "Mashed Potatoes",
                "1. Peel and boil the potatoes.\n" +
                        "2. Drain the potatoes.\n" +
                        "3. Add milk and butter.\n" +
                        "4. Mash until smooth."
        );

        addRecipeIngredient(db, recipeId, "potato", 3, "pieces");
        addRecipeIngredient(db, recipeId, "milk", 50, "ml");
        addRecipeIngredient(db, recipeId, "butter", 20, "g");


        // 15. Garlic Mashed Potatoes
        recipeId = addRecipe(
                db,
                "Garlic Mashed Potatoes",
                "1. Peel and boil the potatoes.\n" +
                        "2. Cook the garlic lightly.\n" +
                        "3. Add the butter and garlic to the potatoes.\n" +
                        "4. Mash until smooth."
        );

        addRecipeIngredient(db, recipeId, "potato", 3, "pieces");
        addRecipeIngredient(db, recipeId, "garlic", 2, "pieces");
        addRecipeIngredient(db, recipeId, "butter", 20, "g");


        // 16. Baked Potato with Cheese
        recipeId = addRecipe(
                db,
                "Baked Potato with Cheese",
                "1. Bake the potatoes until soft.\n" +
                        "2. Cut the potatoes open.\n" +
                        "3. Add butter and cheese.\n" +
                        "4. Serve while hot."
        );

        addRecipeIngredient(db, recipeId, "potato", 2, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 40, "g");
        addRecipeIngredient(db, recipeId, "butter", 10, "g");


        // 17. Banana Pancakes
        recipeId = addRecipe(
                db,
                "Banana Pancakes",
                "1. Mash the banana.\n" +
                        "2. Mix in the egg and flour.\n" +
                        "3. Add the milk and mix well.\n" +
                        "4. Cook small pancakes in a pan."
        );

        addRecipeIngredient(db, recipeId, "banana", 1, "pieces");
        addRecipeIngredient(db, recipeId, "egg", 1, "pieces");
        addRecipeIngredient(db, recipeId, "flour", 100, "g");
        addRecipeIngredient(db, recipeId, "milk", 100, "ml");


        // 18. Cheese Toast
        recipeId = addRecipe(
                db,
                "Cheese Toast",
                "1. Place cheese onto the bread.\n" +
                        "2. Toast until the bread is crisp and the cheese melts.\n" +
                        "3. Serve warm."
        );

        addRecipeIngredient(db, recipeId, "bread", 2, "pieces");
        addRecipeIngredient(db, recipeId, "cheese", 30, "g");


        // 19. Tomato Scrambled Eggs
        recipeId = addRecipe(
                db,
                "Tomato Scrambled Eggs",
                "1. Chop the tomato.\n" +
                        "2. Beat the eggs.\n" +
                        "3. Cook the tomato briefly in a pan.\n" +
                        "4. Add the eggs and stir until cooked."
        );

        addRecipeIngredient(db, recipeId, "egg", 2, "pieces");
        addRecipeIngredient(db, recipeId, "tomato", 1, "pieces");


        // 20. Simple Vegetable Rice
        recipeId = addRecipe(
                db,
                "Simple Vegetable Rice",
                "1. Cook the rice until soft.\n" +
                        "2. Chop the carrot.\n" +
                        "3. Cook the carrot and peas.\n" +
                        "4. Mix the vegetables into the rice."
        );

        addRecipeIngredient(db, recipeId, "rice", 200, "g");
        addRecipeIngredient(db, recipeId, "carrot", 1, "pieces");
        addRecipeIngredient(db, recipeId, "peas", 50, "g");
    }

    // Add one recipe and return its ID
    private long addRecipe(SQLiteDatabase db, String name, String method) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_METHOD, method);

        return db.insert(TABLE_RECIPES, null, values);
    }

    // Add an ingredient required by a recipe
    private void addRecipeIngredient(SQLiteDatabase db, long recipeId,
                                     String ingredientName,
                                     double quantity,
                                     String unit) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID_FK, recipeId);
        values.put(COLUMN_INGREDIENT_NAME, ingredientName);
        values.put(COLUMN_REQUIRED_QUANTITY, quantity);
        values.put(COLUMN_REQUIRED_UNIT, unit);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }

    // Get all recipes
    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME)
                );

                String method = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_METHOD)
                );

                Recipe recipe = new Recipe(id, name, method);
                recipes.add(recipe);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipes;
    }

    // Get the required ingredients for one recipe
    public ArrayList<RecipeIngredient> getRecipeIngredients(int recipeId) {

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COLUMN_RECIPE_ID_FK + " = ?",
                new String[]{String.valueOf(recipeId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_RECIPE_INGREDIENT_ID)
                );

                String ingredientName = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_REQUIRED_UNIT)
                );

                RecipeIngredient ingredient = new RecipeIngredient(
                        id,
                        recipeId,
                        ingredientName,
                        quantity,
                        unit
                );

                ingredients.add(ingredient);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredients;
    }

    // Add a new ingredient to the pantry
    public long addPantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY, item.getExpiryDate());

        long result = db.insert(TABLE_PANTRY, null, values);

        db.close();

        return result;
    }

    // Get all pantry items from the database
    public ArrayList<PantryItem> getAllPantryItems() {

        ArrayList<PantryItem> pantryItems = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_PANTRY,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME + " ASC"
        );

        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_ID)
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_NAME)
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_UNIT)
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_EXPIRY)
                );

                PantryItem item = new PantryItem(
                        id,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

                pantryItems.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return pantryItems;
    }

    // Update an existing pantry item
    public int updatePantryItem(PantryItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_QUANTITY, item.getQuantity());
        values.put(COLUMN_UNIT, item.getUnit());
        values.put(COLUMN_EXPIRY, item.getExpiryDate());

        int result = db.update(
                TABLE_PANTRY,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())}
        );

        db.close();

        return result;
    }

    // Delete an ingredient from the pantry
    public int deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_PANTRY,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return result;
    }
}