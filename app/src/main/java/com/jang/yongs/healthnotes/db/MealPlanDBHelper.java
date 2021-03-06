package com.jang.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.jang.yongs.healthnotes.model.MealPlan;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by yongs on 2018-07-09.
 */

public class MealPlanDBHelper extends HealthNotesDBHelper {
    private static final int DATABASE_VERSION = HealthNotesDBHelper.DATABASE_VERSION;

    private static final String DATABASE_NAME = HealthNotesDBHelper.DATABASE_NAME;

    private static final String TABLE_MEALPLAN = HealthNotesDBHelper.TABLE_MEALPLAN;

    private static final String KEY_CREATED_AT = HealthNotesDBHelper.KEY_MEALPLAN_CREATED_AT;
    private static final String KEY_ORDER = HealthNotesDBHelper.KEY_MEALPLAN_ORDER;
    private static final String KEY_FOOD = HealthNotesDBHelper.KEY_MEALPLAN_FOOD;
    private static final String KEY_CALORIE = HealthNotesDBHelper.KEY_MEALPLAN_CALORIE;
    private static final String KEY_CARBOHYDRATE = HealthNotesDBHelper.KEY_MEALPLAN_CARBOHYDRATE;
    private static final String KEY_PROTEIN = HealthNotesDBHelper.KEY_MEALPLAN_PROTEIN;
    private static final String KEY_FAT = HealthNotesDBHelper.KEY_MEALPLAN_FAT;

    private static final String DROP_TABLE_MEALPLAN =HealthNotesDBHelper.DROP_TABLE_MEALPLAN;

    public MealPlanDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MealPlanDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MealPlanDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
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

