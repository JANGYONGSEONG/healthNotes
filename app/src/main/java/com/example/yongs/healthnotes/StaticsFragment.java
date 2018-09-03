package com.example.yongs.healthnotes;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.example.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.example.yongs.healthnotes.db.ExerciseDBHelper;
import com.example.yongs.healthnotes.db.MealPlanDBHelper;
import com.example.yongs.healthnotes.db.PerformanceDBHelper;
import com.example.yongs.healthnotes.models.BodyComposition;
import com.example.yongs.healthnotes.models.Exercise;
import com.example.yongs.healthnotes.models.MealPlan;
import com.example.yongs.healthnotes.models.Performance;

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

    MealPlanDBHelper mealPlanDBHelper;
    ArrayList<MealPlan> mealPlanArrayList = new ArrayList<MealPlan>();

    BodyCompositionDBHelper  bodyCompositionDBHelper;
    ArrayList<BodyComposition> bodyCompositionArrayList = new ArrayList<BodyComposition>();

    ExerciseDBHelper exerciseDBHelper;
    ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();

    PerformanceDBHelper performanceDBHelper;
    ArrayList<Performance> performanceArrayList = new ArrayList<Performance>();


    List<String> planTitles = new ArrayList<String>();
    List<String> detailTitles = new ArrayList<String>();
    List<String> weights = new ArrayList<String>();



    String planTitle = null;
    String detailTitle = null;
    String weight = null;

    String state = "meal";

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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_statics, container, false);

        ButterKnife.bind(this,view);

        mealPlanDBHelper = new MealPlanDBHelper(getContext(),"HealthNotes",null,1);
        bodyCompositionDBHelper = new BodyCompositionDBHelper(getContext(),"HealthNotes",null,1);
        exerciseDBHelper = new ExerciseDBHelper(getContext(),"HealthNotes",null,1);
        performanceDBHelper = new PerformanceDBHelper(getContext(),"HealthNotes",null,1);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(state.equals("meal")){
            inflater.inflate(R.menu.menu_meal,menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("식단 그래프");
            linearLayout.setVisibility(View.GONE);
            setMealCalorie();
        }else if(state.equals("body")){
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
                state = "exercise";
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_change_meal:
                state = "meal";
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_change_bodycomposition:
                state = "body";
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

    private void setMealCalorie(){
        mealPlanArrayList = mealPlanDBHelper.getTotalPerDate();

        if(mealPlanArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            int maxValue = mealPlanArrayList.get(0).getCalorie();
            int minValue = mealPlanArrayList.get(0).getCalorie();

            XYSeries series = new XYSeries("칼로리");

            int n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mealPlanArrayList.get(i).getCalorie());

                if (maxValue < mealPlanArrayList.get(i).getCalorie())
                    maxValue = mealPlanArrayList.get(i).getCalorie();
                if (minValue > mealPlanArrayList.get(i).getCalorie())
                    minValue = mealPlanArrayList.get(i).getCalorie();
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(false, false);
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(mealPlanArrayList.size()+1);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mealPlanArrayList.get(i).getDate()));
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();
            ;

            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }
    }

    private void setMealCarbohydrate(){
        mealPlanArrayList = mealPlanDBHelper.getTotalPerDate();

        if(mealPlanArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            int maxValue = mealPlanArrayList.get(0).getCarbohydrate();
            int minValue = mealPlanArrayList.get(0).getCarbohydrate();

            XYSeries series = new XYSeries("탄수화물");

            int n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mealPlanArrayList.get(i).getCarbohydrate());

                if (maxValue < mealPlanArrayList.get(i).getCarbohydrate())
                    maxValue = mealPlanArrayList.get(i).getCarbohydrate();
                if (minValue > mealPlanArrayList.get(i).getCarbohydrate())
                    minValue = mealPlanArrayList.get(i).getCarbohydrate();

            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(mealPlanArrayList.size()+1);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mealPlanArrayList.get(i).getDate()));
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();

            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }

    }

    private void setMealProtein(){
        mealPlanArrayList = mealPlanDBHelper.getTotalPerDate();

        if(mealPlanArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            int maxValue = mealPlanArrayList.get(0).getProtein();
            int minValue = mealPlanArrayList.get(0).getProtein();

            XYSeries series = new XYSeries("단백질");

            int n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mealPlanArrayList.get(i).getProtein());

                if (maxValue < mealPlanArrayList.get(i).getProtein())
                    maxValue = mealPlanArrayList.get(i).getProtein();
                if (minValue > mealPlanArrayList.get(i).getProtein())
                    minValue = mealPlanArrayList.get(i).getProtein();

            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(mealPlanArrayList.size()+1);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mealPlanArrayList.get(i).getDate()));
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();


            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }

    }

    private void setMealFat(){
        mealPlanArrayList = mealPlanDBHelper.getTotalPerDate();

        if(mealPlanArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            int maxValue = mealPlanArrayList.get(0).getFat();
            int minValue = mealPlanArrayList.get(0).getFat();

            XYSeries series = new XYSeries("지방");

            int n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, mealPlanArrayList.get(i).getFat());

                if (maxValue < mealPlanArrayList.get(i).getFat())
                    maxValue = mealPlanArrayList.get(i).getFat();
                if (minValue > mealPlanArrayList.get(i).getFat())
                    minValue = mealPlanArrayList.get(i).getFat();
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,mealPlanArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(mealPlanArrayList.size()+1);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = mealPlanArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(mealPlanArrayList.get(i).getDate()));
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();

            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }

    }

    private void setBodyCompositionWeight(){
        bodyCompositionArrayList = bodyCompositionDBHelper.getAll();

        if(bodyCompositionArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            float maxValue = bodyCompositionArrayList.get(0).getWeight();
            float minValue = bodyCompositionArrayList.get(0).getWeight();

            XYSeries series = new XYSeries("체중");

            int n=1;

            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                series.add(n++, bodyCompositionArrayList.get(i).getWeight());

                if (maxValue < bodyCompositionArrayList.get(i).getWeight())
                    maxValue = bodyCompositionArrayList.get(i).getWeight();
                if (minValue > bodyCompositionArrayList.get(i).getWeight())
                    minValue = bodyCompositionArrayList.get(i).getWeight();

                Log.i("TestSizeStatics1",String.valueOf(bodyCompositionArrayList.get(i).getDate()));
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(bodyCompositionArrayList.size()+1);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                mRenderer.addXTextLabel(n++,String.valueOf(bodyCompositionArrayList.get(i).getDate()));

                Log.i("TestSizeStatics2",String.valueOf(bodyCompositionArrayList.get(i).getDate()));
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();

            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);

        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }
    }

    private void setBodyCompositionMuscleMass(){

        bodyCompositionArrayList = bodyCompositionDBHelper.getAll();

        if(bodyCompositionArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            float maxValue = bodyCompositionArrayList.get(0).getMuscleMass();
            float minValue = bodyCompositionArrayList.get(0).getMuscleMass();

            XYSeries series = new XYSeries("골격근량");

            int n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getMuscleMass()!=-1) {
                    series.add(n++, bodyCompositionArrayList.get(i).getMuscleMass());

                    if (maxValue < bodyCompositionArrayList.get(i).getMuscleMass())
                        maxValue = bodyCompositionArrayList.get(i).getMuscleMass();
                    if (minValue > bodyCompositionArrayList.get(i).getMuscleMass())
                        minValue = bodyCompositionArrayList.get(i).getMuscleMass();
                }
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(n);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getMuscleMass()!=-1) {
                    mRenderer.addXTextLabel(n++, String.valueOf(bodyCompositionArrayList.get(i).getDate()));
                }
            }

            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();


            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }

    }

    private void setBodyCompositionBodyFatMass(){

        bodyCompositionArrayList = bodyCompositionDBHelper.getAll();

        if(bodyCompositionArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            float maxValue = bodyCompositionArrayList.get(0).getBodyFatMass();
            float minValue = bodyCompositionArrayList.get(0).getBodyFatMass();

            XYSeries series = new XYSeries("체지방량");

            int n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getBodyFatMass()!=-1){
                    series.add(n++, bodyCompositionArrayList.get(i).getBodyFatMass());

                    if (maxValue < bodyCompositionArrayList.get(i).getBodyFatMass())
                        maxValue = bodyCompositionArrayList.get(i).getBodyFatMass();
                    if (minValue > bodyCompositionArrayList.get(i).getBodyFatMass())
                        minValue = bodyCompositionArrayList.get(i).getBodyFatMass();
                }
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(n);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getBodyFatMass()!=-1)
                    mRenderer.addXTextLabel(n++,String.valueOf(bodyCompositionArrayList.get(i).getDate()));
            }


            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();


            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }
    }

    private void setBodyCompositionPercentBodyFat(){

        bodyCompositionArrayList = bodyCompositionDBHelper.getAll();

        if(bodyCompositionArrayList.size()!=0) {
            chartLyt.setVisibility(View.VISIBLE);
            empty_data.setVisibility(View.GONE);
            float maxValue = bodyCompositionArrayList.get(0).getPercentBodyFat();
            float minValue = bodyCompositionArrayList.get(0).getPercentBodyFat();

            XYSeries series = new XYSeries("체지방률");

            int n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getPercentBodyFat()!=-1){
                    series.add(n++, bodyCompositionArrayList.get(i).getPercentBodyFat());

                    if (maxValue < bodyCompositionArrayList.get(i).getPercentBodyFat())
                        maxValue = bodyCompositionArrayList.get(i).getPercentBodyFat();
                    if (minValue > bodyCompositionArrayList.get(i).getPercentBodyFat())
                        minValue = bodyCompositionArrayList.get(i).getPercentBodyFat();
                }

            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(series);

            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(1);
            renderer.setColor(Color.parseColor("#F3C645"));
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(10);

            XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
            mRenderer.addSeriesRenderer(renderer);

            mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            mRenderer.setPanEnabled(true, false);
            mRenderer.setZoomEnabled(true,false);
            mRenderer.setPanLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setZoomLimits(new double[] {0,bodyCompositionArrayList.size(),0,0});
            mRenderer.setChartTitleTextSize(50);
            mRenderer.setLabelsTextSize(30);
            mRenderer.setBarSpacing(1);

            mRenderer.setXAxisMin(0);
            mRenderer.setXAxisMax(n);
            mRenderer.setYAxisMin(minValue * 0.75);
            mRenderer.setYAxisMax(maxValue * 1.25);

            mRenderer.setXLabels(0);

            n=1;
            for (int i = bodyCompositionArrayList.size() - 1; i >= 0; i--) {
                if(bodyCompositionArrayList.get(i).getPercentBodyFat()!=-1){
                    mRenderer.addXTextLabel(n++,String.valueOf(bodyCompositionArrayList.get(i).getDate()));
                }
            }


            mRenderer.setShowGrid(true);

            chartLyt.removeAllViews();

            GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

            chartLyt.addView(chartView, 0);
        }else{
            empty_data.setVisibility(View.VISIBLE);
            chartLyt.setVisibility(View.GONE);
        }
    }

    private void setExerciseTimes(){
        weightTextView.setVisibility(View.VISIBLE);
        empty_data.setVisibility(View.VISIBLE);
        chartLyt.setVisibility(View.GONE);
        planTitleTextView.setText("");
        detailTitleTextView.setText("");
        weightTextView.setText("");
        planTitle=null;
        detailTitle=null;
        weight=null;

        planTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planTitles.clear();
                detailTitles.clear();
                weights.clear();
                detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));
                weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, weights));
                detailTitleTextView.setText("");
                weightTextView.setText("");

                exerciseArrayList = exerciseDBHelper.getAllTitle();

                if(exerciseArrayList.size()!=0) {
                    for (int i = 0; i < exerciseArrayList.size(); i++) {
                        planTitles.add(exerciseArrayList.get(i).getPlanTitle());
                    }
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, planTitles));
                }
                planTitleTextView.showDropDown();
            }
        });

        detailTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailTitles.clear();
                weights.clear();
                weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, weights));
                weightTextView.setText("");

                if(planTitleTextView.getText().toString().length()!=0){
                    planTitle = planTitleTextView.getText().toString();
                    exerciseArrayList = exerciseDBHelper.getDetailThePlanTitle(planTitle);
                    if(exerciseArrayList.size()!=0) {
                        for (int i = 0; i < exerciseArrayList.size(); i++) {
                            detailTitles.add(exerciseArrayList.get(i).getDetailTitle());
                        }
                        detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));
                    }
                }
                detailTitleTextView.showDropDown();
            }
        });

        weightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weights.clear();
                if(detailTitleTextView.getText().toString().length()!=0){
                    detailTitle = detailTitleTextView.getText().toString();
                    performanceArrayList = performanceDBHelper.getAll(planTitle,detailTitle);
                    if(performanceArrayList.size()!=0) {
                        for (int i = 0; i < performanceArrayList.size(); i++) {
                            if(!weights.contains(String.valueOf(performanceArrayList.get(i).getWeight()))) {
                                weights.add(String.valueOf(performanceArrayList.get(i).getWeight()));
                            }
                        }
                        weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, weights));
                    }
                }
                weightTextView.showDropDown();
            }
        });


        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                planTitle = planTitleTextView.getText().toString();
                detailTitle = detailTitleTextView.getText().toString();
                weight = weightTextView.getText().toString();

                if(planTitle.length()==0 || detailTitle.length()==0 || weight.length()==0) {
                    Toast.makeText(getActivity(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else if(!planTitles.contains(planTitle) || !detailTitles.contains(detailTitle) || !weights.contains(weight)) {
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else{
                    empty_data.setVisibility(View.GONE);
                    chartLyt.setVisibility(View.VISIBLE);
                    performanceArrayList = performanceDBHelper.getAll(planTitle,detailTitle,Float.parseFloat(weight));

                    if(performanceArrayList.size()!=0) {
                        empty_data.setVisibility(View.GONE);
                        int maxValue = performanceArrayList.get(0).getTimes();
                        int minValue = performanceArrayList.get(0).getTimes();

                        XYSeries series = new XYSeries("중량별 반복 횟수 비교");

                        int n=1;
                        for (int i = performanceArrayList.size() - 1; i >= 0; i--) {
                            series.add(n++, performanceArrayList.get(i).getTimes());

                            if (maxValue < performanceArrayList.get(i).getTimes())
                                maxValue = performanceArrayList.get(i).getTimes();
                            if (minValue > performanceArrayList.get(i).getTimes())
                                minValue = performanceArrayList.get(i).getTimes();
                        }

                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

                        dataset.addSeries(series);

                        XYSeriesRenderer renderer = new XYSeriesRenderer();
                        renderer.setLineWidth(1);
                        renderer.setColor(Color.parseColor("#F3C645"));
                        renderer.setDisplayBoundingPoints(true);
                        renderer.setPointStyle(PointStyle.CIRCLE);
                        renderer.setPointStrokeWidth(10);

                        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
                        mRenderer.addSeriesRenderer(renderer);

                        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        mRenderer.setPanEnabled(false, false);
                        mRenderer.setPanEnabled(true, false);
                        mRenderer.setZoomEnabled(true,false);
                        mRenderer.setPanLimits(new double[] {0,performanceArrayList.size(),0,0});
                        mRenderer.setZoomLimits(new double[] {0,performanceArrayList.size(),0,0});
                        mRenderer.setChartTitleTextSize(50);
                        mRenderer.setLabelsTextSize(30);
                        mRenderer.setBarSpacing(1);

                        mRenderer.setXAxisMin(0);
                        mRenderer.setXAxisMax(performanceArrayList.size()+1);
                        mRenderer.setYAxisMin(minValue * 0.75);
                        mRenderer.setYAxisMax(maxValue * 1.25);

                        mRenderer.setXLabels(0);

                        n=1;
                        for (int i = performanceArrayList.size() - 1; i >= 0; i--) {
                            mRenderer.addXTextLabel(n++,String.valueOf(performanceArrayList.get(i).getDate()) + "\n" + String.valueOf(performanceArrayList.get(i).getOrder()) + "set");
                        }

                        mRenderer.setShowGrid(true);

                        chartLyt.removeAllViews();


                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

                        chartLyt.addView(chartView, 0);
                    }else{
                        empty_data.setVisibility(View.VISIBLE);
                        chartLyt.setVisibility(View.GONE);
                    }
                    planTitleTextView.setText("");
                    detailTitleTextView.setText("");
                    weightTextView.setText("");
                    planTitle=null;
                    detailTitle=null;
                    weight=null;
                    planTitles.clear();
                    detailTitles.clear();
                    weights.clear();
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, planTitles));
                    detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));
                    weightTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, weights));

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
        planTitle=null;
        detailTitle=null;

        planTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planTitles.clear();
                detailTitles.clear();
                detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));
                detailTitleTextView.setText("");

                exerciseArrayList = exerciseDBHelper.getAllTitle();

                if(exerciseArrayList.size()!=0) {
                    for (int i = 0; i < exerciseArrayList.size(); i++) {
                        planTitles.add(exerciseArrayList.get(i).getPlanTitle());
                    }
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, planTitles));
                }
                planTitleTextView.showDropDown();
            }
        });

        detailTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailTitles.clear();
                if(planTitleTextView.getText().toString().length()!=0){
                    planTitle = planTitleTextView.getText().toString();
                    exerciseArrayList = exerciseDBHelper.getDetailThePlanTitle(planTitle);
                    if(exerciseArrayList.size()!=0) {
                        for (int i = 0; i < exerciseArrayList.size(); i++) {
                            detailTitles.add(exerciseArrayList.get(i).getDetailTitle());
                        }
                        detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));
                    }
                }
                detailTitleTextView.showDropDown();
            }
        });

        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                planTitle = planTitleTextView.getText().toString();
                detailTitle = detailTitleTextView.getText().toString();

                if(planTitle.length()==0 || detailTitle.length()==0) {
                    Toast.makeText(getActivity(), "입력되지 않은 값이 있습니다.", Toast.LENGTH_SHORT).show();
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else if(!planTitles.contains(planTitle) || !detailTitles.contains(detailTitle) ) {
                    empty_data.setVisibility(View.VISIBLE);
                    chartLyt.setVisibility(View.GONE);
                }else{
                    empty_data.setVisibility(View.GONE);
                    chartLyt.setVisibility(View.VISIBLE);
                    performanceArrayList = performanceDBHelper.getAllMaximum(planTitle,detailTitle);

                    if(performanceArrayList.size()!=0) {
                        empty_data.setVisibility(View.GONE);
                        float maxValue = performanceArrayList.get(0).getWeight();
                        float minValue = performanceArrayList.get(0).getWeight();

                        XYSeries series = new XYSeries("날짜별 최대 중량 비교");

                        int n=1;
                        for (int i = performanceArrayList.size()-1; i >= 0; i--) {
                            series.add(n++, performanceArrayList.get(i).getWeight());

                            if (maxValue < performanceArrayList.get(i).getWeight())
                                maxValue = performanceArrayList.get(i).getWeight();
                            if (minValue > performanceArrayList.get(i).getWeight())
                                minValue = performanceArrayList.get(i).getWeight();
                        }

                        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

                        dataset.addSeries(series);

                        XYSeriesRenderer renderer = new XYSeriesRenderer();
                        renderer.setLineWidth(1);
                        renderer.setColor(Color.parseColor("#F3C645"));
                        renderer.setDisplayBoundingPoints(true);
                        renderer.setPointStyle(PointStyle.CIRCLE);
                        renderer.setPointStrokeWidth(10);

                        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
                        mRenderer.addSeriesRenderer(renderer);

                        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                        mRenderer.setPanEnabled(false, false);
                        mRenderer.setPanEnabled(true, false);
                        mRenderer.setZoomEnabled(true,false);
                        mRenderer.setPanLimits(new double[] {0,performanceArrayList.size(),0,0});
                        mRenderer.setZoomLimits(new double[] {0,performanceArrayList.size(),0,0});
                        mRenderer.setChartTitleTextSize(50);
                        mRenderer.setLabelsTextSize(30);
                        mRenderer.setBarSpacing(1);

                        mRenderer.setXAxisMin(0);
                        mRenderer.setXAxisMax(performanceArrayList.size()+1);
                        mRenderer.setYAxisMin(minValue * 0.75);
                        mRenderer.setYAxisMax(maxValue * 1.25);

                        mRenderer.setXLabels(0);

                        n=1;
                        for (int i = performanceArrayList.size() - 1; i >= 0; i--) {
                            mRenderer.addXTextLabel(n++,String.valueOf(performanceArrayList.get(i).getDate()));
                        }

                        mRenderer.setShowGrid(true);

                        chartLyt.removeAllViews();


                        GraphicalView chartView = ChartFactory.getLineChartView(getActivity(), dataset, mRenderer);

                        chartLyt.addView(chartView, 0);
                    }else{
                        empty_data.setVisibility(View.VISIBLE);
                        chartLyt.setVisibility(View.GONE);
                    }
                    planTitleTextView.setText("");
                    detailTitleTextView.setText("");
                    planTitle=null;
                    detailTitle=null;
                    planTitles.clear();
                    detailTitles.clear();
                    planTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, planTitles));
                    detailTitleTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, detailTitles));

                    imm.hideSoftInputFromWindow(detailTitleTextView.getWindowToken(),0);
                }
            }
        });
    }

}
