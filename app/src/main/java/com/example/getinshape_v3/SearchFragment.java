package com.example.getinshape_v3;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getinshape_v3.FoodModel.FoodModel;
import com.example.getinshape_v3.Utils.DataBaseFoodHelper;
import com.example.getinshape_v3.Utils.DataBaseUserHelper;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {

    private TextView foodTextView, servingSizeTextView, calorieTextView,
            dateTextView;

    private EditText editText;

    private Button search_button, add_button;

    private String query;

    DataBaseFoodHelper dataBaseFoodHelper;

    DataBaseUserHelper dataBaseUserHelper;

    String currentUserEmail;
    String food_name;
    double serving_size_g;
    double calories;
    long millis;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    FoodModel foodModel;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = getActivity().getIntent();
        Bundle b = intent.getExtras();

        currentUserEmail = b.getString("currentUserEmail");

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        foodTextView = (TextView) getView().findViewById(R.id.food_textView);
        servingSizeTextView = (TextView) getView().findViewById(R.id.serving_size_textView);
        calorieTextView = (TextView) getView().findViewById(R.id.calorie_textView);
//        dateTextView = (TextView) getView().findViewById(R.id.date_textView);

        editText = (EditText) getView().findViewById(R.id.food_editText);

        search_button = (Button) getView().findViewById(R.id.food_button);
        add_button = (Button) getView().findViewById(R.id.add_button);


        dataBaseFoodHelper = new DataBaseFoodHelper(getActivity().getApplicationContext());
        dataBaseUserHelper = new DataBaseUserHelper(getActivity().getApplicationContext());

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get input from user
                query = editText.getText().toString();
                fetchFood(query);

            }
        });
    }

    public void fetchFood(String query) {

        //URL provided by the API
        String url = "https://api.calorieninjas.com/v1/nutrition?query=" + query;

        //Request a String response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        foodModel = displayFood(response);

                        //Add food to diary
                        add_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //Check if user has entered the information in the home page
                                Cursor cursor = dataBaseUserHelper.getUserBmi(currentUserEmail);
                                if (cursor != null && cursor.getCount() > 0) {

                                    //Insert foodModel to database
                                    Boolean checkInsertData = dataBaseFoodHelper.insertFoodData(foodModel);
                                    if (checkInsertData == true) {
                                        Toast.makeText(getActivity().getApplicationContext(), "New entry inserted!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity().getApplicationContext(), "Sorry, entry not inserted.", Toast.LENGTH_LONG).show();
                                    }

                                    //Open DiaryFragment
                                    openDiaryFragment();
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "Please enter you personal information in the home page before adding food to your diary",
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                foodTextView.setText("We got an error, please try again");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("X-Api-Key", "OaSfrBrLm0b+NODkh1e1Ug==hcj7Tnm2ZmJ5gDWN");
                return header;
            }
        };

        //Add the request to the Request Queue
        queue.add(stringRequest);

    }

    private FoodModel displayFood(String response) {

        //Create a FoodModel object
        FoodModel foodItem = new FoodModel();

        try {
            //String manipulation
            response = response.replace("{\"items\": [{", "");
            response = response.replace("}]}", "");

            //Create a food Array
            String[] foodArray = new String[12];

            //Split the string by commas
            String elements[] = response.split(",");

            //Iterate the elements and add the values to the array
            for (int i = 0; i < elements.length; i++) {
                //Split the data by colon to separate keys and values
                String splitElements[] = elements[i].split(":");
                String value = splitElements[1].trim();
                foodArray[i] = value;
            }

            food_name = foodArray[0];
            serving_size_g = Double.parseDouble(foodArray[2]);
            calories = Double.parseDouble(foodArray[1]);
            millis = System.currentTimeMillis();

            //Populate the FoodModel object
            foodItem.setLocalDateTime(millis);
            foodItem.setEmail(currentUserEmail);
            foodItem.setFoodName(food_name);
            foodItem.setServingSize(serving_size_g);
            foodItem.setCalories(calories);

            //Display data
            foodTextView.setText(StringUtils.capitalize(food_name.replace("\"", "")));
            servingSizeTextView.setText(String.valueOf(serving_size_g) + " g");
            calorieTextView.setText(String.valueOf(calories) + " kcal");
//            Date resultDate = new Date(millis);
//            dateTextView.setText(sdf.format(resultDate));


        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "Sorry, food not found!\nPlease try again.", Toast.LENGTH_LONG).show();
        }

        return foodItem;

    }

    public void openDiaryFragment() {
        Fragment fragment = new DiaryFragment();
        FragmentChangeListener fragmentChangeListener = (FragmentChangeListener) getActivity();
        fragmentChangeListener.replaceFragment(fragment);
    }

    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}