package com.hele.hardware.analyser.base;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);

}
