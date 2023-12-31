package com.example.getinshape_v3;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getinshape_v3.Utils.DataBaseUserHelper;

import java.text.SimpleDateFormat;

public class HomeFragment extends Fragment {

    private EditText ageEditText, heightEditText, weightEditText;
    private Spinner genderSpinner, activityLevelSpinner, objectiveSpinner;
    private Button saveButton;
    private TextView homeTitle, homeGreeting, homeBmi, homeBmiMessage;
    private ImageButton logoutButton;

    String userGender, userActivityLevel, userObjective, greeting;
    int userAge, userHeight;
    double userWeight, bmi, recommendedCalorieIntake;

    //Total Daily Energy Expenditure
    double tdee;

    String currentUserEmail;

    DataBaseUserHelper dataBaseUserHelper;

    int currentUserAge, currentUserHeight;
    double currentUserWeight, currentUserBmi;
    String currentUserGender, currentUserActivityLevel, currentUserObjective;

    long millis;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();
        currentUserEmail = b.getString("currentUserEmail");

        //Initialize dataBaseUserHelper
        dataBaseUserHelper = new DataBaseUserHelper(getActivity().getApplicationContext());

        ageEditText = (EditText) getView().findViewById(R.id.age_editText);
        heightEditText = (EditText) getView().findViewById(R.id.height_editText);
        weightEditText = (EditText) getView().findViewById(R.id.weight_editText);
        genderSpinner = (Spinner) getView().findViewById(R.id.gender_spinner);
        activityLevelSpinner = (Spinner) getView().findViewById(R.id.activity_level_spinner);
        objectiveSpinner = (Spinner) getView().findViewById(R.id.objective_spinner);
        saveButton = (Button) getView().findViewById(R.id.save_button);
        homeTitle = (TextView) getView().findViewById(R.id.home_title);
        homeGreeting = (TextView) getView().findViewById(R.id.home_greeting);
        homeBmi = (TextView) getView().findViewById(R.id.home_bmi);
        homeBmiMessage = (TextView) getView().findViewById(R.id.home_bmi_message);
        logoutButton = (ImageButton) getView().findViewById(R.id.logout_button);

        // Load the user information if it exists
        try {
            currentUserAge = getUserAge(currentUserEmail);
            if (currentUserAge > 0) {

                currentUserHeight = getUserHeight(currentUserEmail);
                currentUserWeight = getUserWeight(currentUserEmail);
                currentUserGender = getUserGender(currentUserEmail);
                currentUserActivityLevel = getUserActivityLevel(currentUserEmail);
                currentUserObjective = getUserObjective(currentUserEmail);

                homeTitle.setText("Here is your current information");
                ageEditText.setText(String.valueOf(currentUserAge));
                heightEditText.setText(String.valueOf(currentUserHeight));
                weightEditText.setText(String.valueOf(currentUserWeight));

                ArrayAdapter<String> genderAdapter = (ArrayAdapter<String>) genderSpinner.getAdapter();
                int genderIndex = genderAdapter.getPosition(currentUserGender);
                genderSpinner.setSelection(genderIndex);
                ArrayAdapter<String> levelAdapter = (ArrayAdapter<String>) activityLevelSpinner.getAdapter();
                int levelIndex = levelAdapter.getPosition(currentUserActivityLevel);
                activityLevelSpinner.setSelection(levelIndex);
                ArrayAdapter<String> objectiveAdapter = (ArrayAdapter<String>) objectiveSpinner.getAdapter();
                int objectiveIndex = objectiveAdapter.getPosition(currentUserObjective);
                objectiveSpinner.setSelection(objectiveIndex);

                currentUserBmi = getUserBmi(currentUserEmail);
                homeBmi.setText("Your Body Mass Index (BMI) is " + String.format("%.2f", currentUserBmi));

                if(currentUserBmi < 18.5) {
                    homeBmiMessage.setText("You are underweight. \nTry to keep your BMI between 18.5 and 24.9");
                } else if (currentUserBmi > 24.9) {
                    homeBmiMessage.setText("You are overweight. \nTry to keep your BMI between 18.5 and 24.9");
                } else {
                    homeBmiMessage.setText("Way to go! \nTry to maintain your BMI between 18.5 and 24.9");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Call the greeting function
        try {
            greeting(currentUserEmail);
            homeGreeting.setText("Hello, " + greeting + "!");

        } catch (Exception e) {
            e.printStackTrace();
            homeGreeting.setText("Hello!");
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userAge = Integer.parseInt(ageEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your age", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    userHeight = Integer.parseInt(heightEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    userWeight = Double.parseDouble(weightEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return;
                }

                userGender = genderSpinner.getSelectedItem().toString();
                if (genderSpinner.getSelectedItemPosition() == 1) {
                    userGender = "Female";
                } else {
                    userGender = "Male";
                }

                userActivityLevel = activityLevelSpinner.getSelectedItem().toString();
                if(activityLevelSpinner.getSelectedItemPosition() == 1) {
                    userActivityLevel = "Sedentary";
                } else if (activityLevelSpinner.getSelectedItemPosition() == 2) {
                    userActivityLevel = "Lightly Active";
                } else if (activityLevelSpinner.getSelectedItemPosition() == 3) {
                    userActivityLevel = "Moderately Active";
                } else if (activityLevelSpinner.getSelectedItemPosition() == 4) {
                    userActivityLevel = "Very Active";
                } else if (activityLevelSpinner.getSelectedItemPosition() == 5) {
                    userActivityLevel = "Extremely Active";
                }

                userObjective = objectiveSpinner.getSelectedItem().toString();
                if(objectiveSpinner.getSelectedItemPosition() == 1) {
                    userObjective = "Maintain Weight";
                } else if(objectiveSpinner.getSelectedItemPosition() == 2) {
                    userObjective = "Lose Weight";
                } else if (objectiveSpinner.getSelectedItemPosition() == 3) {
                    userObjective = "Gain Weight";
                }

                calculateRecommendedCalorieIntake();
                bmi = calculateBmi(userHeight, userWeight);

                millis = System.currentTimeMillis();


                //Insert user details into the database and move to the search tab
                if (genderSpinner.getSelectedItemPosition() == 0 || activityLevelSpinner.getSelectedItemPosition() == 0 ||
                        objectiveSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all the fields above", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean checkInsertData = dataBaseUserHelper.insertUserDetails(millis, currentUserEmail, userAge, userHeight,
                            userWeight, userGender, userActivityLevel, userObjective, recommendedCalorieIntake, bmi);
                    if (checkInsertData == true) {
                        Toast.makeText(getActivity().getApplicationContext(), "New entry inserted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Sorry, entry not inserted.", Toast.LENGTH_SHORT).show();
                    }
                    openSearchFragment();
                }
            }
        });

