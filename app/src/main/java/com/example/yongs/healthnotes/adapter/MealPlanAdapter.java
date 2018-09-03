package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.MealPlan;

import java.util.ArrayList;

/**
     * Created by yongs on 2018-07-13.
     */

    public class MealPlanAdapter extends BaseAdapter {
        Context context;
        public static ArrayList<MealPlan> mealPlanArrayList;

        private boolean checkBoxState = false;

        MealPlanHolder holder;

    public MealPlanAdapter(Context context, ArrayList<MealPlan> mealPlanArrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_plan_item,null);

            holder = new MealPlanHolder();
            holder.totalCalorie = (TextView)convertView.findViewById(R.id.totalCalorie);
            holder.totalCarbohydrate = (TextView)convertView.findViewById(R.id.totalCarbohydrate);
            holder.totalProtein = (TextView)convertView.findViewById(R.id.totalProtein);
            holder.totalFat = (TextView)convertView.findViewById(R.id.totalFat);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.mealPlanCheckbox);

            convertView.setTag(holder);
        }else{
            holder = (MealPlanHolder) convertView.getTag();
        }

        holder.totalCalorie.setText(String.valueOf(mealPlanArrayList.get(position).getCalorie()));
        holder.totalCarbohydrate.setText(String.valueOf(mealPlanArrayList.get(position).getCarbohydrate()));
        holder.totalProtein.setText(String.valueOf(mealPlanArrayList.get(position).getProtein()));
        holder.totalFat.setText(String.valueOf(mealPlanArrayList.get(position).getFat()));
        holder.date.setText(String.valueOf(mealPlanArrayList.get(position).getDate()));

        if(mealPlanArrayList.get(position).isSelected()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setClickable(false);
        holder.checkBox.setFocusable(false);

        if(checkBoxState) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void setCheckBoxState(boolean checkBoxState){
        this.checkBoxState = checkBoxState;
        notifyDataSetChanged();
    }

    public boolean getCheckBoxState(){
        return checkBoxState;
    }

    public void resetCheckBox() {
        for(int i=0;i<mealPlanArrayList.size();i++) {
            mealPlanArrayList.get(i).setSelected(false);
        }
        holder.checkBox.setChecked(false);
    }

    public int getCountSelectedCheckBox() {
        int count = 0;
        for(int i=0;i<mealPlanArrayList.size();i++) {
            if(mealPlanArrayList.get(i).isSelected()){
                count++;
            }
        }
        return count;
    }

    public class MealPlanHolder {
        public TextView totalCalorie;
        public TextView totalCarbohydrate;
        public TextView totalProtein;
        public TextView totalFat;
        public TextView date;
        public CheckBox checkBox;
    }

}