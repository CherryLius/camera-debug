package com.hele.hardware.analyser.behavior;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.base.BaseFragment;
import com.hele.hardware.analyser.capture.CaptureActivity;

public class BehaviorActivity extends BaseActivity implements BehaviorFragment.BehaviorListener {

    private static final String TAG = "BehaviorActivity";
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        BehaviorFragment fragment = new BehaviorFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_container, fragment)
                .commit();
        mFragment = fragment;
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
    public void onBackPressed() {
        if (mFragment != null && mFragment instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragment;
            if (fragment.onBackPressed())
                return;
        }
        super.onBackPressed();
    }

    @Override
    public void onBehavior(Bundle arguments) {
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra("arguments", arguments);
        startActivity(intent);
        //ResultActivity.toActivity(this, "");
        finish();
    }
}