    public void insertMealPlan(int KEY_ORDER, String KEY_FOOD, int KEY_CALORIE, int KEY_CARBOHYDRATE, int KEY_PROTEIN, int KEY_FAT, Date DATE){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_MEALPLAN + " VALUES('"+
                DATE + "', '" +
                KEY_ORDER + "', '" +
                KEY_FOOD + "', '" +
                KEY_CALORIE + "', '" +
                KEY_CARBOHYDRATE + "', '" +
                KEY_PROTEIN + "', '" +
                KEY_FAT +  "');"
        );
        db.close();
    }


    public ArrayList<MealPlan> getMealPlanListSelectedDate(Date date){
        ArrayList<MealPlan> mealPlanList = new ArrayList<MealPlan>();

        String SELECT_ALL_THE_DATE = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + KEY_CREATED_AT + " = '" + date + "' ORDER BY "  + KEY_ORDER + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL_THE_DATE, null);

        if(cursor.moveToFirst()) {
            do{
                MealPlan mealPlan = new MealPlan();
                mealPlan.setDate(Date.valueOf(cursor.getString(0)));
                mealPlan.setOrder(cursor.getInt(1));
                mealPlan.setFood(cursor.getString(2));
                mealPlan.setCalorie(cursor.getInt(3));
                mealPlan.setCarbohydrate(cursor.getInt(4));
                mealPlan.setProtein(cursor.getInt(5));
                mealPlan.setFat(cursor.getInt(6));
                mealPlanList.add(mealPlan);
            }while(cursor.moveToNext());
        }
        return mealPlanList;
    }

    public ArrayList<MealPlan> getMealPlanTotalListEachDate() {
        ArrayList<MealPlan> mealPlanList = new ArrayList<MealPlan>();

        String SELECT_TOTAL = "SELECT " + KEY_CREATED_AT + ", " +
                " SUM(" + KEY_CALORIE + ") AS total_calorie," +
                " SUM(" + KEY_CARBOHYDRATE + ") AS total_carbohydrate," +
                " SUM(" + KEY_PROTEIN + ") AS total_protein," +
                " SUM(" + KEY_FAT + ") AS total_fat" +
        " FROM " + TABLE_MEALPLAN + " GROUP BY " + KEY_CREATED_AT + " ORDER BY " + KEY_CREATED_AT + " DESC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_TOTAL, null);

        if(cursor.moveToFirst()) {
            do{
                MealPlan mealPlan = new MealPlan();
                mealPlan.setDate(Date.valueOf(cursor.getString(0)));
                mealPlan.setCalorie(cursor.getInt(1));
                mealPlan.setCarbohydrate(cursor.getInt(2));
                mealPlan.setProtein(cursor.getInt(3));
                mealPlan.setFat(cursor.getInt(4));
                mealPlanList.add(mealPlan);
            }while(cursor.moveToNext());
        }

        return mealPlanList;
    }

    public ArrayList<MealPlan> getMealPlanTotalSearchedDate(Date date) {
        ArrayList<MealPlan> mealPlanList = new ArrayList<MealPlan>();

        String SELECT_TOTAL = "SELECT " + KEY_CREATED_AT + ", " +
                " SUM(" + KEY_CALORIE + ") AS total_calorie," +
                " SUM(" + KEY_CARBOHYDRATE + ") AS total_carbohydrate," +
                " SUM(" + KEY_PROTEIN + ") AS total_protein," +
                " SUM(" + KEY_FAT + ") AS total_fat" +
                " FROM " + TABLE_MEALPLAN +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "'"+
                " GROUP BY " + KEY_CREATED_AT + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_TOTAL, null);

        if(cursor.moveToFirst()) {
            do{
                MealPlan mealPlan = new MealPlan();
                mealPlan.setDate(Date.valueOf(cursor.getString(0)));
                mealPlan.setCalorie(cursor.getInt(1));
                mealPlan.setCarbohydrate(cursor.getInt(2));
                mealPlan.setProtein(cursor.getInt(3));
                mealPlan.setFat(cursor.getInt(4));
                mealPlanList.add(mealPlan);
            }while(cursor.moveToNext());
        }

        return mealPlanList;
    }

    public MealPlan getMealPlanSelectedOrder(Date date, int order){
        MealPlan  mealPlan = new MealPlan();

        String SELECT = "SELECT * FROM " + TABLE_MEALPLAN + " WHERE " + KEY_CREATED_AT + " = '" + date + "' AND " + KEY_ORDER + " = " + order + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()) {
            do{
                mealPlan.setDate(Date.valueOf(cursor.getString(0)));
                mealPlan.setOrder(cursor.getInt(1));
                mealPlan.setFood(cursor.getString(2));
                mealPlan.setCalorie(cursor.getInt(3));
                mealPlan.setCarbohydrate(cursor.getInt(4));
                mealPlan.setProtein(cursor.getInt(5));
                mealPlan.setFat(cursor.getInt(6));
            }while(cursor.moveToNext());
        }

        return mealPlan;
    }

    public void deleteMealPlanTotalListSelectedDate(Date date) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEALPLAN + " WHERE " + KEY_CREATED_AT + " = '" + date + "';");
        db.close();
    }

    public void deleteMealPlanSelectedOrder(Date date,int order) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MEALPLAN + " WHERE " + KEY_CREATED_AT + " = '" + date + "' AND " + KEY_ORDER + " = " + order + ";");
        db.close();
    }

    public void updateMealPlanOrderSelectedDate(Date date, int first, int last ) {
        SQLiteDatabase db = getWritableDatabase();
        int currentOrder = 0;
        int changedOrder = 0;
        for(int i=first; i<=last; i++) {
            currentOrder = i;
            changedOrder = currentOrder - 1;
            db.execSQL("UPDATE " + TABLE_MEALPLAN + " SET " +
                    KEY_ORDER + " = " + changedOrder +
                    " WHERE " + KEY_CREATED_AT + " = '" + date + "' AND " + KEY_ORDER + " = " + currentOrder + ";"
            );
        }
        db.close();
    }

    public void updateMealPlanSelectedOrder(String food, int calorie, int carbohydrate, int protein, int fat, Date date, int order){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_MEALPLAN + " SET " +
                KEY_FOOD + " = '" + food + "', " +
                KEY_CALORIE + " = " + calorie + ", " +
                KEY_CARBOHYDRATE + " = " + carbohydrate + ", " +
                KEY_PROTEIN + " = " + protein + ", " +
                KEY_FAT + " = " + fat +
                " WHERE " + KEY_CREATED_AT + " = '" + date + "' AND " + KEY_ORDER + " = " + order + ";"
        );
        db.close();
    }

}