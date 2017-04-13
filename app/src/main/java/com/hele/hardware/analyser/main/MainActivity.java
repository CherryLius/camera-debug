package com.hele.hardware.analyser.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.LifecycleCallback;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.capture.CaptureFragment;
import com.hele.hardware.analyser.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LifecycleCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayMap<Integer, Fragment> mCacheFragments = new ArrayMap<>(1);
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        mFragment = fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        replaceFragment(getFragment(position));
    }

    @Override
    public void onAttachActivity(Fragment fragment) {
        setupWindow();
    }

    @Override
    public void onDetachActivity(Fragment fragment) {
        displayWindow();
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragment;
            if (fragment.onBackPressed())
                return;
        }
        super.onBackPressed();
    }

    private Fragment getFragment(int position) {
        if (mCacheFragments.containsKey(position)) {
            return mCacheFragments.get(position);
        }
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new CaptureFragment();
                break;
            default:
                Utils.showToastOnUiThread(this, "Not Impl");
                break;
        }
        if (fragment != null)
            mCacheFragments.put(position, fragment);
        return fragment;
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            mFragment = fragment;
        }
    }

    private void setupWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void displayWindow() {
        if (getSupportActionBar() != null)
            getSupportActionBar().show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
