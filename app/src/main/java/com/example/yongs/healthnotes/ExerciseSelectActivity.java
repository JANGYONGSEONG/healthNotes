package com.example.yongs.healthnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yongs.healthnotes.db.ExerciseDBHelper;
import com.example.yongs.healthnotes.models.Exercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseSelectActivity extends AppCompatActivity {

    @BindView(R.id.exercise0) CheckBox exercise0;
    @BindView(R.id.exercise1) CheckBox exercise1;
    @BindView(R.id.exercise2) CheckBox exercise2;
    @BindView(R.id.exercise3) CheckBox exercise3;
    @BindView(R.id.exercise4) CheckBox exercise4;
    @BindView(R.id.exercise5) CheckBox exercise5;
    @BindView(R.id.exercise6) CheckBox exercise6;
    @BindView(R.id.exercise7) CheckBox exercise7;
    @BindView(R.id.exercise8) CheckBox exercise8;
    @BindView(R.id.exercise9) CheckBox exercise9;
    @BindView(R.id.exercise10) CheckBox exercise10;
    @BindView(R.id.exercise11) CheckBox exercise11;

    @BindView(R.id.exercise_title_input) EditText exerciseTitle;
    @BindView(R.id.exercise_set_input) EditText exerciseSet;
    @BindView(R.id.exercise_select_left_button) Button cancelButton;
    @BindView(R.id.exercise_select_right_button) Button okButton;

    @BindView(R.id.toolbar) Toolbar toolbar;

    Activity mActivity = ExerciseSelectActivity.this;

    ExerciseDBHelper dbHelper;

    Exercise exercise;

    String beforeTitle;
    String title;
    int set;
    int exercisePartIndex;

    boolean isUpdate = false;
    boolean doExercise = false;
    String planTitle;


    ArrayList<Exercise> exerciseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_select);
        ButterKnife.bind(this);

        setToolbar();

        dbHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);

        buttonSet();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("계획한 운동을 입력해주세요.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkBoxSet();

        setUpdate();


    }

    private void checkBoxSet() {

        exercise0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise0.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise0.setChecked(isChecked);
                }
            }
        });

        exercise1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise1.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise1.setChecked(isChecked);
                }
            }
        });

        exercise2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise2.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise2.setChecked(isChecked);
                }
            }
        });

        exercise3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise3.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise3.setChecked(isChecked);
                }
            }
        });

        exercise4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise4.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise4.setChecked(isChecked);
                }
            }
        });

        exercise5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise5.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise5.setChecked(isChecked);
                }
            }
        });

        exercise6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise6.setChecked(isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise6.setChecked(isChecked);
                }
            }
        });

        exercise7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise7.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise7.setChecked(isChecked);
                }
            }
        });

        exercise8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise8.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise8.setChecked(isChecked);
                }
            }
        });

        exercise9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise9.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise9.setChecked(isChecked);
                }
            }
        });

        exercise10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise10.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise0.setChecked(!isChecked);
                    exercise11.setChecked(!isChecked);
                }else {
                    exercise10.setChecked(isChecked);
                }
            }
        });

        exercise11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    exercise11.setChecked(isChecked);
                    exercise1.setChecked(!isChecked);
                    exercise2.setChecked(!isChecked);
                    exercise3.setChecked(!isChecked);
                    exercise4.setChecked(!isChecked);
                    exercise5.setChecked(!isChecked);
                    exercise6.setChecked(!isChecked);
                    exercise7.setChecked(!isChecked);
                    exercise8.setChecked(!isChecked);
                    exercise9.setChecked(!isChecked);
                    exercise10.setChecked(!isChecked);
                    exercise0.setChecked(!isChecked);
                }else {
                    exercise11.setChecked(isChecked);
                }
            }
        });
    }

    private void setUpdate() {
        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("update",false);
        doExercise = intent.getBooleanExtra("doExercise",false);

        if(doExercise) {
            planTitle = intent.getStringExtra("planTitle");
            Log.i("TextPlanTitle_ExerciseSelectActivity", planTitle);
        }

        if(isUpdate){
            exercise = intent.getParcelableExtra("exercise");
            beforeTitle = exercise.getDetailTitle();
            exerciseTitle.setText(beforeTitle);
            exerciseSet.setText(String.valueOf(exercise.getTotalSet()));
            switch(exercise.getExercisePart()){
                case 0:
                    exercise0.setChecked(true);
                    break;
                case 1:
                    exercise1.setChecked(true);
                    break;
                case 2:
                    exercise2.setChecked(true);
                    break;
                case 3:
                    exercise3.setChecked(true);
                    break;
                case 4:
                    exercise4.setChecked(true);
                    break;
                case 5:
                    exercise5.setChecked(true);
                    break;
                case 6:
                    exercise6.setChecked(true);
                    break;
                case 7:
                    exercise7.setChecked(true);
                    break;
                case 8:
                    exercise8.setChecked(true);
                    break;
                case 9:
                    exercise9.setChecked(true);
                    break;
                case 10:
                    exercise10.setChecked(true);
                    break;
                case 11:
                    exercise11.setChecked(true);
                    break;
            }
        }else{
            exercise = new Exercise();
        }
    }

    private void buttonSet(){

        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        okButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
                boolean overlap = false;

                ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
                exerciseArrayList.addAll(dbHelper.getAllPlanTitleIsNull());

                for(int i=0;i<exerciseArrayList.size();i++){
                    if(title!=null){
                        if (title.equals(exerciseArrayList.get(i).getDetailTitle()))
                            overlap = true;
                    }
                }

                Log.i("TestDoExercise_ExerciseSelectActivity", String.valueOf(doExercise));
                Log.i("TestUpdate_ExerciseSelectActivity", String.valueOf(isUpdate));
                if(!exercise.checkExerciseSelectInput()) {
                    Toast.makeText(ExerciseSelectActivity.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else if(!doExercise && !isUpdate && !overlap){
                    dbHelper.insert(exercisePartIndex,title,set);
                    Intent intent = new Intent(mActivity,ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("doExercise",doExercise);
                    startActivity(intent);
                }else if(!doExercise && isUpdate){
                    dbHelper.update(exercisePartIndex,title,set,beforeTitle);
                    Intent intent = new Intent(mActivity,ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("doExercise",doExercise);
                    startActivity(intent);
                }else if(doExercise && !isUpdate && !overlap){
                    dbHelper.insert(planTitle,exercisePartIndex,title,set);
                    Intent intent = new Intent(mActivity,ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle",planTitle);
                    intent.putExtra("doExercise",doExercise);
                    startActivity(intent);
                }else if(doExercise && isUpdate){
                    dbHelper.update(exercisePartIndex,title,set,beforeTitle);
                    Intent intent = new Intent(mActivity,ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle",planTitle);
                    intent.putExtra("doExercise",doExercise);
                    startActivity(intent);
                }else{
                    Toast.makeText(ExerciseSelectActivity.this, "title 중복", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getValue(){
        if(exerciseTitle.getText().toString().length() != 0){
            title = exerciseTitle.getText().toString();
            exercise.setDetailTitle(title);
        }

        if(exerciseSet.getText().toString().length() != 0){
            set = Integer.parseInt(exerciseSet.getText().toString());
            exercise.setTotalSet(set);
        }

        exercisePartIndex = getCheckBoxIndex();
        exercise.setExercisePart(exercisePartIndex);

    }

    public int getCheckBoxIndex() {
        if(exercise0.isChecked())
            return 0;
        else if(exercise1.isChecked())
            return 1;
        else if(exercise2.isChecked())
            return 2;
        else if(exercise3.isChecked())
            return 3;
        else if(exercise4.isChecked())
            return 4;
        else if(exercise5.isChecked())
            return 5;
        else if(exercise6.isChecked())
            return 6;
        else if(exercise7.isChecked())
            return 7;
        else if(exercise8.isChecked())
            return 8;
        else if(exercise9.isChecked())
            return 9;
        else if(exercise10.isChecked())
            return 10;
        else if(exercise11.isChecked())
            return 11;
        return -1;
    }

}