package com.hele.hardware.analyser.result;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hele.hardware.analyser.App;
import com.hele.hardware.analyser.dao.ResultInfoDaoHelper;
import com.hele.hardware.analyser.model.ImmunityInfo;
import com.hele.hardware.analyser.model.ResultInfo;
import com.hele.hardware.analyser.opencv.OpenCVUtil;
import com.hele.hardware.analyser.util.Logger;
import com.hele.hardware.analyser.util.Utils;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ResultPresenter implements ResultContract.Presenter {

    private static final String TAG = "ResultPresenter";

    private ResultContract.View mView;

    private ExecutorService mExecutor;
    private AnalyseTask mTask;
    private Gson mGson;
//    private HandlerThread mBackgroundThread;
//    private Handler mBackgroundHandler;

    public ResultPresenter(@NonNull ResultContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mGson = new Gson();
    }

    @Override
    public void setup() {
        mExecutor = Executors.newFixedThreadPool(5);
        //startBackgroundThread();
        initOpenCVAsync();
    }

    @Override
    public void destroy() {
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
            mTask = null;
        }
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
        }
        //stopBackgroundThread();
    }

    @Override
    public void showImage(ImageView iv, String path) {
        Glide.with(App.getContext())
                .load(path)
                .into(iv);
    }

    @Override
    public void analyse(final String path) {
        mTask = new AnalyseTask(mView);
        mTask.executeOnExecutor(mExecutor, path);
        //analyseOnThread(path);
    }

    @Override
    public void saveResult(String path, ImmunityInfo info) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setName(Utils.getRandomEnglish());
        resultInfo.setIdentity(Utils.getRandomNumber());
        resultInfo.setPicturePath(path);
        resultInfo.setDateTime(new Date().getTime());
        resultInfo.setValue(mGson.toJson(info));
        ResultInfoDaoHelper.instance().add(resultInfo);
        mView.showToast("保存成功");
    }


    private void initOpenCVAsync() {
        if (!OpenCVLoader.initDebug()) {
            Logger.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, App.getContext(), mLoaderCallback);
        } else {
            Logger.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

//    private void analyseOnThread(final String path) {
//        mBackgroundHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                File file = new File(path);
//                if (!file.exists()) {
//                    mView.showToast("empty path");
//                    return;
//                }
//                float[] levels = OpenCVUtil.clipPaper(path);
//                if (levels == null) return;
//                StringBuilder builder = new StringBuilder();
//                for (int i = 0; i < levels.length; i++) {
//                    builder.append("level[")
//                            .append(i)
//                            .append("]=")
//                            .append(levels[i])
//                            .append('\n');
//                }
//                mView.showResult(builder.toString());
//            }
//        });
//    }
//
//    private void startBackgroundThread() {
//        mBackgroundThread = new HandlerThread("result");
//        mBackgroundThread.start();
//        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
//    }
//
//    private void stopBackgroundThread() {
//        if (mBackgroundThread != null) {
//            mBackgroundHandler.removeCallbacksAndMessages(null);
//            mBackgroundThread.quitSafely();
//            mBackgroundThread = null;
//            mBackgroundHandler = null;
//        }
//    }

    private LoaderCallbackInterface mLoaderCallback = new LoaderCallbackInterface() {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Logger.i(TAG, "OpenCV loaded successfully");
                    break;
            }
        }

        @Override
        public void onPackageInstall(int operation, InstallCallbackInterface callback) {

        }
    };

    private class AnalyseTask extends AsyncTask<String, Void, ImmunityInfo> {

        private SoftReference<ResultContract.View> mViewRef;

        public AnalyseTask(ResultContract.View view) {
            mViewRef = new SoftReference<>(view);
        }

        @Override
        protected ImmunityInfo doInBackground(String... params) {
            if (params == null || params.length == 0)
                return null;
            String path = params[0];
            File file = new File(path);
            if (!file.exists() && mViewRef.get() != null) {
                mViewRef.get().showToast("empty path");
                return null;
            }
            float[] levels = OpenCVUtil.clipPaper(path);
            if (levels == null) return null;
            ImmunityInfo info = new ImmunityInfo(levels[0], levels[1], levels[2], levels[3]);
            return info;
        }

        @Override
        protected void onPostExecute(ImmunityInfo info) {
            super.onPostExecute(info);
            if (info != null && mViewRef.get() != null) {
                mViewRef.get().showResult(info);
            }
        }
    }
}