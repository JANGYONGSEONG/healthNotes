package com.jang.yongs.healthnotes.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
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

import com.jang.yongs.healthnotes.MainActivity;
import com.jang.yongs.healthnotes.R;
import com.jang.yongs.healthnotes.adapter.BodyCompositionAdapter;
import com.jang.yongs.healthnotes.db.BodyCompositionDBHelper;
import com.jang.yongs.healthnotes.model.BodyComposition;
import com.jang.yongs.healthnotes.popup.BodyCompositionPopup;

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

    BodyCompositionAdapter mBodyCompositionAdapter;
    ArrayList<BodyComposition> mBodyCompositionArrayList;
    BodyCompositionDBHelper mDBHelper;
    CheckBox mCheckBox;

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
        View view = inflater.inflate(R.layout.fragment_body_composition, container, false);
        ButterKnife.bind(this, view);

        setList();

        setFab();

        return view;
    }

    private void setList() {
        mDBHelper = new BodyCompositionDBHelper(getContext(), "HealthNotes",null,1);

        mBodyCompositionArrayList = mDBHelper.getBodyCompositionList();

        mBodyCompositionAdapter = new BodyCompositionAdapter(this.getActivity(),mBodyCompositionArrayList);

        list.setAdapter(mBodyCompositionAdapter);

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
        mBodyCompositionAdapter.bodyCompositionArrayList.clear();
        mBodyCompositionAdapter.bodyCompositionArrayList.addAll(mDBHelper.getBodyCompositionList());
        list.setAdapter(mBodyCompositionAdapter);
        mBodyCompositionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mBodyCompositionAdapter.getCheckBoxState()) {
            inflater.inflate(R.menu.menu_delete, menu);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mBodyCompositionAdapter.getCountSelectedCheckBox()));
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            mCheckBox = (CheckBox) menu.findItem(R.id.action_check).getActionView();
            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCheckBox.setChecked(isChecked);
                    for(int i=0; i<mBodyCompositionAdapter.bodyCompositionArrayList.size(); i++) {
                        mBodyCompositionAdapter.bodyCompositionArrayList.get(i).setSelected(mCheckBox.isChecked());
                    }
                    if(mBodyCompositionAdapter.getCountSelectedCheckBox() == 0){
                        ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                    }else {
                        ((MainActivity) getActivity()).getSupportActionBar().setTitle(String.valueOf(mBodyCompositionAdapter.getCountSelectedCheckBox()));
                    }
                    mBodyCompositionAdapter.notifyDataSetChanged();
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
                            mBodyCompositionAdapter.bodyCompositionArrayList.clear();
                            mBodyCompositionAdapter.bodyCompositionArrayList.addAll(mDBHelper.getBodyCompositionSearchedDate(Date.valueOf(date)));
                            list.setAdapter(mBodyCompositionAdapter);
                            mBodyCompositionAdapter.notifyDataSetChanged();

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
                mBodyCompositionAdapter.setCheckBoxState(false);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_delete:
                for(int i=0;i<mBodyCompositionAdapter.bodyCompositionArrayList.size();i++){
                    if(mBodyCompositionAdapter.bodyCompositionArrayList.get(i).isSelected()){
                        mDBHelper.deleteBodyCompositionSelectedDate(mBodyCompositionAdapter.bodyCompositionArrayList.get(i).getDate());
                    }
                }

                listRenewal();

                mBodyCompositionAdapter.setCheckBoxState(false);
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
            if(mBodyCompositionAdapter.getCheckBoxState()){
                mBodyCompositionAdapter.bodyCompositionArrayList.get(position).setSelected(!mBodyCompositionAdapter.bodyCompositionArrayList.get(position).isSelected());
                if(mBodyCompositionAdapter.getCountSelectedCheckBox() == 0){
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("목록을 선택하세요.");
                }else{
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle(String.valueOf(mBodyCompositionAdapter.getCountSelectedCheckBox()));
                }
                mBodyCompositionAdapter.notifyDataSetChanged();
            }else{
                Intent intent = new Intent(getActivity(),BodyCompositionPopup.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("update",true);
                intent.putExtra("date",String.valueOf(mBodyCompositionAdapter.bodyCompositionArrayList.get(position).getDate()));
                startActivity(intent);
            }
        }
    }

    private class ListViewItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mBodyCompositionAdapter.resetCheckBox();
            mBodyCompositionAdapter.setCheckBoxState(true);
            mBodyCompositionAdapter.bodyCompositionArrayList.get(position).setSelected(true);
            getActivity().invalidateOptionsMenu();

            return true;
        }
    }
}
