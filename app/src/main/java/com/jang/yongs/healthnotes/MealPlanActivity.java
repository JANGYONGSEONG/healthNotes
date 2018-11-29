package com.jang.yongs.healthnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jang.yongs.healthnotes.adapter.MealPlanDetailAdapter;
import com.jang.yongs.healthnotes.db.MealPlanDBHelper;
import com.jang.yongs.healthnotes.model.MealPlan;
import com.jang.yongs.healthnotes.popup.MealPlanPopup;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealPlanActivity extends AppCompatActivity {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    Intent mIntent;
    Date mDate;

    Activity mActivity = this;

    MealPlanDetailAdapter mMealPlanDetailAdapter;
    ArrayList<MealPlan> mMealPlanArrayList;
    MealPlanDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mDate = Date.valueOf(mIntent.getStringExtra("Date"));

        setToolbar();

        setList();

        setFab();

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.valueOf(mDate));
    }

    private void setList() {
        mDBHelper = new MealPlanDBHelper(getApplicationContext(),"HealthNotes",null,1);

        mMealPlanArrayList =  mDBHelper.getMealPlanListSelectedDate(mDate);

        mMealPlanDetailAdapter = new MealPlanDetailAdapter(getApplicationContext(),mMealPlanArrayList);

        list.setAdapter(mMealPlanDetailAdapter);

        list.setOnItemClickListener( new MealPlanActivity.ListViewItemClickListener() );
        list.setOnItemLongClickListener( new MealPlanActivity.ListViewItemLongClickListener() );

    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mActivity,MealPlanPopup.class);
                mIntent.addFlags(mIntent.FLAG_ACTIVITY_CLEAR_TOP);
                mIntent.putExtra("meal_order", mMealPlanArrayList.get(mMealPlanArrayList.size()-1).getOrder()+1);
                mIntent.putExtra("date",String.valueOf(mDate));
                startActivity(mIntent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        listReNewal();
    }

    private void listReNewal() {
        mMealPlanDetailAdapter.mealPlanArrayList.clear();
        mMealPlanDetailAdapter.mealPlanArrayList.addAll(mDBHelper.getMealPlanListSelectedDate(mDate));
        list.setAdapter(mMealPlanDetailAdapter);
        mMealPlanDetailAdapter.notifyDataSetChanged();
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
            Intent mIntent = new Intent(mActivity,MealPlanPopup.class);
            mIntent.addFlags(mIntent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("update",true);
            mIntent.putExtra("meal_order", mMealPlanArrayList.get(position).getOrder());
            mIntent.putExtra("date",String.valueOf(mDate));
            startActivity(mIntent);
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int selectOrder = mMealPlanDetailAdapter.mealPlanArrayList.get(position).getOrder();
            final int lastOrder = mMealPlanDetailAdapter.mealPlanArrayList.get(mMealPlanDetailAdapter.mealPlanArrayList.size()-1).getOrder();

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(String.valueOf(selectOrder) + "번째 식사 삭제");
            builder.setMessage("삭제하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDBHelper.deleteMealPlanSelectedOrder(mDate,selectOrder);
                            if(selectOrder!=lastOrder){
                                mDBHelper.updateMealPlanOrderSelectedDate(mDate,selectOrder+1,lastOrder);
                            }
                            listReNewal();
                            if(mMealPlanDetailAdapter.mealPlanArrayList.size()==0)
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
