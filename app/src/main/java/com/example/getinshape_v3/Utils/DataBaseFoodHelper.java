package com.example.getinshape_v3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.getinshape_v3.FoodModel.FoodModel;

public class DataBaseFoodHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "FoodData.db";
    public static final String TABLE_NAME = "userDiary";

    public DataBaseFoodHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(LOCAL_DATE_TIME INTEGER PRIMARY KEY, EMAIL TEXT, " +
                "FOOD_NAME TEXT, SERVING_SIZE_G REAL, CALORIES REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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

    // TODO: 'WHERE EMAIL =?'
//    public Cursor getUserFoodData(String email) {
//        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
//        return cursor;
//    }

    public Cursor getUserFoodData() {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return cursor;
    }

    // TODO: 'WHERE EMAIL =?'
//    public Cursor getCalorieIntake(String email) {
//        db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("SELECT SUM(CALORIES) FROM " + TABLE_NAME, null);
//        return cursor;
//    }

    public Cursor getCalorieIntake() {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(CALORIES) FROM " + TABLE_NAME, null);
        return cursor;
    }
}
