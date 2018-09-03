package com.example.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.yongs.healthnotes.models.BodyComposition;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-14.
 */

public class BodyCompositionDBHelper extends DBHelper {

    private float weight;
    private float muscleMass;
    private float bodyFatMass;
    private float percentBodyFat;
    private Date date;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "HealthNotes";

    private static final String TABLE_BODYCOMPOSITION = "bodycomposition";

    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_MUSCLEMASS = "musclemass";
    private static final String KEY_BODYFATMASS = "bodyfatmass";
    private static final String KEY_PERCENTBODYFAT = "percentbodyfat";

    String DROP_TABLE_BODYCOMPOSITION = "DROP TABLE " + TABLE_BODYCOMPOSITION;

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

    }

    public void insert(float weight, float muscleMass, float bodyFatMass, float percentBodyFat, Date date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_BODYCOMPOSITION + " VALUES('" +
                date + "', " +
                weight + ", " +
                muscleMass + ", " +
                bodyFatMass + ", " +
                percentBodyFat + ");"
        );
        db.close();
    }

    public ArrayList<BodyComposition> getAll() {
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
                bodyComposition.setPercentBodyFat(cursor.getFloat(4));
                bodyCompositionList.add(bodyComposition);
            }while(cursor.moveToNext());
        }
        return bodyCompositionList;
    }

    public ArrayList<BodyComposition> getBodyCompositionForSearch(Date date){
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
                bodyComposition.setPercentBodyFat(cursor.getFloat(4));
                bodyCompositionArrayList.add(bodyComposition);
            }while(cursor.moveToNext());
        }
        return bodyCompositionArrayList;
    }

    public BodyComposition getBodyComposition(Date date){
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
                bodyComposition.setPercentBodyFat(cursor.getFloat(4));
            }while(cursor.moveToNext());
        }
        return bodyComposition;
    }

    public void delete(Date date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BODYCOMPOSITION + " WHERE " + KEY_CREATED_AT + " = '" + date + "';");
        db.close();
    }

    public void update(float weight, float muscleMass, float bodyFatMass, float percentBodyFat, Date date){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BODYCOMPOSITION + " SET " +
                KEY_WEIGHT + " = " + weight + ", " +
                KEY_MUSCLEMASS + " = " + muscleMass + ", " +
                KEY_BODYFATMASS + " = " + bodyFatMass + ", " +
                KEY_PERCENTBODYFAT + " = " + percentBodyFat +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "';"
        );
        db.close();
    }

}
