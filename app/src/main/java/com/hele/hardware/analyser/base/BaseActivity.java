package com.hele.hardware.analyser.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hele.hardware.analyser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/4.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRootLayoutId() > 0) {
            setContentView(getRootLayoutId());
        } else {
            setContentView(R.layout.activity_base);
            inflateContent(new ContainerHolder(getWindow().getDecorView()));
        }
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        titleView.setText(getToolBarTitle());
    }

    private void inflateContent(ContainerHolder holder) {
        if (getContentLayoutId() > 0) {
            LayoutInflater.from(this).inflate(getContentLayoutId(), holder.contentContainer, true);
        }

        if (getToolbarContentLayoutId() > 0) {
            View toolView = LayoutInflater.from(this).inflate(getToolbarContentLayoutId(), null);
            holder.toolbarContainer.addView(toolView);
        }
    }

    @LayoutRes
    protected int getRootLayoutId() {
        return 0;
    }

    @LayoutRes
    protected abstract int getContentLayoutId();

    @LayoutRes
    protected abstract int getToolbarContentLayoutId();

    protected abstract String getToolBarTitle();

    class ContainerHolder {

        @BindView(R.id.toolbar_container)
        LinearLayout toolbarContainer;
        @BindView(R.id.content_container)
        FrameLayout contentContainer;

        ContainerHolder(View rootView) {
            ButterKnife.bind(this, rootView);
        }

        @OnClick(R.id.iv_back)
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
            }
        }
    }
}