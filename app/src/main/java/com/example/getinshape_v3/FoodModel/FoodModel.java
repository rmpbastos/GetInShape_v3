package com.example.getinshape_v3.FoodModel;

public class FoodModel {

    private long localDateTime;
    private String email, foodName;
    private double servingSize, calories;

    public long getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(long localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
