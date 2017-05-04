package com.hele.hardware.analyser.result;

import android.widget.ImageView;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;
import com.hele.hardware.analyser.model.ResultInfo;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface ResultContract {

    interface View extends BaseView<Presenter> {
        void showResult(String result);

        void showToast(String text);
    }

    interface Presenter extends BasePresenter {

        void setup();

        void destroy();

        void showImage(ImageView iv, String path);

        void analyse(String path);

        void saveResult(ResultInfo info);

    }
}
