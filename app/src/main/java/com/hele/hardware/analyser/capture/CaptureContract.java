package com.hele.hardware.analyser.capture;

import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface CaptureContract {

    interface View extends BaseView<Presenter> {
        void showToast(String text);
    }

    interface Presenter extends BasePresenter {
        void capture();
    }
}
