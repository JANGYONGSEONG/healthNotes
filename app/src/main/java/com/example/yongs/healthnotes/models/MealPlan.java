package com.example.yongs.healthnotes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by yongs on 2018-06-25.
 */

public class MealPlan implements Parcelable {

    private int order;
    private String food;
    private int calorie;
    private int carbohydrate;
    private int protein;
    private int fat;
    private Date date;
    boolean selected;


    public MealPlan() {
        this.order = -1;
        this.food = null;
        this.calorie = -1;
        this.carbohydrate = -1;
        this.protein = -1;
        this.fat = -1;
        this.date = null;
        this.selected = false;
    }

    public MealPlan(int order, String food, int calorie, int carbohydrate, int protein, int fat, Date date, boolean selected) {

        this.order = order;
        this.food = food;
        this.calorie = calorie;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.date = date;
        this.selected = selected;

    }

    protected MealPlan(Parcel in) {

        order = in.readInt();
        food = in.readString();
        calorie = in.readInt();
        carbohydrate = in.readInt();
        protein = in.readInt();
        fat = in.readInt();
        date = Date.valueOf(in.readString());
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getFood() { return food; }

    public void setFood(String food) { this.food = food; }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int totalCalorie) {
        this.calorie = totalCalorie;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public boolean checkInput(){
        if(getFood()==null
                ||getCalorie()==-1
                ||getCarbohydrate()==-1
                ||getProtein()==-1
                ||getFat()==-1)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order);
        dest.writeString(food);
        dest.writeInt(calorie);
        dest.writeInt(carbohydrate);
        dest.writeInt(protein);
        dest.writeInt(fat);
        dest.writeString(String.valueOf(date));
    }

    public static final Parcelable.Creator<MealPlan> CREATOR = new Parcelable.Creator<MealPlan>() {
        @Override
        public MealPlan createFromParcel(Parcel in) {
            return new MealPlan(in);
        }

        @Override
        public MealPlan[] newArray(int size) {
            return new MealPlan[size];
        }
    };


}
