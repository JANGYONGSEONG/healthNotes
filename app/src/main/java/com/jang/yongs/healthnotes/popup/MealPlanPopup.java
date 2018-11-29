package com.jang.yongs.healthnotes.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.db.MealPlanDBHelper;
import com.jang.yongs.healthnotes.model.MealPlan;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealPlanPopup extends AppCompatActivity {

    @BindView(R.id.meal_plan_popup_title)
    TextView popupTitle;
    @BindView(R.id.meal_plan_popup_food_input)
    EditText food;
    @BindView(R.id.meal_plan_popup_totalCalorie_input)
    EditText calorie;
    @BindView(R.id.meal_plan_popup_carbohydrate_input)
    EditText carbohydrate;
    @BindView(R.id.meal_plan_popup_protein_input)
    EditText protein;
    @BindView(R.id.meal_plan_popup_fat_input)
    EditText fat;
    @BindView(R.id.meal_plan_popup_date_picker)
    DatePicker date;
    @BindView(R.id.meal_plan_popup_left_button)
    Button cancelButton;
    @BindView(R.id.meal_plan_popup_right_button)
    Button okButton;

    Activity mActivity = MealPlanPopup.this;

    Intent mIntent;

    MealPlanDBHelper mDBHelper;

    MealPlan mMealPlan = new MealPlan();

    int mMealOrder;
    String mFoodContent;
    int mTotalCalorieValue;
    int mCarbohydrateValue;
    int mProteinValue;
    int mFatValue;
    Date mDateValue = null;

    boolean mIsUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPopup();

        setContentView(R.layout.activity_meal_plan_popup);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mMealOrder = mIntent.getIntExtra("meal_order", 0);
        mIsUpdate = mIntent.getBooleanExtra("update", false);

        mDBHelper = new MealPlanDBHelper(this.getApplicationContext(), "HealthNotes", null, 1);

        setLayout();

        setUpdate();

        buttonSet();

    }

    private void setPopup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        this.setFinishOnTouchOutside(false);
    }

    private void setLayout() {
        popupTitle.setText(String.valueOf(mMealOrder) + "번째 식사");

        if (mMealOrder != 1 || mIsUpdate) {
            mDateValue = Date.valueOf(mIntent.getStringExtra("date"));
            LinearLayout dateLayout = findViewById(R.id.meal_plan_popup_date_layout);
            dateLayout.setVisibility(View.GONE);
        }

    }

    private void setUpdate() {
        if (mIsUpdate) {
            mMealPlan = mDBHelper.getMealPlanSelectedOrder(mDateValue, mMealOrder);
            food.setText(mMealPlan.getFood());
            calorie.setText(String.valueOf(mMealPlan.getCalorie()));
            carbohydrate.setText(String.valueOf(mMealPlan.getCarbohydrate()));
            protein.setText(String.valueOf(mMealPlan.getProtein()));
            fat.setText(String.valueOf(mMealPlan.getFat()));
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

                boolean uniqueDate = true;

                ArrayList<MealPlan> mealPlanArrayList = mDBHelper.getMealPlanTotalListEachDate();

                if (!mMealPlan.checkInput()) {
                    Toast.makeText(MealPlanPopup.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if(mMealOrder==1 && !mIsUpdate) {
                        for (int i = 0; i < mealPlanArrayList.size(); i++) {
                            if (mMealPlan.getDate().equals(mealPlanArrayList.get(i).getDate())) {
                                Toast.makeText(MealPlanPopup.this, "해당 날짜는 이미 입력되어 있습니다.", Toast.LENGTH_SHORT).show();
                                uniqueDate = false;
                            }
                        }
                    }
                }

                if (mMealPlan.checkInput() && uniqueDate && !mIsUpdate) {
                    mDBHelper.insertMealPlan(mMealOrder, mFoodContent, mTotalCalorieValue, mCarbohydrateValue, mProteinValue, mFatValue, mDateValue);
                    mActivity.finish();
                } else if (mMealPlan.checkInput() && uniqueDate && mIsUpdate) {
                    mDBHelper.updateMealPlanSelectedOrder(mFoodContent, mTotalCalorieValue, mCarbohydrateValue, mProteinValue, mFatValue, mDateValue, mMealOrder);
                    mActivity.finish();
                }
            }
        });
    }

    private void getValue() {
        if (food.getText().toString().length() != 0) {
            mFoodContent = food.getText().toString();
            mMealPlan.setFood(mFoodContent);
        }

        if (calorie.getText().toString().length() != 0) {
            mTotalCalorieValue = Integer.parseInt(calorie.getText().toString());
            mMealPlan.setCalorie(mTotalCalorieValue);
        }

        if (carbohydrate.getText().toString().length() != 0) {
            mCarbohydrateValue = Integer.parseInt(carbohydrate.getText().toString());
            mMealPlan.setCarbohydrate(mCarbohydrateValue);
        }

        if (protein.getText().toString().length() != 0) {
            mProteinValue = Integer.parseInt(protein.getText().toString());
            mMealPlan.setProtein(mProteinValue);
        }

        if (fat.getText().toString().length() != 0) {
            mFatValue = Integer.parseInt(fat.getText().toString());
            mMealPlan.setFat(mFatValue);
        }

        if (!mIsUpdate && mMealOrder == 1) {
            int year = date.getYear();
            int month = date.getMonth() + 1;
            int day = date.getDayOfMonth();
            String dateString = "" + year + "-" + month + "-" + day;
            mDateValue = Date.valueOf(dateString);
        }

        mMealPlan.setDate(mDateValue);

    }

}