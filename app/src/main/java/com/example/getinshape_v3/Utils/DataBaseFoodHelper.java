package com.example.getinshape_v3.Utils;

import android.content.ContentValues;
import android.content.Context;
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

//    public Boolean insertFoodData(long localDateTime, String email, String foodName,
//                                  double servingSize, double calories) {
//        db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("LOCAL_DATE_TIME", localDateTime);
//        contentValues.put("EMAIL", email);
//        contentValues.put("FOOD_NAME", foodName);
//        contentValues.put("SERVING_SIZE_G", servingSize);
//        contentValues.put("CALORIES", calories);
//        long result = db.insert(TABLE_NAME, null, contentValues);
//
//        if (result == -1) {
//            return false;
//        } else {
//            return true;
//        }
//    }

}
