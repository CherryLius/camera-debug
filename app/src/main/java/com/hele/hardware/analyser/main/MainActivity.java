package com.hele.hardware.analyser.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.MainCardAdapter;
import com.hele.hardware.analyser.model.CardItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initView();
        new MainPresenter(this, this);
        mPresenter.initMenu();
    }

    void init() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
    }

    void initView() {
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

    @OnClick({R.id.layout_0, R.id.layout_1, R.id.layout_2, R.id.et_search})
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
                Snackbar.make(recyclerView, "searchView", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
