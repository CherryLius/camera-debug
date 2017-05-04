package com.hele.hardware.analyser.main;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;
import com.hele.hardware.analyser.model.CardItem;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {
        void showMenu(List<CardItem> list);
    }

    interface Presenter extends BasePresenter {
        void initMenu();

        void gotoAnalyser();

        void gotoQuery();

        void gotoMore();
    }
}
