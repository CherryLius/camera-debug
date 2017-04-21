package com.hele.hardware.analyser.behavior;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.BehaviorAdapter;
import com.hele.hardware.analyser.model.DotItem;
import com.ui.picker.TimePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BehaviorFragment extends BaseFragment implements BehaviorContract.View, StepFragment.OnStepListener, TimePicker.OnTimePickListener {

    private BehaviorContract.Presenter mPresenter;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private BehaviorAdapter mAdapter;

    private List<StepFragment> mStepFragments;
    private BehaviorListener mBehaviorListener;
    private Bundle mCaptureArgs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new BehaviorPresenter(this);
        mStepFragments = new ArrayList<>(3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_behavior, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initFragments();
        mPresenter.loadSteps();
    }

    void initView() {
        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        mAdapter = new BehaviorAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);
    }

    void initFragments() {
        for (int i = 0; i < 2; i++) {
            StepFragment fragment = StepFragment.newInstance(i);
            fragment.setStepListener(this);
            fragment.setOnTimePickListener(this);
            mStepFragments.add(fragment);
        }

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        for (int i = 0; i < mStepFragments.size(); i++) {
            if (!mStepFragments.get(i).isAdded()) {
                ft.add(R.id.fragment_container, mStepFragments.get(i))
                        .addToBackStack(null);
            }
            if (i > 0)
                ft.hide(mStepFragments.get(i));
        }
        ft.show(mStepFragments.get(0))
                .commitAllowingStateLoss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BehaviorListener)
            mBehaviorListener = (BehaviorListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBehaviorListener = null;
    }

    @Override
    public void setPresenter(@NonNull BehaviorContract.Presenter presenter) {
        mPresenter = presenter;
        //mPresenter.parseList(getContext());
    }

    @Override
    public void showList(String[] array) {
        //ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, array);
        //setListAdapter(adapter);
    }

    @Override
    public void showSteps(List<DotItem> list) {
        mAdapter.update(list);
    }

    @Override
    public void before(int currentStep) {

    }

    @Override
    public void next(int currentStep) {
        mPresenter.updateStepState(currentStep);
        if (currentStep < mStepFragments.size() - 1) {
            StepFragment fragment = mStepFragments.get(currentStep + 1);
            getChildFragmentManager().beginTransaction()
                    .hide(mStepFragments.get(currentStep))
                    .show(fragment)
                    .commitAllowingStateLoss();
        } else {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            for (int i = 0; i < mStepFragments.size(); i++) {
                ft.remove(mStepFragments.get(i));
            }
            ft.commitAllowingStateLoss();
            mStepFragments.clear();
            if (mBehaviorListener != null)
                mBehaviorListener.onBehavior(mCaptureArgs);
        }
    }

    @Override
    public void onTimePicked(String hour, String minute) {
        mCaptureArgs = new Bundle();
        mCaptureArgs.putString("hour", hour);
        mCaptureArgs.putString("minute", minute);
    }

    public interface BehaviorListener {
        void onBehavior(Bundle arguments);
    }
}