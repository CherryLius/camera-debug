package com.hele.hardware.analyser.result;

import com.hele.hardware.analyser.dao.ResultInfoDaoHelper;
import com.hele.hardware.analyser.model.ResultInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultListPresenter implements ResultListContract.Presenter {

    private ResultListContract.View mView;

    public ResultListPresenter(ResultListContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void loadResults() {
        List<ResultInfo> results = ResultInfoDaoHelper.instance().getAll();
        mView.showResults(results);
    }
}
