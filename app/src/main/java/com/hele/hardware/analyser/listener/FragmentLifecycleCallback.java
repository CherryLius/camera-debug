package com.hele.hardware.analyser.listener;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface FragmentLifecycleCallback {
    void onAttachActivity(Fragment fragment);

    void onDetachActivity(Fragment fragment);
}
