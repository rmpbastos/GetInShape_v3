package com.example.getinshape_v3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getinshape_v3.Adapter.FoodAdapter;
import com.example.getinshape_v3.FoodModel.FoodModel;
import com.example.getinshape_v3.Utils.DataBaseFoodHelper;
import com.example.getinshape_v3.Utils.DataBaseUserHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiaryFragment extends Fragment implements onDialogCloseListener {

    RecyclerView mRecyclerView;
    DataBaseFoodHelper dataBaseFoodHelper;
    DataBaseUserHelper dataBaseUserHelper;

    FoodAdapter foodAdapter;
    ArrayList<FoodModel> mList;

    Context context;

    private TextView calorie_targetTV, calories_eaten_todayTV, calories_remainingTV, currentDateTV;

    private String currentUserEmail, calorie_target_str, calories_eaten_str;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        currentUserEmail = b.getString("currentUserEmail");



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        dataBaseFoodHelper = new DataBaseFoodHelper(getActivity());
        dataBaseUserHelper = new DataBaseUserHelper(getActivity());
        mList = new ArrayList<>();

        context = requireContext();
        foodAdapter = new FoodAdapter(dataBaseFoodHelper, context);

        //Get all food from the current day
        mList = dataBaseFoodHelper.getAllFoodFromCurrentDay(currentUserEmail);
//        mList = dataBaseFoodHelper.getAllFood(currentUserEmail);
        foodAdapter.setFood(mList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(foodAdapter);


        currentDateTV = (TextView) getView().findViewById(R.id.currentDateTV);
        calories_eaten_todayTV = (TextView) getView().findViewById(R.id.calories_eaten_todayTV);
        calorie_targetTV = (TextView) getView().findViewById(R.id.calorie_targetTV);
        calories_remainingTV = (TextView) getView().findViewById(R.id.calories_remainingTV);

        try {

            // Get the current date
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
            String currentDate = dateFormat.format(calendar.getTime());
            currentDateTV.setText(currentDate);

            loadCalorieIntake(currentUserEmail);

            loadRecommendedIntake(currentUserEmail);

            double calories_remaining = Double.parseDouble(calorie_target_str) - Double.parseDouble(calories_eaten_str);
            String calories_remaining_str = String.format("%.2f", calories_remaining);
            calories_remainingTV.setText(calories_remaining_str);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Oops, your diary is empty!", Toast.LENGTH_LONG).show();
        }

        //Add reference to RecyclerViewTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(foodAdapter, context,
                currentUserEmail, calories_eaten_todayTV, calorie_targetTV, calories_remainingTV));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void loadCalorieIntake(String email) {
        //Retrieve the data from the database
        Cursor result = dataBaseFoodHelper.getCalorieIntake(email);

        while (result.moveToNext()) {
            calories_eaten_str = result.getString(0);
            calories_eaten_todayTV.setText(String.format("%.2f kcal", Double.parseDouble(calories_eaten_str)));
//            calories_eaten_todayTV.setText(result.getString(0));
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

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        //Get all food from current day
        mList = dataBaseFoodHelper.getAllFoodFromCurrentDay(currentUserEmail);
//        mList = dataBaseFoodHelper.getAllFood(currentUserEmail);
        foodAdapter.setFood(mList);
        foodAdapter.notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}