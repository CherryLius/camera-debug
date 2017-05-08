package com.hele.hardware.analyser.behavior;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.BehaviorAdapter;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.capture.CaptureActivity;
import com.hele.hardware.analyser.model.DotItem;
import com.ui.picker.TimePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BehaviorActivity extends BaseActivity implements BehaviorContract.View,
        StepFragment.OnStepListener, TimePicker.OnTimePickListener {

    private static final int[] STEPS = new int[]{StepFragment.STEP_CONFIRM,
            StepFragment.STEP_USER_ADD,
            StepFragment.STEP_TIME_SETTING};

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private BehaviorAdapter mAdapter;

    private BehaviorContract.Presenter mPresenter;
    private List<StepFragment> mStepFragments;

    private int mCurrentStep = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        new BehaviorPresenter(this);
        mStepFragments = new ArrayList<>(3);

        initView();
        initFragments();
        mPresenter.loadSteps();
    }

    void initView() {
        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        mAdapter = new BehaviorAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);
    }

    void initFragments() {
        for (int i = 0; i < 3; i++) {
            StepFragment fragment = StepFragment.newInstance(STEPS[i]);
            fragment.setStepListener(this);
            fragment.setOnTimePickListener(this);
            mStepFragments.add(fragment);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mStepFragments.size(); i++) {
            if (!mStepFragments.get(i).isAdded()) {
                ft.add(R.id.fragment_container, mStepFragments.get(i))
                        .addToBackStack(null);
            }
            if (i > 0)
                ft.hide(mStepFragments.get(i));
        }
        mCurrentStep = 0;
        ft.show(mStepFragments.get(mCurrentStep)).commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_behavior;
    }

    @Override
    protected int getToolbarContentLayoutId() {
        return 0;
    }

    @Override
    protected String getToolBarTitle() {
        return null;
    }

    @Override
    public void setPresenter(@NonNull BehaviorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showList(String[] array) {

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
            mCurrentStep = currentStep + 1;
            StepFragment fragment = mStepFragments.get(mCurrentStep);
            getSupportFragmentManager().beginTransaction()
                    .hide(mStepFragments.get(currentStep))
                    .show(fragment)
                    .commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for (int i = 0; i < mStepFragments.size(); i++) {
                ft.remove(mStepFragments.get(i));
            }
            ft.commit();
            mStepFragments.clear();
        }
    }

    @Override
    public void onTimePicked(String hour, String minute) {
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentStep < 0 || mCurrentStep > mStepFragments.size() - 1)
            return;
        Fragment fragment = mStepFragments.get(mCurrentStep);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
