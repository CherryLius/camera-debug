package com.hele.hardware.analyser.capture;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.camera.ICameraCallback;
import com.hele.hardware.analyser.camera.CameraCompact;
import com.hele.hardware.analyser.camera.ImageManager;

/**
 * Created by Administrator on 2017/4/7.
 */

public class CapturePresenter implements CaptureContract.Presenter, ICameraCallback, CaptureRenderer.OnFilterChangeListener {

    CaptureContract.View mView;
    CameraCompact mCameraCompact;

    private CaptureContract.CaptureListener mListener;
    private byte[] mLastData;
    private Bitmap mLastBitmap;

    private Handler mHandler;

    public CapturePresenter(@NonNull CaptureContract.View view, @NonNull CaptureRenderer renderer) {
        mView = view;
        mView.setPresenter(this);
        mCameraCompact = renderer.getCameraCompat();
        mCameraCompact.setCallback(this);
        renderer.setFilterChangeListener(this);
        mHandler = new Handler();
    }

    @Override
    public void capture() {
        if (mView.isInCapture())
            mCameraCompact.capture();
        else
            mView.showToast("not in Capture!");
    }

    @Override
    public void savePicture() {
        ImageManager.instance().saveImage(mLastBitmap, mLastData, mCameraCompact.getFunction());
    }

    @Override
    public void setCaptureListener(CaptureContract.CaptureListener listener) {
        mListener = listener;
    }

    @Override
    public void onCaptured(Bitmap bitmap, byte[] data) {
        mView.showBitmap(bitmap);
        mView.updateController(false);
        if (bitmap != mLastBitmap)
            mLastBitmap = bitmap;
        if (data != mLastData)
            mLastData = data;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                savePicture();
            }
        }, 500);
    }

    @Override
    public void onPictureSaved(Bitmap bitmap, String filePath) {
        mView.showToast(filePath);
        if (mListener != null)
            mListener.onAnalyse(filePath);
    }

    @Override
    public void onFilterChanged(int filter) {
        if (filter == CaptureRenderer.STATE_CAPTURE)
            mCameraCompact.startPreview();
    }
}
