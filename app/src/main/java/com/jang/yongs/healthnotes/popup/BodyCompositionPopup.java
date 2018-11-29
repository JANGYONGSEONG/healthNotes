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
import android.widget.Toast;

import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.jang.yongs.healthnotes.model.BodyComposition;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BodyCompositionPopup extends AppCompatActivity {

    @BindView(R.id.body_composition_popup_weight_input) EditText weight;
    @BindView (R.id.body_composition_popup_muscle_input) EditText muscleMass;
    @BindView (R.id.body_composition_popup_bodyfat_input) EditText bodyFatMass;
    @BindView (R.id.body_composition_popup_percent_bodyfat_input) EditText bodyFatPercent;
    @BindView (R.id.body_composition_popup_date_picker) DatePicker date;
    @BindView (R.id.body_composition_popup_left_button) Button cancelButton;
    @BindView (R.id.body_composition_popup_right_button) Button okButton;

    Intent mIntent;

    Activity mActivity = BodyCompositionPopup.this;

    BodyCompositionDBHelper mDBHelper;

    BodyComposition mBodyComposition = new BodyComposition();

    float mWeighValue;
    float mMuscleMassValue;
    float mBodyFatMassValue;
    float mBodyFatPercentValue;
    Date mDateValue = null;

    boolean mIsUpdate = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPopup();

        setContentView(R.layout.activity_body_composition_popup);
        ButterKnife.bind(this);

        mIntent = getIntent();
        mIsUpdate = mIntent.getBooleanExtra("update",false);

        mDBHelper = new BodyCompositionDBHelper(this.getApplicationContext(),"HealthNotes",null,1);

        setUpdate();

        setButton();

    }

    private void setPopup() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        this.setFinishOnTouchOutside(false);
    }

    private void setUpdate() {
        if(mIsUpdate){
            mDateValue = Date.valueOf(mIntent.getStringExtra("date"));
            mBodyComposition = mDBHelper.getBodyCompositionSelectedDate(mDateValue);
            weight.setText(String.valueOf(mBodyComposition.getWeight()));
            if(mBodyComposition.getMuscleMass() != -1)
                muscleMass.setText(String.valueOf(mBodyComposition.getMuscleMass()));
            if(mBodyComposition.getBodyFatMass() != -1)
                bodyFatMass.setText(String.valueOf(mBodyComposition.getBodyFatMass()));
            if(mBodyComposition.getBodyFatPercent() != -1)
                bodyFatPercent.setText(String.valueOf(mBodyComposition.getBodyFatPercent()));

            LinearLayout linearLayout  = findViewById(R.id.rootView);
            LinearLayout dateLayout = findViewById(R.id.body_composition_popup_date_layout);
            linearLayout.removeView(dateLayout);
        }
    }

    private void setButton() {

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

                ArrayList<BodyComposition> bodyCompositionArrayList = mDBHelper.getBodyCompositionList();

                if (!mBodyComposition.checkInput()) {
                    Toast.makeText(BodyCompositionPopup.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    if(!mIsUpdate) {
                        for (int i = 0; i < bodyCompositionArrayList.size(); i++) {
                            if (mBodyComposition.getDate().equals(bodyCompositionArrayList.get(i).getDate())) {
                                Toast.makeText(BodyCompositionPopup.this, "해당 날짜는 이미 입력되어 있습니다.", Toast.LENGTH_SHORT).show();
                                uniqueDate = false;
                            }
                        }
                    }
                }

                if(mBodyComposition.checkInput() && uniqueDate && !mIsUpdate){
                    mDBHelper.insertBodyComposition(mWeighValue,mMuscleMassValue,mBodyFatMassValue,mBodyFatPercentValue,mDateValue);
                    mActivity.finish();
                }else if(mBodyComposition.checkInput() && uniqueDate && mIsUpdate){
                    mDBHelper.updateBodyCompositionSelectedDate(mWeighValue,mMuscleMassValue,mBodyFatMassValue,mBodyFatPercentValue,mDateValue);
                    mActivity.finish();
                }
            }
        });
    }

    private void getValue() {

        if (weight.getText().toString().length() != 0) {
            mWeighValue = Float.parseFloat(weight.getText().toString());
            mBodyComposition.setWeight(mWeighValue);
        }else{
            mWeighValue = -1;
            mBodyComposition.setWeight(mWeighValue);
        }

        if (muscleMass.getText().toString().length() != 0) {
            mMuscleMassValue = Float.parseFloat(muscleMass.getText().toString());
            mBodyComposition.setMuscleMass(mMuscleMassValue);
        }else{
            mMuscleMassValue = -1;
            mBodyComposition.setMuscleMass(mMuscleMassValue);
        }

        if (bodyFatMass.getText().toString().length() != 0) {
            mBodyFatMassValue = Float.parseFloat(bodyFatMass.getText().toString());
            mBodyComposition.setBodyFatMass(mBodyFatMassValue);
        }else{
            mBodyFatMassValue = -1;
            mBodyComposition.setBodyFatMass(mBodyFatMassValue);
        }

        if (bodyFatPercent.getText().toString().length() != 0) {
            mBodyFatPercentValue = Float.parseFloat(bodyFatPercent.getText().toString());
            mBodyComposition.setBodyFatPercent(mBodyFatPercentValue);
        }else{
            mBodyFatPercentValue = -1;
            mBodyComposition.setBodyFatPercent(mBodyFatPercentValue);
        }


        if(!mIsUpdate) {
            int year = date.getYear();
            int month = date.getMonth() + 1;
            int day = date.getDayOfMonth();
            String dateString = "" + year + "-" + month + "-" + day;
            mDateValue = Date.valueOf(dateString);
        }
        mBodyComposition.setDate(mDateValue);
    }


}

