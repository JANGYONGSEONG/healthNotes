package com.example.yongs.healthnotes;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yongs.healthnotes.adapter.ExercisePlanAdapter;
import com.example.yongs.healthnotes.db.ExerciseDBHelper;
import com.example.yongs.healthnotes.models.Exercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.empty_list) TextView emptyListItem;
    @BindView(R.id.fab) FloatingActionButton fab;

    ExercisePlanAdapter exercisePlanAdapter;
    ArrayList<Exercise> exerciseArrayList;
    ArrayList<Exercise> exerciseDetailTitleArrayList;
    String detailDescribe;
    ExerciseDBHelper dbHelper;
    CheckBox checkBox;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    ActivityCompat.finishAffinity(getActivity());
                    System.runFinalization();
                    System.exit(0);
                }
                return false;
            }
        });

        ButterKnife.bind(this, view);

        dbHelper = new ExerciseDBHelper(getContext(), "HealthNotes", null, 1);

        ArrayList<Exercise> exerciseWrongDetailArrayList = new ArrayList<Exercise>();
        exerciseWrongDetailArrayList = dbHelper.getAllPlanTitleIsNull();

        for (int j = 0; j < exerciseWrongDetailArrayList.size(); j++) {
            dbHelper.deleteWrongDetail(exerciseWrongDetailArrayList.get(j).getDetailTitle());
        }

        exerciseArrayList = dbHelper.getAllTitle();

        for(int i=0;i<exerciseArrayList.size();i++){
            exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exerciseArrayList.get(i).getPlanTitle());
            if(exerciseDetailTitleArrayList.size() != 1) {
                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                exerciseArrayList.get(i).setDetailDescribe(detailDescribe + " 외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
            }else{
                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
            }
        }

        exercisePlanAdapter = new ExercisePlanAdapter(this.getActivity(), exerciseArrayList);

        list.setAdapter(exercisePlanAdapter);

        list.setOnItemClickListener(new ListViewItemClickListener());
        list.setOnItemLongClickListener(new ListViewItemLongClickListener());

        list.setEmptyView(emptyListItem);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExerciseSelectActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Exercise> exerciseWrongDetailArrayList = new ArrayList<Exercise>();
        exerciseWrongDetailArrayList = dbHelper.getAllPlanTitleIsNull();

        for (int j = 0; j < exerciseWrongDetailArrayList.size(); j++) {
                dbHelper.deleteWrongDetail(exerciseWrongDetailArrayList.get(j).getDetailTitle());
        }

        exercisePlanAdapter.exerciseArrayList.clear();
        exercisePlanAdapter.exerciseArrayList.addAll(dbHelper.getAllTitle());

        for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++){
            exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
            if(exerciseDetailTitleArrayList.size() != 1) {
                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe + "외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
            }else{
                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
            }
        }
        list.setAdapter(exercisePlanAdapter);
        exercisePlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(exercisePlanAdapter.getCheckBoxState()){
            inflater.inflate(R.menu.menu_delete, menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(exercisePlanAdapter.getCountSelectedCheckBox()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            checkBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkBox.setChecked(isChecked);
                    for(int i=0; i<exercisePlanAdapter.exerciseArrayList.size(); i++) {
                        exercisePlanAdapter.exerciseArrayList.get(i).setSelected(checkBox.isChecked());
                    }
                    if(exercisePlanAdapter.getCountSelectedCheckBox() == 0){
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    }else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(exercisePlanAdapter.getCountSelectedCheckBox()));
                    }
                    exercisePlanAdapter.notifyDataSetChanged();
                }
            });
        }else{
            inflater.inflate(R.menu.menu_search, menu);

            final MenuItem search = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            searchView.setQueryHint("운동 계획명 혹은 운동 목록명을 입력해주세요.");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String inputValue){

                    ArrayList<Exercise> tmpArrayList = new ArrayList<Exercise>();

                    exercisePlanAdapter.exerciseArrayList.clear();
                    exercisePlanAdapter.exerciseArrayList.addAll(dbHelper.getAllTitle());
                    for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++){
                        exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                        if(exerciseDetailTitleArrayList.size() != 1) {
                            detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                            exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe + "외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
                        }else{
                            detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                            exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
                        }
                    }
                    list.setAdapter(exercisePlanAdapter);
                    exercisePlanAdapter.notifyDataSetChanged();

                    for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++ ) {
                        if (inputValue.equals(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle())) {
                            tmpArrayList.add(exercisePlanAdapter.exerciseArrayList.get(i));
                            exercisePlanAdapter.exerciseArrayList.clear();
                            exercisePlanAdapter.exerciseArrayList.addAll(tmpArrayList);
                            list.setAdapter(exercisePlanAdapter);
                            exercisePlanAdapter.notifyDataSetChanged();
                            return true;
                        }
                    }


                    for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++ ){

                        exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());

                        for(int j=0;j<exerciseDetailTitleArrayList.size();j++){
                            if(inputValue.equals(exerciseDetailTitleArrayList.get(j).getDetailTitle())){
                                if(!tmpArrayList.contains(dbHelper.getThePlanByDetailTitle(exerciseDetailTitleArrayList.get(j).getDetailTitle())))
                                    tmpArrayList.addAll(dbHelper.getThePlanByDetailTitle(exerciseDetailTitleArrayList.get(j).getDetailTitle()));
                            }
                        }
                    }


                    exercisePlanAdapter.exerciseArrayList.clear();
                    exercisePlanAdapter.exerciseArrayList.addAll(tmpArrayList);
                    if(exercisePlanAdapter.exerciseArrayList.size()>0){
                        for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++){
                            exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                            if(exerciseDetailTitleArrayList.size() != 1) {
                                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                                exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe + "외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
                            }else{
                                detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                                exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
                            }
                        }

                        list.setAdapter(exercisePlanAdapter);
                        exercisePlanAdapter.notifyDataSetChanged();
                        return true;
                    }else{
                            exercisePlanAdapter.exerciseArrayList.clear();
                            list.setAdapter(exercisePlanAdapter);
                            exercisePlanAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), "운동 계획명 또는 운동 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                            return false;
                        }

                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

            });

                    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                        @Override
                        public boolean onClose() {
                    exercisePlanAdapter.exerciseArrayList.clear();
                    exercisePlanAdapter.exerciseArrayList.addAll(dbHelper.getAllTitle());

                    for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++){
                        exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                        if(exerciseDetailTitleArrayList.size() != 1) {
                            detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                            exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe + "외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
                        }else{
                            detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                            exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
                        }
                    }
                    list.setAdapter(exercisePlanAdapter);
                    exercisePlanAdapter.notifyDataSetChanged();

                    searchView.onActionViewCollapsed();
                    searchView.setQuery("",false);
                    searchView.clearFocus();

                    return true;
                }
            });

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("운동");
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                exercisePlanAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_delete:
                for (int i = 0; i < exercisePlanAdapter.exerciseArrayList.size(); i++) {
                    if (exercisePlanAdapter.exerciseArrayList.get(i).isSelected()) {
                        dbHelper.deletePlan(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                    }
                }

                exercisePlanAdapter.exerciseArrayList.clear();
                exercisePlanAdapter.exerciseArrayList.addAll(dbHelper.getAllTitle());

                for(int i=0;i<exercisePlanAdapter.exerciseArrayList.size();i++){
                    exerciseDetailTitleArrayList =  dbHelper.getAllDetailTitle(exercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                    if(exerciseDetailTitleArrayList.size() != 1) {
                        detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                        exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe + "외 " + String.valueOf(exerciseDetailTitleArrayList.size() - 1));
                    }else{
                        detailDescribe = exerciseDetailTitleArrayList.get(0).getDetailTitle();
                        exercisePlanAdapter.exerciseArrayList.get(i).setDetailDescribe(detailDescribe);
                    }
                }
                list.setAdapter(exercisePlanAdapter);
                exercisePlanAdapter.notifyDataSetChanged();

                exercisePlanAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_search:
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ListViewItemClickListener implements android.widget.AdapterView.OnItemClickListener {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (exercisePlanAdapter.getCheckBoxState()) {
                    exercisePlanAdapter.exerciseArrayList.get(position).setSelected(!exercisePlanAdapter.exerciseArrayList.get(position).isSelected());
                    if (exercisePlanAdapter.getCountSelectedCheckBox() == 0) {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    } else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(exercisePlanAdapter.getCountSelectedCheckBox()));
                    }
                    exercisePlanAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(getActivity(),ExerciseDetailActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("doExercise",true);
                    intent.putExtra("planTitle",exercisePlanAdapter.exerciseArrayList.get(position).getPlanTitle());
                    Log.i("TextPlanTitle_ExerciseFragment",exercisePlanAdapter.exerciseArrayList.get(position).getPlanTitle());
                    startActivity(intent);
                }
            }
        }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                exercisePlanAdapter.resetCheckBox();
                exercisePlanAdapter.setCheckBoxState(true);
                exercisePlanAdapter.exerciseArrayList.get(position).setSelected(true);
                getActivity().invalidateOptionsMenu();

                return true;

            }
        }


}


