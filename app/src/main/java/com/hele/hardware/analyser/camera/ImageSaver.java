package com.hele.hardware.analyser.camera;

/**
 * Created by Administrator on 2017/4/10.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.hele.hardware.analyser.util.HLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSaver implements Runnable {
    private static final String TAG = "ImageSaver";

    private Context mContext;
    private Matrix mMatrix;
    /**
     * image data
     */
    private byte[] mData;
    /**
     * for camera2
     */
    private Image mImage;

    private File mFile;

    private Function mFunction;

    public ImageSaver(Context context, byte[] data, String parentPath, Function function) {
        init(context, parentPath, function);
        mData = data;
    }

    public ImageSaver(Context context, Image image, String parentPath, Function function) {
        init(context, parentPath, function);
        mImage = image;
    }

    private void init(Context context, String parentPath, Function function) {
        mContext = context;
        mFunction = function;

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        mFile = new File(parentPath, fileName);
    }


    @Override
    public void run() {
        if (mImage != null) {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            mData = bytes;
        } else {
            if (mFunction.isFrontCamera()) {
                Bitmap bitmap = rotateBitmap(BitmapFactory.decodeByteArray(mData, 0, mData.length));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                mData = bos.toByteArray();
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mFile);
            outputStream.write(mData);
            outputStream.flush();

            fileScan(mFile.getAbsolutePath());
            if (mFunction.getCameraCallback() != null) {
                mFunction.getCameraCallback().onPictureSaved(mFile.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            HLog.e(TAG, "FileNotFound", e);
        } catch (IOException e) {
            e.printStackTrace();
            HLog.e(TAG, "IOException", e);
        } finally {
            if (mImage != null)
                mImage.close();
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
