package com.hele.hardware.analyser.camera;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/4/7.
 */

public interface CameraCallback {
    void onPictureSaved(Bitmap bitmap, String filePath);
}
