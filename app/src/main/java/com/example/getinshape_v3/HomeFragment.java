package com.example.getinshape_v3;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.getinshape_v3.Utils.DataBaseUserHelper;

public class HomeFragment extends Fragment {

    private EditText ageEditText, heightEditText, weightEditText;
    private Spinner genderSpinner, activityLevelSpinner, objectiveSpinner;
    private Button saveButton;

    String userGender, userActivityLevel, userObjective;
    int userAge, userHeight;
    double userWeight;

    //Total Daily Energy Expenditure
    double tdee;

    //Recommended calorie intake
    double recommendedCalorieIntake;

    //TODO: DELETE AFTER TESTS - HARDCODED VARIABLE
    String currentUserEmail = "rafabastos@email.com";

    DataBaseUserHelper dataBaseUserHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}