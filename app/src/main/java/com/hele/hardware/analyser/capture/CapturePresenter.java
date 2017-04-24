package com.hele.hardware.analyser.capture;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.camera.CameraCallback;
import com.hele.hardware.analyser.camera.CameraCompact;

/**
 * Created by Administrator on 2017/4/7.
 */

public class CapturePresenter implements CaptureContract.Presenter, CameraCallback, CaptureRenderer.OnFilterChangeListener {

    CaptureContract.View mView;
    CameraCompact mCameraCompact;

    public CapturePresenter(@NonNull CaptureContract.View view, @NonNull CaptureRenderer renderer) {
        mView = view;
        mView.setPresenter(this);
        mCameraCompact = renderer.getCameraCompat();
        mCameraCompact.setCallback(this);
        renderer.setFilterChangeListener(this);
    }

    @Override
    public void capture() {
        if (mView.isInCapture())
            mCameraCompact.capture();
        else
            mView.showToast("not in Capture!");
    }

    @Override
    public void onPictureSaved(Bitmap bitmap, String filePath) {
        mView.showToast(filePath);
        mView.showBitmap(bitmap, filePath);
        mView.updateController(false);
    }

    @Override
    public void onFilterChanged(int filter) {
        if (filter == CaptureRenderer.STATE_CAPTURE)
            mCameraCompact.startPreview();
    }
}
