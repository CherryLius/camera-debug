package com.hele.hardware.analyser.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.model.ImmunityInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultActivity extends BaseActivity implements ResultContract.View {

    private static final String TAG = "ResultActivity";
    private static final String EXTRA_PATH = "extra_path";

    @BindView(R.id.tv_result)
    AppCompatTextView resultTextView;
    @BindView(R.id.iv_result)
    AppCompatImageView resultImageView;
    @BindView(R.id.layout_result)
    LinearLayout resultLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.tv_date)
    TextView dateView;

    @BindView(R.id.tv_gray_con)
    TextView conView;
    @BindView(R.id.tv_gray_tnl)
    TextView tnlView;
    @BindView(R.id.tv_gray_ck_mb)
    TextView ckMBView;
    @BindView(R.id.tv_gray_myo)
    TextView myoView;

    @BindView(R.id.tv_gray_percent_con)
    TextView conPercentView;
    @BindView(R.id.tv_gray_percent_tnl)
    TextView tnlPercentView;
    @BindView(R.id.tv_gray_percent_ck_mb)
    TextView ckMBPercentView;
    @BindView(R.id.tv_gray_percent_myo)
    TextView myoPercentView;

    private ResultContract.Presenter mPresenter;
    private String mPath;
    private SimpleDateFormat mFormatter;
    private ImmunityInfo mInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    protected int getToolbarContentLayoutId() {
        return 0;
    }

    @Override
    protected String getToolBarTitle() {
        return "结果显示";
    }

    void init() {
        new ResultPresenter(this);
        mFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
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
    public void showResult(final ImmunityInfo info) {
        progressBar.setVisibility(View.GONE);
        resultLayout.setVisibility(View.VISIBLE);
        conView.setText(info.getCon() + "");
        tnlView.setText(info.getTnl() + "");
        ckMBView.setText(info.getCKMB() + "");
        myoView.setText(info.getMyo() + "");

        conPercentView.setText(info.getCon() / info.getCon() + "");
        tnlPercentView.setText(info.getTnl() / info.getCon() + "");
        ckMBPercentView.setText(info.getCKMB() / info.getCon() + "");
        myoPercentView.setText(info.getMyo() / info.getCon() + "");

        dateView.setText(mFormatter.format(new Date().getTime()));
        mInfo = info;
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
                mPresenter.saveResult(mPath, mInfo);
                finish();
                break;
        }
    }

    public static void toActivity(Activity activity, String path) {
        Intent in = new Intent(activity, ResultActivity.class);
        in.putExtra(EXTRA_PATH, path);
        activity.startActivity(in);
    }

}