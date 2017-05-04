package com.hele.hardware.analyser.user.info;

import android.app.Activity;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;
import com.hele.hardware.analyser.model.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */

public interface UserInfoContract {

    interface View extends BaseView<Presenter> {
        void showUsers(List<UserInfo> users);
    }

    interface Presenter extends BasePresenter {
        void loadUsers();

        SectionItemDecoration.ISectionProvider getSectionProvider();

        void gotoUserAdd(Activity activity, int requestCode);
    }
}
