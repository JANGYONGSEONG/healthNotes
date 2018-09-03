package com.example.yongs.healthnotes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;


/**
 * Created by yongs on 2018-06-25.
 */

public class BodyComposition implements Parcelable {

    private float weight;
    private float muscleMass;
    private float bodyFatMass;
    private float percentBodyFat;
    private Date date;
    boolean selected;

    public BodyComposition() {
        this.weight = -1;
        this.muscleMass = -1;
        this.bodyFatMass = -1;
        this.percentBodyFat = -1;
        this.date = null;
        this.selected = false;
    }

    public BodyComposition(float weight, float muscleMass, float bodyFatMass, float percentBodyFat, Date date, boolean selected) {
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFatMass = bodyFatMass;
        this.percentBodyFat = percentBodyFat;
        this.date = date;
        this.selected = selected;
    }

    protected BodyComposition(Parcel in) {
        weight = in.readFloat();
        muscleMass = in.readFloat();
        bodyFatMass = in.readFloat();
        percentBodyFat = in.readFloat();
        date  = Date.valueOf(in.readString());
    }



    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(float muscleMass) {
        this.muscleMass = muscleMass;
    }

    public float getBodyFatMass() {
        return bodyFatMass;
    }

    public void setBodyFatMass(float bodyFatMass) {
        this.bodyFatMass = bodyFatMass;
    }

    public float getPercentBodyFat() {
        return percentBodyFat;
    }

    public void setPercentBodyFat(float percentBodyFat) {
        this.percentBodyFat = percentBodyFat;
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

    public boolean checkInput() {
        if(getWeight() == -1 )
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(weight);
        dest.writeFloat(muscleMass);
        dest.writeFloat(bodyFatMass);
        dest.writeFloat(percentBodyFat);
        dest.writeString(String.valueOf(date));
    }

    public static final Parcelable.Creator<BodyComposition> CREATOR = new Parcelable.Creator<BodyComposition>() {
        @Override
        public BodyComposition createFromParcel(Parcel in) {
            return new BodyComposition(in);
        }

        @Override
        public BodyComposition[] newArray(int size) {
            return new BodyComposition[size];
        }
    };
}