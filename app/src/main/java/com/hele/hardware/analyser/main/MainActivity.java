package com.hele.hardware.analyser.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hele.hardware.analyser.LifecycleCallback;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.capture.CaptureFragment;
import com.hele.hardware.analyser.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hele.hardware.analyser.common.Constants.PERMISSION_REQUEST_CODE_CAMERA;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LifecycleCallback {

    //private HashMap<String, SoftReference<Fragment>> mFragmentMap = new HashMap<>(1);
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (Utils.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    fragment = new CaptureFragment();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_REQUEST_CODE_CAMERA);
                }
                break;
        }
        replaceFragment(fragment);

    }

//    private void cacheFragment(Fragment f) {
//        String key = f.getClass().getCanonicalName();
//        HLog.i("Test", "key=" + key);
//        if (!mFragmentMap.containsKey(key) || mFragmentMap.get(key).get() == null) {
//            mFragmentMap.put(key, new SoftReference<>(f));
//        }
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < permissions.length; i++) {
//            sb.append("permission: ")
//                    .append(permissions[i])
//                    .append("--->grantResult: " + grantResults[i]);
//        }
//        HLog.i("Test", "requestCode=" + requestCode + ", " + sb.toString());
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    replaceFragment(new CaptureFragment());
                } else {
                    Toast.makeText(this, "没有摄像头权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
    }


    @Override
    public void onAttachActivity(Fragment fragment) {
        setupWindow();
    }

    @Override
    public void onDetachActivity(Fragment fragment) {
        displayWindow();
    }

    private void setupWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
    }

    private void displayWindow() {
        if (getSupportActionBar() != null)
            getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.getDecorView().setSystemUiVisibility(0);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            window.setNavigationBarColor(Color.BLACK);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
