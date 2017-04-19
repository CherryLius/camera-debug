package com.hele.hardware.analyser.behavior;

import android.content.Context;

import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface BehaviorContract {
    interface View extends BaseView<Presenter> {
        void showList(String[] array);
    }

    interface Presenter extends BasePresenter {
        void parseList(Context context);
    }
}
