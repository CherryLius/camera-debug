package com.hele.hardware.analyser.capture;

import android.support.annotation.NonNull;

import com.hele.hardware.analyser.camera.CameraCallback;
import com.hele.hardware.analyser.camera.CameraCompact;

/**
 * Created by Administrator on 2017/4/7.
 */

public class CapturePresenter implements CaptureContract.Presenter, CameraCallback {

    CaptureContract.View mView;
    CameraCompact mCameraCompact;

    public CapturePresenter(@NonNull CaptureContract.View view, @NonNull CaptureRenderer renderer) {
        mView = view;
        mView.setPresenter(this);
        mCameraCompact = renderer.getCameraCompat();
        mCameraCompact.setCallback(this);
    }

    @Override
    public void capture() {
        mCameraCompact.capture();
    }

    @Override
    public void onPictureSaved(String filePath) {
        mView.showToast(filePath);
    }
}
