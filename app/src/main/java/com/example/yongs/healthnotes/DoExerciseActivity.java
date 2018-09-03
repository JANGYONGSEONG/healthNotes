package com.example.yongs.healthnotes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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

import com.example.yongs.healthnotes.adapter.DoExerciseAdapter;
import com.example.yongs.healthnotes.db.ExerciseDBHelper;
import com.example.yongs.healthnotes.db.PerformanceDBHelper;
import com.example.yongs.healthnotes.models.Exercise;
import com.example.yongs.healthnotes.models.Performance;

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
    Intent intent;
    Exercise exercise = null;
    int position;

    ArrayList<Exercise> exerciseArrayList;
    ExerciseDBHelper exerciseDBHelper;

    PerformanceDBHelper dbHelper;
    ArrayList<Performance> performanceArrayList;
    DoExerciseAdapter doExerciseAdapter;

    List<String> dateList = new ArrayList<String>();

    Performance performance = new Performance();
    float weightValue;
    int timesValue;

    long now = System.currentTimeMillis();
    Date dateValue = new Date(now);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);
        ButterKnife.bind(this);

        intent = getIntent();
        exercise = intent.getParcelableExtra("exercise");
        position = intent.getIntExtra("position",-1);
        exerciseDBHelper = new ExerciseDBHelper(getApplicationContext(),"HealthNotes",null,1);
        exerciseArrayList = exerciseDBHelper.getDetailThePlanTitle(exercise.getPlanTitle());
        Log.i("TestSize", "onCreate");
        setToolbar();

        setList();

        date.setText(String.valueOf(dateValue));

    }


    @Override
    protected void onResume() {
        super.onResume();

        setButton();

        setButtonVisible();

        listRenewal();
    }

    private void listRenewal() {
        doExerciseAdapter.performanceArrayList.clear();
        doExerciseAdapter.performanceArrayList.addAll(dbHelper.getAll(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue));
        list.setAdapter(doExerciseAdapter);
        doExerciseAdapter.notifyDataSetChanged();
    }

    private void setList() {
        dbHelper = new PerformanceDBHelper(this.getApplicationContext(),"HealthNotes",null,1);

        performanceArrayList = dbHelper.getAll(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue);

        doExerciseAdapter = new DoExerciseAdapter(getApplicationContext(),performanceArrayList);

        list.setAdapter(doExerciseAdapter);

        list.setOnItemClickListener( new DoExerciseActivity.ListViewItemClickListener() );

    }


    public void setButtonVisible(){
        if(dbHelper.getLastOrder(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue) == exercise.getTotalSet()){
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

                int order = dbHelper.getLastOrder(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue);

                if(!performance.checkInput()){
                    Toast.makeText(mActivity,"입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    if(order == 0){
                        dbHelper.insert(exercise.getPlanTitle(),exercise.getDetailTitle(), dateValue, 1, weightValue, timesValue);
                        listRenewal();
                        weight.setText("");
                        times.setText("");
                        performance.setWeight(-1);
                        performance.setTimes(-1);
                        weight.requestFocus();
                        setButtonVisible();
                    }else{
                        dbHelper.insert(exercise.getPlanTitle(), exercise.getDetailTitle(), dateValue, order+1, weightValue, timesValue);
                        listRenewal();
                        weight.setText("");
                        times.setText("");
                        performance.setWeight(-1);
                        performance.setTimes(-1);
                        weight.requestFocus();
                        setButtonVisible();
                    }
                }

            }
        });
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(exercise.getDetailTitle());
    }

    private void getValue() {
        if(weight.getText().toString().length() != 0) {
            weightValue = Integer.parseInt(weight.getText().toString());
            performance.setWeight(weightValue);
        }

        if(times.getText().toString().length() != 0){
            timesValue = Integer.parseInt(times.getText().toString());
            performance.setTimes(timesValue);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);



        if(exerciseArrayList.size()==1){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
            menu.findItem(R.id.action_finish).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }else{
            if(position == (exerciseArrayList.size()-1)){
                menu.findItem(R.id.action_before).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                menu.getItem(1).setVisible(false);
                menu.findItem(R.id.action_finish).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }else if(position == 0){
                menu.getItem(0).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.findItem(R.id.action_next).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                if(position!=-1){
                    Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", exerciseArrayList.get(++position));
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
                return true;
            case R.id.action_before:
                if(position!=-1){
                    Intent intent = new Intent(mActivity, DoExerciseActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("exercise", exerciseArrayList.get(--position));
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
                return true;
            case R.id.action_finish:
                intent = new Intent(mActivity,MainActivity.class);
                startActivity(intent);
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
                        dateList.clear();
                        ArrayList<Performance> dateArraylist = new ArrayList<Performance>();
                        dateArraylist.addAll(dbHelper.getAllDateDoPerformance(exercise.getPlanTitle(),exercise.getDetailTitle()));

                        if(dateArraylist.size()!=0) {
                            for (int i = 0; i < dateArraylist.size(); i++) {
                                dateList.add(String.valueOf(dateArraylist.get(i).getDate()));
                            }
                            editText.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_dropdown_item_1line, dateList));
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
                                dateValue = Date.valueOf(dateString);
                                date.setText(dateString);
                                doExerciseAdapter.performanceArrayList.clear();
                                doExerciseAdapter.performanceArrayList.addAll(dbHelper.getAll(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue));
                                list.setAdapter(doExerciseAdapter);
                                doExerciseAdapter.notifyDataSetChanged();
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
                intent = new Intent(mActivity,ExerciseDetailActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("planTitle",exercise.getPlanTitle());
                intent.putExtra("doExercise",true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

            builder.setTitle(String.valueOf(performanceArrayList.get(position).getOrder()) + " set 수정");

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


                        dbHelper.update(exercise.getPlanTitle(),exercise.getDetailTitle(),dateValue,updateWeightValue,updateTimesValue,doExerciseAdapter.performanceArrayList.get(position).getOrder());

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