        //Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserEmail = "";
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                Toast.makeText(getActivity().getApplicationContext(), "See you soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double getUserBmi(String email) {
        Cursor result = dataBaseUserHelper.getUserBmi(email);
        while (result.moveToNext()) {
            bmi = result.getDouble(0);
        }
        return bmi;
    }

    private int getUserAge(String email) {
        Cursor result = dataBaseUserHelper.getUserAge(email);
        while (result.moveToNext()) {
            currentUserAge = result.getInt(0);
        }
        return currentUserAge;
    }

    private int getUserHeight(String email) {
        Cursor result = dataBaseUserHelper.getUserHeight(email);
        while (result.moveToNext()) {
            currentUserHeight = result.getInt(0);
        }
        return currentUserHeight;
    }

    private double getUserWeight(String email) {
        Cursor result = dataBaseUserHelper.getUserWeight(email);
        while (result.moveToNext()) {
            currentUserWeight = result.getDouble(0);
        }
        return currentUserWeight;
    }

    private String getUserGender(String email) {
        Cursor result = dataBaseUserHelper.getUserGender(email);
        while (result.moveToNext()) {
            currentUserGender = result.getString(0);
        }
        return currentUserGender;
    }

    private String getUserActivityLevel(String email) {
        Cursor result = dataBaseUserHelper.getUserActivityLevel(email);
        while (result.moveToNext()) {
            currentUserActivityLevel = result.getString(0);
        }
        return currentUserActivityLevel;
    }

    private String getUserObjective(String email) {
        Cursor result = dataBaseUserHelper.getUserObjective(email);
        while (result.moveToNext()) {
            currentUserObjective = result.getString(0);
        }
        return currentUserObjective;
    }


    public void calculateRecommendedCalorieIntake() {

        //Basal Metabolic Rate
        double bmr;

        //TDEE for a female user
        if(userGender.equals("Female")) {
            bmr = 655 + (9.6 * userWeight) + (1.8 * userHeight) - (4.7 * userAge);
        } else {
            bmr = 66 + (13.7 * userWeight) + (5 * userHeight) - (6.8 * userAge);
        }

        if(userActivityLevel.equals("Sedentary")) {
            tdee = bmr * 1.2;
        } else if (userActivityLevel.equals("Lightly Active")) {
            tdee = bmr * 1.375;
        } else if (userActivityLevel.equals("Moderately Active")) {
            tdee = bmr * 1.55;
        } else if (userActivityLevel.equals("Very Active")) {
            tdee = bmr * 1.725;
        } else {
            tdee = bmr * 1.9;
        }

        if(userObjective.equals("Maintain")) {
            recommendedCalorieIntake = tdee;
        } else if (userObjective.equals("Lose")) {
            recommendedCalorieIntake = tdee * 0.8;
        } else {
            recommendedCalorieIntake = tdee * 1.15;
        }
    }

    public double calculateBmi(double userHeight, double userWeight) {
        bmi = userWeight / Math.pow(userHeight/ 100, 2);
        return bmi;
    }

    public void openSearchFragment() {
        Fragment fragment = new SearchFragment();
        FragmentChangeListener fragmentChangeListener = (FragmentChangeListener) getActivity();
        fragmentChangeListener.replaceFragment(fragment);
    }

    public void greeting(String email) {
        int index = email.indexOf("@");
        greeting = email.substring(0, index);
    }

    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}