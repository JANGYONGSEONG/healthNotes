package com.jang.yongs.healthnotes.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jang.yongs.healthnotes.MainActivity;
import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.jang.yongs.healthnotes.db.ExerciseDBHelper;
import com.jang.yongs.healthnotes.db.MealPlanDBHelper;
import com.jang.yongs.healthnotes.db.PerformanceDBHelper;
import com.jang.yongs.healthnotes.model.BodyComposition;
import com.jang.yongs.healthnotes.model.Exercise;
import com.jang.yongs.healthnotes.model.HealthNotesModels;
import com.jang.yongs.healthnotes.model.MealPlan;
import com.jang.yongs.healthnotes.model.Performance;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaticsFragment extends Fragment {

    @BindView(R.id.chart) LinearLayout chartLyt;
    @BindView(R.id.empty_data) TextView empty_data;

    @BindView(R.id.statics_exercise) LinearLayout linearLayout;
    @BindView(R.id.statics_plan_title) AutoCompleteTextView planTitleTextView;
    @BindView(R.id.statics_detail_title) AutoCompleteTextView detailTitleTextView;
    @BindView(R.id.statics_weight) AutoCompleteTextView weightTextView;
    @BindView(R.id.statics_button) Button btn;

    InputMethodManager imm;

    MealPlanDBHelper mMealPlanDBHelper;
    ArrayList<MealPlan> mMealPlanArrayList = new ArrayList<MealPlan>();

    BodyCompositionDBHelper mBodyCompositionDBHelper;
    ArrayList<BodyComposition> mBodyCompositionArrayList = new ArrayList<BodyComposition>();

    ExerciseDBHelper mExerciseDBHelper;
    ArrayList<Exercise> mExerciseArrayList = new ArrayList<Exercise>();

    PerformanceDBHelper mPerformanceDBHelper;
    ArrayList<Performance> mPerformanceArrayList = new ArrayList<Performance>();

    List<String> mPlanTitles = new ArrayList<String>();
    List<String> mDetailTitles = new ArrayList<String>();
    List<String> mWeights = new ArrayList<String>();

    String mPlanTitle = null;
    String mExerciseTitle = null;
    String mWeight = null;

    String mState = "meal";

    XYSeries series;
    XYMultipleSeriesRenderer mRenderer;
    XYMultipleSeriesDataset dataset;

    public StaticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = getActivity().getCurrentFocus();
        if(view != null){
            imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_statics, container, false);

        ButterKnife.bind(this,view);

        mMealPlanDBHelper = new MealPlanDBHelper(getContext(),"HealthNotes",null,1);
        mBodyCompositionDBHelper = new BodyCompositionDBHelper(getContext(),"HealthNotes",null,1);
        mExerciseDBHelper = new ExerciseDBHelper(getContext(),"HealthNotes",null,1);
        mPerformanceDBHelper = new PerformanceDBHelper(getContext(),"HealthNotes",null,1);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mState.equals("meal")){
            inflater.inflate(R.menu.menu_meal,menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("식단 그래프");
            linearLayout.setVisibility(View.GONE);
            setMealCalorie();
        }else if(mState.equals("body")){
            inflater.inflate(R.menu.menu_body,menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("체성분 그래프");
            linearLayout.setVisibility(View.GONE);
            setBodyCompositionWeight();
        }else{
            inflater.inflate(R.menu.menu_exercise,menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("운동 그래프");
            linearLayout.setVisibility(View.VISIBLE);
            setExerciseTimes();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_exercise:
                mState = "exercise";
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_change_meal:
                mState = "meal";
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_change_bodycomposition:
                mState = "body";
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_change_calorie:
                setMealCalorie();
                return true;
            case R.id.action_change_carbohydrate:
                setMealCarbohydrate();
                return true;
            case R.id.action_change_protein:
                setMealProtein();
                return true;
            case R.id.action_change_fat:
                setMealFat();
                return true;
            case R.id.action_change_weight:
                setBodyCompositionWeight();
                return true;
            case R.id.action_change_muscle:
                setBodyCompositionMuscleMass();
                return true;
            case R.id.action_change_bodyfat:
                setBodyCompositionBodyFatMass();
                return true;
            case R.id.action_change_bodyfat_percent:
                setBodyCompositionPercentBodyFat();
                return true;
            case R.id.action_change_exercise_search_times:
                setExerciseTimes();
                return true;
            case R.id.action_change_exercise_search_maximum:
                setExerciseWeight();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setEmptyChart(){
        empty_data.setVisibility(View.VISIBLE);
        chartLyt.setVisibility(View.GONE);
    }

    private <E extends HealthNotesModels> void setIntegerChart(ArrayList<E> arrayList, int minValue, int maxValue){
        chartLyt.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.GONE);

        dataset = new XYMultipleSeriesDataset();

        dataset.addSeries(series);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(1);
        renderer.setColor(Color.parseColor("#F3C645"));
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(10);

        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setPanEnabled(false, false);
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true,false);
        mRenderer.setPanLimits(new double[] {0,arrayList.size(),0,0});
        mRenderer.setZoomLimits(new double[] {0,arrayList.size(),0,0});
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setBarSpacing(1);

        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(arrayList.size()+1);
        mRenderer.setYAxisMin(minValue * 0.75);
        mRenderer.setYAxisMax(maxValue * 1.25);

        mRenderer.setXLabels(0);

        mRenderer.setShowGrid(true);

        chartLyt.removeAllViews();


        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

        chartLyt.addView(chartView, 0);

    }

    private <E extends HealthNotesModels> void setFloatChart(ArrayList<E> arrayList, float minValue, float maxValue){
        chartLyt.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.GONE);

        dataset = new XYMultipleSeriesDataset();

        dataset.addSeries(series);

        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setLineWidth(1);
        renderer.setColor(Color.parseColor("#F3C645"));
        renderer.setDisplayBoundingPoints(true);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setPointStrokeWidth(10);

        mRenderer = new XYMultipleSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
        mRenderer.setPanEnabled(false, false);
        mRenderer.setPanEnabled(true, false);
        mRenderer.setZoomEnabled(true,false);
        mRenderer.setPanLimits(new double[] {0,arrayList.size(),0,0});
        mRenderer.setZoomLimits(new double[] {0,arrayList.size(),0,0});
        mRenderer.setChartTitleTextSize(50);
        mRenderer.setLabelsTextSize(30);
        mRenderer.setBarSpacing(1);

        mRenderer.setXAxisMin(0);
        mRenderer.setXAxisMax(arrayList.size()+1);
        mRenderer.setYAxisMin(minValue * 0.75);
        mRenderer.setYAxisMax(maxValue * 1.25);

        mRenderer.setXLabels(0);

        mRenderer.setShowGrid(true);

        chartLyt.removeAllViews();


        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

        chartLyt.addView(chartView, 0);

    }

    private void setMealCalorie(){
        mMealPlanArrayList = mMealPlanDBHelper.getMealPlanTotalListEachDate();

        if(mMealPlanArrayList.size()!=0) {
            int maxValue = mMealPlanArrayList.get(0).getCalorie();
            int minValue = mMealPlanArrayList.get(0).getCalorie();

            series = new XYSeries("칼로리");

            int n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mMealPlanArrayList.get(i).getCalorie());

                if (maxValue < mMealPlanArrayList.get(i).getCalorie())
                    maxValue = mMealPlanArrayList.get(i).getCalorie();
                if (minValue > mMealPlanArrayList.get(i).getCalorie())
                    minValue = mMealPlanArrayList.get(i).getCalorie();
            }

            setIntegerChart(mMealPlanArrayList,minValue,maxValue);

            n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mMealPlanArrayList.get(i).getDate()));
            }
        }else{
            setEmptyChart();
        }
    }

    private void setMealCarbohydrate(){
        mMealPlanArrayList = mMealPlanDBHelper.getMealPlanTotalListEachDate();

        if(mMealPlanArrayList.size()!=0) {
            int maxValue = mMealPlanArrayList.get(0).getCarbohydrate();
            int minValue = mMealPlanArrayList.get(0).getCarbohydrate();

            series = new XYSeries("탄수화물");

            int n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mMealPlanArrayList.get(i).getCarbohydrate());

                if (maxValue < mMealPlanArrayList.get(i).getCarbohydrate())
                    maxValue = mMealPlanArrayList.get(i).getCarbohydrate();
                if (minValue > mMealPlanArrayList.get(i).getCarbohydrate())
                    minValue = mMealPlanArrayList.get(i).getCarbohydrate();

            }

            setIntegerChart(mMealPlanArrayList,minValue,maxValue);

            n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mMealPlanArrayList.get(i).getDate()));
            }
        }else{
            setEmptyChart();
        }

    }

    private void setMealProtein(){
        mMealPlanArrayList = mMealPlanDBHelper.getMealPlanTotalListEachDate();

        if(mMealPlanArrayList.size()!=0) {
            int maxValue = mMealPlanArrayList.get(0).getProtein();
            int minValue = mMealPlanArrayList.get(0).getProtein();

            series = new XYSeries("단백질");

            int n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mMealPlanArrayList.get(i).getProtein());

                if (maxValue < mMealPlanArrayList.get(i).getProtein())
                    maxValue = mMealPlanArrayList.get(i).getProtein();
                if (minValue > mMealPlanArrayList.get(i).getProtein())
                    minValue = mMealPlanArrayList.get(i).getProtein();

            }

            setIntegerChart(mMealPlanArrayList,minValue,maxValue);

            n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mMealPlanArrayList.get(i).getDate()));
            }
        }else{
            setEmptyChart();
        }

    }

    private void setMealFat(){
        mMealPlanArrayList = mMealPlanDBHelper.getMealPlanTotalListEachDate();

        if(mMealPlanArrayList.size()!=0) {
            int maxValue = mMealPlanArrayList.get(0).getFat();
            int minValue = mMealPlanArrayList.get(0).getFat();

            series = new XYSeries("지방");

            int n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mMealPlanArrayList.get(i).getFat());

                if (maxValue < mMealPlanArrayList.get(i).getFat())
                    maxValue = mMealPlanArrayList.get(i).getFat();
                if (minValue > mMealPlanArrayList.get(i).getFat())
                    minValue = mMealPlanArrayList.get(i).getFat();
            }

            setIntegerChart(mMealPlanArrayList,minValue,maxValue);

            n=1;
            for (int i = mMealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mMealPlanArrayList.get(i).getDate()));
            }

        }else{
            setEmptyChart();
        }

    }

    private void setBodyCompositionWeight(){
        mBodyCompositionArrayList = mBodyCompositionDBHelper.getBodyCompositionList();

        if(mBodyCompositionArrayList.size()!=0) {
            float maxValue = mBodyCompositionArrayList.get(0).getWeight();
            float minValue = mBodyCompositionArrayList.get(0).getWeight();

            series = new XYSeries("체중");

            int n=1;

            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mBodyCompositionArrayList.get(i).getWeight());

                if (maxValue < mBodyCompositionArrayList.get(i).getWeight())
                    maxValue = mBodyCompositionArrayList.get(i).getWeight();
                if (minValue > mBodyCompositionArrayList.get(i).getWeight())
                    minValue = mBodyCompositionArrayList.get(i).getWeight();
            }

            setFloatChart(mBodyCompositionArrayList,minValue,maxValue);

            n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mBodyCompositionArrayList.get(i).getDate()));

            }
        }else{
            setEmptyChart();
        }
    }

    private void setBodyCompositionMuscleMass(){

        mBodyCompositionArrayList = mBodyCompositionDBHelper.getBodyCompositionList();

        if(mBodyCompositionArrayList.size()!=0) {
            float maxValue = mBodyCompositionArrayList.get(0).getMuscleMass();
            float minValue = mBodyCompositionArrayList.get(0).getMuscleMass();

            series = new XYSeries("골격근량");

            int n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getMuscleMass()!=-1) {
                    series.add(n++, mBodyCompositionArrayList.get(i).getMuscleMass());

                    if (maxValue < mBodyCompositionArrayList.get(i).getMuscleMass())
                        maxValue = mBodyCompositionArrayList.get(i).getMuscleMass();
                    if (minValue > mBodyCompositionArrayList.get(i).getMuscleMass())
                        minValue = mBodyCompositionArrayList.get(i).getMuscleMass();
                }
            }

            setFloatChart(mBodyCompositionArrayList,minValue,maxValue);

            n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getMuscleMass()!=-1) {
                    mRenderer.addXTextLabel(n++, String.valueOf(mBodyCompositionArrayList.get(i).getDate()));
                }
            }
        }else{
            setEmptyChart();
        }

    }

    private void setBodyCompositionBodyFatMass(){

        mBodyCompositionArrayList = mBodyCompositionDBHelper.getBodyCompositionList();

        if(mBodyCompositionArrayList.size()!=0) {
            float maxValue = mBodyCompositionArrayList.get(0).getBodyFatMass();
            float minValue = mBodyCompositionArrayList.get(0).getBodyFatMass();

            series = new XYSeries("체지방량");

            int n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getBodyFatMass()!=-1){
                    series.add(n++, mBodyCompositionArrayList.get(i).getBodyFatMass());

                    if (maxValue < mBodyCompositionArrayList.get(i).getBodyFatMass())
                        maxValue = mBodyCompositionArrayList.get(i).getBodyFatMass();
                    if (minValue > mBodyCompositionArrayList.get(i).getBodyFatMass())
                        minValue = mBodyCompositionArrayList.get(i).getBodyFatMass();
                }
            }

            setFloatChart(mBodyCompositionArrayList,minValue,maxValue);

            n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getBodyFatMass()!=-1)
                    mRenderer.addXTextLabel(n++,String.valueOf(mBodyCompositionArrayList.get(i).getDate()));
            }
        }else{
            setEmptyChart();
        }
    }

    private void setBodyCompositionPercentBodyFat(){

        mBodyCompositionArrayList = mBodyCompositionDBHelper.getBodyCompositionList();

        if(mBodyCompositionArrayList.size()!=0) {
            float maxValue = mBodyCompositionArrayList.get(0).getBodyFatPercent();
            float minValue = mBodyCompositionArrayList.get(0).getBodyFatPercent();

            series = new XYSeries("체지방률");

            int n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getBodyFatPercent()!=-1){
                    series.add(n++, mBodyCompositionArrayList.get(i).getBodyFatPercent());

                    if (maxValue < mBodyCompositionArrayList.get(i).getBodyFatPercent())
                        maxValue = mBodyCompositionArrayList.get(i).getBodyFatPercent();
                    if (minValue > mBodyCompositionArrayList.get(i).getBodyFatPercent())
                        minValue = mBodyCompositionArrayList.get(i).getBodyFatPercent();
                }

            }

            setFloatChart(mBodyCompositionArrayList,minValue,maxValue);

            n=1;
            for (int i = mBodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(mBodyCompositionArrayList.get(i).getBodyFatPercent()!=-1){
                    mRenderer.addXTextLabel(n++,String.valueOf(mBodyCompositionArrayList.get(i).getDate()));
                }
            }
        }else{
            setEmptyChart();
        }
    }

    private void setExerciseTimes(){
        weightTextView.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.VISIBLE);
        chartLyt.setVisibility(View.GONE);
        planTitleTextView.setText("");
        detailTitleTextView.setText("");
        weightTextView.setText("");
        mPlanTitle=null;
        mExerciseTitle=null;
        mWeight=null;

        planTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlanTitles.clear();
                mDetailTitles.clear();
                mWeights.clear();
                detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));
                weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mWeights));
                detailTitleTextView.setText("");
                weightTextView.setText("");

                mExerciseArrayList = mExerciseDBHelper.getExercisePlanList();

                if(mExerciseArrayList.size()!=0) {
                    for (int i = 0; i < mExerciseArrayList.size(); i++) {
                        mPlanTitles.add(mExerciseArrayList.get(i).getPlanTitle());
                    }
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mPlanTitles));
                }
                planTitleTextView.showDropDown();
            }
        });

        detailTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailTitles.clear();
                mWeights.clear();
                weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mWeights));
                weightTextView.setText("");

                if(planTitleTextView.getText().toString().length()!=0){
                    mPlanTitle = planTitleTextView.getText().toString();
                    mExerciseArrayList = mExerciseDBHelper.getExerciseListSelectedPlan(mPlanTitle);
                    if(mExerciseArrayList.size()!=0) {
                        for (int i = 0; i < mExerciseArrayList.size(); i++) {
                            mDetailTitles.add(mExerciseArrayList.get(i).getExerciseTitle());
                        }
                        detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));
                    }
                }
                detailTitleTextView.showDropDown();
            }
        });

        weightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeights.clear();
                if(detailTitleTextView.getText().toString().length()!=0){
                    mExerciseTitle = detailTitleTextView.getText().toString();
                    mPerformanceArrayList = mPerformanceDBHelper.getPerformance(mPlanTitle,mExerciseTitle);
                    if(mPerformanceArrayList.size()!=0) {
                        for (int i = 0; i < mPerformanceArrayList.size(); i++) {
                            if(!mWeights.contains(String.valueOf(mPerformanceArrayList.get(i).getWeight()))) {
                                mWeights.add(String.valueOf(mPerformanceArrayList.get(i).getWeight()));
                            }
                        }
                        weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mWeights));
                    }
                }
                weightTextView.showDropDown();
            }
        });


        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlanTitle = planTitleTextView.getText().toString();
                mExerciseTitle = detailTitleTextView.getText().toString();
                mWeight = weightTextView.getText().toString();

                if(mPlanTitle.length()==0 || mExerciseTitle.length()==0 || mWeight.length()==0) {
                    Toast.makeText(getActivity(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else if(!mPlanTitles.contains(mPlanTitle) || !mDetailTitles.contains(mExerciseTitle) || !mWeights.contains(mWeight)) {
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else{
                    mPerformanceArrayList = mPerformanceDBHelper.getPerformanceSelectedWeight(mPlanTitle,mExerciseTitle,Float.parseFloat(mWeight));

                    if(mPerformanceArrayList.size()!=0) {
                        int maxValue = mPerformanceArrayList.get(0).getTimes();
                        int minValue = mPerformanceArrayList.get(0).getTimes();

                        series = new XYSeries("중량별 반복 횟수 비교");

                        int n=1;
                        for (int i = mPerformanceArrayList.size() - 1; i >= 0; i--) {
                            series.add(n++, mPerformanceArrayList.get(i).getTimes());

                            if (maxValue < mPerformanceArrayList.get(i).getTimes())
                                maxValue = mPerformanceArrayList.get(i).getTimes();
                            if (minValue > mPerformanceArrayList.get(i).getTimes())
                                minValue = mPerformanceArrayList.get(i).getTimes();
                        }

                        setIntegerChart(mPerformanceArrayList,minValue,maxValue);

                        n=1;
                        for (int i = mPerformanceArrayList.size() - 1; i >= 0; i--) {
                            mRenderer.addXTextLabel(n++,String.valueOf(mPerformanceArrayList.get(i).getDate()) + "\n" + String.valueOf(mPerformanceArrayList.get(i).getOrder()) + "set");
                        }
                    }else{
                        setEmptyChart();
                    }
                    planTitleTextView.setText("");
                    detailTitleTextView.setText("");
                    weightTextView.setText("");
                    mPlanTitle=null;
                    mExerciseTitle=null;
                    mWeight=null;
                    mPlanTitles.clear();
                    mDetailTitles.clear();
                    mWeights.clear();
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mPlanTitles));
                    detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));
                    weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mWeights));

                    imm.hideSoftInputFromWindow(weightTextView.getWindowToken(),0);

                }
            }
        });
    }

    private void setExerciseWeight(){
        weightTextView.setVisibility(View.GONE);
        empty_data.setVisibility(View.VISIBLE);
        chartLyt.setVisibility(View.GONE);
        planTitleTextView.setText("");
        detailTitleTextView.setText("");
        mPlanTitle=null;
        mExerciseTitle=null;

        planTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlanTitles.clear();
                mDetailTitles.clear();
                detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));
                detailTitleTextView.setText("");

                mExerciseArrayList = mExerciseDBHelper.getExercisePlanList();

                if(mExerciseArrayList.size()!=0) {
                    for (int i = 0; i < mExerciseArrayList.size(); i++) {
                        mPlanTitles.add(mExerciseArrayList.get(i).getPlanTitle());
                    }
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mPlanTitles));
                }
                planTitleTextView.showDropDown();
            }
        });

        detailTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailTitles.clear();
                if(planTitleTextView.getText().toString().length()!=0){
                    mPlanTitle = planTitleTextView.getText().toString();
                    mExerciseArrayList = mExerciseDBHelper.getExerciseListSelectedPlan(mPlanTitle);
                    if(mExerciseArrayList.size()!=0) {
                        for (int i = 0; i < mExerciseArrayList.size(); i++) {
                            mDetailTitles.add(mExerciseArrayList.get(i).getExerciseTitle());
                        }
                        detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));
                    }
                }
                detailTitleTextView.showDropDown();
            }
        });

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlanTitle = planTitleTextView.getText().toString();
                mExerciseTitle = detailTitleTextView.getText().toString();

                if(mPlanTitle.length()==0 || mExerciseTitle.length()==0) {
                    Toast.makeText(getActivity(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else if(!mPlanTitles.contains(mPlanTitle) || !mDetailTitles.contains(mExerciseTitle) ) {
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else{
                    mPerformanceArrayList = mPerformanceDBHelper.getPerformanceMaximum(mPlanTitle,mExerciseTitle);

                    if(mPerformanceArrayList.size()!=0) {
                        float maxValue = mPerformanceArrayList.get(0).getWeight();
                        float minValue = mPerformanceArrayList.get(0).getWeight();

                        series = new XYSeries("날짜별 최대 중량 비교");

                        int n=1;
                        for (int i = mPerformanceArrayList.size()-1; i >= 0; i--) {
                            series.add(n++, mPerformanceArrayList.get(i).getWeight());

                            if (maxValue < mPerformanceArrayList.get(i).getWeight())
                                maxValue = mPerformanceArrayList.get(i).getWeight();
                            if (minValue > mPerformanceArrayList.get(i).getWeight())
                                minValue = mPerformanceArrayList.get(i).getWeight();
                        }

                        setFloatChart(mPerformanceArrayList,minValue,maxValue);

                        n=1;
                        for (int i = mPerformanceArrayList.size() - 1; i >= 0; i--) {
                            mRenderer.addXTextLabel(n++,String.valueOf(mPerformanceArrayList.get(i).getDate()));
                        }
                    }else{
                        setEmptyChart();
                    }
                    planTitleTextView.setText("");
                    detailTitleTextView.setText("");
                    mPlanTitle=null;
                    mExerciseTitle=null;
                    mPlanTitles.clear();
                    mDetailTitles.clear();
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mPlanTitles));
                    detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, mDetailTitles));

                    imm.hideSoftInputFromWindow(detailTitleTextView.getWindowToken(),0);
                }
            }
        });
    }

}
