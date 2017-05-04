package com.hele.hardware.analyser.user.add;

import android.app.Activity;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;

/**
 * Created by Administrator on 2017/5/3.
 */

public interface UserAddContract {

    interface View extends BaseView<Presenter> {
        String getName();

        String getSex();

        String getBirthday();

        void showChosenBirthday(String birthday);

        void showInputError(String et, String message);
    }

    interface Presenter extends BasePresenter {
        boolean saveInfo();

        void chooseBirthday(Activity activity);

        void hideKeyboard(Activity activity);
    }

}
