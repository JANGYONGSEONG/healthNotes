package com.jang.yongs.healthnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jang.yongs.healthnotes.common.Common;
import com.jang.yongs.healthnotes.db.ExerciseDBHelper;
import com.jang.yongs.healthnotes.model.Exercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseSelectActivity extends AppCompatActivity {

    @BindView(R.id.chest)
    CheckBox chest;
    @BindView(R.id.back)
    CheckBox back;
    @BindView(R.id.shoulder)
    CheckBox shoulder;
    @BindView(R.id.trapezius)
    CheckBox trapezius;
    @BindView(R.id.hip)
    CheckBox hip;
    @BindView(R.id.thigh)
    CheckBox thigh;
    @BindView(R.id.calf)
    CheckBox calf;
    @BindView(R.id.waist)
    CheckBox waist;
    @BindView(R.id.forearm)
    CheckBox forearm;
    @BindView(R.id.abs)
    CheckBox abs;
    @BindView(R.id.biceps)
    CheckBox biceps;
    @BindView(R.id.triceps)
    CheckBox triceps;

    @BindView(R.id.exercise_title_input)
    EditText exerciseTitle;
    @BindView(R.id.exercise_set_input)
    EditText exerciseSet;
    @BindView(R.id.exercise_select_left_button)
    Button cancelButton;
    @BindView(R.id.exercise_select_right_button)
    Button okButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Activity mActivity = ExerciseSelectActivity.this;

    ExerciseDBHelper mDBHelper;

    Exercise mExercise;

    String mCurrentTitle;
    String mTitle;
    int mSet;
    Common.ExercisePart mExercisePart;

    boolean mIsUpdate = false;   //기존의 Exercise를 update 하는가 새로운 Exercise를 추가하는가
    boolean mDoExercise = false; //기존의 ExercisePlan에서 Exercise를 추가하는가 새롭게 만드는 ExercisePlan에서 Exercise를 추가하는가.
    String mPlanTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_select);
        ButterKnife.bind(this);

        setToolbar();

        mDBHelper = new ExerciseDBHelper(getApplicationContext(), "HealthNotes", null, 1);

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

        chest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chest.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    chest.setChecked(isChecked);
                }
            }
        });

        back.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    back.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    back.setChecked(isChecked);
                }
            }
        });

        shoulder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shoulder.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    back.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    shoulder.setChecked(isChecked);
                }
            }
        });

        trapezius.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    trapezius.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    trapezius.setChecked(isChecked);
                }
            }
        });

        hip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hip.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    hip.setChecked(isChecked);
                }
            }
        });

        thigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    thigh.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    thigh.setChecked(isChecked);
                }
            }
        });

        calf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    calf.setChecked(isChecked);
                    chest.setChecked(!isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    calf.setChecked(isChecked);
                }
            }
        });

        waist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    waist.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    chest.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    waist.setChecked(isChecked);
                }
            }
        });

        forearm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    forearm.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    chest.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    forearm.setChecked(isChecked);
                }
            }
        });

        abs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    abs.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    chest.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    abs.setChecked(isChecked);
                }
            }
        });

        biceps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    biceps.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    chest.setChecked(!isChecked);
                    triceps.setChecked(!isChecked);
                } else {
                    biceps.setChecked(isChecked);
                }
            }
        });

        triceps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    triceps.setChecked(isChecked);
                    back.setChecked(!isChecked);
                    shoulder.setChecked(!isChecked);
                    trapezius.setChecked(!isChecked);
                    hip.setChecked(!isChecked);
                    thigh.setChecked(!isChecked);
                    calf.setChecked(!isChecked);
                    waist.setChecked(!isChecked);
                    forearm.setChecked(!isChecked);
                    abs.setChecked(!isChecked);
                    biceps.setChecked(!isChecked);
                    chest.setChecked(!isChecked);
                } else {
                    triceps.setChecked(isChecked);
                }
            }
        });
    }

    private void setUpdate() {
        Intent intent = getIntent();
        mIsUpdate = intent.getBooleanExtra("update", false);
        mDoExercise = intent.getBooleanExtra("doExercise", false);

        if (mDoExercise) {
            mPlanTitle = intent.getStringExtra("planTitle");
        }

        if (mIsUpdate) {
            mExercise = intent.getParcelableExtra("exercise");
            mCurrentTitle = mExercise.getExerciseTitle();
            exerciseTitle.setText(mCurrentTitle);
            exerciseSet.setText(String.valueOf(mExercise.getTotalSet()));
            switch (mExercise.getExercisePart()) {
                case Chest:
                    chest.setChecked(true);
                    break;
                case Back:
                    back.setChecked(true);
                    break;
                case Shoulder:
                    shoulder.setChecked(true);
                    break;
                case Trapezius:
                    trapezius.setChecked(true);
                    break;
                case Hip:
                    hip.setChecked(true);
                    break;
                case Thigh:
                    thigh.setChecked(true);
                    break;
                case Calf:
                    calf.setChecked(true);
                    break;
                case Waist:
                    waist.setChecked(true);
                    break;
                case Forearm:
                    forearm.setChecked(true);
                    break;
                case Abs:
                    abs.setChecked(true);
                    break;
                case Biceps:
                    biceps.setChecked(true);
                    break;
                case Triceps:
                    triceps.setChecked(true);
                    break;
            }
        } else {
            mExercise = new Exercise();
        }
    }

    private void buttonSet() {

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
                boolean overlap = false;    //Exercise title 중복

                ArrayList<Exercise> mExerciseArrayList = new ArrayList<Exercise>();
                if(!mDoExercise) {
                    mExerciseArrayList.addAll(mDBHelper.getExerciseListPlanTitleIsNull());

                    for (int i = 0; i < mExerciseArrayList.size(); i++) {
                        if (mTitle != null) {
                            if (mTitle.equals(mExerciseArrayList.get(i).getExerciseTitle()))
                                overlap = true;
                        }
                    }
                }else{
                    mExerciseArrayList.addAll(mDBHelper.getExerciseListSelectedPlan(mPlanTitle));

                    for (int i = 0; i < mExerciseArrayList.size(); i++) {
                        if (mTitle != null) {
                            if (mTitle.equals(mExerciseArrayList.get(i).getExerciseTitle()))
                                overlap = true;
                        }
                    }
                }

                if (!mExercise.checkExerciseSelect()) {
                    Toast.makeText(ExerciseSelectActivity.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                } else if (!mDoExercise && !mIsUpdate && !overlap) { //새롭게 생성 중인 Exercise Plan에서 새로운 Exercise를 추가하는 경우
                    mDBHelper.insertExercise(String.valueOf(mExercisePart), mTitle, mSet);
                    Intent intent = new Intent(mActivity, ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("doExercise", mDoExercise);
                    startActivity(intent);
                } else if (!mDoExercise && mIsUpdate) { //새롭게 생성 중인 Exercise Plan에서 Exercise를 변경하는 경우
                    mDBHelper.updateExercise(String.valueOf(mExercisePart), mTitle, mSet, mCurrentTitle);
                    Intent intent = new Intent(mActivity, ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("doExercise", mDoExercise);
                    startActivity(intent);
                } else if (mDoExercise && !mIsUpdate && !overlap) { //기존의 Exercise Plan에서 새로운 Exercise를 추가하는 경우
                    mDBHelper.insertExercise(mPlanTitle, String.valueOf(mExercisePart), mTitle, mSet);
                    Intent intent = new Intent(mActivity, ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle", mPlanTitle);
                    intent.putExtra("doExercise", mDoExercise);
                    startActivity(intent);
                } else if (mDoExercise && mIsUpdate) { //기존의 Exercise Plan에서 Exercise를 변경하는 경우
                    mDBHelper.updateExercise(String.valueOf(mExercisePart), mTitle, mSet, mCurrentTitle);
                    Intent intent = new Intent(mActivity, ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle", mPlanTitle);
                    intent.putExtra("doExercise", mDoExercise);
                    startActivity(intent);
                } else {
                    Toast.makeText(ExerciseSelectActivity.this, "title 중복", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getValue() {
        if (exerciseTitle.getText().toString().length() != 0) {
            mTitle = exerciseTitle.getText().toString();
            mExercise.setExerciseTitle(mTitle);
        }

        if (exerciseSet.getText().toString().length() != 0) {
            mSet = Integer.parseInt(exerciseSet.getText().toString());
            mExercise.setTotalSet(mSet);
        }

        mExercisePart = getExcercisePart();
        mExercise.setExercisePart(mExercisePart);

    }

    public Common.ExercisePart getExcercisePart() {
        if (chest.isChecked())
            return Common.ExercisePart.Chest;
        else if (back.isChecked())
            return Common.ExercisePart.Back;
        else if (shoulder.isChecked())
            return Common.ExercisePart.Shoulder;
        else if (trapezius.isChecked())
            return Common.ExercisePart.Trapezius;
        else if (hip.isChecked())
            return Common.ExercisePart.Hip;
        else if (thigh.isChecked())
            return Common.ExercisePart.Thigh;
        else if (calf.isChecked())
            return Common.ExercisePart.Calf;
        else if (waist.isChecked())
            return Common.ExercisePart.Waist;
        else if (forearm.isChecked())
            return Common.ExercisePart.Forearm;
        else if (abs.isChecked())
            return Common.ExercisePart.Abs;
        else if (biceps.isChecked())
            return Common.ExercisePart.Biceps;
        else if (triceps.isChecked())
            return Common.ExercisePart.Triceps;
        return null;
    }

}