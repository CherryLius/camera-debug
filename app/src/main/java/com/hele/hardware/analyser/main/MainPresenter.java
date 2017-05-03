package com.hele.hardware.analyser.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.behavior.BehaviorActivity;
import com.hele.hardware.analyser.model.CardItem;
import com.hele.hardware.analyser.result.ResultListActivity;
import com.hele.hardware.analyser.user.info.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public class MainPresenter implements MainContract.Presenter {

    private Context mContext;
    private MainContract.View mView;

    MainPresenter(Context context, @NonNull MainContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void initMenu() {
        List<CardItem> menuList = new ArrayList<>(3);

        CardItem menu = new CardItem();
        menu.setTitle("免疫检测");
        menu.setDescription("功能使用说明:\n试纸板摆放-时间设定-拍照显示-图片解析-结果");
        menuList.add(menu);

        menu = new CardItem();
        menu.setTitle("结果查询");
        menu.setDescription("查询历史结果记录");
        menuList.add(menu);

        menu = new CardItem();
        menu.setTitle("更多");
        menuList.add(menu);

        mView.showMenu(menuList);
    }

    @Override
    public void gotoAnalyser() {
        Intent intent = new Intent(mContext, BehaviorActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void gotoQuery() {
        Intent intent = new Intent(mContext, ResultListActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void gotoMore() {
        //ResultActivity.toActivity((Activity) mContext, "");
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        mContext.startActivity(intent);
    }
}
