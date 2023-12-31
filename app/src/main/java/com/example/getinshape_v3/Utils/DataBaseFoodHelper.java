package com.example.getinshape_v3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.getinshape_v3.FoodModel.FoodModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseFoodHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "FoodData.db";
    public static final String TABLE_NAME = "userDiary";

    public DataBaseFoodHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(LOCAL_DATE_TIME INTEGER PRIMARY KEY, EMAIL TEXT, " +
                "FOOD_NAME TEXT, SERVING_SIZE_G REAL, CALORIES REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert data to table
    public Boolean insertFoodData(FoodModel foodModel) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LOCAL_DATE_TIME", foodModel.getLocalDateTime());
        contentValues.put("EMAIL", foodModel.getEmail());
        contentValues.put("FOOD_NAME", foodModel.getFoodName());
        contentValues.put("SERVING_SIZE_G", foodModel.getServingSize());
        contentValues.put("CALORIES", foodModel.getCalories());
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean deleteFoodData(long localDateTime) {
        db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "LOCAL_DATE_TIME=?", new String[]{String.valueOf(localDateTime)});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Get all food to be used by the recycler view
    public ArrayList<FoodModel> getAllFood(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<FoodModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, "EMAIL =?", new String[]{String.valueOf(email)},
                    null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        FoodModel food = new FoodModel();
                        food.setLocalDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("LOCAL_DATE_TIME")));
                        food.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")));
                        food.setFoodName(cursor.getString(cursor.getColumnIndexOrThrow("FOOD_NAME")));
                        food.setServingSize(cursor.getDouble(cursor.getColumnIndexOrThrow("SERVING_SIZE_G")));
                        food.setCalories(cursor.getDouble(cursor.getColumnIndexOrThrow("CALORIES")));
                        modelList.add(food);

                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    //Get all food to be used by the recycler view - Only for the current day
    public ArrayList<FoodModel> getAllFoodFromCurrentDay(String email) {
//        db = this.getWritableDatabase();
        db = this.getReadableDatabase();
        Cursor cursor = null;
        ArrayList<FoodModel> modelList = new ArrayList<>();

        //Current date in milliseconds
        long currentMillis = System.currentTimeMillis();

        // Calculate the start and end time of the current day in milliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfDayMillis = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endOfDayMillis = calendar.getTimeInMillis();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, "LOCAL_DATE_TIME >= ? AND LOCAL_DATE_TIME <= ? AND EMAIL = ?",
                    new String[]{String.valueOf(startOfDayMillis), String.valueOf(endOfDayMillis), String.valueOf(email)}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        FoodModel food = new FoodModel();
                        food.setLocalDateTime(cursor.getLong(cursor.getColumnIndexOrThrow("LOCAL_DATE_TIME")));
                        food.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")));
                        food.setFoodName(cursor.getString(cursor.getColumnIndexOrThrow("FOOD_NAME")));
                        food.setServingSize(cursor.getDouble(cursor.getColumnIndexOrThrow("SERVING_SIZE_G")));
                        food.setCalories(cursor.getDouble(cursor.getColumnIndexOrThrow("CALORIES")));
                        modelList.add(food);

                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            cursor.close();
        }
        return modelList;
    }

    public Cursor getUserFoodData(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }


    public Cursor getCalorieIntake(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(CALORIES) FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }
}
