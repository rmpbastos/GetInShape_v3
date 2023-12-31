package com.example.getinshape_v3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseUserHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserData.db";
    public static final String TABLE_NAME = "userDetails";

    SQLiteDatabase db;


    public DataBaseUserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(LOCAL_DATE_TIME INTEGER PRIMARY KEY, EMAIL TEXT, " +
                "USER_AGE INTEGER, USER_HEIGHT INTEGER, USER_WEIGHT REAL, USER_GENDER TEXT,USER_ACTIVITY_LEVEL TEXT, " +
                "USER_OBJECTIVE TEXT, RECOMMENDED_CALORIE_INTAKE REAL, BMI REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    //Insert data to table
    public Boolean insertUserDetails(long timestamp, String email, int age, int height, double weight, String gender,
                                     String activityLevel, String objective, double recommendedCalorieIntake, double bmi) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LOCAL_DATE_TIME", timestamp);
        contentValues.put("EMAIL", email);
        contentValues.put("USER_AGE", age);
        contentValues.put("USER_HEIGHT", height);
        contentValues.put("USER_WEIGHT", weight);
        contentValues.put("USER_GENDER", gender);
        contentValues.put("USER_ACTIVITY_LEVEL", activityLevel);
        contentValues.put("USER_OBJECTIVE", objective);
        contentValues.put("RECOMMENDED_CALORIE_INTAKE", recommendedCalorieIntake);
        contentValues.put("BMI", bmi);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getUserRecommendedIntake(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT RECOMMENDED_CALORIE_INTAKE FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserBmi(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT BMI FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }


    public Cursor getUserAge(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_AGE FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserHeight(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_HEIGHT FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserWeight(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_WEIGHT FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserGender(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_GENDER FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserActivityLevel(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_ACTIVITY_LEVEL FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }

    public Cursor getUserObjective(String email) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT USER_OBJECTIVE FROM " + TABLE_NAME + " WHERE EMAIL =?",
                new String[]{String.valueOf(email)});
        return cursor;
    }


}
