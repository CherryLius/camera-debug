package com.hele.hardware.analyser.user.add;

import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface UserAddContract {

    interface View extends BaseView<Presenter> {
        String getName();

        String getSex();

        String getAge();

        void showInputError(String et, String message);
    }

    interface Presenter extends BasePresenter {
        boolean saveInfo();
    }

}
