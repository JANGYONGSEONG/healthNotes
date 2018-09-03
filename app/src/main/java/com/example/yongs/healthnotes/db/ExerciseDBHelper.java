package com.example.yongs.healthnotes.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.example.yongs.healthnotes.models.Exercise;

import java.util.ArrayList;

/**
 * Created by yongs on 2018-08-16.
 */

public class ExerciseDBHelper extends DBHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "HealthNotes";

    private static final String TABLE_EXERCISE = "exercise";

    private static final String KEY_ID = "id";
    private static final String KEY_PLAN_TITLE = "title";
    private static final String KEY_DETAIL_DESCRIBE = "describe";
    private static final String KEY_EXERCISEPART = "exercisepart";
    private static final String KEY_DETAIL_TITLE = "detailtitle";
    private static final String KEY_TOTAL_SET = "totalset";

    String DROP_TABLE_EXERCISE = "DROP TABLE " + TABLE_EXERCISE;

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
    }

    public void insert(String planTitle, int exercisePart, String detailTitle, int totalSet){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_EXERCISE + " VALUES(null, '" +
                planTitle + "', '" +
                exercisePart + "', '" +
                detailTitle + "', '" +
                totalSet + "');"
        );
        db.close();
    }

    public void insert(int exercisePart, String detailTitle, int totalSet){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_EXERCISE + " VALUES(null, null, '" +
                exercisePart + "', '" +
                detailTitle + "', '" +
                totalSet + "');"
        );
        db.close();
    }

    public ArrayList<Exercise> getAll() {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT_ALL = "SELECT * FROM " + TABLE_EXERCISE + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALL, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setPlanTitle(cursor.getString(1));
                exercise.setExercisePart(cursor.getInt(2));
                exercise.setDetailTitle(cursor.getString(3));
                exercise.setTotalSet(cursor.getInt(4));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getAllTitle() {
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

    public ArrayList<Exercise> getAllDetailTitle(String planTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT " + KEY_DETAIL_TITLE + " FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + planTitle +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setDetailTitle(cursor.getString(0));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getAllPlanTitleIsNull() {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT * FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " IS NULL;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();
                exercise.setExercisePart(cursor.getInt(2));
                exercise.setDetailTitle(cursor.getString(3));
                exercise.setTotalSet(cursor.getInt(4));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getDetailThePlanTitle(String planTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT * FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + planTitle + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT, null);

        if(cursor.moveToFirst()){
            do{
                Exercise exercise = new Exercise();

                exercise.setPlanTitle(cursor.getString(1));
                exercise.setExercisePart(cursor.getInt(2));
                exercise.setDetailTitle(cursor.getString(3));
                exercise.setTotalSet(cursor.getInt(4));
                exerciseList.add(exercise);
            }while(cursor.moveToNext());
        }
        return exerciseList;
    }

    public ArrayList<Exercise> getThePlanByDetailTitle(String detailTitle) {
        ArrayList<Exercise> exerciseList = new ArrayList<Exercise>();

        String SELECT= "SELECT DISTINCT " + KEY_PLAN_TITLE + " FROM " + TABLE_EXERCISE + " WHERE " + KEY_DETAIL_TITLE + " = '" + detailTitle + "';";

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

    public void update(String planTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_PLAN_TITLE + " = '" + planTitle + "'" +
                " WHERE " + KEY_PLAN_TITLE + " is NULL ;"
        );
        db.close();
    }

    public void update(int exercisePart,String detailTitle,int totalSet, String beforeTitle){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_DETAIL_TITLE + " = '" + detailTitle + "', " +
                KEY_TOTAL_SET + " = " + totalSet + ", " +
                KEY_EXERCISEPART + " = " + exercisePart + " " +
                "WHERE " + KEY_DETAIL_TITLE + " = '" + beforeTitle + "';"
        );
        db.close();
    }

    public void update(String title,int set){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EXERCISE + " SET " +
                KEY_TOTAL_SET + " = " + set +
                " WHERE " + KEY_DETAIL_TITLE + " = '" + title + "';"
        );
        db.close();
    }

    public void deleteDetail(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_DETAIL_TITLE + " = '" + title + "';" );
        db.close();
    }

    public void deleteWrongDetail(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_DETAIL_TITLE + " = '" + title + "' AND " + KEY_PLAN_TITLE + " IS NULL;" );
        db.close();
    }

    public void deletePlan(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_PLAN_TITLE + " = '" + title + "';" );
        db.close();
    }

    public void deletePlanEmpty() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EXERCISE + " WHERE " + KEY_DETAIL_TITLE + " = null;" );
        db.close();
    }

}
