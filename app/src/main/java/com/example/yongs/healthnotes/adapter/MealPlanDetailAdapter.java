package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.MealPlan;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-11.
 */

public class MealPlanDetailAdapter extends BaseAdapter {

    Context context;
    public static ArrayList<MealPlan> mealPlanArrayList;

    MealPlanDetailHolder holder;

    public MealPlanDetailAdapter(Context context, ArrayList<MealPlan> mealPlanArrayList) {
        this.context = context;
        this.mealPlanArrayList = mealPlanArrayList;
    }

    @Override
    public int getCount() {
        return mealPlanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealPlanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_plan_detail_item,null);

            holder = new MealPlanDetailHolder();
            holder.order = (TextView)convertView.findViewById(R.id.order);
            holder.food = (TextView)convertView.findViewById(R.id.food);
            holder.calorie = (TextView)convertView.findViewById(R.id.calorie);
            holder.carbohydrate = (TextView)convertView.findViewById(R.id.carbohydrate);
            holder.protein = (TextView)convertView.findViewById(R.id.protein);
            holder.fat = (TextView)convertView.findViewById(R.id.fat);

            convertView.setTag(holder);
        }else{
            holder = (MealPlanDetailHolder) convertView.getTag();
        }

        holder.order.setText(String.valueOf(mealPlanArrayList.get(position).getOrder()));
        holder.food.setText(mealPlanArrayList.get(position).getFood());
        holder.calorie.setText(String.valueOf(mealPlanArrayList.get(position).getCalorie()));
        holder.carbohydrate.setText(String.valueOf(mealPlanArrayList.get(position).getCarbohydrate()));
        holder.protein.setText(String.valueOf(mealPlanArrayList.get(position).getProtein()));
        holder.fat.setText(String.valueOf(mealPlanArrayList.get(position).getFat()));

        return convertView;
    }

    public class MealPlanDetailHolder {
        public TextView order;
        public TextView food;
        public TextView calorie;
        public TextView carbohydrate;
        public TextView protein;
        public TextView fat;
    }
}
