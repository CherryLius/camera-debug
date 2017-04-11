package com.hele.hardware.analyser;

import android.content.Context;
import android.support.v4.app.Fragment;

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

    public boolean onBackPressed() {
        return false;
    }
}
