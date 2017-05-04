package com.hele.hardware.analyser.result;

import com.hele.hardware.analyser.base.BasePresenter;
import com.hele.hardware.analyser.base.BaseView;
import com.hele.hardware.analyser.model.ResultInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultListContract {

    interface View extends BaseView<Presenter> {
        void showResults(List<ResultInfo> results);
    }

    interface Presenter extends BasePresenter {
        void loadResults();
    }
}
