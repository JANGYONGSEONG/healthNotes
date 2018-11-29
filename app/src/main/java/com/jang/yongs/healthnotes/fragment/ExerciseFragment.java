package com.jang.yongs.healthnotes.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
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

import com.jang.yongs.healthnotes.ExerciseDetailActivity;
import com.jang.yongs.healthnotes.ExerciseSelectActivity;
import com.jang.yongs.healthnotes.MainActivity;
import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.adapter.ExercisePlanAdapter;
import com.jang.yongs.healthnotes.db.ExerciseDBHelper;
import com.jang.yongs.healthnotes.db.PerformanceDBHelper;
import com.jang.yongs.healthnotes.model.Exercise;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment {

    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.empty_list)
    TextView emptyListItem;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    ExercisePlanAdapter mExercisePlanAdapter;
    ArrayList<Exercise> mExercisePlanArrayList;
    ArrayList<Exercise> mExerciseContentArrayList;
    String mExerciseDescribe;
    ExerciseDBHelper mDBHelper;
    PerformanceDBHelper mPerformanceDBHelper;
    CheckBox mCheckBox;

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
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ActivityCompat.finishAffinity(getActivity());
                    System.runFinalization();
                    System.exit(0);
                }
                return false;
            }
        });

        ButterKnife.bind(this, view);

        mPerformanceDBHelper = new PerformanceDBHelper(getContext(), "HealthNotes", null, 1);

        setList();

        setFab();

        return view;
    }

    private void setList() {
        mDBHelper = new ExerciseDBHelper(getContext(), "HealthNotes", null, 1);

        deleteInCompleteExercise();

        mExercisePlanArrayList = mDBHelper.getExercisePlanList();

        setExerciseDescribe();

        mExercisePlanAdapter = new ExercisePlanAdapter(this.getActivity(), mExercisePlanArrayList);

        list.setAdapter(mExercisePlanAdapter);

        list.setOnItemClickListener(new ListViewItemClickListener());
        list.setOnItemLongClickListener(new ListViewItemLongClickListener());

        list.setEmptyView(emptyListItem);
    }

    private void deleteInCompleteExercise() {
        ArrayList<Exercise> InCompleteExerciseArrayList = new ArrayList<Exercise>();
        InCompleteExerciseArrayList = mDBHelper.getExerciseListPlanTitleIsNull();

        for (int j = 0; j < InCompleteExerciseArrayList.size(); j++) {
            mDBHelper.deleteExerciseListExercisePlanTitleIsNull(InCompleteExerciseArrayList.get(j).getExerciseTitle());
        }
    }

    private void setExerciseDescribe() {
        for (int i = 0; i < mExercisePlanArrayList.size(); i++) {
            mExerciseContentArrayList = mDBHelper.getExerciseTitleListSelectedPlan(mExercisePlanArrayList.get(i).getPlanTitle());
            if (mExerciseContentArrayList.size() != 1) {
                mExerciseDescribe = mExerciseContentArrayList.get(0).getExerciseTitle();
                mExercisePlanArrayList.get(i).setExerciseDescribe(mExerciseDescribe + " 외 " + String.valueOf(mExerciseContentArrayList.size() - 1));
            } else {
                mExerciseDescribe = mExerciseContentArrayList.get(0).getExerciseTitle();
                mExercisePlanArrayList.get(i).setExerciseDescribe(mExerciseDescribe);
            }
        }
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExerciseSelectActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }

    private void listRenewal() {
        mExercisePlanAdapter.exerciseArrayList.clear();
        mExercisePlanAdapter.exerciseArrayList.addAll(mDBHelper.getExercisePlanList());

        setExerciseDescribe();
        list.setAdapter(mExercisePlanAdapter);
        mExercisePlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        deleteInCompleteExercise();

        listRenewal();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mExercisePlanAdapter.getCheckBoxState()) {
            inflater.inflate(R.menu.menu_delete, menu);
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mExercisePlanAdapter.getCountSelectedCheckBox()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mCheckBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCheckBox.setChecked(isChecked);
                    for (int i = 0; i < mExercisePlanAdapter.exerciseArrayList.size(); i++) {
                        mExercisePlanAdapter.exerciseArrayList.get(i).setSelected(mCheckBox.isChecked());
                    }
                    if (mExercisePlanAdapter.getCountSelectedCheckBox() == 0) {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    } else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mExercisePlanAdapter.getCountSelectedCheckBox()));
                    }
                    mExercisePlanAdapter.notifyDataSetChanged();
                }
            });
        } else {
            inflater.inflate(R.menu.menu_search, menu);

            final MenuItem search = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            searchView.setQueryHint("운동 계획명 혹은 운동 이름을 입력해주세요.");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String inputValue) {

                    ArrayList<Exercise> tmpArrayList = new ArrayList<Exercise>();

                    listRenewal();

                    // inputValue와 동일한 planTitle이 있는지 확인
                    for (int i = 0; i < mExercisePlanAdapter.exerciseArrayList.size(); i++) {
                        if (inputValue.equals(mExercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle())) {
                            tmpArrayList.add(mExercisePlanAdapter.exerciseArrayList.get(i));
                            mExercisePlanAdapter.exerciseArrayList.clear();
                            mExercisePlanAdapter.exerciseArrayList.addAll(tmpArrayList);
                            list.setAdapter(mExercisePlanAdapter);
                            mExercisePlanAdapter.notifyDataSetChanged();
                            return true;
                        }
                    }

                    // inputValue와 동일한 exerciseTtile이 있는지 확인
                    for (int i = 0; i < mExercisePlanAdapter.exerciseArrayList.size(); i++) {
                        mExerciseContentArrayList = mDBHelper.getExerciseTitleListSelectedPlan(mExercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                        for (int j = 0; j < mExerciseContentArrayList.size(); j++) {
                            if (inputValue.equals(mExerciseContentArrayList.get(j).getExerciseTitle())) {
                                if (!tmpArrayList.contains(mDBHelper.getExercisePlanSelectedExercise(mExerciseContentArrayList.get(j).getExerciseTitle())))
                                    tmpArrayList.addAll(mDBHelper.getExercisePlanSelectedExercise(mExerciseContentArrayList.get(j).getExerciseTitle()));
                            }
                        }
                    }


                    mExercisePlanAdapter.exerciseArrayList.clear();
                    mExercisePlanAdapter.exerciseArrayList.addAll(tmpArrayList);
                    if (mExercisePlanAdapter.exerciseArrayList.size() > 0) {
                        setExerciseDescribe();
                        list.setAdapter(mExercisePlanAdapter);
                        mExercisePlanAdapter.notifyDataSetChanged();
                        return true;
                    } else {
                        mExercisePlanAdapter.exerciseArrayList.clear();
                        list.setAdapter(mExercisePlanAdapter);
                        mExercisePlanAdapter.notifyDataSetChanged();
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
                    listRenewal();

                    searchView.onActionViewCollapsed();
                    searchView.setQuery("", false);
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
                mExercisePlanAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_delete:
                for (int i = 0; i < mExercisePlanAdapter.exerciseArrayList.size(); i++) {
                    if (mExercisePlanAdapter.exerciseArrayList.get(i).isSelected()) {
                        mDBHelper.deleteExercisePlan(mExercisePlanAdapter.exerciseArrayList.get(i).getPlanTitle());
                    }
                }

                listRenewal();

                mExercisePlanAdapter.setCheckBoxState(false);
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
            if (mExercisePlanAdapter.getCheckBoxState()) {
                mExercisePlanAdapter.exerciseArrayList.get(position).setSelected(!mExercisePlanAdapter.exerciseArrayList.get(position).isSelected());
                if (mExercisePlanAdapter.getCountSelectedCheckBox() == 0) {
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                } else {
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mExercisePlanAdapter.getCountSelectedCheckBox()));
                }
                mExercisePlanAdapter.notifyDataSetChanged();
            } else {
                Intent intent = new Intent(getActivity(), ExerciseDetailActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("doExercise", true);
                intent.putExtra("planTitle", mExercisePlanAdapter.exerciseArrayList.get(position).getPlanTitle());
                startActivity(intent);
            }
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mExercisePlanAdapter.resetCheckBox();
            mExercisePlanAdapter.setCheckBoxState(true);
            mExercisePlanAdapter.exerciseArrayList.get(position).setSelected(true);
            getActivity().invalidateOptionsMenu();

            return true;

        }
    }


}


