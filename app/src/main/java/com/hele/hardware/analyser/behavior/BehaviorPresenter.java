package com.hele.hardware.analyser.behavior;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.R;
import com.hele.hardware.analyser.model.DotItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class BehaviorPresenter implements BehaviorContract.Presenter {

    private BehaviorContract.View mView;
    private List<DotItem> mItemList;

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

    @Override
    public void loadSteps() {
        mItemList = new ArrayList<>(5);

        DotItem item = new DotItem();
        item.setTitle("试纸板摆放");
        item.setSelected(true);
        mItemList.add(item);

        item = new DotItem();
        item.setTitle("用户选择");
        mItemList.add(item);

        item = new DotItem();
        item.setTitle("时间设定");
        mItemList.add(item);

        item = new DotItem();
        item.setTitle("拍照显示");
        mItemList.add(item);

        item = new DotItem();
        item.setTitle("图片解析");
        mItemList.add(item);

        item = new DotItem();
        item.setTitle("结果");
        mItemList.add(item);

        mView.showSteps(mItemList);
    }

    @Override
    public void updateStepState(int position) {
        if (mItemList == null) return;
        if (position < 0 || position > mItemList.size() - 1) {
            return;
        }
        DotItem item = mItemList.get(position + 1);
        item.setSelected(true);
        mView.showSteps(mItemList);
    }
}
