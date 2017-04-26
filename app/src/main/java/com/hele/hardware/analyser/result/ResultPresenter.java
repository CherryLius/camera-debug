package com.hele.hardware.analyser.result;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hele.hardware.analyser.App;
import com.hele.hardware.analyser.dao.ResultInfoDaoHelper;
import com.hele.hardware.analyser.model.ResultInfo;
import com.hele.hardware.analyser.opencv.OpenCVUtil;
import com.hele.hardware.analyser.util.HLog;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultPresenter implements ResultContract.Presenter {

    private static final String TAG = "ResultPresenter";

    private ResultContract.View mView;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    public ResultPresenter(@NonNull ResultContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void setup() {
        startBackgroundThread();
        initOpenCVAsync();
    }

    @Override
    public void destroy() {
        stopBackgroundThread();
    }

    @Override
    public void showImage(ImageView iv, String path) {
        Glide.with(App.getContext())
                .load(path)
                .into(iv);
    }

    @Override
    public void analyse(final String path) {
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                File file = new File(path);
                if (!file.exists()) {
                    mView.showToast("empty path");
                    return;
                }
                float[] levels = OpenCVUtil.clipTestPaper(path);
                if (levels == null) return;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < levels.length; i++) {
                    builder.append("level[")
                            .append(i)
                            .append("]=")
                            .append(levels[i])
                            .append('\n');
                }
                mView.showResult(builder.toString());
            }
        });
    }

    @Override
    public void saveResult(ResultInfo info) {
        ResultInfoDaoHelper.instance().add(info);
    }

    private void initOpenCVAsync() {
        if (!OpenCVLoader.initDebug()) {
            HLog.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, App.getContext(), mLoaderCallback);
        } else {
            HLog.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("result");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            try {
                mBackgroundHandler.removeCallbacksAndMessages(null);
                mBackgroundThread.quitSafely();
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private LoaderCallbackInterface mLoaderCallback = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    HLog.i(TAG, "OpenCV loaded successfully");
                    break;
            }
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };
}