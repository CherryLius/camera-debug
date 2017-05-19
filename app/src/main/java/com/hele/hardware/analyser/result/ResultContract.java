package com.hele.hardware.analyser.result;

import android.widget.ImageView;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;
import com.hele.hardware.analyser.model.ImmunityInfo;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface ResultContract {

    interface View extends BaseView<Presenter> {
        void showResult(ImmunityInfo result);

        void showToast(String text);
    }

    interface Presenter extends BasePresenter {

        void setup();

        void destroy();

        void showImage(ImageView iv, String path);

        void analyse(String path);

        void saveResult(String path, ImmunityInfo info);

    }
}
