package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.BodyComposition;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-14.
 */

public class BodyCompositionAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<BodyComposition> bodyCompositionArrayList;

    private boolean checkBoxState = false;

    BodyCompositionHolder holder;

    public BodyCompositionAdapter(Context context, ArrayList<BodyComposition> bodyCompositionArrayList) {
        this.context = context;
        this.bodyCompositionArrayList = bodyCompositionArrayList;
    }

    @Override
    public int getCount() {
        return bodyCompositionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return bodyCompositionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.body_composition_item,null);

            holder = new BodyCompositionHolder();

            holder.weight = (TextView)convertView.findViewById(R.id.weight);
            holder.muscleMass = (TextView)convertView.findViewById(R.id.muscleMass);
            holder.bodyFatMass = (TextView)convertView.findViewById(R.id.bodyFatMass);
            holder.percentBodyFat = (TextView)convertView.findViewById(R.id.percentBodyFat);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.BodyCompositionCheckbox);

           convertView.setTag(holder);
        }else{
            holder = (BodyCompositionHolder) convertView.getTag();
        }


        holder.weight.setText(String.valueOf(bodyCompositionArrayList.get(position).getWeight()));
        if(bodyCompositionArrayList.get(position).getMuscleMass()!=-1)
            holder.muscleMass.setText(String.valueOf(bodyCompositionArrayList.get(position).getMuscleMass()));
        if(bodyCompositionArrayList.get(position).getBodyFatMass()!=-1)
            holder.bodyFatMass.setText(String.valueOf(bodyCompositionArrayList.get(position).getBodyFatMass()));
        if(bodyCompositionArrayList.get(position).getPercentBodyFat()!=-1)
            holder.percentBodyFat.setText(String.valueOf(bodyCompositionArrayList.get(position).getPercentBodyFat()));
        holder.date.setText(String.valueOf(bodyCompositionArrayList.get(position).getDate()));

        if(bodyCompositionArrayList.get(position).isSelected()){
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
        for(int i=0;i<bodyCompositionArrayList.size();i++) {
            bodyCompositionArrayList.get(i).setSelected(false);
        }
        holder.checkBox.setChecked(false);
    }

    public int getCountSelectedCheckBox() {
        int count = 0;
        for(int i=0;i<bodyCompositionArrayList.size();i++) {
            if(bodyCompositionArrayList.get(i).isSelected()){
                count++;
            }
        }
        return count;
    }

    public class BodyCompositionHolder {
        public TextView weight;
        public TextView muscleMass;
        public TextView bodyFatMass;
        public TextView percentBodyFat;
        public TextView date;
        public CheckBox checkBox;
    }
}
