package com.example.yongs.healthnotes.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongs on 2018-08-14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "HealthNotes";


    private static final String TABLE_BODYCOMPOSITION = "bodycomposition";
    private static final String KEY_BODYCOMPOSITION_CREATED_AT = "created_at";
    private static final String KEY_BODYCOMPOSITION_WEIGHT = "weight";
    private static final String KEY_BODYCOMPOSITION_MUSCLEMASS = "musclemass";
    private static final String KEY_BODYCOMPOSITION_BODYFATMASS = "bodyfatmass";
    private static final String KEY_BODYCOMPOSITION_PERCENTBODYFAT = "percentbodyfat";
    String DROP_TABLE_BODYCOMPOSITION = "DROP TABLE " + TABLE_BODYCOMPOSITION;



    private static final String TABLE_MEALPLAN = "mealplan";
    private static final String KEY_MEALPLAN_CREATED_AT = "created_at";
    private static final String KEY_MEALPLAN_ORDER = "meal_order";
    private static final String KEY_MEALPLAN_FOOD = "food";
    private static final String KEY_MEALPLAN_CALORIE = "calorie";
    private static final String KEY_MEALPLAN_CARBOHYDRATE = "carbohydrate";
    private static final String KEY_MEALPLAN_PROTEIN = "protein";
    private static final String KEY_MEALPLAN_FAT = "fat";
    String DROP_TABLE_MEALPLAN ="DROP TABLE " + TABLE_MEALPLAN;



    private static final String TABLE_EXERCISE = "exercise";
    private static final String KEY_EXERCISE_ID = "id";
    private static final String KEY_EXERCISE_PLAN_TITLE = "title";
    private static final String KEY_EXERCISE_EXERCISEPART = "exercisepart";
    private static final String KEY_EXERCISE_DETAIL_TITLE = "detailtitle";
    private static final String KEY_EXERCISE_TOTAL_SET = "totalset";
    String DROP_TABLE_EXERCISE = "DROP TABLE " + TABLE_EXERCISE;



    private static final String TABLE_PERFORMANCE = "performance";
    private static final String KEY_PERFORMANCE_PLAN_TITLE = "performance_plan_title";
    private static final String KEY_PERFORMANCE_TITLE = "performance_title";
    private static final String KEY_PERFORMANCE_CREATED_AT = "performance_created_at";
    private static final String KEY_PERFORMANCE_ORDER = "performance_order";
    private static final String KEY_PERFORMANCE_WEIGHT = "performance_weigth";
    private static final String KEY_PERFORMANCE_TIMES = "performance_times";
    String DROP_TABLE_PERFORMANCE = "DROP TABLE " + TABLE_PERFORMANCE;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BODYCOMPOSITION =
                "CREATE TABLE " + TABLE_BODYCOMPOSITION + "(" +
                        KEY_BODYCOMPOSITION_CREATED_AT + " DATETIME NOT NULL, " +
                        KEY_BODYCOMPOSITION_WEIGHT + " REAL NOT NULL, " +
                        KEY_BODYCOMPOSITION_MUSCLEMASS + " REAL, " +
                        KEY_BODYCOMPOSITION_BODYFATMASS + " REAL, " +
                        KEY_BODYCOMPOSITION_PERCENTBODYFAT + " REAL, " +
                        "PRIMARY KEY(" + KEY_BODYCOMPOSITION_CREATED_AT + ")" +
                        ");";

        String CREATE_TABLE_MEALPLAN =
                "CREATE TABLE " + TABLE_MEALPLAN + "(" +
                        KEY_MEALPLAN_CREATED_AT + " DATETIME NOT NULL, " +
                        KEY_MEALPLAN_ORDER + " INTEGER NOT NULL, " +
                        KEY_MEALPLAN_FOOD + " VARCHAR2(4000) NOT NULL, " +
                        KEY_MEALPLAN_CALORIE + " INTEGER NOT NULL, " +
                        KEY_MEALPLAN_CARBOHYDRATE + " INTEGER NOT NULL, " +
                        KEY_MEALPLAN_PROTEIN + " INTEGER NOT NULL, " +
                        KEY_MEALPLAN_FAT + " INTEGER NOT NULL, " +
                        "PRIMARY KEY(" + KEY_MEALPLAN_CREATED_AT + "," + KEY_MEALPLAN_ORDER + ")" +
                        ");";

        String CREATE_TABLE_EXERCISE =
                "CREATE TABLE " + TABLE_EXERCISE + "(" +
                        KEY_EXERCISE_ID + " INTEGER AUTO_INCREMENT ," +
                        KEY_EXERCISE_PLAN_TITLE + " VARCHAR2(4000), " +
                        KEY_EXERCISE_EXERCISEPART + " INTEGER NOT NULL, " +
                        KEY_EXERCISE_DETAIL_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_EXERCISE_TOTAL_SET + " INTEGER NOT NULL, " +
                        "PRIMARY KEY(" + KEY_EXERCISE_ID + ")"+
                        ");";

        String CREATE_TABLE_PERFORMANCE =
                "CREATE TABLE " + TABLE_PERFORMANCE + "(" +
                        KEY_PERFORMANCE_PLAN_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_PERFORMANCE_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_PERFORMANCE_CREATED_AT + " DATETIME NOT NULL, " +
                        KEY_PERFORMANCE_ORDER + " INTEGER NOT NULL, " +
                        KEY_PERFORMANCE_WEIGHT + " REAL NOT NULL, " +
                        KEY_PERFORMANCE_TIMES + " INTEGER NOT NULL, " +
                        "PRIMARY KEY(" + KEY_PERFORMANCE_CREATED_AT + "," + KEY_PERFORMANCE_PLAN_TITLE + "," + KEY_PERFORMANCE_TITLE + "," + KEY_PERFORMANCE_ORDER + ")"+
                        ");";

        db.execSQL(CREATE_TABLE_BODYCOMPOSITION);
        db.execSQL(CREATE_TABLE_MEALPLAN);
        db.execSQL(CREATE_TABLE_EXERCISE);
        db.execSQL(CREATE_TABLE_PERFORMANCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
