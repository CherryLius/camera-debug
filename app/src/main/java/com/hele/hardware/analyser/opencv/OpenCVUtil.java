package com.hele.hardware.analyser.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;

import com.hele.hardware.analyser.util.Logger;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class OpenCVUtil {

    private static final String TAG = "OpenCVUtil";

    public static Bitmap mat2bitmap(Mat mat) {
        if (mat == null)
            return null;
        if (mat.width() == 0 || mat.height() == 0)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

    public static Mat rgb2gray(String filename) {
        return Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
    }

    public static Mat rgb2gray(Context context, @IdRes int id) {
        try {
            Mat mat = Utils.loadResource(context, id);
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
            return mat;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 局部自适应二值化
     */
    public static void adaptiveThreshold(Mat src, Mat dst) {
        // 11 4/11 2
        Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 2);
    }

    public static void adaptiveThreshold(Mat src, Mat dst, int block, int C) {
        // 11 4/11 2
        Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, block, C);
    }

    /**
     * 找轮廓
     */
    public static void findContours(Mat mat, List<MatOfPoint> points) {
        Imgproc.findContours(mat, points, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
    }

    /**
     * 获取试纸板边界rect
     */
    private static Rect findBoundRect(List<MatOfPoint> points) {
        Rect rect = null;
        double maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            MatOfPoint point = points.get(i);
            Rect tmp = Imgproc.boundingRect(point);
            double area = tmp.area();
            if (maxArea < area) {
                maxArea = area;
                rect = tmp;
            }
        }
        return rect;
    }

    /**
     * 浓度区域边界
     *
     * @param points
     * @return
     */
    private static Rect findRect(List<MatOfPoint> points) {
        Rect maxAreaRect = null;
        Rect secondAreaRect = null;
        for (int i = 0; i < points.size(); i++) {
            MatOfPoint point = points.get(i);
            Rect tmp = Imgproc.boundingRect(point);
            double area = tmp.area();
            if (maxAreaRect == null) {
                maxAreaRect = tmp;
                secondAreaRect = maxAreaRect;
            } else if (area > maxAreaRect.area()) {
                secondAreaRect = maxAreaRect;
                maxAreaRect = tmp;
            } else if (secondAreaRect.area() == maxAreaRect.area()
                    || area > secondAreaRect.area()) {
                secondAreaRect = tmp;
            }
        }
        return secondAreaRect;
    }

    /**
     * 直方图
     */

    public static Mat calcHist(Mat src, int histSize) {
        Mat hist = new Mat();
        Imgproc.calcHist(Arrays.asList(src), new MatOfInt(0), new Mat(), hist, new MatOfInt(histSize), new MatOfFloat(0, histSize));
        return hist;
    }

    /**
     * 裁切试纸板得到浓度区域
     *
     * @param path
     */
    public static float[] clipPaper(String path) {
        return clipPaper(path, 11, 4);
    }


    public static float[] clipPaper(String path, int block, int C) {
        //灰度图
        Mat grayMat = rgb2gray(path);

        //二值化
        Mat dst = new Mat();
        adaptiveThreshold(grayMat, dst, block, C);
        //裁切
        grayMat = clip2clip(grayMat, dst);

        //灰度直方图
        Mat hist = calcHist(grayMat, 256);
        //背景阈值
        double bgValue = 255 - histMaxLoc(hist);
        return calcGrayLevel(grayMat, averageGray(grayMat, bgValue), 4, false);
    }

    //裁切
    public static Mat clip2clip(Mat srcMat, Mat thresholdMat) {
        List<MatOfPoint> points = new ArrayList<>();
        findContours(thresholdMat, points);

        //裁切背景
        Rect rect = findBoundRect(points);
        if (rect != null) {
            srcMat = new Mat(srcMat, rect);
            thresholdMat = new Mat(thresholdMat, rect);
        }
        //找浓度区域
        points.clear();
        findContours(thresholdMat, points);

        rect = findRect(points);
        //二次裁切 大小根据最终拍摄格式来调整
        if (rect != null) {
            rect = new Rect(rect.x + (int) (rect.width * 0.2f),
                    rect.y + (int) (rect.height * 0.2f),
                    (int) (rect.width * 0.6f),
                    (int) (rect.height * 0.6f));
            srcMat = new Mat(srcMat, rect);
        }
        return srcMat;
    }

    private static double histMaxLoc(Mat hist) {
        /*
         *  直方图：纵坐标代表每一种颜色值在图像中的像素总数
         *        横坐标代表图像像素种类：颜色值 灰度值
         */
        Core.MinMaxLocResult minMaxLoc = Core.minMaxLoc(hist);
        Logger.e(TAG, "min:loc=" + minMaxLoc.minLoc + ", val = " + minMaxLoc.minVal
                + ", max:loc=" + minMaxLoc.maxLoc + ", val=" + minMaxLoc.maxVal);
        return minMaxLoc.maxLoc.y;
    }

    //纵向灰度平均值
    private static float[] averageGray(Mat img, double bgValue) {
        float[] avg = new float[img.cols()];
        for (int i = 0; i < img.cols(); i++) {
            for (int j = 0; j < img.rows(); j++) {
                double[] val = img.get(j, i);
                avg[i] += (255 - val[0]);
            }
            avg[i] /= img.rows();
            if (avg[i] < bgValue) {
                avg[i] = 0;
            } else {
                avg[i] -= bgValue;
            }
            Logger.e(TAG, "avg[" + i + "]=" + avg[i]);
        }
        return avg;
    }

    //计算灰度值
    private static float[] calcGrayLevel(Mat img, float[] avg, int count, boolean reverse) {

        int wSize = 12;
        Float[] weightValues = new Float[count];
        float[] weights = new float[count];
        for (int i = 0; i < count; i++) {
            float maxVal = 0.0f;
            for (int j = 0; j < (img.cols() / count); j++) {
                if (j + wSize >= img.cols() / count)
                    break;
                float val = .0f;
                int x = i * img.cols() / count + j;
                for (int k = 0; k < wSize; k++) {
                    val += avg[x + k];
                }
                if (val > maxVal)
                    maxVal = val;
            }
            weightValues[i] = maxVal;
            //weights[i] = maxVal / weightValues[0];
        }

        if (reverse) {
            reverse(weightValues);
        }

        for (int i = 0; i < weightValues.length; i++) {
            weights[i] = weightValues[i] / weightValues[0];
        }

        for (int i = 0; i < weights.length; i++) {
            Logger.e(TAG, "weight=" + weights[i] + ",val=" + weightValues[i]);
        }
        float[] result = new float[weightValues.length];
        for (int i = 0; i < weightValues.length; i++) {
            result[i] = weightValues[i];
        }
        return result;
    }

    private static <T> void reverse(T[] array) {
        int size = array.length;
        for (int i = 0; i < size / 2; i++) {
            T tmp = array[i];
            array[i] = array[size - 1 - i];
            array[size - 1 - i] = tmp;
        }
    }
}
