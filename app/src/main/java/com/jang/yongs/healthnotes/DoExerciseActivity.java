package com.jang.yongs.healthnotes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jang.yongs.healthnotes.adapter.DoExerciseAdapter;
import com.jang.yongs.healthnotes.db.ExerciseDBHelper;
import com.jang.yongs.healthnotes.db.PerformanceDBHelper;
import com.jang.yongs.healthnotes.model.Exercise;
import com.jang.yongs.healthnotes.model.Performance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoExerciseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list) ListView list;
    @BindView(R.id.empty_list) TextView emptyListItem;
    @BindView(R.id.do_exercise_weight_input) EditText weight;
    @BindView(R.id.do_exercise_times_input) EditText times;
    @BindView(R.id.do_exercise_record_button) Button recordButton;
    @BindView(R.id.do_exercise_date) TextView date;


    Activity mActivity = DoExerciseActivity.this;
    Intent mIntent;
    Exercise mExercise = null;
    int mPosition;  //현재위치이며 다음운동 또는 이전운동을 알기위해 사용된다.

    ArrayList<Exercise> mExerciseArrayList;
    ExerciseDBHelper mExerciseDBHelper;

    PerformanceDBHelper mPerformanceDBHelper;
    ArrayList<Performance> mPerformanceArrayList;
    DoExerciseAdapter mDoExerciseAdapter;

    List<String> mDateList = new ArrayList<String>();

    Performance mPerformance = new Performance();
    float mWeightValue;
    int mTimesValue;

    long now = System.currentTimeMillis();
    Date mDateValue = new Date(now);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mExercise = mIntent.getParcelableExtra("exercise");
        mPosition = mIntent.getIntExtra("position",-1);
        mExerciseDBHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);
        mExerciseArrayList = mExerciseDBHelper.getExerciseListSelectedPlan(mExercise.getPlanTitle());

        setToolbar();

        setList();

        date.setText(String.valueOf(mDateValue));

    }


    @Override
    protected void onResume() {
        super.onResume();

        setButton();

        setButtonVisible();

        listRenewal();
    }

    private void listRenewal() {
        mDoExerciseAdapter.performanceArrayList.clear();
        mDoExerciseAdapter.performanceArrayList.addAll(mPerformanceDBHelper.getPerformanceSelectedDate(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue));
        list.setAdapter(mDoExerciseAdapter);
        mDoExerciseAdapter.notifyDataSetChanged();
    }

    private void setList() {
        mPerformanceDBHelper = new PerformanceDBHelper(this.getApplicationContext(),"HealthNotes",null,1);

        mPerformanceArrayList = mPerformanceDBHelper.getPerformanceSelectedDate(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue);

        mDoExerciseAdapter = new DoExerciseAdapter(getApplicationContext(),mPerformanceArrayList);

        list.setAdapter(mDoExerciseAdapter);

        list.setOnItemClickListener( new DoExerciseActivity.ListViewItemClickListener() );

    }


    public void setButtonVisible(){
        if(mPerformanceDBHelper.getLastOrder(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue) == mExercise.getTotalSet()){
            LinearLayout linearlayout = findViewById(R.id.do_exercise_input);
            linearlayout.setVisibility(View.GONE);
        }else{
            LinearLayout linearlayout = findViewById(R.id.do_exercise_input);
            linearlayout.setVisibility(View.VISIBLE);
        }

    }

    private void setButton() {

        recordButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();

                int order = mPerformanceDBHelper.getLastOrder(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue);

                if(!mPerformance.checkInput()){
                    Toast.makeText(mActivity,"입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    if(order == 0){
                        mPerformanceDBHelper.insertPerformance(mExercise.getPlanTitle(),mExercise.getExerciseTitle(), mDateValue, 1, mWeightValue, mTimesValue, mExercise.getId());
                        listRenewal();
                        inputRenewal();
                    }else{
                        mPerformanceDBHelper.insertPerformance(mExercise.getPlanTitle(), mExercise.getExerciseTitle(), mDateValue, order+1, mWeightValue, mTimesValue, mExercise.getId());
                        listRenewal();
                        inputRenewal();
                    }
                }

            }
        });
    }

    private void inputRenewal(){
        weight.setText("");
        times.setText("");
        mPerformance.setWeight(-1);
        mPerformance.setTimes(-1);
        weight.requestFocus();
        setButtonVisible();
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mExercise.getExerciseTitle());
    }

    private void getValue() {
        if(weight.getText().toString().length() != 0) {
            mWeightValue = Float.parseFloat(weight.getText().toString());
            mPerformance.setWeight(mWeightValue);
        }

        if(times.getText().toString().length() != 0){
            mTimesValue = Integer.parseInt(times.getText().toString());
            mPerformance.setTimes(mTimesValue);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);

        if(mExerciseArrayList.size()==1){   //ExercisePlan의 Exercise가 1개인 경우
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.findItem(R.id.action_finish).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }else{
            if(mPosition == (mExerciseArrayList.size()-1)){
                menu.findItem(R.id.action_before).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.getItem(1).setVisible(false);
                menu.findItem(R.id.action_finish).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }else if(mPosition == 0){
                menu.getItem(0).setVisible(false);
                menu.findItem(R.id.action_next).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.getItem(2).setVisible(false);
            }else{
                menu.findItem(R.id.action_before).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.findItem(R.id.action_next).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.getItem(2).setVisible(false);
            }
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_next:
                if(mPosition!=-1){
                    Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", mExerciseArrayList.get(++mPosition));
                    intent.putExtra("position",mPosition);
                    startActivity(intent);
                }
                return true;
            case R.id.action_before:
                if(mPosition!=-1){
                    Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", mExerciseArrayList.get(--mPosition));
                    intent.putExtra("position",mPosition);
                    startActivity(intent);
                }
                return true;
            case R.id.action_finish:
                mIntent = new Intent(mActivity,MainActivity.class);
                startActivity(mIntent);
                return true;
            case R.id.action_review:
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

                builder.setTitle("날짜 변경");

                final AutoCompleteTextView editText = new AutoCompleteTextView(mActivity);
                editText.setInputType(InputType.TYPE_CLASS_DATETIME);
                editText.setThreshold(0);

                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDateList.clear();
                        ArrayList<Performance> dateArraylist = new ArrayList<Performance>();
                        dateArraylist.addAll(mPerformanceDBHelper.getPerformanceDateList(mExercise.getPlanTitle(),mExercise.getExerciseTitle()));

                        if(dateArraylist.size()!=0) {
                            for (int i = 0; i < dateArraylist.size(); i++) {
                                mDateList.add(String.valueOf(dateArraylist.get(i).getDate()));
                            }
                            editText.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_dropdown_item_1line, mDateList));
                        }
                        editText.showDropDown();
                    }
                });

                builder.setView(editText);

                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String dateString = editText.getText().toString();

                        if(dateString.split("-").length==3){
                            if(dateString.split("-")[0].length()==4 && dateString.split("-")[1].length()==2 && dateString.split("-")[2].length()==2){
                                mDateValue = Date.valueOf(dateString);
                                date.setText(dateString);
                                mDoExerciseAdapter.performanceArrayList.clear();
                                mDoExerciseAdapter.performanceArrayList.addAll(mPerformanceDBHelper.getPerformanceSelectedDate(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue));
                                list.setAdapter(mDoExerciseAdapter);
                                mDoExerciseAdapter.notifyDataSetChanged();
                                listRenewal();

                                Date date1 = new Date(now);

                                if(dateString.equals(String.valueOf(date1))){
                                    setButtonVisible();
                                }else{
                                    LinearLayout linearlayout = findViewById(R.id.do_exercise_input);
                                    linearlayout.setVisibility(View.GONE);
                                }
                                dialog.dismiss();
                            }else{
                                Toast.makeText(mActivity, "2018-01-01 형태로 입력해 주세요", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mActivity, "2018-01-01 형태로 입력해 주세요", Toast.LENGTH_SHORT).show();
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
            case android.R.id.home:
                mIntent = new Intent(mActivity,ExerciseDetailActivity.class);
                mIntent.addFlags(mIntent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent.putExtra("planTitle",mExercise.getPlanTitle());
                mIntent.putExtra("doExercise",true);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            builder.setTitle(String.valueOf(mPerformanceArrayList.get(position).getOrder()) + " set 수정");

            LinearLayout layout = new LinearLayout(view.getContext());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            final EditText weightEditText = new EditText(mActivity);
            weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            TextView textView1 = new TextView(mActivity);
            textView1.setText(" Kg X ");

            final EditText timesEditText = new EditText(mActivity);
            timesEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            TextView textView2 = new TextView(mActivity);
            textView2.setText("회");

            layout.addView(weightEditText);
            layout.addView(textView1);
            layout.addView(timesEditText);
            layout.addView(textView2);
            builder.setView(layout);

            builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String weight = weightEditText.getText().toString();
                    String times = timesEditText.getText().toString();
                    if(weight.length()!=0 && times.length()!=0){
                        int updateWeightValue = Integer.parseInt(weight);
                        int updateTimesValue = Integer.parseInt(times);


                        mPerformanceDBHelper.updatePerformance(mExercise.getPlanTitle(),mExercise.getExerciseTitle(),mDateValue,updateWeightValue,updateTimesValue,mDoExerciseAdapter.performanceArrayList.get(position).getOrder());

                        listRenewal();

                        dialog.dismiss();
                    }else{
                        Toast.makeText(mActivity, "모든 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
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

        }
    }
}