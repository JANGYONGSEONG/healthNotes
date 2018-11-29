package com.jang.yongs.healthnotes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jang.yongs.healthnotes.adapter.ExerciseDetailAdapter;
import com.jang.yongs.healthnotes.db.ExerciseDBHelper;
import com.jang.yongs.healthnotes.db.PerformanceDBHelper;
import com.jang.yongs.healthnotes.model.Exercise;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseDetailActivity extends AppCompatActivity {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.empty_list) TextView emptyListItem;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    Activity mActivity = ExerciseDetailActivity.this;

    Intent mIntent;

    Boolean mDoExercise;
    String mPlanTitle;

    ExerciseDetailAdapter mExerciseDetailAdapter;
    ArrayList<Exercise> mExerciseArrayList;
    ExerciseDBHelper mExerciseDBHelper;

    CheckBox mCheckBox;

    PerformanceDBHelper mPerformanceDBHelper;

    long now = System.currentTimeMillis();
    Date mDateValue = new Date(now);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mDoExercise = mIntent.getBooleanExtra("doExercise",false);
        if(mDoExercise) {
            mPlanTitle = mIntent.getStringExtra("planTitle");
        }

        mPerformanceDBHelper = new PerformanceDBHelper(getApplicationContext(),"HealthNotes",null,1);

        setToolbar();

        setList();

        setFab();


    }

    public void setToolbar() {
        setSupportActionBar(toolbar);
        if(mDoExercise){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mPlanTitle);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("운동 목록");
        }
    }

    public void setList() {
        mExerciseDBHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);

        if(mDoExercise){
            mExerciseArrayList =  mExerciseDBHelper.getExerciseListSelectedPlan(mPlanTitle);
        }else{
            mExerciseArrayList =  mExerciseDBHelper.getExerciseListPlanTitleIsNull();
        }

        mExerciseDetailAdapter = new ExerciseDetailAdapter(getApplicationContext(),mExerciseArrayList);

        list.setAdapter(mExerciseDetailAdapter);

        list.setOnItemClickListener( new ExerciseDetailActivity.ListViewItemClickListener() );
        list.setOnItemLongClickListener( new ExerciseDetailActivity.ListViewItemLongClickListener() );
    }

    public void setFab(){
        if(mDoExercise){
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity,ExerciseSelectActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listRenewal();
    }

    public void listRenewal() {
        mExerciseDetailAdapter.exerciseArrayList.clear();
        if(mDoExercise){
            mExerciseDetailAdapter.exerciseArrayList.addAll(mExerciseDBHelper.getExerciseListSelectedPlan(mPlanTitle));

        }else {
            mExerciseDetailAdapter.exerciseArrayList.addAll(mExerciseDBHelper.getExerciseListPlanTitleIsNull());
        }
        list.setAdapter(mExerciseDetailAdapter);
        mExerciseDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            if(mExerciseDetailAdapter.getCheckBoxState()) {
                getMenuInflater().inflate(R.menu.menu_delete, menu);

                getSupportActionBar().setTitle(String.valueOf(mExerciseDetailAdapter.getCountSelectedCheckBox()));
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                mCheckBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
                mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mCheckBox.setChecked(isChecked);
                        for(int i = 0; i < mExerciseDetailAdapter.exerciseArrayList.size(); i++) {
                            mExerciseDetailAdapter.exerciseArrayList.get(i).setSelected(mCheckBox.isChecked());
                        }
                        if(mExerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                            getSupportActionBar().setTitle("목록을 선택하세요.");
                        }else{
                            getSupportActionBar().setTitle(String.valueOf(mExerciseDetailAdapter.getCountSelectedCheckBox()));
                        }
                        mExerciseDetailAdapter.notifyDataSetChanged();
                    }
                });
            }else{
                if(mDoExercise) {
                    getMenuInflater().inflate(R.menu.menu_start, menu);
                    getSupportActionBar().setTitle(mPlanTitle);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }else{
                    getMenuInflater().inflate(R.menu.menu_confirm, menu);
                    getSupportActionBar().setTitle("운동 목록");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

            switch (item.getItemId()) {
                case R.id.action_cancel:
                    mExerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_delete:
                    for (int i = 0; i < mExerciseDetailAdapter.exerciseArrayList.size(); i++) {
                        if (mExerciseDetailAdapter.exerciseArrayList.get(i).isSelected()) {
                            mExerciseDBHelper.deleteExercise(mExerciseDetailAdapter.exerciseArrayList.get(i).getId());
                            mExerciseDBHelper.deleteEmptyExercisePlan();
                        }
                    }
                    listRenewal();
                    mExerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_start:
                    intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", mExerciseDetailAdapter.exerciseArrayList.get(0));
                    intent.putExtra("position",0);
                    startActivity(intent);
                    return true;
                case R.id.action_insert:
                    intent = new Intent(mActivity, ExerciseSelectActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle", mPlanTitle);
                    intent.putExtra("update", false);
                    intent.putExtra("doExercise",true);
                    startActivity(intent);
                    return true;
                case R.id.action_delete_mode:
                    mExerciseDetailAdapter.resetCheckBox();
                    mExerciseDetailAdapter.setCheckBoxState(true);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case android.R.id.home:
                    intent = new Intent(mActivity,MainActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.action_confirm:
                    if(mExerciseDetailAdapter.exerciseArrayList.size()==0){
                        Toast.makeText(mActivity, "운동을 추가해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                        builder.setTitle("계획하신 운동(들)의 제목을 등록해주세요.");

                        final EditText editText = new EditText(mActivity);
                        editText.setSingleLine(true);

                        builder.setView(editText);

                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = editText.getText().toString();
                                if(title.length()!=0) {
                                    ArrayList<Exercise> exerciseArrayList = mExerciseDBHelper.getExercisePlanList();
                                    for(int i=0; i<exerciseArrayList.size(); i++){
                                        if(title.equals(exerciseArrayList.get(i).getPlanTitle())) {
                                            Toast.makeText(mActivity, "동일한 제목이 존재합니다.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }else{
                                            mExerciseDBHelper.updateExercisePlanTitle(title);
                                            Intent intent = new Intent(mActivity, MainActivity.class);
                                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }


                                }else{
                                    Toast.makeText(mActivity, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();

                        return true;
                    }
                default:
                    return super.onOptionsItemSelected(item);
            }
    }


    private class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mExerciseDetailAdapter.getCheckBoxState()) {
                    mExerciseDetailAdapter.exerciseArrayList.get(position).setSelected(!mExerciseDetailAdapter.exerciseArrayList.get(position).isSelected());
                    if (mExerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                        getSupportActionBar().setTitle("목록을 선택하세요.");
                    } else {
                        getSupportActionBar().setTitle(String.valueOf(mExerciseDetailAdapter.getCountSelectedCheckBox()));
                    }
                    mExerciseDetailAdapter.notifyDataSetChanged();
                } else {
                    if(mDoExercise) {
                        Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("exercise", mExerciseDetailAdapter.exerciseArrayList.get(position));
                        Log.i("test_id", String.valueOf(mExerciseDetailAdapter.exerciseArrayList.get(position).getId()));
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(mActivity, ExerciseSelectActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("exercise", mExerciseDetailAdapter.exerciseArrayList.get(position));
                        intent.putExtra("update", true);
                        startActivity(intent);
                    }
                }
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            if(mDoExercise){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setTitle(mExerciseDetailAdapter.exerciseArrayList.get(position).getExerciseTitle());
                builder.setMessage("해당 운동을 수정하시겠습니까? 삭제하시겠습니까?");

                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder updateTotalSetBuilder = new AlertDialog.Builder(mActivity);

                        updateTotalSetBuilder.setTitle(mExerciseDetailAdapter.exerciseArrayList.get(position).getExerciseTitle() + " 의 새로운 세트 수를 입력하세요.");

                        final EditText editText = new EditText(mActivity);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        updateTotalSetBuilder.setView(editText);

                        updateTotalSetBuilder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String set = editText.getText().toString();
                                if(set.length()!=0){
                                    int setValue = Integer.parseInt(set);

                                    if(mExerciseDetailAdapter.exerciseArrayList.get(position).getTotalSet()>setValue){
                                        for(int i=setValue+1; i<=mExerciseDetailAdapter.exerciseArrayList.get(position).getTotalSet(); i++){
                                            mPerformanceDBHelper.deletePerformanceSelectedOrder(mExerciseDetailAdapter.exerciseArrayList.get(position).getPlanTitle(),mExerciseDetailAdapter.exerciseArrayList.get(position).getExerciseTitle(),mDateValue,i);
                                        }
                                    }
                                    mExerciseDBHelper.updateExerciseSet(mExerciseDetailAdapter.exerciseArrayList.get(position).getExerciseTitle(),setValue);

                                    listRenewal();

                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(mActivity, "세트 수가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        updateTotalSetBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        updateTotalSetBuilder.show();
                    }
                });

                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mExerciseDBHelper.deleteExercise(mExerciseDetailAdapter.exerciseArrayList.get(position).getId());
                        mExerciseDBHelper.deleteEmptyExercisePlan();
                        listRenewal();
                    }
                });
                builder.show();
            }else {
                mExerciseDetailAdapter.resetCheckBox();
                mExerciseDetailAdapter.setCheckBoxState(true);
                mExerciseDetailAdapter.exerciseArrayList.get(position).setSelected(true);
                mActivity.invalidateOptionsMenu();
            }

            return true;
        }
    }


}
