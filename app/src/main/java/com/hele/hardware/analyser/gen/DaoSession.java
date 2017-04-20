package com.hele.hardware.analyser.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.hele.hardware.analyser.model.ResultInfo;

import com.hele.hardware.analyser.gen.ResultInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig resultInfoDaoConfig;

    private final ResultInfoDao resultInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        resultInfoDaoConfig = daoConfigMap.get(ResultInfoDao.class).clone();
        resultInfoDaoConfig.initIdentityScope(type);

        resultInfoDao = new ResultInfoDao(resultInfoDaoConfig, this);

        registerDao(ResultInfo.class, resultInfoDao);
    }
    
    public void clear() {
        resultInfoDaoConfig.clearIdentityScope();
    }

    public ResultInfoDao getResultInfoDao() {
        return resultInfoDao;
    }

}
