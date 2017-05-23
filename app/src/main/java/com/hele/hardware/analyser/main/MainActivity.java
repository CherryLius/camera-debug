package com.hele.hardware.analyser.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener /*implements MainContract.View*/ {
    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private List<Fragment> mBottomFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentList();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected int getToolbarContentLayoutId() {
        return 0;
    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                showFragment(0);
                return true;
            case R.id.navigation_android_0:
                showFragment(1);
                return true;
            case R.id.navigation_android_1:
                showFragment(2);
                return true;
        }
        return false;
    }

    private void initFragmentList() {
        mBottomFragmentList = new ArrayList<>(3);
        mBottomFragmentList.add(new HomeFragment());
        mBottomFragmentList.add(new EmptyFragment());
        mBottomFragmentList.add(new EmptyFragment());
    }

    private void showFragment(int position) {
        Fragment f = mBottomFragmentList.get(position);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (!f.isAdded()) {
            ft.add(R.id.fragment_container, f);
        }
        //hide others
        for (int i = 0; i < mBottomFragmentList.size(); i++) {
            if (i != position) {
                ft.hide(mBottomFragmentList.get(i));
            }
        }
        ft.show(f).commit();
    }
}
