package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.Exercise;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-15.
 */

public class ExercisePlanAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<Exercise> exerciseArrayList;

    private boolean checkBoxState = false;

    ExercisePlanHolder holder;

    public ExercisePlanAdapter(Context context, ArrayList<Exercise> exerciseArrayList) {
        this.context = context;
        this.exerciseArrayList = exerciseArrayList;
    }

    @Override
    public int getCount() {
        return exerciseArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return exerciseArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.exercise_plan_item,null);

            holder = new ExercisePlanHolder();
            holder.planTitle = (TextView)convertView.findViewById(R.id.planTitle);
            holder.detailDescribe = (TextView)convertView.findViewById(R.id.detailDescribe);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.exercisePlanCheckbox);

            convertView.setTag(holder);
        }else{
            holder = (ExercisePlanHolder) convertView.getTag();
        }

        holder.planTitle.setText(String.valueOf(exerciseArrayList.get(position).getPlanTitle()));
        holder.detailDescribe.setText(String.valueOf(exerciseArrayList.get(position).getDetailDescribe()));

        if(exerciseArrayList.get(position).isSelected()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setClickable(false);
        holder.checkBox.setFocusable(false);

        if(checkBoxState) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
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
        for(int i=0;i<exerciseArrayList.size();i++) {
            exerciseArrayList.get(i).setSelected(false);
        }
        holder.checkBox.setChecked(false);
    }

    public int getCountSelectedCheckBox() {
        int count = 0;
        for(int i=0;i<exerciseArrayList.size();i++) {
            if(exerciseArrayList.get(i).isSelected()){
                count++;
            }
        }
        return count;
    }

    public class ExercisePlanHolder {
        public TextView planTitle;
        public TextView detailDescribe;
        public CheckBox checkBox;
    }
}
