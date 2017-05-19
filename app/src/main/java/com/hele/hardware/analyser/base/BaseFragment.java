package com.hele.hardware.analyser.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.hele.hardware.analyser.listener.LifecycleCallback;

/**
 * Created by Administrator on 2017/4/7.
 */

public class BaseFragment extends Fragment {
    private LifecycleCallback lifecycleCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LifecycleCallback)
            lifecycleCallback = (LifecycleCallback) context;
        if (lifecycleCallback != null)
            lifecycleCallback.onAttachActivity(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (lifecycleCallback != null) {
            lifecycleCallback.onDetachActivity(this);
            lifecycleCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean onBackPressed() {
        return false;
    }
}
