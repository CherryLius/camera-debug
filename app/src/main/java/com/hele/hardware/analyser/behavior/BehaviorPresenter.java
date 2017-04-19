package com.hele.hardware.analyser.behavior;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.R;

/**
 * Created by Administrator on 2017/4/6.
 */

public class BehaviorPresenter implements BehaviorContract.Presenter {

    private BehaviorContract.View mView;

    public BehaviorPresenter(@NonNull BehaviorContract.View mainView) {
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
