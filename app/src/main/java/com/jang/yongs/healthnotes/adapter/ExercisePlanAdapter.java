package com.jang.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.model.Exercise;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-15.
 */

public class ExercisePlanAdapter extends HealthNotesAdapter {
    private Context context;
    public static ArrayList<Exercise> exerciseArrayList;

    private ExercisePlanHolder holder;

    private CheckBoxState checkBoxState = new CheckBoxState(false);

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
            holder.exerciseDescribe = (TextView)convertView.findViewById(R.id.exerciseDescribe);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.exercisePlanCheckbox);

            convertView.setTag(holder);
        }else{
            holder = (ExercisePlanHolder) convertView.getTag();
        }

        holder.planTitle.setText(String.valueOf(exerciseArrayList.get(position).getPlanTitle()));
        holder.exerciseDescribe.setText(String.valueOf(exerciseArrayList.get(position).getExerciseDescribe()));

        setCheckBoxVisibility(position);

        return convertView;
    }
    public void setCheckBoxVisibility(int position){
        super.setCheckBoxVisibility(checkBoxState, holder.checkBox, exerciseArrayList, position);
    }

    public void setCheckBoxState(boolean checkBoxState) {
        super.setCheckBoxState(this,this.checkBoxState,checkBoxState);
    }

    public boolean getCheckBoxState() {
        return super.getCheckBoxState(checkBoxState.currentState);
    }

    public  void resetCheckBox() {
        super.resetCheckBox(holder.checkBox, exerciseArrayList);
    }

    public int getCountSelectedCheckBox() {
        return super.getCountSelectedCheckBox(exerciseArrayList);
    }

    private class ExercisePlanHolder{
        private TextView planTitle;
        private TextView exerciseDescribe;
        private CheckBox checkBox;
    }
}
