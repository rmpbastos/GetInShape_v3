package com.example.getinshape_v3;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.getinshape_v3.Utils.DataBaseFoodHelper;

import java.text.SimpleDateFormat;

public class SearchFragment extends Fragment {

    private TextView foodTextView, servingSizeTextView, calorieTextView,
            dateTextView;

    private EditText editText;

    private Button search_button, add_button;

    private String query;

    DataBaseFoodHelper dataBaseFoodHelper;

    String food_name;
    double serving_size_g;
    double calories;
    long millis;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public interface OnFragmentInteractionListener {
        //TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}