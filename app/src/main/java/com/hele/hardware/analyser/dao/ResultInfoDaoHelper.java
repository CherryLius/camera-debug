package com.hele.hardware.analyser.dao;

import com.hele.hardware.analyser.gen.ResultInfoDao;
import com.hele.hardware.analyser.model.ResultInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultInfoDaoHelper implements IDaoFunction<ResultInfo> {

    private static ResultInfoDaoHelper sInstance;

    private ResultInfoDao mDao;

    public static ResultInfoDaoHelper instance() {
        if (sInstance == null)
            sInstance = new ResultInfoDaoHelper();
        return sInstance;
    }

    private ResultInfoDaoHelper() {
        mDao = DaoManager.instance().getDaoSession().getResultInfoDao();
    }

    @Override
    public long add(ResultInfo data) {
        if (mDao != null)
            return mDao.insertOrReplace(data);
        return -1;
    }

    @Override
    public void delete(long id) {
        if (mDao != null)
            mDao.deleteByKey(id);
    }

    @Override
    public void delete(ResultInfo data) {
        if (mDao != null)
            mDao.delete(data);
    }

    @Override
    public void update(ResultInfo data) {
        if (mDao != null)
            mDao.insertOrReplace(data);
    }

    @Override
    public ResultInfo query(long id) {
        if (mDao != null)
            return mDao.load(id);
        return null;
    }

    @Override
    public long getTotalCount() {
        if (mDao != null) {
            QueryBuilder<ResultInfo> queryBuilder = mDao.queryBuilder();
            return queryBuilder.buildCount().count();
        }
        return 0;
    }

    @Override
    public List<ResultInfo> getAll() {
        if (mDao != null)
            return mDao.loadAll();
        return null;
    }

    @Override
    public void deleteAll() {
        if (mDao != null)
            mDao.deleteAll();
    }
}
