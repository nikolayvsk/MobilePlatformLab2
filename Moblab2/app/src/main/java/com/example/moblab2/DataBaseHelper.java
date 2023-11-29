package com.example.moblab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "cars";
    private static final String ID = "id";
    private static final String BRAND = "brand";
    private static final String BODY_TYPE = "body_type";
    private static final String COLOR = "color";
    private static final String ENGINE_CAPACITY = "engine_capacity";
    private static final String PRICE = "price";

    private static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BRAND + " VARCHAR(255)," +
            BODY_TYPE + " VARCHAR(255)," +
            COLOR + " VARCHAR(255)," +
            ENGINE_CAPACITY + " DOUBLE," +
            PRICE + " DOUBLE" +
            ");";

    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public List<CarModel> getCars() {
        List<CarModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME;

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int carId = cursor.getInt(0);
                    String brand = cursor.getString(cursor.getColumnIndexOrThrow(BRAND));
                    String bodyType = cursor.getString(cursor.getColumnIndexOrThrow(BODY_TYPE));
                    String color = cursor.getString(cursor.getColumnIndexOrThrow(COLOR));
                    double engineCapacity = cursor.getDouble(cursor.getColumnIndexOrThrow(ENGINE_CAPACITY));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE));

                    CarModel newCar = new CarModel(carId, brand, bodyType, color, engineCapacity, price);
                    returnList.add(newCar);
                } while (cursor.moveToNext());
            }
        }

        return returnList;
    }

    public boolean addCar(CarModel carModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BRAND, carModel.getBrand());
        cv.put(BODY_TYPE, carModel.getBodyType());
        cv.put(COLOR, carModel.getColor());
        cv.put(ENGINE_CAPACITY, carModel.getEngineCapacity());
        cv.put(PRICE, carModel.getPrice());

        try {
            db.insertOrThrow(TABLE_NAME, null, cv);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public double getAvgEngineCapacity() {
        String queryString = "SELECT AVG(" + ENGINE_CAPACITY + ") FROM " + TABLE_NAME;
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                return cursor.getDouble(0);
            } else {
                return 0;
            }
        }
    }

    public List<CarModel> getSelectedCars() {
        List<CarModel> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                BODY_TYPE + "='Universal' AND " +
                COLOR + "='Red'";

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int carId = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
                    String brand = cursor.getString(cursor.getColumnIndexOrThrow(BRAND));
                    String bodyType = cursor.getString(cursor.getColumnIndexOrThrow(BODY_TYPE));
                    String color = cursor.getString(cursor.getColumnIndexOrThrow(COLOR));
                    double engineCapacity = cursor.getDouble(cursor.getColumnIndexOrThrow(ENGINE_CAPACITY));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow(PRICE));

                    CarModel newCar = new CarModel(carId, brand, bodyType, color, engineCapacity, price);
                    returnList.add(newCar);
                } while (cursor.moveToNext());
            }
        }

        return returnList;
    }

    public void deleteCars(String brand) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(TABLE_NAME, BRAND + "=? ", new String[]{brand});
        }
    }
}