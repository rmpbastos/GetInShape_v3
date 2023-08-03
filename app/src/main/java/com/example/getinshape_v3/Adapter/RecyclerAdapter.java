package com.example.getinshape_v3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getinshape_v3.MainActivity;
import com.example.getinshape_v3.R;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;

//    MainActivity activity;
    private ArrayList food_id, serving_id, calories_id, timestamp_id;

    long millis;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    public RecyclerAdapter(Context context, ArrayList food_id, ArrayList serving_id, ArrayList calories_id, ArrayList timestamp_id) {
        this.context = context;
        this.food_id = food_id;
        this.serving_id = serving_id;
        this.calories_id = calories_id;
        this.timestamp_id = timestamp_id;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        holder.food_id.setText(StringUtils.capitalize(String.valueOf(food_id.get(position)).replace("\"", "")));
        holder.serving_id.setText(String.valueOf(serving_id.get(position)) + " g");
        holder.calories_id.setText(String.valueOf(calories_id.get(position)) + " kcal");
        millis = (Long)timestamp_id.get(position);
        Date resultDate = new Date(millis);
        holder.timestamp_id.setText(String.valueOf(sdf.format(resultDate)));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView food_id, serving_id, calories_id, timestamp_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            food_id = itemView.findViewById(R.id.food_name);
            serving_id = itemView.findViewById(R.id.food_serving);
            calories_id = itemView.findViewById(R.id.food_calories);
            timestamp_id = itemView.findViewById(R.id.food_timestamp);
        }

    }
}
