package com.hele.hardware.analyser.debug;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hele.hardware.analyser.BaseFragment;
import com.hele.hardware.analyser.R;

/**
 * Created by Administrator on 2017/4/21.
 */

public class DebugFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug, container, false);
    }
}
