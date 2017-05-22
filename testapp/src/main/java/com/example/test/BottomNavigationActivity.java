package com.example.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.test.fragment.DashboardFragment;
import com.example.test.fragment.HomeFragment;
import com.example.test.fragment.NotificationsFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showDefaultFragment();
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(1);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(2);
                    return true;
            }
            return false;
        }

    };

    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        initFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showDefaultFragment();
    }

    private void initFragment() {
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new DashboardFragment());
        mFragmentList.add(new NotificationsFragment());
    }

    private void showDefaultFragment() {
        showFragment(0);
    }

    private void showFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = mFragmentList.get(position);
        ft.replace(R.id.container, f);
        ft.show(f);
        ft.commit();
    }
}
