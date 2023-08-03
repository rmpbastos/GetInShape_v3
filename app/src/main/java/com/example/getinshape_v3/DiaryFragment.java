package com.example.getinshape_v3;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.getinshape_v3.Adapter.FoodAdapter;
import com.example.getinshape_v3.FoodModel.FoodModel;
import com.example.getinshape_v3.Utils.DataBaseFoodHelper;
import com.example.getinshape_v3.Utils.DataBaseUserHelper;

import java.util.ArrayList;

public class DiaryFragment extends Fragment implements onDialogCloseListener {

    RecyclerView mRecyclerView;
    DataBaseFoodHelper dataBaseFoodHelper;
    DataBaseUserHelper dataBaseUserHelper;

    FoodAdapter foodAdapter;
    ArrayList<FoodModel> mList;

    private TextView calorie_targetTV, calories_eaten_todayTV, calories_remainingTV;

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


        // THIS CHUNK OF CODE BELOW IS WORKING
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        dataBaseFoodHelper = new DataBaseFoodHelper(getActivity());
        dataBaseUserHelper = new DataBaseUserHelper(getActivity());
        mList = new ArrayList<>();
        foodAdapter = new FoodAdapter(dataBaseFoodHelper,  getActivity());

        mList = dataBaseFoodHelper.getAllFood();
        foodAdapter.setFood(mList);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(foodAdapter);
        // THIS CHUNK OF CODE ABOVE IS WORKING


        calories_eaten_todayTV = (TextView) getView().findViewById(R.id.calories_eaten_todayTV);
        calorie_targetTV = (TextView) getView().findViewById(R.id.calorie_targetTV);
        calories_remainingTV = (TextView) getView().findViewById(R.id.calories_remainingTV);

        try {


            loadCalorieIntake();

            loadRecommendedIntake();

            double calories_remaining = Double.parseDouble(calorie_target_str) - Double.parseDouble(calories_eaten_str);
            String calories_remaining_str = String.format("%.2f", calories_remaining);
            calories_remainingTV.setText(calories_remaining_str);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(getActivity().getApplicationContext(), "Oops, the diary is empty", Toast.LENGTH_LONG).show();
        }

    }

    private void loadCalorieIntake() {
        //Retrieve the data from the database
        Cursor result = dataBaseFoodHelper.getCalorieIntake();

        while (result.moveToNext()) {
            calories_eaten_todayTV.setText(result.getString(0));
            calories_eaten_str = result.getString(0);
        }
    }

    private void loadRecommendedIntake() {
        //Retrieve the data from the database
        Cursor result = dataBaseUserHelper.getUserRecommendedIntake();

        while (result.moveToNext()) {
            calorie_targetTV.setText(result.getString(0));
            calorie_target_str = result.getString(0);
        }
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = dataBaseFoodHelper.getAllFood();
        foodAdapter.setFood(mList);
        foodAdapter.notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}