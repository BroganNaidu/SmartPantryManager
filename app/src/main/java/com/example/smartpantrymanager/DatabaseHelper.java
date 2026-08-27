package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PANTRY = "pantry_items";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY = "expiry_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL, " +
                COLUMN_EXPIRY + " TEXT)";

        db.execSQL(createPantryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
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