package com.hele.hardware.analyser.dao;

import com.hele.hardware.analyser.gen.UserInfoDao;
import com.hele.hardware.analyser.model.UserInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/3.
 */

public class UserInfoDaoHelper implements IDaoFunction<UserInfo> {
    private static UserInfoDaoHelper sInstance;

    private UserInfoDao mDao;

    public static UserInfoDaoHelper instance() {
        if (sInstance == null)
            sInstance = new UserInfoDaoHelper();
        return sInstance;
    }

    private UserInfoDaoHelper() {
        mDao = DaoManager.instance().getDaoSession().getUserInfoDao();
    }

    @Override
    public long add(UserInfo data) {
        if (mDao != null)
            return mDao.insertOrReplace(data);
        return -1;
    }

    @Override
    public void delete(long id) {
        if (mDao != null) {
            mDao.deleteByKey(id);
        }
    }

    @Override
    public void delete(UserInfo data) {
        if (mDao != null)
            mDao.delete(data);
    }

    @Override
    public void update(UserInfo data) {
        if (mDao != null)
            mDao.insertOrReplace(data);
    }

    @Override
    public UserInfo query(long id) {
        if (mDao != null)
            return mDao.load(id);
        return null;
    }

    @Override
    public long getTotalCount() {
        if (mDao != null) {
            QueryBuilder<UserInfo> queryBuilder = mDao.queryBuilder();
            return queryBuilder.buildCount().count();
        }
        return 0;
    }

    @Override
    public List<UserInfo> getAll() {
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
