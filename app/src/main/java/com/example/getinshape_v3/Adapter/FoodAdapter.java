package com.example.getinshape_v3.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getinshape_v3.FoodModel.FoodModel;
import com.example.getinshape_v3.MainActivity;
import com.example.getinshape_v3.R;
import com.example.getinshape_v3.Utils.DataBaseFoodHelper;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private ArrayList<FoodModel> mList = new ArrayList<>();

    private MainActivity activity;
    private DataBaseFoodHelper dataBaseFoodHelper;

    long millis;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

    Context context;

    public FoodAdapter(DataBaseFoodHelper dataBaseFoodHelper, Context context) {
        this.dataBaseFoodHelper = dataBaseFoodHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final FoodModel item = mList.get(position);
        holder.foodName.setText(StringUtils.capitalize(item.getFoodName().replace("\"", "")));
        holder.foodServing.setText(String.valueOf(item.getServingSize()) + " g");
        holder.foodCalories.setText(String.valueOf(item.getCalories()) + " kcal");
        millis = item.getLocalDateTime();
        Date resultDate = new Date(millis);
        holder.foodTimestamp.setText(String.valueOf(sdf.format(resultDate)));
    }

    public Context getContext() {
        return activity;
    }

    public void setFood(ArrayList<FoodModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteFood(int position) {
        FoodModel item = mList.get(position);
        dataBaseFoodHelper.deleteFoodData(item.getLocalDateTime());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView foodName, foodServing, foodCalories, foodTimestamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.food_name);
            foodServing = itemView.findViewById(R.id.food_serving);
            foodCalories = itemView.findViewById(R.id.food_calories);
            foodTimestamp = itemView.findViewById(R.id.food_timestamp);
        }
    }
}
