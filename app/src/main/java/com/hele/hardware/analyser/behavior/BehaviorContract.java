package com.hele.hardware.analyser.behavior;

import android.content.Context;

import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;
import com.hele.hardware.analyser.model.DotItem;

import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface BehaviorContract {
    interface View extends BaseView<Presenter> {
        void showList(String[] array);

        void showSteps(List<DotItem> list);
    }

    interface Presenter extends BasePresenter {
        void parseList(Context context);

        void loadSteps();
    }
}
