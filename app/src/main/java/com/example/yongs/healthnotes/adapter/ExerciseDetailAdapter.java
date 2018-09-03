package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.Exercise;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-16.
 */

public class ExerciseDetailAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<Exercise> exerciseArrayList;

    private boolean checkBoxState = false;

    ExerciseDetailHolder holder;

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

            holder.exercisePartImage = (ImageView)convertView.findViewById(R.id.exercisePartImage);
            holder.detailTitle = (TextView)convertView.findViewById(R.id.detailTitle);
            holder.totalSet = (TextView)convertView.findViewById(R.id.totalSet);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.exerciseDetailCheckbox);


            switch(exerciseArrayList.get(position).getExercisePart()){
                case 0:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.chest));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 1:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.back));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 2:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.shoulder));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 3:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.trapezius));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 4:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hip));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 5:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.thigh));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 6:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.calf));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 7:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.waist));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 8:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.forearm));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 9:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.abs));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 10:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.biceps));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                case 11:
                    holder.exercisePartImage.setImageDrawable(context.getResources().getDrawable(R.drawable.triceps));
                    holder.exercisePartImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    break;
                default:
                    break;
            }


            convertView.setTag(holder);
        }else{
            holder = (ExerciseDetailHolder) convertView.getTag();
        }

        holder.detailTitle.setText(String.valueOf(exerciseArrayList.get(position).getDetailTitle()));
        holder.totalSet.setText(String.valueOf(exerciseArrayList.get(position).getTotalSet()));


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

    public class ExerciseDetailHolder {
        public ImageView exercisePartImage;
        public TextView detailTitle;
        public TextView totalSet;
        public CheckBox checkBox;
    }
}
