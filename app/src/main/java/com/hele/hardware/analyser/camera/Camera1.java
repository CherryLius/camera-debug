package com.hele.hardware.analyser.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Size;

import com.hele.hardware.analyser.util.HLog;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Camera1 implements Function {
    private static final String TAG = "Camera1";

    private Context mContext;
    private int mCameraId;
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private Camera.CameraInfo mCameraInfo;

    private SurfaceTexture mSurfaceTexture;

    private Handler mBackgroundHandler;
    private String mFilePath;
    private CameraCallback mCallback;

    public Camera1(Context context) {
        mContext = context;
        mCameraInfo = new Camera.CameraInfo();
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        mFilePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }


    @Override
    public void openCamera(Handler handler) {
        mBackgroundHandler = handler;
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mCamera = Camera.open(mCameraId);

                    setupCameraParams();
                    mCamera.setParameters(mParameters);
                    mCamera.setPreviewTexture(mSurfaceTexture);
                    mCamera.startPreview();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void closeCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void capture() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //stopPreview();
                mBackgroundHandler.post(new ImageSaver(mContext, data, mFilePath, Camera1.this));
                startPreview();
            }
        });
    }

    @Override
    public void startPreview() {
        if (mCamera != null)
            mCamera.startPreview();
    }

    @Override
    public void stopPreview() {
        if (mCamera != null)
            mCamera.stopPreview();
    }

    @Override
    public void setSurfaceTexture(SurfaceTexture texture) {
        mSurfaceTexture = texture;
    }

    @Override
    public int getOrientation() {
        Camera.getCameraInfo(mCameraId, mCameraInfo);
        return mCameraInfo.orientation;
    }

    @Override
    public boolean isFrontCamera() {
        return mCameraId != Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    @Override
    public void setCameraCallback(CameraCallback cb) {
        mCallback = cb;
    }

    @Override
    public CameraCallback getCameraCallback() {
        return mCallback;
    }

    private void setupCameraParams() {
        mParameters = mCamera.getParameters();
        //mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mParameters.setPreviewFormat(ImageFormat.NV21);
        mParameters.setPictureFormat(ImageFormat.JPEG);
        mParameters.setRotation(90);

        List<String> focusModes = mParameters.getSupportedFocusModes();
//        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
//            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//        }
        if (focusModes.contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        double screenRatio = CameraUtil.findFullscreenRatio(mContext,
                cameraSizesToArray(mParameters.getSupportedPictureSizes()));

        Size previewSize = CameraUtil.getOptimalPreviewSize(mContext,
                cameraSizesToArray(mParameters.getSupportedPreviewSizes()),
                screenRatio, false);
        mParameters.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());

        Size pictureSize = CameraUtil.getOptimalPictureSize(
                cameraSizesToArray(mParameters.getSupportedPictureSizes()),
                screenRatio, 640, 480);
        mParameters.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
        HLog.e(TAG, "optimal pic size: " + pictureSize.getWidth() + "x" + pictureSize.getHeight());
    }

    @NonNull
    private Size[] cameraSizesToArray(@NonNull List<Camera.Size> sizes) {
        Size[] result = new Size[sizes.size()];
        for (int i = 0; i < sizes.size(); i++) {
            result[i] = cameraSizeToSize(sizes.get(i));
        }
        return result;
    }

    private static Size cameraSizeToSize(Camera.Size size) {
        return new Size(size.width, size.height);
    }
}
