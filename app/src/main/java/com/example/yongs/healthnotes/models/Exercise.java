package com.example.yongs.healthnotes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yongs on 2018-08-15.
 */

public class Exercise implements Parcelable {

    public static final int divisionNum = 12;
    final String[] division = {"trapezius", "shoulder", "chest", "triceps", "biceps", "forearm", "abs", "back", "waist", "hip", "thigh", "calf"};

    String planTitle;
    String detailDescribe;
    int exercisePart;
    String detailTitle;
    int totalSet;

    boolean selected;

    public Exercise() {
        planTitle = null;
        detailDescribe = null;

        exercisePart = -1;
        detailTitle = null;
        totalSet = 0;

        selected = false;
    }

    public Exercise(String planTitle, String detailDescribe, int exercisePart, String detailTitle, int totalSet, boolean selected) {
        this.planTitle = planTitle;
        this.detailDescribe = detailDescribe;

        this.exercisePart = exercisePart;
        this.detailTitle = detailTitle;
        this.totalSet = totalSet;

        this.selected = selected;
    }

    public String getDetailDescribe() {
        return detailDescribe;
    }

    public void setDetailDescribe(String detailDescribe) {
        this.detailDescribe = detailDescribe;
    }

    public int getExercisePart() {
        return exercisePart;
    }

    public void setExercisePart(int exercisePart) {
        this.exercisePart = exercisePart;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public int getTotalSet() {
        return totalSet;
    }

    public void setTotalSet(int totalSet) {
        this.totalSet = totalSet;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean checkPlanTitleInput() {
        if(getPlanTitle() == null)
            return false;
        return true;

    }

    public boolean checkExerciseSelectInput() {
        if(getExercisePart() == -1
                ||getDetailTitle() == null
                ||getTotalSet() == 0)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Exercise(Parcel in) {
        planTitle = in.readString();
        detailDescribe = in.readString();

        exercisePart = in.readInt();
        detailTitle = in.readString();
        totalSet = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planTitle);
        dest.writeString(detailDescribe);

        dest.writeInt(exercisePart);
        dest.writeString(detailTitle);
        dest.writeInt(totalSet);
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

}
