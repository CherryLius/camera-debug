package com.hele.hardware.analyser.capture;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.camera.CameraCompact;
import com.hele.hardware.analyser.opengl.GLRotation;
import com.hele.hardware.analyser.opengl.OpenGLUtils;
import com.hele.hardware.analyser.opengl.Rotation;
import com.hele.hardware.analyser.util.HLog;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CaptureRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CaptureRenderer";

    private GLSurfaceView mGLSurfaceView;

    private YUVFilter mFilter;
    private SurfaceTexture mSurfaceTexture;
    private int mTextureId = OpenGLUtils.NO_TEXTURE;
    private CameraCompact mCameraCompact;

    public CaptureRenderer(@NonNull Context context, @NonNull GLSurfaceView glSurfaceView) {
        mGLSurfaceView = glSurfaceView;

        mFilter = new YUVFilter(context);

        mTextureId = OpenGLUtils.getExternalOESTextureId();
        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(CaptureRenderer.this);

        mCameraCompact = new CameraCompact(context);
        mCameraCompact.setSurfaceTexture(mSurfaceTexture);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(this);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        HLog.i(TAG, "onSurfaceCreated");
        GLES20.glDisable(GLES20.GL_DITHER);
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mFilter.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
            float[] mtx = new float[16];
            mSurfaceTexture.getTransformMatrix(mtx);
            mFilter.render(mTextureId, mtx);
        }
    }

    public void resume() {
        mGLSurfaceView.onResume();
        mCameraCompact.start();
        boolean flipHorizontal = mCameraCompact.isFrontCamera();
        HLog.d("Test", "orientation=" + mCameraCompact.getOrientation());
        adjustPosition(mCameraCompact.getOrientation(), flipHorizontal, !flipHorizontal);
    }

    public void pause() {
        mCameraCompact.stop();
        mGLSurfaceView.onPause();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mGLSurfaceView.requestRender();
    }

    private void adjustPosition(int orientation, boolean flipHorizontal, boolean flipVertical) {
        Rotation rotation = Rotation.valueOf(orientation);
        HLog.i(TAG, "[adjustPosition] orientation=" + orientation + ",rotation=" + rotation);
        float[] textureCords = GLRotation.getRotation(rotation, flipHorizontal, flipVertical);
        mFilter.updateTextureBuffer(textureCords);
    }

    public CameraCompact getCameraCompat() {
        return mCameraCompact;
    }
}
