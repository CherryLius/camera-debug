package com.hele.hardware.analyser.capture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.result.ResultActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends AppCompatActivity implements CaptureContract.CaptureListener {

    private static final String TAG = "CaptureActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        init();
    }


    void init() {
        CaptureFragment cf = new CaptureFragment();
        cf.setCaptureListener(this);
        mFragment = cf;
        if (getIntent() != null) {
            Bundle args = getIntent().getBundleExtra("arguments");
            mFragment.setArguments(args);
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
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
