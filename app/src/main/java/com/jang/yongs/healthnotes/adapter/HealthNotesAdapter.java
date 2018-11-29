package com.jang.yongs.healthnotes.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.jang.yongs.healthnotes.model.HealthNotesModels;

import java.util.ArrayList;

public class HealthNotesAdapter extends BaseAdapter {

    public class CheckBoxState {
        public boolean currentState;

        public CheckBoxState(boolean boo){
            this.currentState = boo;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public <E extends HealthNotesModels> void setCheckBoxVisibility(CheckBoxState checkBoxState, CheckBox checkBox, ArrayList<E> arrayList, int position){
        if (checkBoxState.currentState) {
            checkBox.setVisibility(View.VISIBLE);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        if (arrayList.get(position).isSelected()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setClickable(false);
        checkBox.setFocusable(false);
    }

    public <E extends HealthNotesAdapter> void setCheckBoxState(E adapter, CheckBoxState checkBoxState, boolean checkBoxStateValue) {
        checkBoxState.currentState = checkBoxStateValue;
        adapter.notifyDataSetChanged();
    }

    public boolean getCheckBoxState(boolean checkBoxState) {
        return checkBoxState;
    }

    public <E extends HealthNotesModels> void resetCheckBox(CheckBox checkBox, ArrayList<E> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setSelected(false);
        }
        checkBox.setChecked(false);
    }

    public <E extends HealthNotesModels> int getCountSelectedCheckBox(ArrayList<E> arrayList) {
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).isSelected()) {
                count++;
            }
        }
        return count;
    }
}
