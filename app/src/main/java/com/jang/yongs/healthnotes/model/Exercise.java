package com.jang.yongs.healthnotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jang.yongs.healthnotes.common.Common;

public class Exercise extends HealthNotesModels implements Parcelable {

    private int id;
    private String planTitle;
    private String exerciseDescribe;

    private Common.ExercisePart exercisePart;
    private String exerciseTitle;
    private int totalSet;

    private boolean selected;

    public Exercise() {

        id = 0;
        planTitle = null;
        exerciseDescribe = null;

        exercisePart = null;
        exerciseTitle = null;
        totalSet = 0;

        selected = false;
    }

    public Exercise(int id,String planTitle, String exerciseDescribe, Common.ExercisePart exercisePart, String exerciseTitle, int totalSet, boolean selected) {
        this.id = id;
        this.planTitle = planTitle;
        this.exerciseDescribe = exerciseDescribe;

        this.exercisePart = exercisePart;
        this.exerciseTitle = exerciseTitle;
        this.totalSet = totalSet;

        this.selected = selected;
    }

    protected Exercise(Parcel in) {
        id = in.readInt();
        planTitle = in.readString();
        exerciseDescribe = in.readString();
        exercisePart = Common.ExercisePart.valueOf(in.readString());
        exerciseTitle = in.readString();
        totalSet = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getExerciseDescribe() {
        return exerciseDescribe;
    }

    public void setExerciseDescribe(String detailDescribe) {
        this.exerciseDescribe = detailDescribe;
    }

    public Common.ExercisePart getExercisePart() {
        return exercisePart;
    }

    public void setExercisePart(Common.ExercisePart exercisePart) {
        this.exercisePart = exercisePart;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public int getTotalSet() {
        return totalSet;
    }

    public void setTotalSet(int totalSet) {
        this.totalSet = totalSet;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean checkPlanTitle() {
        if(getPlanTitle() == null)
            return false;
        return true;
    }

    public boolean checkExerciseSelect() {
        if(getExercisePart() == null
                || getExerciseTitle() == null
                ||getTotalSet() == 0)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(planTitle);
        dest.writeString(exerciseDescribe);
        dest.writeString(String.valueOf(exercisePart));
        dest.writeString(exerciseTitle);
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
