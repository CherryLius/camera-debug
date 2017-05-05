package com.hele.hardware.analyser.user.info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.adapter.UserInfoAdapter;
import com.hele.hardware.analyser.base.BaseActivity;
import com.hele.hardware.analyser.listener.OnItemClickListener;
import com.hele.hardware.analyser.model.UserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/2.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoContract.View {

    private static final int REQUEST_CODE_USER_ADD = 101;
    private static final String EXTRA_TYPE = "extra_type";
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private UserInfoAdapter mAdapter;
    private UserInfoContract.Presenter mPresenter;

    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getStringExtra(EXTRA_TYPE);
        init();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected int getToolbarContentLayoutId() {
        return R.layout.layout_search;
    }

    @Override
    protected String getToolBarTitle() {
        return "用户信息";
    }

    void init() {
        new UserInfoPresenter(this);

        mAdapter = new UserInfoAdapter();

        if ("type_select".equals(mType)) {
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, long id) {
                    mPresenter.onItemClick(UserInfoActivity.this, position);
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new SectionItemDecoration(this, mPresenter.getSectionProvider()));
        recyclerView.setAdapter(mAdapter);

        mPresenter.loadUsers();
    }

    @OnClick({R.id.iv_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                mPresenter.gotoUserAdd(this, REQUEST_CODE_USER_ADD);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_USER_ADD && resultCode == RESULT_OK) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public static void toActivityForResult(Activity activity, String type, int requestCode) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        activity.startActivityForResult(intent, requestCode);
    }
}
