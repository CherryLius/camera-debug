package com.hele.hardware.analyser.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.MainCardAdapter;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.model.CardItem;
import com.hele.hardware.analyser.result.DebugActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    AppCompatTextView titleView;
    @BindView(R.id.et_search)
    AppCompatEditText searchView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.layout_0)
    View mMenuLayout0;
    @BindView(R.id.layout_1)
    View mMenuLayout1;
    @BindView(R.id.layout_2)
    View mMenuLayout2;

    private MainCardAdapter mMainCardAdapter;
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        new MainPresenter(this, this);
        mPresenter.initMenu();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.activity_main;
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

    void initView() {
        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        mMainCardAdapter = new MainCardAdapter(this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mMainCardAdapter);
    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMenu(List<CardItem> list) {
        mMainCardAdapter.updateMenu(list);
    }

    @OnClick({R.id.layout_0, R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.et_search})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_0:
                mPresenter.gotoAnalyser();
                break;
            case R.id.layout_1:
                mPresenter.gotoQuery();
                break;
            case R.id.layout_2:
                mPresenter.gotoMore();
                break;
            case R.id.et_search:
                Snackbar.make(recyclerView, "searchView", Snackbar.LENGTH_SHORT)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
            case R.id.layout_3:
                startActivity(new Intent(this, DebugActivity.class));
                break;
        }
    }
}
