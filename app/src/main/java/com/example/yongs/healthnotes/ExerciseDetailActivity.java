package com.example.yongs.healthnotes;

import android.app.Activity;
import android.content.Context;
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

import com.example.yongs.healthnotes.adapter.ExerciseDetailAdapter;
import com.example.yongs.healthnotes.db.ExerciseDBHelper;
import com.example.yongs.healthnotes.db.PerformanceDBHelper;
import com.example.yongs.healthnotes.models.Exercise;

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
    Context mContext = this;

    Intent intent;

    Boolean doExercise;
    String planTitle;

    ExerciseDetailAdapter exerciseDetailAdapter;
    ArrayList<Exercise> exerciseArrayList;
    ExerciseDBHelper    dbHelper;

    CheckBox checkBox;

    PerformanceDBHelper performanceDBHelper;

    long now = System.currentTimeMillis();
    Date dateValue = new Date(now);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        ButterKnife.bind(this);

        intent = getIntent();
        doExercise = intent.getBooleanExtra("doExercise",false);
        if(doExercise)
            planTitle = intent.getStringExtra("planTitle");

        performanceDBHelper = new PerformanceDBHelper(getApplicationContext(),"HealthNotes",null,1);

        setToolbar();

        setList();

        setFab();


    }

    public void setToolbar() {
        if(doExercise){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(planTitle);
        }else{
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("운동 목록");
        }
    }

    public void setList() {
        if(doExercise){
            dbHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);

            exerciseArrayList =  dbHelper.getDetailThePlanTitle(planTitle);

            exerciseDetailAdapter = new ExerciseDetailAdapter(getApplicationContext(),exerciseArrayList);

            list.setAdapter(exerciseDetailAdapter);

            list.setOnItemClickListener( new ExerciseDetailActivity.ListViewItemClickListener() );
            list.setOnItemLongClickListener( new ExerciseDetailActivity.ListViewItemLongClickListener() );

        }else{
            dbHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);

            exerciseArrayList =  dbHelper.getAllPlanTitleIsNull();

            exerciseDetailAdapter = new ExerciseDetailAdapter(getApplicationContext(),exerciseArrayList);

            list.setAdapter(exerciseDetailAdapter);

            list.setOnItemClickListener( new ExerciseDetailActivity.ListViewItemClickListener() );
            list.setOnItemLongClickListener( new ExerciseDetailActivity.ListViewItemLongClickListener() );

        }
    }

    public void setFab(){
        if(doExercise){
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
        if(doExercise){
            exerciseDetailAdapter.exerciseArrayList.clear();
            exerciseDetailAdapter.exerciseArrayList.addAll(dbHelper.getDetailThePlanTitle(planTitle));
            list.setAdapter(exerciseDetailAdapter);
            exerciseDetailAdapter.notifyDataSetChanged();
        }else {
            exerciseDetailAdapter.exerciseArrayList.clear();
            exerciseDetailAdapter.exerciseArrayList.addAll(dbHelper.getAllPlanTitleIsNull());
            list.setAdapter(exerciseDetailAdapter);
            exerciseDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(doExercise){
            if(exerciseDetailAdapter.getCheckBoxState()) {
                getMenuInflater().inflate(R.menu.menu_delete, menu);

                getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                checkBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkBox.setChecked(isChecked);
                        for(int i = 0; i < exerciseDetailAdapter.exerciseArrayList.size(); i++) {
                            exerciseDetailAdapter.exerciseArrayList.get(i).setSelected(checkBox.isChecked());
                        }
                        if(exerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                            getSupportActionBar().setTitle("목록을 선택하세요.");
                        }else{
                            getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                        }
                        exerciseDetailAdapter.notifyDataSetChanged();
                    }
                });
            }else{
                getMenuInflater().inflate(R.menu.menu_start,menu);
                getSupportActionBar().setTitle(planTitle);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }else{
            if(exerciseDetailAdapter.getCheckBoxState()) {
                getMenuInflater().inflate(R.menu.menu_delete, menu);

                getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                checkBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        checkBox.setChecked(isChecked);
                        for(int i = 0; i < exerciseDetailAdapter.exerciseArrayList.size(); i++) {
                            exerciseDetailAdapter.exerciseArrayList.get(i).setSelected(checkBox.isChecked());
                        }
                        if(exerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                            getSupportActionBar().setTitle("목록을 선택하세요.");
                        }else{
                            getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                        }
                        exerciseDetailAdapter.notifyDataSetChanged();
                    }
                });
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
        if(doExercise){
            switch (item.getItemId()) {
                case R.id.action_cancel:
                    exerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_delete:
                    for (int i = 0; i < exerciseDetailAdapter.exerciseArrayList.size(); i++) {
                        if (exerciseDetailAdapter.exerciseArrayList.get(i).isSelected()) {
                            dbHelper.deleteDetail(exerciseDetailAdapter.exerciseArrayList.get(i).getDetailTitle());
                            dbHelper.deletePlanEmpty();
                            performanceDBHelper.delete(exerciseDetailAdapter.exerciseArrayList.get(i).getPlanTitle(),exerciseDetailAdapter.exerciseArrayList.get(i).getDetailTitle(),dateValue);
                        }
                    }

                    listRenewal();

                    exerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_start:
                    intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", exerciseDetailAdapter.exerciseArrayList.get(0));
                    intent.putExtra("position",0);
                    startActivity(intent);
                    return true;
                case R.id.action_insert:
                    intent = new Intent(mActivity, ExerciseSelectActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("planTitle", planTitle);
                    Log.i("TextPlanTitle_ExerciseDetailActivity_insert",planTitle);
                    intent.putExtra("update", false);
                    intent.putExtra("doExercise",true);
                    startActivity(intent);
                    return true;
                case R.id.action_delete_mode:
                    exerciseDetailAdapter.resetCheckBox();
                    exerciseDetailAdapter.setCheckBoxState(true);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case android.R.id.home:
                    intent = new Intent(mActivity,MainActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }else {
            switch (item.getItemId()) {
                case R.id.action_cancel:
                    exerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_delete:
                    for (int i = 0; i < exerciseDetailAdapter.exerciseArrayList.size(); i++) {
                        if (exerciseDetailAdapter.exerciseArrayList.get(i).isSelected()) {
                            dbHelper.deleteDetail(exerciseDetailAdapter.exerciseArrayList.get(i).getDetailTitle());
                            dbHelper.deletePlanEmpty();
                            performanceDBHelper.deletePlanTitleIsNull(exerciseDetailAdapter.exerciseArrayList.get(i).getDetailTitle(),dateValue);
                        }
                    }

                    listRenewal();

                    exerciseDetailAdapter.setCheckBoxState(false);
                    mActivity.invalidateOptionsMenu();
                    return true;
                case R.id.action_confirm:
                    if(exerciseDetailAdapter.exerciseArrayList.size()==0){
                        Toast.makeText(mActivity, "계획하신 운동(들)의 제목을 등록해주세요.", Toast.LENGTH_SHORT).show();
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
                                    ArrayList<Exercise> exerciseArrayList = dbHelper.getAllTitle();
                                    for(int i=0; i<exerciseArrayList.size(); i++){
                                        if(title.equals(exerciseArrayList.get(i).getPlanTitle())) {
                                            Toast.makeText(mActivity, "동일한 제목이 존재합니다.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }else{
                                            dbHelper.update(title);
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
    }


    private class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(doExercise) {
                if (exerciseDetailAdapter.getCheckBoxState()) {
                    exerciseDetailAdapter.exerciseArrayList.get(position).setSelected(!exerciseDetailAdapter.exerciseArrayList.get(position).isSelected());
                    if (exerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                        getSupportActionBar().setTitle("목록을 선택하세요.");
                    } else {
                        getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                    }
                    exerciseDetailAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", exerciseDetailAdapter.exerciseArrayList.get(position));
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            } else {
                if (exerciseDetailAdapter.getCheckBoxState()) {
                    exerciseDetailAdapter.exerciseArrayList.get(position).setSelected(!exerciseDetailAdapter.exerciseArrayList.get(position).isSelected());
                    if (exerciseDetailAdapter.getCountSelectedCheckBox() == 0) {
                        getSupportActionBar().setTitle("목록을 선택하세요.");
                    } else {
                        getSupportActionBar().setTitle(String.valueOf(exerciseDetailAdapter.getCountSelectedCheckBox()));
                    }
                    exerciseDetailAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mActivity, ExerciseSelectActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", exerciseDetailAdapter.exerciseArrayList.get(position));
                    intent.putExtra("update", true);
                    startActivity(intent);
                }
            }
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            if(doExercise){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setTitle(exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle());
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

                        updateTotalSetBuilder.setTitle(exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle() + " 의 새로운 세트 수를 입력하세요.");

                        final EditText editText = new EditText(mActivity);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        updateTotalSetBuilder.setView(editText);

                        updateTotalSetBuilder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String set = editText.getText().toString();
                                if(set.length()!=0){
                                    int setValue = Integer.parseInt(set);

                                    if(exerciseDetailAdapter.exerciseArrayList.get(position).getTotalSet()>setValue){
                                        for(int i=setValue+1; i<=exerciseDetailAdapter.exerciseArrayList.get(position).getTotalSet(); i++){
                                            performanceDBHelper.delete(exerciseDetailAdapter.exerciseArrayList.get(position).getPlanTitle(),exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle(),dateValue,i);
                                        }
                                    }
                                    dbHelper.update(exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle(),setValue);

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
                        dbHelper.deleteDetail(exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle());
                        dbHelper.deletePlanEmpty();
                        performanceDBHelper.delete(exerciseDetailAdapter.exerciseArrayList.get(position).getPlanTitle(),exerciseDetailAdapter.exerciseArrayList.get(position).getDetailTitle(),dateValue);
                        listRenewal();
                    }
                });
                builder.show();
            }else {
                exerciseDetailAdapter.resetCheckBox();
                exerciseDetailAdapter.setCheckBoxState(true);
                exerciseDetailAdapter.exerciseArrayList.get(position).setSelected(true);
                mActivity.invalidateOptionsMenu();
            }

            return true;
        }
    }


}
