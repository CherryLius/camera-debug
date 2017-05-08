package com.hele.hardware.analyser.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.hele.hardware.analyser.App;
import com.hele.hardware.analyser.util.FileUtils;
import com.hele.hardware.analyser.util.HLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ImageManager {

    private static final String TAG = "ImageManager";

    private static ImageManager sInstance;

    private Context mContext;
    private Matrix mMatrix;

    private SimpleDateFormat mFormat;
    private File mParentFile;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    public static ImageManager instance() {
        if (sInstance == null)
            synchronized (ImageManager.class) {
                if (sInstance == null)
                    sInstance = new ImageManager();
            }
        return sInstance;
    }

    private ImageManager() {
        mContext = App.getContext();
        mFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        mParentFile = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        FileUtils.checkFileExist(mParentFile);
        startBackgroundThread();
    }

    public void release() {
        stopBackgroundThread();
        mContext = null;
        sInstance = null;
    }

    public void execute(final Image image, final IFunction function) {
        if (image == null) return;
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    Bitmap bitmap = decodeBitmap(mContext, bytes);
                    if (function.getCameraCallback() != null)
                        function.getCameraCallback().onCaptured(bitmap, bytes);
                } finally {
                    image.close();
                }
            }
        });
    }

    public void execute(final byte[] data, final IFunction function) {
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                byte[] bytes = data;
                if (function.isFrontCamera()) {
                    bitmap = rotateBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bytes = bos.toByteArray();
                } else {
                    bitmap = decodeBitmap(mContext, bytes);
                }
                if (function.getCameraCallback() != null)
                    function.getCameraCallback().onCaptured(bitmap, bytes);
            }
        });
    }

    public void saveImage(final Bitmap bitmap, final byte[] data, final IFunction function) {
        mBackgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                String fileName = mFormat.format(date) + ".jpg";
                File file = new File(mParentFile, fileName);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(data);
                    outputStream.flush();

                    fileScan(file.getAbsolutePath());
                    if (function.getCameraCallback() != null) {
                        function.getCameraCallback().onPictureSaved(bitmap, file.getAbsolutePath());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    HLog.e(TAG, "FileNotFound", e);
                } catch (IOException e) {
                    e.printStackTrace();
                    HLog.e(TAG, "IOException", e);
                } finally {
                    if (outputStream != null)
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("image");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        if (mBackgroundThread != null) {
            mBackgroundHandler.removeCallbacksAndMessages(null);
            mBackgroundThread.quitSafely();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }
    }

    private void fileScan(String... path) {
        MediaScannerConnection.scanFile(mContext, path, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                HLog.d(TAG, "scan file !!!!!!!!!!!!");
            }
        });
    }


    private Bitmap rotateBitmap(@NonNull Bitmap bm) {
        if (mMatrix == null)
            mMatrix = new Matrix();
        mMatrix.reset();
        mMatrix.postRotate(180);
        mMatrix.postScale(-1, 1);
        Bitmap ret = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mMatrix, true);
        return ret;
    }

    private static Bitmap decodeBitmap(Context context, byte[] bytes) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        HLog.d(TAG, "options: " + options.outWidth + "x" + options.outHeight);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int targetDensityDpi = dm.densityDpi;
        int expectW = dm.widthPixels;
        int expectH = dm.heightPixels;
        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, expectW, expectH);

        double xScale = options.outWidth / (float) expectW;
        double yScale = options.outHeight / (float) expectH;
        HLog.d(TAG, "xScale=" + xScale + ",yScale=" + yScale
                + ",targetDensity=" + targetDensityDpi);

        options.inTargetDensity = targetDensityDpi;
        options.inJustDecodeBounds = false;
        Bitmap ret = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return ret;
    }

    private static int calculateInSampleSize(int outWidth, int outHeight, int expectW, int expectH) {
        int inSampleSize = 1;
        if (outWidth > expectW || outHeight > expectH) {
            final int halfHeight = outWidth / 2;
            final int halfWidth = outHeight / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > expectH
                    && (halfWidth / inSampleSize) > expectW) {
                inSampleSize *= 2;
            }
        }
        HLog.i(TAG, "inSampleSize=" + inSampleSize);
        return inSampleSize;
    }
}
