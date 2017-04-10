package com.hele.hardware.analyser;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface LifecycleCallback {
    void onAttachActivity(Fragment fragment);

    void onDetachActivity(Fragment fragment);
}
