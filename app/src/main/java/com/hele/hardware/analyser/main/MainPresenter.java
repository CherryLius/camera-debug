package com.hele.hardware.analyser.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.R;

/**
 * Created by Administrator on 2017/4/6.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mView = mainView;
        if (mView == null)
            throw new NullPointerException("mView should not be Null");
        mView.setPresenter(this);
    }

    @Override
    public void parseList(Context context) {
        String[] items = context.getResources().getStringArray(R.array.main_items);
        mView.showList(items);
    }
}
