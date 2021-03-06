package com.jang.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.jang.yongs.healthnotes.model.BodyComposition;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-14.
 */

public class BodyCompositionDBHelper extends HealthNotesDBHelper {
    private static final int DATABASE_VERSION = HealthNotesDBHelper.DATABASE_VERSION;

    private static final String DATABASE_NAME = HealthNotesDBHelper.DATABASE_NAME;

    private static final String TABLE_BODYCOMPOSITION = HealthNotesDBHelper.TABLE_BODYCOMPOSITION;

    private static final String KEY_CREATED_AT = HealthNotesDBHelper.KEY_BODYCOMPOSITION_CREATED_AT;
    private static final String KEY_WEIGHT = HealthNotesDBHelper.KEY_BODYCOMPOSITION_WEIGHT;
    private static final String KEY_MUSCLEMASS = HealthNotesDBHelper.KEY_BODYCOMPOSITION_MUSCLEMASS;
    private static final String KEY_BODYFATMASS = HealthNotesDBHelper.KEY_BODYCOMPOSITION_BODYFATMASS;
    private static final String KEY_BODYFATPERCENT = HealthNotesDBHelper.KEY_BODYCOMPOSITION_BODYFATPERCENT;

    private static final String DROP_TABLE_BODYCOMPOSITION = HealthNotesDBHelper.DROP_TABLE_BODYCOMPOSITION;

    public BodyCompositionDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BodyCompositionDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BodyCompositionDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db,oldVersion,newVersion);
    }

    public void insertBodyComposition(float weight, float muscleMass, float bodyFatMass, float bodyFatPercent, Date date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_BODYCOMPOSITION + " VALUES('" +
                date + "', " +
                weight + ", " +
                muscleMass + ", " +
                bodyFatMass + ", " +
                bodyFatPercent + ");"
        );
        db.close();
    }

    public ArrayList<BodyComposition> getBodyCompositionList() {
        ArrayList<BodyComposition> bodyCompositionList = new ArrayList<BodyComposition>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_BODYCOMPOSITION + " ORDER BY " + KEY_CREATED_AT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                BodyComposition bodyComposition = new BodyComposition();
                bodyComposition.setDate(Date.valueOf(cursor.getString(0)));
                bodyComposition.setWeight(cursor.getFloat(1));
                bodyComposition.setMuscleMass(cursor.getFloat(2));
                bodyComposition.setBodyFatMass(cursor.getFloat(3));
                bodyComposition.setBodyFatPercent(cursor.getFloat(4));
                bodyCompositionList.add(bodyComposition);
            }while(cursor.moveToNext());
        }
        return bodyCompositionList;
    }

    public ArrayList<BodyComposition> getBodyCompositionSearchedDate(Date date){
        ArrayList<BodyComposition> bodyCompositionArrayList = new ArrayList<BodyComposition>();

        String SELECT = "SELECT * FROM " + TABLE_BODYCOMPOSITION +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToNext()) {
            do{
                BodyComposition bodyComposition = new BodyComposition();
                bodyComposition.setDate(Date.valueOf(cursor.getString(0)));
                bodyComposition.setWeight(cursor.getFloat(1));
                bodyComposition.setMuscleMass(cursor.getFloat(2));
                bodyComposition.setBodyFatMass(cursor.getFloat(3));
                bodyComposition.setBodyFatPercent(cursor.getFloat(4));
                bodyCompositionArrayList.add(bodyComposition);
            }while(cursor.moveToNext());
        }
        return bodyCompositionArrayList;
    }

    public BodyComposition getBodyCompositionSelectedDate(Date date){
        BodyComposition bodyComposition = null;

        String SELECT = "SELECT * FROM " + TABLE_BODYCOMPOSITION +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToNext()) {
            do{
                bodyComposition = new BodyComposition();
                bodyComposition.setDate(Date.valueOf(cursor.getString(0)));
                bodyComposition.setWeight(cursor.getFloat(1));
                bodyComposition.setMuscleMass(cursor.getFloat(2));
                bodyComposition.setBodyFatMass(cursor.getFloat(3));
                bodyComposition.setBodyFatPercent(cursor.getFloat(4));
            }while(cursor.moveToNext());
        }
        return bodyComposition;
    }

    public void deleteBodyCompositionSelectedDate(Date date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BODYCOMPOSITION + " WHERE " + KEY_CREATED_AT + " = '" + date + "';");
        db.close();
    }

    public void updateBodyCompositionSelectedDate(float weight, float muscleMass, float bodyFatMass, float bodyFatPercent, Date date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BODYCOMPOSITION + " SET " +
                KEY_WEIGHT + " = " + weight + ", " +
                KEY_MUSCLEMASS + " = " + muscleMass + ", " +
                KEY_BODYFATMASS + " = " + bodyFatMass + ", " +
                KEY_BODYFATPERCENT + " = " + bodyFatPercent +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "';"
        );
        db.close();
    }

}
