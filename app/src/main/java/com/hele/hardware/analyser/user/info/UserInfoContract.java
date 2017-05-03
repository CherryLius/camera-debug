package com.hele.hardware.analyser.user.info;

import android.content.Context;

import com.cherry.library.ui.view.recycler.SectionItemDecoration;
import com.hele.hardware.analyser.BasePresenter;
import com.hele.hardware.analyser.BaseView;
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

        void gotoUserAdd(Context context);
    }
}
