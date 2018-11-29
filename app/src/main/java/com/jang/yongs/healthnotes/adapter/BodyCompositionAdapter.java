package com.jang.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.model.BodyComposition;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-14.
 */

public class BodyCompositionAdapter extends HealthNotesAdapter {
    private Context context;
    public static ArrayList<BodyComposition> bodyCompositionArrayList;

    private BodyCompositionHolder holder;

    private CheckBoxState checkBoxState = new CheckBoxState(false);

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
            holder.bodyFatPercent = (TextView)convertView.findViewById(R.id.bodyFatPercent);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.checkBox = (CheckBox)convertView.findViewById(R.id.BodyCompositionCheckbox);

           convertView.setTag(holder);
        }else{
            holder = (BodyCompositionHolder) convertView.getTag();
        }


        holder.weight.setText(String.valueOf(bodyCompositionArrayList.get(position).getWeight()));
        if(bodyCompositionArrayList.get(position).getMuscleMass()!=-1) {
            holder.muscleMass.setText(String.valueOf(bodyCompositionArrayList.get(position).getMuscleMass()));
        }
        if(bodyCompositionArrayList.get(position).getBodyFatMass()!=-1) {
            holder.bodyFatMass.setText(String.valueOf(bodyCompositionArrayList.get(position).getBodyFatMass()));
        }
        if(bodyCompositionArrayList.get(position).getBodyFatPercent()!=-1) {
            holder.bodyFatPercent.setText(String.valueOf(bodyCompositionArrayList.get(position).getBodyFatPercent()));
        }
        holder.date.setText(String.valueOf(bodyCompositionArrayList.get(position).getDate()));

        setCheckBoxVisibility(position);

        return convertView;
    }

    public void setCheckBoxVisibility(int position){
        super.setCheckBoxVisibility(checkBoxState, holder.checkBox, bodyCompositionArrayList, position);
    }

    public void setCheckBoxState(boolean checkBoxState) {
        super.setCheckBoxState(this,this.checkBoxState,checkBoxState);
    }

    public boolean getCheckBoxState() {
        return super.getCheckBoxState(checkBoxState.currentState);
    }

    public  void resetCheckBox() {
        super.resetCheckBox(holder.checkBox, bodyCompositionArrayList);
    }

    public int getCountSelectedCheckBox() {
        return super.getCountSelectedCheckBox(bodyCompositionArrayList);
    }

    private class BodyCompositionHolder {
        private TextView weight;
        private TextView muscleMass;
        private TextView bodyFatMass;
        private TextView bodyFatPercent;
        private TextView date;
        private CheckBox checkBox;
    }
}
