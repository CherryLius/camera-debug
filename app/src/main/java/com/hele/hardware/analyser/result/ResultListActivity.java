package com.hele.hardware.analyser.result;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.ResultAdapter;
import com.hele.hardware.analyser.model.ResultInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultListActivity extends AppCompatActivity implements ResultListContract.View {

    private static final String TAG = "ResultListActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ResultListContract.Presenter mPresenter;
    private ResultAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_recycler);
        ButterKnife.bind(this);
        new ResultListPresenter(this);
        init();
        mPresenter.loadResults();
    }

    private void init() {
        setSupportActionBar(toolbar);

        mAdapter = new ResultAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setPresenter(@NonNull ResultListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showResults(List<ResultInfo> results) {
        mAdapter.updateList(results);
    }
}
