package com.jang.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.jang.yongs.healthnotes.model.Performance;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-21.
 */

public class PerformanceDBHelper extends HealthNotesDBHelper {

    private static final int DATABASE_VERSION = HealthNotesDBHelper.DATABASE_VERSION;

    private static final String DATABASE_NAME = HealthNotesDBHelper.DATABASE_NAME;

    private static final String TABLE_PERFORMANCE = HealthNotesDBHelper.TABLE_PERFORMANCE;

    private static final String KEY_PERFORMANCE_PLAN_TITLE = HealthNotesDBHelper.KEY_PERFORMANCE_PLAN_TITLE;
    private static final String KEY_PERFORMANCE_TITLE = HealthNotesDBHelper.KEY_PERFORMANCE_TITLE;
    private static final String KEY_PERFORMANCE_CREATED_AT = HealthNotesDBHelper.KEY_PERFORMANCE_CREATED_AT;
    private static final String KEY_PERFORMANCE_ORDER = HealthNotesDBHelper.KEY_PERFORMANCE_ORDER;
    private static final String KEY_PERFORMANCE_WEIGHT = HealthNotesDBHelper.KEY_PERFORMANCE_WEIGHT;
    private static final String KEY_PERFORMANCE_TIMES = HealthNotesDBHelper.KEY_PERFORMANCE_TIMES;
    private static final String KEY_PERFORMANCE_EXERCISE_ID = HealthNotesDBHelper.KEY_PERFORMANCE_EXERCISE_ID;

    private static final String DROP_TABLE_PERFORMANCE = HealthNotesDBHelper.DROP_TABLE_PERFORMANCE;

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
        super.onUpgrade(db,oldVersion,newVersion);
    }

    public void insertPerformance(String planTitle, String title, Date date, int order, float weight, int times, int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_PERFORMANCE + " VALUES('" +
                planTitle + "', '" +
                title + "', '" +
                date + "', " +
                order + ", " +
                weight + ", " +
                times + ", " +
                id + ");"
        );
        db.close();
    }


    public ArrayList<Performance> getPerformance(String planTitle, String title) {
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' ORDER BY " + KEY_PERFORMANCE_WEIGHT + " ASC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            } while (cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getPerformanceMaximum(String planTitle, String title) {
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT " + KEY_PERFORMANCE_CREATED_AT + ", " +
                        " MAX(" + KEY_PERFORMANCE_WEIGHT + ")" +
                        " FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' GROUP BY " + KEY_PERFORMANCE_CREATED_AT +
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT + " DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(0)));
                performance.setWeight(cursor.getFloat(1));
                performanceArrayList.add(performance);
            } while (cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getPerformanceDateList(String planTitle, String title) {
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT DISTINCT " + KEY_PERFORMANCE_CREATED_AT + " FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_WEIGHT + " IS NOT NULL" +
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT + " DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(0)));
                performanceArrayList.add(performance);
            } while (cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getPerformanceSelectedDate(String planTitle, String title, Date date) {
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date +
                        "' ORDER BY " + KEY_PERFORMANCE_ORDER + " ASC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            } while (cursor.moveToNext());
        }
        return performanceArrayList;
    }

    public ArrayList<Performance> getPerformanceSelectedWeight(String planTitle, String title, float weight) {
        ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();

        String SELECT_ALL =
                "SELECT * FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_WEIGHT + " = " + weight +
                        " ORDER BY " + KEY_PERFORMANCE_CREATED_AT + " DESC;";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if (cursor.moveToFirst()) {
            do {
                Performance performance = new Performance();
                performance.setDate(Date.valueOf(cursor.getString(2)));
                performance.setOrder(cursor.getInt(3));
                performance.setWeight(cursor.getFloat(4));
                performance.setTimes(cursor.getInt(5));
                performanceArrayList.add(performance);
            } while (cursor.moveToNext());
        }
        return performanceArrayList;
    }


    public int getLastOrder(String planTitle, String title, Date date) {

        String SELECT =
                "SELECT " + KEY_PERFORMANCE_ORDER + " FROM " + TABLE_PERFORMANCE +
                        " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                        "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +
                        "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date +
                        "' ORDER BY " + KEY_PERFORMANCE_ORDER + " DESC";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        int order = 0;
        if (cursor.moveToFirst()) {
            order = cursor.getInt(0);
        }
        return order;
    }

    public void deletePerformanceSelectedOrder(String planTitle, String title, Date date, int order) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "' AND " + KEY_PERFORMANCE_ORDER + " = " + order + ";");
        db.close();
    }

    public void deletePerformanceSelectedPlan(String planTitle) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle + "';");
        db.close();
    }

    public void deletePerformanceSelectedExercise(String planTitle, String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERFORMANCE + " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title +  "';");
        db.close();
    }

    public void updatePerformance(String planTitle, String title, Date date, int weight, int times, int set) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_PERFORMANCE + " SET " +
                KEY_PERFORMANCE_WEIGHT + " = " + weight + ", " +
                KEY_PERFORMANCE_TIMES + " = " + times +
                " WHERE " + KEY_PERFORMANCE_PLAN_TITLE + " = '" + planTitle +
                "' AND " + KEY_PERFORMANCE_TITLE + " = '" + title + "' AND " + KEY_PERFORMANCE_CREATED_AT + " = '" + date + "' AND " + KEY_PERFORMANCE_ORDER + " = " + set + ";"
        );
        db.close();
    }

}