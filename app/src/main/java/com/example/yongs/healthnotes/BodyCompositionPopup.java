package com.example.yongs.healthnotes;

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

import com.example.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.example.yongs.healthnotes.models.BodyComposition;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BodyCompositionPopup extends AppCompatActivity {

    @BindView(R.id.body_composition_popup_weight_input) EditText weight;
    @BindView (R.id.body_composition_popup_muscle_input) EditText muscleMass;
    @BindView (R.id.body_composition_popup_bodyfat_input) EditText bodyFatMass;
    @BindView (R.id.body_composition_popup_percent_bodyfat_input) EditText percentBodyFat;
    @BindView (R.id.body_composition_popup_date_picker) DatePicker date;
    @BindView (R.id.body_composition_popup_left_button) Button cancelButton;
    @BindView (R.id.body_composition_popup_right_button) Button okButton;

    Intent intent;

    Activity mActivity = BodyCompositionPopup.this;

    BodyCompositionDBHelper dbhelper;

    BodyComposition bodyComposition = new BodyComposition();

    float weightValue;
    float muscleMassValue;
    float bodyFatMassValue;
    float percentBodyFatValue;
    Date dateValue = null;

    boolean update = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPopup();

        setContentView(R.layout.activity_body_composition_popup);
        ButterKnife.bind(this);

        intent = getIntent();
        update = intent.getBooleanExtra("update",false);

        dbhelper = new BodyCompositionDBHelper(this.getApplicationContext(),"HealthNotes",null,1);

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
        if(update){
            dateValue = Date.valueOf(intent.getStringExtra("date"));
            bodyComposition = dbhelper.getBodyComposition(dateValue);
            weight.setText(String.valueOf(bodyComposition.getWeight()));
            if(bodyComposition.getMuscleMass() != -1)
                muscleMass.setText(String.valueOf(bodyComposition.getMuscleMass()));
            if(bodyComposition.getBodyFatMass() != -1)
                bodyFatMass.setText(String.valueOf(bodyComposition.getBodyFatMass()));
            if(bodyComposition.getPercentBodyFat() != -1)
                percentBodyFat.setText(String.valueOf(bodyComposition.getPercentBodyFat()));

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

                ArrayList<BodyComposition> bodyCompositionArrayList = dbhelper.getAll();

                if (!bodyComposition.checkInput()) {
                    Toast.makeText(BodyCompositionPopup.this, "입력하지 않은 내용이 있습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    for (int i = 0; i < bodyCompositionArrayList.size(); i++) {
                        if (bodyComposition.getDate().equals(bodyCompositionArrayList.get(i).getDate()) && !update) {
                            Toast.makeText(BodyCompositionPopup.this, "해당 날짜는 이미 입력되어 있습니다.", Toast.LENGTH_SHORT).show();
                            uniqueDate = false;
                        }
                    }
                }

                if(bodyComposition.checkInput() && uniqueDate && !update){
                    dbhelper.insert(weightValue,muscleMassValue,bodyFatMassValue,percentBodyFatValue,dateValue);
                    mActivity.finish();
                }else if(bodyComposition.checkInput() && uniqueDate && update){
                    dbhelper.update(weightValue,muscleMassValue,bodyFatMassValue,percentBodyFatValue,dateValue);
                    mActivity.finish();
                }
            }
        });
    }

    private void getValue() {

        if (weight.getText().toString().length() != 0) {
            weightValue = Float.parseFloat(weight.getText().toString());
            bodyComposition.setWeight(weightValue);
        }else{
            weightValue = -1;
            bodyComposition.setWeight(weightValue);
        }

        if (muscleMass.getText().toString().length() != 0) {
            muscleMassValue = Float.parseFloat(muscleMass.getText().toString());
            bodyComposition.setMuscleMass(muscleMassValue);
        }else{
            muscleMassValue = -1;
            bodyComposition.setMuscleMass(muscleMassValue);
        }

        if (bodyFatMass.getText().toString().length() != 0) {
            bodyFatMassValue = Float.parseFloat(bodyFatMass.getText().toString());
            bodyComposition.setBodyFatMass(bodyFatMassValue);
        }else{
            bodyFatMassValue = -1;
            bodyComposition.setBodyFatMass(bodyFatMassValue);
        }

        if (percentBodyFat.getText().toString().length() != 0) {
            percentBodyFatValue = Float.parseFloat(percentBodyFat.getText().toString());
            bodyComposition.setPercentBodyFat(percentBodyFatValue);
        }else{
            percentBodyFatValue = -1;
            bodyComposition.setPercentBodyFat(percentBodyFatValue);
        }


        if(!update) {
            int year = date.getYear();
            int month = date.getMonth() + 1;
            int day = date.getDayOfMonth();
            String dateString = "" + year + "-" + month + "-" + day;
            dateValue = Date.valueOf(dateString);
        }
            bodyComposition.setDate(dateValue);
    }


}

