package com.hele.hardware.analyser.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hele.hardware.analyser.gen.DaoMaster;
import com.hele.hardware.analyser.gen.DaoSession;
import com.hele.hardware.analyser.util.Utils;

/**
 * Created by Administrator on 2017/4/20.
 */

public final class DaoManager {

    private static DaoManager sInstance;

    private Context mContext;
    private DaoMaster.OpenHelper mOpenHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoManager() {

    }

    public static DaoManager instance() {
        if (sInstance == null)
            synchronized (DaoManager.class) {
                if (sInstance == null)
                    sInstance = new DaoManager();
            }
        return sInstance;
    }

    public void init(Context context) {
        mContext = context;
        if (Utils.isApkDebuggable(mContext))
            mOpenHelper = new DaoMaster.DevOpenHelper(mContext, "HE-LE-Database.db");
        else
            mOpenHelper = new DaoOpenHelper(mContext, "HE-LE-Database.db");
        mDaoMaster = new DaoMaster(mOpenHelper.getWritableDb());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    private class DaoOpenHelper extends DaoMaster.OpenHelper {

        public DaoOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }
    }
}
