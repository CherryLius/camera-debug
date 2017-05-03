package com.hele.hardware.analyser.user.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.UserInfoAdapter;
import com.hele.hardware.analyser.model.UserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/2.
 */

public class UserInfoActivity extends AppCompatActivity implements UserInfoContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView titleView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private UserInfoAdapter mAdapter;
    private UserInfoContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        init();
    }

    void init() {
        setSupportActionBar(toolbar);
        titleView.setText("用户信息");
        new UserInfoPresenter(this);

        mAdapter = new UserInfoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SectionItemDecoration(this, mPresenter.getSectionProvider()));
        recyclerView.setAdapter(mAdapter);

        mPresenter.loadUsers();
    }

    @OnClick({R.id.iv_back, R.id.iv_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                mPresenter.gotoUserAdd(this);
                break;
        }
    }

    @Override
    public void setPresenter(@NonNull UserInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUsers(List<UserInfo> users) {
        mAdapter.updateUsers(users);
    }
}
