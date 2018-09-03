package com.example.yongs.healthnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yongs.healthnotes.adapter.MealPlanDetailAdapter;
import com.example.yongs.healthnotes.db.MealPlanDBHelper;
import com.example.yongs.healthnotes.models.MealPlan;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealPlanActivity extends AppCompatActivity {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    Intent intent;
    Date date;

    Activity mActivity = this;

    MealPlanDetailAdapter mealPlanDetailAdapter;
    ArrayList<MealPlan> mealPlanArrayList;
    MealPlanDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        ButterKnife.bind(this);

        intent = getIntent();
        date = Date.valueOf(intent.getStringExtra("Date"));

        setToolbar();

        setList();

        setFab();

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.valueOf(date));
    }

    private void setList() {
        dbHelper = new MealPlanDBHelper(getApplicationContext(),"HealthNotes",null,1);

        mealPlanArrayList =  dbHelper.getAllTheDate(date);

        mealPlanDetailAdapter = new MealPlanDetailAdapter(getApplicationContext(),mealPlanArrayList);

        list.setAdapter(mealPlanDetailAdapter);

        list.setOnItemClickListener( new MealPlanActivity.ListViewItemClickListener() );
        list.setOnItemLongClickListener( new MealPlanActivity.ListViewItemLongClickListener() );

    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("TestDateActivity",String.valueOf(date));
                Intent intent = new Intent(mActivity,MealPlanPopup.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("meal_order", mealPlanArrayList.get(mealPlanArrayList.size()-1).getOrder()+1);
                intent.putExtra("date",String.valueOf(date));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        listReNewal();
    }

    private void listReNewal() {
        mealPlanDetailAdapter.mealPlanArrayList.clear();
        mealPlanDetailAdapter.mealPlanArrayList.addAll(dbHelper.getAllTheDate(date));
        list.setAdapter(mealPlanDetailAdapter);
        mealPlanDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mActivity.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mActivity,MealPlanPopup.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("update",true);
            intent.putExtra("meal_order", mealPlanArrayList.get(position).getOrder());
            intent.putExtra("date",String.valueOf(date));
            startActivity(intent);
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int selectOrder = mealPlanDetailAdapter.mealPlanArrayList.get(position).getOrder();
            final int lastOrder = mealPlanDetailAdapter.mealPlanArrayList.get(mealPlanDetailAdapter.mealPlanArrayList.size()-1).getOrder();

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(String.valueOf(selectOrder) + "번째 식사 삭제");
            builder.setMessage("삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.delete(date,selectOrder);
                            if(selectOrder!=lastOrder){
                                dbHelper.updateOrder(date,selectOrder+1,lastOrder);
                            }
                            listReNewal();
                            if(mealPlanDetailAdapter.mealPlanArrayList.size()==0)
                                mActivity.finish();
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.show();

            return true;
        }
    }
}
