package com.hele.hardware.analyser.capture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.base.BaseFragment;
import com.hele.hardware.analyser.result.ResultActivity;

public class CaptureActivity extends BaseActivity implements CaptureContract.CaptureListener {

    private static final String TAG = "CaptureActivity";
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        init();
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


    void init() {
        CaptureFragment cf = new CaptureFragment();
        cf.setCaptureListener(this);
        mFragment = cf;
        if (getIntent() != null) {
            Bundle args = new Bundle();
            args.putString("hour", getIntent().getStringExtra("hour"));
            args.putString("minute", getIntent().getStringExtra("minute"));
            mFragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.content_container, mFragment).commit();
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
    public void onAnalyse(String path) {
        ResultActivity.toActivity(this, path);
        finish();
    }
}
