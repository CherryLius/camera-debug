package com.hele.hardware.analyser.capture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.hele.hardware.analyser.camera.CameraCompact;
import com.hele.hardware.analyser.capture.filter.ImageFilter;
import com.hele.hardware.analyser.capture.filter.RendererFilter;
import com.hele.hardware.analyser.capture.filter.YUVFilter;
import com.hele.hardware.analyser.opengl.GLRotation;
import com.hele.hardware.analyser.opengl.OpenGLUtils;
import com.hele.hardware.analyser.opengl.Rotation;
import com.hele.hardware.analyser.util.HLog;

import java.lang.ref.SoftReference;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/4/6.
 */

public class CaptureRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private static final String TAG = "CaptureRenderer";

    private Context mContext;
    private GLSurfaceView mGLSurfaceView;

    private RendererFilter mFilter;
    private SurfaceTexture mSurfaceTexture;
    private int mTextureId = OpenGLUtils.NO_TEXTURE;
    private CameraCompact mCameraCompact;

    private ArrayMap<Integer, RendererFilter> mFilterMap;
    private SoftReference<Bitmap> mBitmapReference;

    static final int STATE_CAPTURE = 0;
    static final int STATE_PICTURE = 1;
    private int mState;

    public CaptureRenderer(@NonNull Context context, @NonNull GLSurfaceView glSurfaceView) {
        mContext = context;
        mGLSurfaceView = glSurfaceView;
        mState = STATE_CAPTURE;

        mFilterMap = new ArrayMap<>(2);

        filterFromState(mState);

        //Camera preview textureId
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

        mFilter.setup();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        if (mState == STATE_PICTURE) {
            Bitmap bitmap = getBitmapFromRefer();
            int textureId = OpenGLUtils.loadTexture(bitmap, OpenGLUtils.NO_TEXTURE);
            HLog.e(TAG, "draw picture");
            mFilter.render(textureId, null);
        } else if (mState == STATE_CAPTURE) {
            if (mSurfaceTexture != null) {
                mSurfaceTexture.updateTexImage();
                float[] mtx = new float[16];
                mSurfaceTexture.getTransformMatrix(mtx);
                mFilter.render(mTextureId, mtx);
            }
        }
    }

    public void resume() {
        mGLSurfaceView.onResume();
        mCameraCompact.start();
        boolean flipHorizontal = mCameraCompact.isFrontCamera();
        HLog.d(TAG, "orientation=" + mCameraCompact.getOrientation());
        adjustPosition(mCameraCompact.getOrientation(), flipHorizontal, !flipHorizontal);
    }

    public void pause() {
        mCameraCompact.stop();
        mGLSurfaceView.onPause();
    }

    public void destroy() {
        mGLSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                for (RendererFilter f : mFilterMap.values()) {
                    if (f != null)
                        f.destroy();
                }
            }
        });
        Bitmap bm = getBitmapFromRefer();
        if (bm != null && !bm.isRecycled()) {
            bm.recycle();
        }
        mBitmapReference.clear();
        mBitmapReference = null;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (mState == STATE_CAPTURE) {
            mGLSurfaceView.requestRender();
        }
    }

    public CameraCompact getCameraCompat() {
        return mCameraCompact;
    }

    public void setFilter(final int state) {
        HLog.i(TAG, "setFilter");
        mGLSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                mState = state;
                if (mFilter != null)
                    mFilter.destroy();
                filterFromState(state);
                mFilter.setup();
            }
        });
        mGLSurfaceView.requestRender();
    }

    public void setBitmap(final Bitmap bm) {
        Bitmap oldBitmap = getBitmapFromRefer();
        if (bm != null && oldBitmap != bm) {
            if (oldBitmap != null && !oldBitmap.isRecycled())
                oldBitmap.recycle();
            mBitmapReference = new SoftReference<>(bm);
        }
    }

    public boolean isInCapture() {
        return mState == STATE_CAPTURE;
    }

    private void adjustPosition(int orientation, boolean flipHorizontal, boolean flipVertical) {
        Rotation rotation = Rotation.valueOf(orientation);
        HLog.i(TAG, "[adjustPosition] orientation=" + orientation + ",rotation=" + rotation);
        float[] textureCords = GLRotation.getRotation(rotation, flipHorizontal, flipVertical);
        mFilter.updateTexture(textureCords);
    }

    private void filterFromState(final int state) {
        if (mFilterMap.containsKey(state)) {
            mFilter = mFilterMap.get(state);
        } else {
            if (state == STATE_CAPTURE) {
                mFilter = new YUVFilter(mContext);
            } else if (state == STATE_PICTURE) {
                mFilter = new ImageFilter(mContext);
            }
            if (mFilter != null)
                mFilterMap.put(state, mFilter);
        }
    }

    private Bitmap getBitmapFromRefer() {
        if (mBitmapReference == null)
            return null;
        return mBitmapReference.get();
    }
}
