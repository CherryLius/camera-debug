package com.hele.hardware.analyser.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;

import com.hele.hardware.analyser.util.HLog;

import org.opencv.android.Utils;
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
     * 找多边形
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
            } else if (area > secondAreaRect.area()) {
                secondAreaRect = tmp;
            }
        }
        return secondAreaRect;
    }

    /**
     * 直方图
     */

    public static Mat calcHist(Mat src, int histSize, float[] buffer) {
        Mat hist = new Mat();
        Imgproc.calcHist(Arrays.asList(src), new MatOfInt(0), new Mat(), hist, new MatOfInt(histSize), new MatOfFloat(0, histSize));
//        Size sizeRgba = src.size();
//        Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
//        hist.get(0, 0, buffer);
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
        List<MatOfPoint> points = new ArrayList<>();
        Mat img = rgb2gray(path);
        Mat dst = new Mat();

        adaptiveThreshold(img, dst, block, C);
        findContours(dst, points);

        //裁切背景
        Rect rect = findBoundRect(points);
        if (rect != null) {
            dst = new Mat(dst, rect);
            img = new Mat(img, rect);
            //找浓度区域
            points.clear();
            findContours(dst, points);

            rect = findRect(points);
            //二次裁切
            if (rect != null) {
                rect = new Rect(rect.x + (int) (rect.width * 0.08f),
                        rect.y + (int) (rect.height * 0.13f),
                        (int) (rect.width * 0.84f),
                        (int) (rect.height * 0.74f));
                img = new Mat(img, rect);
                return calcGrayLevel(img);
            }
        }
        return null;
    }

    private static int findMaxPixel(Mat hist) {
        /*
         *  直方图：纵坐标代表每一种颜色值在图像中的像素总数
         *        横坐标代表图像像素种类：颜色值 灰度值
         */
        double max = 0;
        int maxPixel = -1;
        for (int i = 0; i < hist.rows(); i++) {
            for (int j = 0; j < hist.cols(); j++) {
                double[] val = hist.get(i, j);
                if (max < val[0]) {
                    max = val[0];
                    maxPixel = i;
                }
            }
        }
        HLog.e(TAG, "max count=" + max + ",pixel=" + maxPixel);
        HLog.e(TAG, "hist=" + hist.toString() + ",cols=" + hist.cols() + ",rows=" + hist.rows());
        return maxPixel;
    }

    private static float[] getAverageLevel(Mat img, int bgValue, int total) {
        float[] avg = new float[img.cols()];
        for (int i = 0; i < img.cols(); i++) {
            for (int j = 0; j < img.rows(); j++) {
                double[] val = img.get(j, i);
                avg[i] += (total - val[0]);
            }
            avg[i] /= img.rows();
            if (avg[i] < bgValue) {
                avg[i] = 0;
            } else {
                avg[i] -= bgValue;
            }
            HLog.e(TAG, "avg[" + i + "]=" + avg[i]);
        }
        return avg;
    }

    private static float[] calcGrayLevel(Mat img) {
        //计算灰度值
        float[] buffer = new float[256];
        Mat hist = calcHist(img, 256, buffer);

        int bgValue = (hist.rows() - 1) - findMaxPixel(hist);

        HLog.i(TAG, "img cols=" + img.cols() + ", rows=" + img.rows());

        float[] avg = getAverageLevel(img, bgValue, (hist.rows() - 1));

        int wSize = 15;
        Float[] weightValues = new Float[4];
        float[] weights = new float[4];
        for (int i = 0; i < 4; i++) {
            float maxVal = 0.0f;
            for (int j = 0; j < (img.cols() / 4); j++) {
                if (j + wSize >= img.cols() / 4)
                    break;
                float val = .0f;
                int x = i * img.cols() / 4 + j;
                for (int k = 0; k < wSize; k++) {
                    val += avg[x + k];
                }
                if (val > maxVal)
                    maxVal = val;
            }
            weightValues[i] = maxVal;
            //weights[i] = maxVal / weightValues[0];
        }

        if (weightValues[0] < weightValues[weightValues.length - 1]) {
            reverse(weightValues);
        }

        for (int i = 0; i < weightValues.length; i++) {
            weights[i] = weightValues[i] / weightValues[0];
        }

        for (int i = 0; i < weights.length; i++) {
            HLog.e(TAG, "weight=" + weights[i] + ",val=" + weightValues[i]);
        }
        return weights;
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
