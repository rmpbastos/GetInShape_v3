package com.example.getinshape_v3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getinshape_v3.Adapter.FoodAdapter;
import com.example.getinshape_v3.FoodModel.FoodModel;
import com.example.getinshape_v3.Utils.DataBaseFoodHelper;
import com.example.getinshape_v3.Utils.DataBaseUserHelper;

import java.util.ArrayList;

public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {

    private FoodAdapter adapter;

    private TextView calorie_targetTV, calories_eaten_todayTV, calories_remainingTV;

    private String currentUserEmail, calorie_target_str, calories_eaten_str;

    int itemCount;

    DataBaseFoodHelper dataBaseFoodHelper;
    DataBaseUserHelper dataBaseUserHelper;

    Context context;

    public RecyclerViewTouchHelper(FoodAdapter adapter, Context context, String email,
                                   TextView caloriesEatenTodayTV, TextView calorieTargetTV,
                                   TextView caloriesRemainingTV) {
        super(0, ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.context = context;
        currentUserEmail = email;
        calorie_targetTV = calorieTargetTV;
        calories_eaten_todayTV = caloriesEatenTodayTV;
        calories_remainingTV = caloriesRemainingTV;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        final int position = viewHolder.getAdapterPosition();
        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
        builder.setTitle("Delete Entry?");
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteFood(position);



                dataBaseFoodHelper = new DataBaseFoodHelper(context);
                dataBaseUserHelper = new DataBaseUserHelper(context);

                //Update the calorie count
                try {

                    loadCalorieIntake(currentUserEmail);

                    loadRecommendedIntake(currentUserEmail);

                    double calories_remaining = Double.parseDouble(calorie_target_str) - Double.parseDouble(calories_eaten_str);
                    String calories_remaining_str = String.format("%.2f", calories_remaining);
                    calories_remainingTV.setText(calories_remaining_str);
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity().getApplicationContext(), "Oops, your diary is empty!", Toast.LENGTH_LONG).show();
                }

                //Update the consumed calorie count to zero when there is no more cards left in the recycler view
                itemCount = adapter.getItemCount();
                if(itemCount <= 0) {
                    calories_eaten_str = "0";
                    calories_eaten_todayTV.setText(String.format("%.2f", Double.parseDouble(calories_eaten_str)));
                    double calories_remaining = Double.parseDouble(calorie_target_str) - Double.parseDouble(calories_eaten_str);
                    String calories_remaining_str = String.format("%.2f", calories_remaining);
                    calories_remainingTV.setText(calories_remaining_str);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.notifyItemChanged(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadCalorieIntake(String email) {
        //Retrieve the data from the database
        Cursor result = dataBaseFoodHelper.getCalorieIntake(email);

        while (result.moveToNext()) {
            calories_eaten_str = result.getString(0);
            calories_eaten_todayTV.setText(String.format("%.2f", Double.parseDouble(calories_eaten_str)));
        }
    }

    private void loadRecommendedIntake(String email) {
        //Retrieve the data from the database
        Cursor result = dataBaseUserHelper.getUserRecommendedIntake(email);

        while (result.moveToNext()) {
            calorie_targetTV.setText(result.getString(0));
            calorie_target_str = result.getString(0);
        }
    }




}
