package com.hele.hardware.analyser.behavior;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.capture.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BehaviorActivity extends AppCompatActivity implements BehaviorFragment.BehaviorListener {

    private static final String TAG = "BehaviorActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();

        BehaviorFragment fragment = new BehaviorFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        mFragment = fragment;
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
