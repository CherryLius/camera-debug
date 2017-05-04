package com.hele.hardware.analyser.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.ResultInfo;
import com.hele.hardware.analyser.util.Utils;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultActivity extends AppCompatActivity implements ResultContract.View {

    private static final String TAG = "ResultActivity";
    private static final String EXTRA_PATH = "extra_path";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_result)
    AppCompatTextView resultTextView;
    @BindView(R.id.iv_result)
    AppCompatImageView resultImageView;
    @BindView(R.id.layout_result)
    LinearLayout resultLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ResultContract.Presenter mPresenter;
    private String mPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        init();
    }

    void init() {
        new ResultPresenter(this);
        setSupportActionBar(toolbar);
        resultTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        progressBar.setVisibility(View.VISIBLE);
        resultLayout.setVisibility(View.GONE);

        mPresenter.setup();
        if (getIntent() != null) {
            String path = getIntent().getStringExtra(EXTRA_PATH);
            if (TextUtils.isEmpty(path))
                path = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "123.bmp").getAbsolutePath();
            mPresenter.showImage(resultImageView, path);
            mPresenter.analyse(path);
            mPath = path;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void setPresenter(@NonNull ResultContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);
                resultTextView.setText(result);
            }
        });
    }

    @Override
    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResultActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_save)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                Toast.makeText(this, "save result", Toast.LENGTH_SHORT).show();
                ResultInfo info = new ResultInfo();
                info.setName(Utils.getRandomEnglish());
                info.setIdentity(Utils.getRandomNumber());
                info.setPicturePath(mPath);
                info.setDateTime(new Date().getTime());
                info.setValue(resultTextView.getText().toString());
                mPresenter.saveResult(info);
                break;
        }
    }

    public static void toActivity(Activity activity, String path) {
        Intent in = new Intent(activity, ResultActivity.class);
        in.putExtra(EXTRA_PATH, path);
        activity.startActivity(in);
    }

}