package com.jang.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.model.MealPlan;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-07-13.
 */

public class MealPlanAdapter extends HealthNotesAdapter {
    private Context context;
    public static ArrayList<MealPlan> mealPlanArrayList;

    private MealPlanHolder holder;

    private CheckBoxState checkBoxState = new CheckBoxState(false);

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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_plan_item, null);

            holder = new MealPlanHolder();
            holder.totalCalorie = (TextView) convertView.findViewById(R.id.totalCalorie);
            holder.totalCarbohydrate = (TextView) convertView.findViewById(R.id.totalCarbohydrate);
            holder.totalProtein = (TextView) convertView.findViewById(R.id.totalProtein);
            holder.totalFat = (TextView) convertView.findViewById(R.id.totalFat);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.mealPlanCheckbox);

            convertView.setTag(holder);
        } else {
            holder = (MealPlanHolder) convertView.getTag();
        }

        holder.totalCalorie.setText(String.valueOf(mealPlanArrayList.get(position).getCalorie()));
        holder.totalCarbohydrate.setText(String.valueOf(mealPlanArrayList.get(position).getCarbohydrate()));
        holder.totalProtein.setText(String.valueOf(mealPlanArrayList.get(position).getProtein()));
        holder.totalFat.setText(String.valueOf(mealPlanArrayList.get(position).getFat()));
        holder.date.setText(String.valueOf(mealPlanArrayList.get(position).getDate()));

        setCheckBoxVisibility(position);

        return convertView;
    }

    public void setCheckBoxVisibility(int position){
        super.setCheckBoxVisibility(checkBoxState, holder.checkBox, mealPlanArrayList, position);
    }

    public void setCheckBoxState(boolean checkBoxState) {
        super.setCheckBoxState(this,this.checkBoxState,checkBoxState);
    }

    public boolean getCheckBoxState() {
        return super.getCheckBoxState(checkBoxState.currentState);
    }

    public  void resetCheckBox() {
        super.resetCheckBox(holder.checkBox, mealPlanArrayList);
    }

    public int getCountSelectedCheckBox() {
        return super.getCountSelectedCheckBox(mealPlanArrayList);
    }

    private class MealPlanHolder{
        private TextView totalCalorie;
        private TextView totalCarbohydrate;
        private TextView totalProtein;
        private TextView totalFat;
        private TextView date;
        private CheckBox checkBox;
    }
}