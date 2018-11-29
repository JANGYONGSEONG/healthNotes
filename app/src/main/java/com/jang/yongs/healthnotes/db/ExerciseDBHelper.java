package com.jang.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.jang.yongs.healthnotes.common.Common;
import com.jang.yongs.healthnotes.model.Exercise;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-16.
 */

public class ExerciseDBHelper extends HealthNotesDBHelper {
    private static final int DATABASE_VERSION = HealthNotesDBHelper.DATABASE_VERSION;

    private static final String DATABASE_NAME = HealthNotesDBHelper.DATABASE_NAME;

    private static final String TABLE_EXERCISE = HealthNotesDBHelper.TABLE_EXERCISE;

    private static final String KEY_ID = HealthNotesDBHelper.KEY_EXERCISE_ID;
    private static final String KEY_PLAN_TITLE = HealthNotesDBHelper.KEY_EXERCISE_PLAN_TITLE;
    private static final String KEY_EXERCISEPART = HealthNotesDBHelper.KEY_EXERCISE_EXERCISEPART;
    private static final String KEY_EXERCISE_TITLE = HealthNotesDBHelper.KEY_EXERCISE_EXERCISE_TITLE;
    private static final String KEY_TOTAL_SET = HealthNotesDBHelper.KEY_EXERCISE_TOTAL_SET;

    private static final String DROP_TABLE_EXERCISE = HealthNotesDBHelper.DROP_TABLE_EXERCISE;

    public ExerciseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ExerciseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ExerciseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
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

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    public void insertExercise(String planTitle, String exercisePart, String exerciseTitle, int totalSet){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_EXERCISE + " VALUES(null, '" +
                planTitle + "', '" +
                exercisePart + "', '" +
                exerciseTitle + "', '" +
                totalSet + "');"
        );
        db.close();
    }

    public void insertExercise(String exercisePart, String exerciseTitle, int totalSet){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_EXERCISE + " VALUES(null, null, '" +
                exercisePart + "', '" +
                exerciseTitle + "', '" +
                totalSet + "');"
        );
        db.close();
    }

    public ArrayList<Exercise> getExercisePlanList() {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT DISTINCT " + KEY_PLAN_TITLE + " FROM " + TABLE_EXERCISE + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setPlanTitle(cursor.getString(0));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }


    public ArrayList<Exercise> getExerciseListPlanTitleIsNull() {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT * FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " IS NULL;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setExercisePart(Common.ExercisePart.valueOf(cursor.getString(2)));
                exercise.setExerciseTitle(cursor.getString(3));
                exercise.setTotalSet(cursor.getInt(4));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getExerciseTitleListSelectedPlan(String planTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT " + KEY_EXERCISE_TITLE + " FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + planTitle +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setExerciseTitle(cursor.getString(0));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getExerciseListSelectedPlan(String planTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT * FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + planTitle + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();

                exercise.setId(cursor.getInt(0));
                exercise.setPlanTitle(cursor.getString(1));
                exercise.setExercisePart(Common.ExercisePart.valueOf(cursor.getString(2)));
                exercise.setExerciseTitle(cursor.getString(3));
                exercise.setTotalSet(cursor.getInt(4));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getExercisePlanSelectedExercise(String exerciseTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT DISTINCT " + KEY_PLAN_TITLE + " FROM " + TABLE_EXERCISE + " WHERE " + KEY_EXERCISE_TITLE + " = '" + exerciseTitle + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();

                exercise.setPlanTitle(cursor.getString(0));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public void updateExercisePlanTitle(String planTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_PLAN_TITLE + " = '" + planTitle + "'" +
                " WHERE " + KEY_PLAN_TITLE + " is NULL ;"
        );
        db.close();
    }

    public void updateExercise(String exercisePart,String exerciseTitle,int totalSet, String beforeTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_EXERCISE_TITLE + " = '" + exerciseTitle + "', " +
                KEY_TOTAL_SET + " = " + totalSet + ", " +
                KEY_EXERCISEPART + " = '" + exercisePart + "' " +
                "WHERE " + KEY_EXERCISE_TITLE + " = '" + beforeTitle + "';"
        );
        db.close();
    }

    public void updateExerciseSet(String title,int set){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_TOTAL_SET + " = " + set +
                " WHERE " + KEY_EXERCISE_TITLE + " = '" + title + "';"
        );
        db.close();
    }

    public void deleteExercise(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_EXERCISE_ID+ " = '" + id + "';" );
        db.close();
    }

    public void deleteExerciseListExercisePlanTitleIsNull(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_EXERCISE_TITLE + " = '" + title + "' AND " + KEY_PLAN_TITLE + " IS NULL;" );
        db.close();
    }

    public void deleteExercisePlan(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + title + "';" );
        db.close();
    }

    public void deleteEmptyExercisePlan() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_EXERCISE_TITLE + " = null;" );
        db.close();
    }

}
