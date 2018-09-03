package com.example.yongs.healthnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yongs.healthnotes.db.MealPlanDBHelper;
import com.example.yongs.healthnotes.models.MealPlan;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealPlanPopup extends AppCompatActivity{

    @BindView (R.id.meal_plan_popup_title) TextView popupTitle;
    @BindView (R.id.meal_plan_popup_food_input) EditText food;
    @BindView (R.id.meal_plan_popup_totalCalorie_input) EditText calorie;
    @BindView (R.id.meal_plan_popup_carbohydrate_input) EditText carbohydrate;
    @BindView (R.id.meal_plan_popup_protein_input) EditText protein;
    @BindView (R.id.meal_plan_popup_fat_input) EditText fat;
    @BindView (R.id.meal_plan_popup_date_picker) DatePicker date;
    @BindView (R.id.meal_plan_popup_left_button) Button cancelButton;
    @BindView (R.id.meal_plan_popup_right_button) Button okButton;

    Intent intent;

    Activity mActivity = MealPlanPopup.this;

    MealPlanDBHelper dbhelper;

    MealPlan mealPlan = new MealPlan();

    int mealOrder;
    String foodContent;
    int totalCalorieValue;
    int carbohydrateValue;
    int proteinValue;
    int fatValue;
    Date dateValue = null;

    boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPopup();

        setContentView(R.layout.activity_meal_plan_popup);
        ButterKnife.bind(this);

        intent = getIntent();
        mealOrder = intent.getIntExtra("meal_order",0);
        update = intent.getBooleanExtra("update",false);

        dbhelper = new MealPlanDBHelper(this.getApplicationContext(),"HealthNotes",null,1);

        setLayout();

        setUpdate();

        buttonSet();

    }

    private void setPopup(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        this.setFinishOnTouchOutside(false);
    }

    private void setLayout() {
        if(mealOrder!=0) {
            popupTitle.setText(String.valueOf(mealOrder)+"번째 식사");

            if(mealOrder!=1 || update){
                dateValue = Date.valueOf(intent.getStringExtra("date"));
                LinearLayout linearLayout  = findViewById(R.id.rootView);
                LinearLayout dateLayout = findViewById(R.id.meal_plan_popup_date_layout);
                dateLayout.setVisibility(View.GONE);
            }
        }
    }

    private void setUpdate() {
        if(update){
            mealPlan = dbhelper.getMealPlan(dateValue,mealOrder);
            food.setText(mealPlan.getFood());
            calorie.setText(String.valueOf(mealPlan.getCalorie()));
            carbohydrate.setText(String.valueOf(mealPlan.getCarbohydrate()));
            protein.setText(String.valueOf(mealPlan.getProtein()));
            fat.setText(String.valueOf(mealPlan.getFat()));
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

                boolean uniqueDate = true;

                ArrayList<MealPlan> mealPlanArrayList = dbhelper.getAll();

                if(!mealPlan.checkInput()){
                    Toast.makeText(MealPlanPopup.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    for (int i = 0; i < mealPlanArrayList.size(); i++) {
                        if (mealOrder == 1 && mealPlan.getDate().equals(mealPlanArrayList.get(i).getDate()) && !update) {
                            Toast.makeText(MealPlanPopup.this, "해당 날짜는 이미 입력되어 있습니다.", Toast.LENGTH_SHORT).show();
                            uniqueDate = false;
                        }
                    }
                }

                if(mealPlan.checkInput() && uniqueDate && !update){
                    Log.i("TestDatePopup",String.valueOf(dateValue));
                    dbhelper.insert(mealOrder,foodContent,totalCalorieValue,carbohydrateValue,proteinValue,fatValue,dateValue);
                    mActivity.finish();
                }else if(mealPlan.checkInput() && uniqueDate && update){
                    Log.i("TestDatePopup",String.valueOf(dateValue));
                    dbhelper.update(foodContent,totalCalorieValue,carbohydrateValue,proteinValue,fatValue,dateValue,mealOrder);
                    mActivity.finish();
                }
            }
        });
    }

        private void getValue() {
        if(food.getText().toString().length() != 0) {
            foodContent = food.getText().toString();
            mealPlan.setFood(foodContent);
        }

        if(calorie.getText().toString().length() != 0) {
            totalCalorieValue = Integer.parseInt(calorie.getText().toString());
            mealPlan.setCalorie(totalCalorieValue);
        }

        if(carbohydrate.getText().toString().length() != 0) {
            carbohydrateValue = Integer.parseInt(carbohydrate.getText().toString());
            mealPlan.setCarbohydrate(carbohydrateValue);
        }

        if(protein.getText().toString().length() != 0) {
            proteinValue = Integer.parseInt(protein.getText().toString());
            mealPlan.setProtein(proteinValue);
        }

        if(fat.getText().toString().length() != 0) {
            fatValue = Integer.parseInt(fat.getText().toString());
            mealPlan.setFat(fatValue);
        }

        if(!update && mealOrder==1) {
            int year = date.getYear();
            int month = date.getMonth() + 1;
            int day = date.getDayOfMonth();
            String dateString = "" + year + "-" + month + "-" + day;
            dateValue = Date.valueOf(dateString);
        }

        mealPlan.setDate(dateValue);

    }

}