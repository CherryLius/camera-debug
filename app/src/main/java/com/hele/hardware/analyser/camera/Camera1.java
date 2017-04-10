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

    //draw on openGL and get bitmap from openGL
//    private Bitmap doResult(Bitmap bitmap, boolean isRotated) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int[] frameBuffers = new int[1];
//        int[] frameBufferTexture = new int[1];
//
//        GLES20.glGenFramebuffers(1, frameBuffers, 0);
//        GLES20.glGenTextures(1, frameBufferTexture, 0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTexture[0]);
//        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
//                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
//        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
//                GLES20.GL_TEXTURE_2D, frameBufferTexture[0], 0);
//        GLES20.glViewport(0, 0, width, height);
//
//        int textureId = OpenGLUtils.loadTexture(bitmap, OpenGLUtils.NO_TEXTURE, true);
//        FloatBuffer glCubeBuffer = ByteBuffer.allocateDirect(GLRotation.CUBE.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        FloatBuffer glTextureBuffer = ByteBuffer.allocateDirect(GLRotation.TEXTURE_ROTATION_0.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        glCubeBuffer.put(GLRotation.CUBE).position(0);
//        glTextureBuffer.put(GLRotation.getRotation(Rotation.ROTATION_0, false, !isRotated)).position(0);
//
//        IntBuffer ib = IntBuffer.allocate(width * height);
//        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
//
//        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        result.copyPixelsFromBuffer(ib);
//
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
//        GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
//        GLES20.glDeleteTextures(frameBufferTexture.length, frameBufferTexture, 0);
//
//        return bitmap;
//    }
}
