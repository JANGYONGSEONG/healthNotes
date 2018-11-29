package com.jang.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.model.Exercise;

import java.util.ArrayList;



/**
 * Created by yongs on 2018-08-16.
 */

public class ExerciseDetailAdapter extends HealthNotesAdapter {
    private Context context;
    public static ArrayList<Exercise> exerciseArrayList;

    private ExerciseDetailHolder holder;

    private CheckBoxState checkBoxState = new CheckBoxState(false);

    public ExerciseDetailAdapter(Context context, ArrayList<Exercise> exerciseArrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.exercise_detail_item,null);

            holder = new ExerciseDetailHolder();

            holder.exerciseImage = (ImageView)convertView.findViewById(R.id.exerciseImage);
            holder.exerciseTitle = (TextView)convertView.findViewById(R.id.exerciseTitle);
            holder.totalSet = (TextView)convertView.findViewById(R.id.totalSet);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.exerciseDetailCheckbox);

            holder.exerciseImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            switch(exerciseArrayList.get(position).getExercisePart()){
                case Chest:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.chest));
                    break;
                case Back:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.back));
                    break;
                case Shoulder:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.shoulder));
                    break;
                case Trapezius:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.trapezius));
                    break;
                case Hip:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hip));
                    break;
                case Thigh:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.thigh));
                    break;
                case Calf:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.calf));
                    break;
                case Waist:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.waist));
                    break;
                case Forearm:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.forearm));
                    break;
                case Abs:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.abs));
                    break;
                case Biceps:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.biceps));
                    break;
                case Triceps:
                    holder.exerciseImage.setImageDrawable(context.getResources().getDrawable(R.drawable.triceps));
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ExerciseDetailHolder) convertView.getTag();
        }

        holder.exerciseTitle.setText(String.valueOf(exerciseArrayList.get(position).getExerciseTitle()));
        holder.totalSet.setText(String.valueOf(exerciseArrayList.get(position).getTotalSet()));

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

    private class ExerciseDetailHolder{
        private ImageView exerciseImage;
        private TextView exerciseTitle;
        private TextView totalSet;
        private CheckBox checkBox;
    }
}
