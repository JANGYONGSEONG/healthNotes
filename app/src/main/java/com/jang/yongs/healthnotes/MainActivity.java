package com.jang.yongs.healthnotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jang.yongs.healthnotes.fragment.BodyCompositionFragment;
import com.jang.yongs.healthnotes.fragment.ExerciseFragment;
import com.jang.yongs.healthnotes.fragment.MealPlanFragment;
import com.jang.yongs.healthnotes.fragment.StaticsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    public final String FRAGMENT_STATIC_TAG = "fragment_static";
    public final String FRAGMENT_EXERCISE_TAG = "fragment_exercise";
    public final String FRAGMENT_BODY_TAG = "fragment_body";
    public final String FRAGMENT_MEAL_TAG = "fragment_meal";

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setToolbar();

        switchToExercise();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_exercise:
                        getSupportActionBar().setTitle("운동");
                        switchToExercise();
                        break;
                    case R.id.navigation_item_body:
                        getSupportActionBar().setTitle("체성분");
                        switchToBody();
                        break;
                    case R.id.navigation_item_meal:
                        getSupportActionBar().setTitle("식단");
                        switchToMeal();
                        break;
                    case R.id.navigation_item_statics:
                        getSupportActionBar().setTitle("그래프");
                        switchToStatics();
                        break;
                }

                return true;
            }
        });

    }


    private void switchToExercise() {
        FragmentTransaction transaction = getFragmentManagerInstance().beginTransaction();
        ExerciseFragment exerciseDivisionFragment = new ExerciseFragment();
        transaction.replace(R.id.fragment_container, exerciseDivisionFragment, FRAGMENT_EXERCISE_TAG).commitAllowingStateLoss();
    }

    private void switchToMeal() {
        FragmentTransaction transaction = getFragmentManagerInstance().beginTransaction();
        MealPlanFragment mealPlanFragment = new MealPlanFragment();
        transaction.replace(R.id.fragment_container, mealPlanFragment, FRAGMENT_MEAL_TAG).commitAllowingStateLoss();

    }

    private void switchToBody() {
        FragmentTransaction transaction = getFragmentManagerInstance().beginTransaction();
        BodyCompositionFragment bodyCompositionFragment = new BodyCompositionFragment();
        transaction.replace(R.id.fragment_container, bodyCompositionFragment, FRAGMENT_BODY_TAG).commitAllowingStateLoss();

    }

    private void switchToStatics() {
        FragmentTransaction transaction = getFragmentManagerInstance().beginTransaction();
        StaticsFragment staticsFragment = new StaticsFragment();
        transaction.replace(R.id.fragment_container, staticsFragment, FRAGMENT_STATIC_TAG).commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private FragmentManager getFragmentManagerInstance() {

        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        return mFragmentManager;
    }


}
