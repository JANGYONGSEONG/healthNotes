package com.example.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.yongs.healthnotes.models.Performance;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-21.
 */

public class PerformanceDBHelper extends DBHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "HealthNotes";

    private static final String TABLE_PERFORMANCE = "performance";

    private static final String KEY_PERFORMANCE_PLAN_TITLE = "performance_plan_title";
    private static final String KEY_PERFORMANCE_TITLE = "performance_title";
    private static final String KEY_PERFORMANCE_CREATED_AT = "performance_created_at";
    private static final String KEY_PERFORMANCE_ORDER = "performance_order";
    private static final String KEY_PERFORMANCE_WEIGHT = "performance_weigth";
    private static final String KEY_PERFORMANCE_TIMES = "performance_times";

    String DROP_TABLE_PERFORMANCE = "DROP TABLE " + TABLE_PERFORMANCE;

    public PerformanceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public PerformanceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PerformanceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(String planTitle, String title, Date date, int order, float weight, int times){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_PERFORMANCE + " VALUES('" +
                planTitle + "', '" +
                title + "', '" +
                date + "', " +
                order + ", " +
                weight + ", " +
                times + ");"
        );
        db.close();
    }

    public ArrayList<Performance> getAll(){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_PERFORMANCE + " ORDER BY " + KEY_PERFORMANCE_ORDER +" ASC";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getAll(String planTitle,String title){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' ORDER BY " + KEY_PERFORMANCE_WEIGHT +" ASC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getAllMaximum(String planTitle, String title){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT " + KEY_PERFORMANCE_CREATED_AT + ", " +
                        " MAX(" + KEY_PERFORMANCE_WEIGHT + ")" +
                        " FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' GROUP BY " + KEY_PERFORMANCE_CREATED_AT +
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT +" DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(0)));
                performance.setWeight(cursor.getFloat(1));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getAllDateDoPerformance(String planTitle, String title){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT DISTINCT " + KEY_PERFORMANCE_CREATED_AT +" FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_WEIGHT + " IS NOT NULL"+
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT +" DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(0)));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getAll(String planTitle, String title, Date date){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date +
                        "' ORDER BY " + KEY_PERFORMANCE_ORDER +" ASC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getAll(String planTitle, String title, float weight){
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_WEIGHT + " = " + weight +
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT +" DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()) {
            do{
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            }while(cursor.moveToNext());
        }
        return performanceArrayList;
    }



    public int getLastOrder(String planTitle, String title, Date date){

        String SELECT =
                "SELECT "+ KEY_PERFORMANCE_ORDER +" FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date +
                        "' ORDER BY " + KEY_PERFORMANCE_ORDER +" DESC";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        int order = 0;
        if(cursor.moveToFirst()) {
            order = cursor.getInt(0);
        }
        return order;
    }

    public void delete(String planTitle, String title, Date date, int order){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title   + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "' AND " + KEY_PERFORMANCE_ORDER + " = " + order + ";");
        db.close();
    }

    public void delete(String planTitle, String title, Date date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title  + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "';");
        db.close();
    }

    public void deletePlanTitleIsNull(String title, Date date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " IS NULL"  +
                " AND " + KEY_PERFORMANCE_TITLE + " = '" + title  + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "';");
        db.close();
    }

    public void update(String planTitle, String title, Date date, int weight, int times, int set){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PERFORMANCE + " SET " +
                KEY_PERFORMANCE_WEIGHT + " = " + weight + ", " +
                KEY_PERFORMANCE_TIMES + " = " + times +
                " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title  + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "' AND " + KEY_PERFORMANCE_ORDER + " = " + set + ";"
        );
        db.close();
    }

}