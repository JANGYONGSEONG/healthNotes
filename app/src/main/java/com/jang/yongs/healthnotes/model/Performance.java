package com.jang.yongs.healthnotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

public class Performance extends HealthNotesModels implements Parcelable {

    private String title;
    private int order;
    private float weight;
    private int times;
    private Date date;

    public Performance() {
        title = null;
        order = 0;
        weight = -1;
        times = -1;
        date = null;
    }

    public Performance(String title, int order, float weight, int times, Date date) {
        this.title = title;
        this.order = order;
        this.weight = weight;
        this.times = times;
        this.date = date;
    }

    protected Performance(Parcel in){
        title = in.readString();
        order = in.readInt();
        weight = in.readFloat();
        times = in.readInt();
        date = Date.valueOf(in.readString());
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean checkInput(){
        if(getWeight()==-1
                ||getTimes()==-1)
            return false;
        return true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(order);
        dest.writeFloat(weight);
        dest.writeInt(times);
        dest.writeString(String.valueOf(date));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Performance> CREATOR = new Creator<Performance>() {
        @Override
        public Performance createFromParcel(Parcel in) {
            return new Performance(in);
        }

        @Override
        public Performance[] newArray(int size) {
            return new Performance[size];
        }
    };
}
