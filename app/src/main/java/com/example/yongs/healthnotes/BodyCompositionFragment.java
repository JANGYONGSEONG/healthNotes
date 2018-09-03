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

import com.example.yongs.healthnotes.adapter.BodyCompositionAdapter;
import com.example.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.example.yongs.healthnotes.models.BodyComposition;

import java.sql.Date;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BodyCompositionFragment extends Fragment {

    @BindView(R.id.list) ListView list;
    @BindView(R.id.empty_list) TextView emptyListItem;
    @BindView(R.id.fab) FloatingActionButton fab;

    BodyCompositionAdapter bodyCompositionAdapter;
    ArrayList<BodyComposition> bodyCompositionArrayList;
    BodyCompositionDBHelper dbHelper;
    CheckBox checkBox;

    public BodyCompositionFragment() {

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
        View view = inflater.inflate(R.layout.fragment_body_composition, container, false);
        ButterKnife.bind(this, view);

        setList();

        setFab();

        return view;
    }

    private void setList() {
        dbHelper = new BodyCompositionDBHelper(getContext(), "HealthNotes",null,1);

        bodyCompositionArrayList = dbHelper.getAll();

        bodyCompositionAdapter = new BodyCompositionAdapter(this.getActivity(),bodyCompositionArrayList);

        list.setAdapter(bodyCompositionAdapter);

        list.setOnItemClickListener( new ListViewItemClickListener() );
        list.setOnItemLongClickListener( new ListViewItemLongClickListener() );

        list.setEmptyView(emptyListItem);

    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BodyCompositionPopup.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        bodyCompositionAdapter.bodyCompositionArrayList.clear();
        bodyCompositionAdapter.bodyCompositionArrayList.addAll(dbHelper.getAll());
        list.setAdapter(bodyCompositionAdapter);
        bodyCompositionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(bodyCompositionAdapter.getCheckBoxState()) {
            inflater.inflate(R.menu.menu_delete, menu);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(bodyCompositionAdapter.getCountSelectedCheckBox()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            checkBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkBox.setChecked(isChecked);
                    Log.i("TestCheck", String.valueOf(isChecked));
                    for(int i=0; i<bodyCompositionAdapter.bodyCompositionArrayList.size(); i++) {
                        bodyCompositionAdapter.bodyCompositionArrayList.get(i).setSelected(checkBox.isChecked());
                        Log.i("TestCheck", String.valueOf(checkBox.isChecked()));
                    }
                    if(bodyCompositionAdapter.getCountSelectedCheckBox() == 0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    }else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(bodyCompositionAdapter.getCountSelectedCheckBox()));
                    }
                    bodyCompositionAdapter.notifyDataSetChanged();
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
                            bodyCompositionAdapter.bodyCompositionArrayList.clear();
                            bodyCompositionAdapter.bodyCompositionArrayList.addAll(dbHelper.getBodyCompositionForSearch(Date.valueOf(date)));
                            list.setAdapter(bodyCompositionAdapter);
                            bodyCompositionAdapter.notifyDataSetChanged();

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

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("체성분");
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                bodyCompositionAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_delete:
                for(int i=0;i<bodyCompositionAdapter.bodyCompositionArrayList.size();i++){
                    if(bodyCompositionAdapter.bodyCompositionArrayList.get(i).isSelected()){
                        dbHelper.delete(bodyCompositionAdapter.bodyCompositionArrayList.get(i).getDate());
                    }
                }

                listRenewal();

                bodyCompositionAdapter.setCheckBoxState(false);
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
            if(bodyCompositionAdapter.getCheckBoxState()){
                bodyCompositionAdapter.bodyCompositionArrayList.get(position).setSelected(!bodyCompositionAdapter.bodyCompositionArrayList.get(position).isSelected());
                if(bodyCompositionAdapter.getCountSelectedCheckBox() == 0){
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                }else{
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle(String.valueOf(bodyCompositionAdapter.getCountSelectedCheckBox()));
                }
                bodyCompositionAdapter.notifyDataSetChanged();
            }else{
                Intent intent = new Intent(getActivity(),BodyCompositionPopup.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("update",true);
                intent.putExtra("date",String.valueOf(bodyCompositionAdapter.bodyCompositionArrayList.get(position).getDate()));
                startActivity(intent);
            }
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            bodyCompositionAdapter.resetCheckBox();
            bodyCompositionAdapter.setCheckBoxState(true);
            bodyCompositionAdapter.bodyCompositionArrayList.get(position).setSelected(true);
            getActivity().invalidateOptionsMenu();

            return true;
        }
    }
}
