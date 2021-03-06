package com.hele.hardware.analyser.capture;

import android.graphics.Bitmap;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface CaptureContract {

    interface View extends BaseView<Presenter> {
        void showToast(String text);

        void showBitmap(Bitmap bitmap);

        void updateController(boolean isInCapture);

        boolean isInCapture();
    }

    interface Presenter extends BasePresenter {
        void capture();

        void savePicture();

        void setCaptureListener(CaptureListener listener);
    }

    interface CaptureListener {
        void onAnalyse(String path);
    }
}
