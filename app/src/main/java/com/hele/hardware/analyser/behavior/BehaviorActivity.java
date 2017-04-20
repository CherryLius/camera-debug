package com.hele.hardware.analyser.behavior;

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
import com.hele.hardware.analyser.util.HLog;
import com.hele.hardware.analyser.util.Utils;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BehaviorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LifecycleCallback {

    private static final String TAG = "BehaviorActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ArrayMap<Integer, Fragment> mCacheFragments = new ArrayMap<>(1);
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        BehaviorFragment fragment = new BehaviorFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        mFragment = fragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            HLog.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            HLog.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private LoaderCallbackInterface mLoaderCallback = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    HLog.i(TAG, "OpenCV loaded successfully");
                    break;
            }
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };
}
