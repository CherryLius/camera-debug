package com.hele.hardware.analyser.camera;

import android.graphics.SurfaceTexture;
import android.os.Handler;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface IFunction {
    void openCamera(Handler handler);

    void closeCamera();

    void capture();

    void startPreview();

    void stopPreview();

    void setSurfaceTexture(SurfaceTexture texture);

    int getOrientation();

    boolean isFrontCamera();

    void setCameraCallback(ICameraCallback cb);

    ICameraCallback getCameraCallback();
}
