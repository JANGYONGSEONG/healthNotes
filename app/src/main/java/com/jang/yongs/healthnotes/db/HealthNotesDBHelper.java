package com.jang.yongs.healthnotes.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongs on 2018-08-14.
 */

public class HealthNotesDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "HealthNotes";


    public static final String TABLE_BODYCOMPOSITION = "bodycomposition";
    public static final String KEY_BODYCOMPOSITION_CREATED_AT = "created_at";
    public static final String KEY_BODYCOMPOSITION_WEIGHT = "weight";
    public static final String KEY_BODYCOMPOSITION_MUSCLEMASS = "musclemass";
    public static final String KEY_BODYCOMPOSITION_BODYFATMASS = "bodyfatmass";
    public static final String KEY_BODYCOMPOSITION_BODYFATPERCENT = "bodyfatpercent";
    public static final String DROP_TABLE_BODYCOMPOSITION = "DROP TABLE " + TABLE_BODYCOMPOSITION;



    public static final String TABLE_MEALPLAN = "mealplan";
    public static final String KEY_MEALPLAN_CREATED_AT = "created_at";
    public static final String KEY_MEALPLAN_ORDER = "meal_order";
    public static final String KEY_MEALPLAN_FOOD = "food";
    public static final String KEY_MEALPLAN_CALORIE = "calorie";
    public static final String KEY_MEALPLAN_CARBOHYDRATE = "carbohydrate";
    public static final String KEY_MEALPLAN_PROTEIN = "protein";
    public static final String KEY_MEALPLAN_FAT = "fat";
    public static final String DROP_TABLE_MEALPLAN ="DROP TABLE " + TABLE_MEALPLAN;



    public static final String TABLE_EXERCISE = "exercise";
    public static final String KEY_EXERCISE_ID = "id";
    public static final String KEY_EXERCISE_PLAN_TITLE = "plan_title";
    public static final String KEY_EXERCISE_EXERCISEPART = "exercise_part";
    public static final String KEY_EXERCISE_EXERCISE_TITLE = "exercise_title";
    public static final String KEY_EXERCISE_TOTAL_SET = "total_set";
    public static final String DROP_TABLE_EXERCISE = "DROP TABLE " + TABLE_EXERCISE;



    public static final String TABLE_PERFORMANCE = "performance";
    public static final String KEY_PERFORMANCE_PLAN_TITLE = "performance_plan_title";
    public static final String KEY_PERFORMANCE_TITLE = "performance_title";
    public static final String KEY_PERFORMANCE_CREATED_AT = "performance_created_at";
    public static final String KEY_PERFORMANCE_ORDER = "performance_order";
    public static final String KEY_PERFORMANCE_WEIGHT = "performance_weigth";
    public static final String KEY_PERFORMANCE_TIMES = "performance_times";
    public static final String KEY_PERFORMANCE_EXERCISE_ID = "performance_exercise_id";
    public static final String DROP_TABLE_PERFORMANCE = "DROP TABLE " + TABLE_PERFORMANCE;

    public HealthNotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public HealthNotesDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public HealthNotesDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler){
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
                        KEY_BODYCOMPOSITION_BODYFATPERCENT + " REAL, " +
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
                        KEY_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        KEY_EXERCISE_PLAN_TITLE + " VARCHAR2(2000), " +
                        KEY_EXERCISE_EXERCISEPART + " VARCHAR2(4000) NOT NULL, " +
                        KEY_EXERCISE_EXERCISE_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_EXERCISE_TOTAL_SET + " INTEGER NOT NULL" +
                        ");";

        String CREATE_TABLE_PERFORMANCE =
                "CREATE TABLE " + TABLE_PERFORMANCE + "(" +
                        KEY_PERFORMANCE_PLAN_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_PERFORMANCE_TITLE + " VARCHAR2(4000) NOT NULL, " +
                        KEY_PERFORMANCE_CREATED_AT + " DATETIME NOT NULL, " +
                        KEY_PERFORMANCE_ORDER + " INTEGER NOT NULL, " +
                        KEY_PERFORMANCE_WEIGHT + " REAL NOT NULL, " +
                        KEY_PERFORMANCE_TIMES + " INTEGER NOT NULL, " +
                        KEY_PERFORMANCE_EXERCISE_ID + " INTEGER NOT NULL, " +
                        "PRIMARY KEY(" + KEY_PERFORMANCE_CREATED_AT + "," + KEY_PERFORMANCE_ORDER + "," + KEY_PERFORMANCE_EXERCISE_ID + "), "+
                        "FOREIGN KEY(" + KEY_PERFORMANCE_EXERCISE_ID+ ") REFERENCES " + TABLE_EXERCISE + "(" + KEY_EXERCISE_ID + ") " +
                        "ON DELETE CASCADE" +
                        ");";


        db.execSQL(CREATE_TABLE_BODYCOMPOSITION);
        db.execSQL(CREATE_TABLE_MEALPLAN);
        db.execSQL(CREATE_TABLE_EXERCISE);
        db.execSQL(CREATE_TABLE_PERFORMANCE);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_BODYCOMPOSITION);
        db.execSQL(DROP_TABLE_MEALPLAN);
        db.execSQL(DROP_TABLE_EXERCISE);
        db.execSQL(DROP_TABLE_PERFORMANCE);
        onCreate(db);
    }
}
