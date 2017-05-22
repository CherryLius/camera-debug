package com.hele.hardware.analyser.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.MainCardAdapter;
import com.hele.hardware.analyser.base.BaseFragment;
import com.hele.hardware.analyser.model.CardItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/22.
 */

public class HomeFragment extends BaseFragment implements MainContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

    AppCompatActivity mActivity;

    private MainCardAdapter mMainCardAdapter;
    private MainContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
        new MainPresenter(getContext(), this);
        mPresenter.initMenu();
    }

    void initView() {
        mMainCardAdapter = new MainCardAdapter(getContext());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mMainCardAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (toolbar == null)
            return;
        if (context instanceof AppCompatActivity) {
            mActivity = (AppCompatActivity) context;
            mActivity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity.setSupportActionBar(null);
            mActivity = null;
        }
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
                Snackbar.make(recyclerView, "searchView", Snackbar.LENGTH_SHORT)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;
        }
    }
}
