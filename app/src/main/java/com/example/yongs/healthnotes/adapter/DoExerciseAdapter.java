package com.example.yongs.healthnotes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yongs.healthnotes.R;
import com.example.yongs.healthnotes.models.Performance;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-26.
 */

public class DoExerciseAdapter extends BaseAdapter {
    Context context;
    public static ArrayList<Performance> performanceArrayList;

    public DoExerciseAdapter(Context context, ArrayList<Performance> performanceArrayList){
        this.context = context;
        this.performanceArrayList = performanceArrayList;
    }

    @Override
    public int getCount() {
        return performanceArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return performanceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DoExerciseHolder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.do_exercise_item,null);

            holder = new DoExerciseHolder();
            holder.order = (TextView)convertView.findViewById(R.id.order);
            holder.weight = (TextView)convertView.findViewById(R.id.weight);
            holder.times = (TextView)convertView.findViewById(R.id.times);

            convertView.setTag(holder);
        }else{
            holder = (DoExerciseHolder) convertView.getTag();
        }

        holder.order.setText(String.valueOf(performanceArrayList.get(position).getOrder()));
        holder.weight.setText(String.valueOf(performanceArrayList.get(position).getWeight()));
        holder.times.setText(String.valueOf(performanceArrayList.get(position).getTimes()));

        return convertView;
    }

    public class DoExerciseHolder {
        public TextView order;
        public TextView weight;
        public TextView times;
    }
}