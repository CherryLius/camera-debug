package com.hele.hardware.analyser;

import android.app.Application;
import android.content.Context;

import com.hele.hardware.analyser.dao.DaoManager;
import com.hele.hardware.analyser.util.CrashHandler;

/**
 * Created by Administrator on 2017/4/11.
 */

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sContext == null)
            sContext = this;
        CrashHandler.instance().init(this);
        DaoManager.instance().init(this);
    }

    public static Context getContext() {
        return sContext;
    }
}
