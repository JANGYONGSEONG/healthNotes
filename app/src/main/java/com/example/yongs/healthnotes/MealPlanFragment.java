package com.example.yongs.healthnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import com.example.yongs.healthnotes.adapter.MealPlanAdapter;
import com.example.yongs.healthnotes.db.MealPlanDBHelper;
import com.example.yongs.healthnotes.models.MealPlan;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealPlanFragment extends Fragment {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.empty_list) TextView emptyListItem;
    @BindView(R.id.fab) FloatingActionButton fab;

    MealPlanAdapter mealPlanAdapter;
    ArrayList<MealPlan> mealPlanArrayList;
    MealPlanDBHelper dbHelper;
    CheckBox checkBox;

    public MealPlanFragment() {
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
        View view  = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        ButterKnife.bind(this, view);

        setList();

        setFab();

        return view;

    }

    private void setList() {
        dbHelper = new MealPlanDBHelper(getContext(),"HealthNotes",null,1);

        mealPlanArrayList =  dbHelper.getTotalPerDate();

        mealPlanAdapter = new MealPlanAdapter(this.getActivity(),mealPlanArrayList);


        list.setAdapter(mealPlanAdapter);

        list.setOnItemClickListener( new ListViewItemClickListener() );
        list.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        list.setEmptyView(emptyListItem);
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MealPlanPopup.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("meal_order", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        listRenewal();
    }

    private void listRenewal() {
        mealPlanAdapter.mealPlanArrayList.clear();
        mealPlanAdapter.mealPlanArrayList.addAll(dbHelper.getTotalPerDate());
        list.setAdapter(mealPlanAdapter);
        mealPlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(mealPlanAdapter.getCheckBoxState()) {
            inflater.inflate(R.menu.menu_delete, menu);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mealPlanAdapter.getCountSelectedCheckBox()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            checkBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkBox.setChecked(isChecked);

                    for(int i=0; i<mealPlanAdapter.mealPlanArrayList.size(); i++) {
                        mealPlanAdapter.mealPlanArrayList.get(i).setSelected(checkBox.isChecked());
                    }

                    if(mealPlanAdapter.getCountSelectedCheckBox() == 0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    }else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mealPlanAdapter.getCountSelectedCheckBox()));
                    }

                    mealPlanAdapter.notifyDataSetChanged();
                }
            });
        }else{
            inflater.inflate(R.menu.menu_search,menu);

            final MenuItem search = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
            searchView.setQueryHint("Ex)2018-01-01");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String date) {

                    if(date.split("-").length==3){
                        if(date.split("-")[0].length()==4 && date.split("-")[1].length()==2 && date.split("-")[2].length()==2){
                            mealPlanAdapter.mealPlanArrayList.clear();
                            mealPlanAdapter.mealPlanArrayList.addAll(dbHelper.getTotalTheDate(Date.valueOf(date)));
                            list.setAdapter(mealPlanAdapter);
                            mealPlanAdapter.notifyDataSetChanged();
                            return true;
                        }else{
                            Toast.makeText(getContext(), "2018-01-01 형태로 입력해 주세요", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }else{
                        Toast.makeText(getContext(), "2018-01-01 형태로 입력해 주세요", Toast.LENGTH_SHORT).show();
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
                    searchView.setQuery("",false);
                    searchView.clearFocus();

                    return true;
                }
            });

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("식단");
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                mealPlanAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_delete:
                for(int i=0;i<mealPlanAdapter.mealPlanArrayList.size();i++){
                    if(mealPlanAdapter.mealPlanArrayList.get(i).isSelected()){
                       dbHelper.delete(mealPlanAdapter.mealPlanArrayList.get(i).getDate());
                    }
                }

                listRenewal();

                mealPlanAdapter.setCheckBoxState(false);
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
            if(mealPlanAdapter.getCheckBoxState()){
                mealPlanAdapter.mealPlanArrayList.get(position).setSelected(!mealPlanAdapter.mealPlanArrayList.get(position).isSelected());
                if(mealPlanAdapter.getCountSelectedCheckBox() == 0){
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                }else {
                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mealPlanAdapter.getCountSelectedCheckBox()));
                }
                mealPlanAdapter.notifyDataSetChanged();
            }else{

                Log.i("TestDateFragment",String.valueOf(mealPlanAdapter.mealPlanArrayList.get(position).getDate()));
                Intent intent = new Intent(getActivity(),MealPlanActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("Date",String.valueOf(mealPlanAdapter.mealPlanArrayList.get(position).getDate()));
                startActivity(intent);
            }
        }
    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mealPlanAdapter.resetCheckBox();
            mealPlanAdapter.setCheckBoxState(true);
            mealPlanAdapter.mealPlanArrayList.get(position).setSelected(true);
            getActivity().invalidateOptionsMenu();

            return true;
        }
    }


}