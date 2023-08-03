package com.example.getinshape_v3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseUserHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "userDetails";

    DataBaseLoginHelper dataBaseLoginHelper;

    //TODO: DELETE AFTER TESTS - HARDCODED VARIABLE
    String currentUserEmail = "rafabastos@email.com";


    public DataBaseUserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(EMAIL TEXT, USER_AGE INTEGER, USER_HEIGHT INTEGER," +
                "USER_WEIGHT REAL, USER_GENDER TEXT,USER_ACTIVITY_LEVEL TEXT, USER_OBJECTIVE TEXT," +
                "RECOMMENDED_CALORIE_INTAKE REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public Boolean insertUserDetails(String email, int age, int height, double weight, String gender,
                                     String activityLevel, String objective, double recommendedCalorieIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("age", age);
        contentValues.put("height", height);
        contentValues.put("weight", weight);
        contentValues.put("gender", gender);
        contentValues.put("activity_level", activityLevel);
        contentValues.put("objective", objective);
        contentValues.put("recommended_calorie_intake", recommendedCalorieIntake);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
