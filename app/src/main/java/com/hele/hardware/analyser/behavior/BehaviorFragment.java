package com.hele.hardware.analyser.behavior;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.BehaviorAdapter;
import com.hele.hardware.analyser.model.DotItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BehaviorFragment extends BaseFragment implements BehaviorContract.View {

    private BehaviorContract.Presenter mPresenter;
    private AdapterView.OnItemClickListener mItemClickListener;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private BehaviorAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new BehaviorPresenter(this);
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
        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        mAdapter = new BehaviorAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(mAdapter);

        mPresenter.loadSteps();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdapterView.OnItemClickListener)
            mItemClickListener = (AdapterView.OnItemClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
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
}
