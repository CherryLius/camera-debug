package com.hele.hardware.analyser.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.WindowManager;

import com.hele.hardware.analyser.util.HLog;
import com.hele.hardware.analyser.util.Utils;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Camera2 extends CameraDevice.StateCallback implements Function, ImageReader.OnImageAvailableListener {
    private static final String TAG = "Camera2";

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private Context mContext;
    private int mCameraId;// 0为后 1为前
    private CameraManager mCameraManager;
    private CameraDevice mCamera;
    private ImageReader mImageReader;
    private CameraCaptureSession mCaptureSession;

    private Surface mSurface;
    private Handler mBackgroundHandler;
    private CameraCallback mCallback;

    private String mFilePath;

    public Camera2(Context context) {
        mContext = context;
        mCameraManager = Utils.getSystemService(context, Context.CAMERA_SERVICE);
//        mCameraId = CameraCharacteristics.LENS_FACING_BACK;
        mCameraId = CameraCharacteristics.LENS_FACING_FRONT;
        mFilePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    @Override
    public void openCamera(Handler handler) {
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                HLog.e(TAG, "ACCESS CAMERA FAILED!" + ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA));
                return;
            }
            mBackgroundHandler = handler;

            StreamConfigurationMap map = getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);

            double screenRatio = CameraUtil.findFullscreenRatio(mContext, sizes);
            Size picSize = CameraUtil.getOptimalPictureSize(sizes, screenRatio, 640, 480);
            HLog.i(TAG, "picture Size: " + picSize.getWidth() + "x" + picSize.getHeight());
            mImageReader = ImageReader.newInstance(picSize.getWidth(), picSize.getHeight(), ImageFormat.JPEG, 1);
            mImageReader.setOnImageAvailableListener(this, mBackgroundHandler);
            mCameraManager.openCamera(mCameraId + "", this, mBackgroundHandler);

            //debugMethod();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeCamera() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    @Override
    public void capture() {
        if (mCamera == null) {
            HLog.e(TAG, "CameraDevice is null");
            return;
        }
        try {
            CaptureRequest.Builder requestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            requestBuilder.addTarget(mImageReader.getSurface());
            //自动对焦
            requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //闪光灯
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            //rotation
            WindowManager wm = Utils.getSystemService(mContext, Context.WINDOW_SERVICE);
            int rotation = wm.getDefaultDisplay().getRotation();
            HLog.e(TAG, "display rotation=" + rotation);
            requestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CaptureRequest request = requestBuilder.build();
            mCaptureSession.capture(request, new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    HLog.e(TAG, "onCaptureCompleted");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startPreview() {
        try {
            final CaptureRequest.Builder requestBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            requestBuilder.addTarget(mSurface);
            mCamera.createCaptureSession(Arrays.asList(mSurface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (mCamera == null) {
                        HLog.e(TAG, "CameraDevice is null");
                        return;
                    }
                    mCaptureSession = session;
                    try {
                        //自动对焦
                        requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        //闪光灯
                        requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        CaptureRequest request = requestBuilder.build();
                        mCaptureSession.setRepeatingRequest(request, null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    HLog.i(TAG, "configure camera failed.");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopPreview() {
        try {
            mCaptureSession.stopRepeating();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSurfaceTexture(SurfaceTexture texture) {
        if (texture != null) {
            StreamConfigurationMap map = getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
            double screenRatio = CameraUtil.findFullscreenRatio(mContext, sizes);
            Size previewSize = CameraUtil.getOptimalPreviewSize(mContext, sizes, screenRatio, false);
            HLog.e(TAG, "previewSize: " + previewSize);
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            mSurface = new Surface(texture);
        }
    }

    @Override
    public int getOrientation() {
        return (getSensorOrientation() - 90) % 360;
    }

    @Override
    public boolean isFrontCamera() {
        return mCameraId != CameraCharacteristics.LENS_FACING_FRONT;
    }

    @Override
    public void setCameraCallback(CameraCallback cb) {
        mCallback = cb;
    }

    @Override
    public CameraCallback getCameraCallback() {
        return mCallback;
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        mBackgroundHandler.post(new ImageSaver(mContext, reader.acquireNextImage(), mFilePath, this));
    }

    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        mCamera = camera;
        startPreview();
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        closeCamera();
    }

    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        closeCamera();
        HLog.e(TAG, "open Camera err: " + error);
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        HLog.e(TAG, "getPicOrientation=" + ((ORIENTATIONS.get(rotation) + getSensorOrientation() + 270) % 360));
        return (ORIENTATIONS.get(rotation) + getSensorOrientation() + 270) % 360;
    }

    private int getSensorOrientation() {
        int orientation = 0;
        try {
            CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(mCameraId + "");
            orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    private CameraCharacteristics getCharacteristics() {
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId : cameraIdList) {
                HLog.e(TAG, " cameraId= " + cameraId);
                if (!cameraId.equals(mCameraId + ""))
                    continue;
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
                return characteristics;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void debugMethod() {
        try {
            String[] cameraIdList = mCameraManager.getCameraIdList();
            for (String cameraId :
                    cameraIdList) {
                HLog.e(TAG, " cameraId= " + cameraId);
                if (!cameraId.equals(mCameraId + ""))
                    continue;
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(cameraId);
//                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
//                HLog.e(TAG, " facing=" + facing + ",current= " + 0);
//                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT)
//                    continue;
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null)
                    continue;
                Size[] choices = map.getOutputSizes(SurfaceTexture.class);
                for (Size size : choices) {
                    HLog.e(TAG, "size: " + size.toString());
                }
                int orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                HLog.e(TAG, "orientation=" + orientation);

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
