package com.hele.hardware.analyser.capture;

import android.graphics.Bitmap;

import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface CaptureContract {

    interface View extends BaseView<Presenter> {
        void showToast(String text);

        void showBitmap(Bitmap bitmap);

        boolean isInCapture();
    }

    interface Presenter extends BasePresenter {
        void capture();
    }
}
