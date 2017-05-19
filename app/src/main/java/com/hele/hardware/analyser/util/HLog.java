package com.hele.hardware.analyser.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/6.
 */

public class HLog {
    private static final String TAG = "HELE.";

    public static void i(String tag, String msg) {
        Log.i(TAG + tag, buildMessage(msg));
    }

    public static void i(String tag, String format, Object... args) {
        i(tag, String.format(format, args));
    }

    public static void d(String tag, String msg) {
        Log.d(TAG + tag, buildMessage(msg));
    }

    public static void d(String tag, String format, Object... args) {
        d(tag, String.format(format, args));
    }

    public static void v(String tag, String msg) {
        Log.v(TAG + tag, buildMessage(msg));
    }

    public static void v(String tag, String format, Object... args) {
        v(tag, String.format(format, args));
    }

    public static void w(String tag, String msg) {
        Log.w(TAG + tag, buildMessage(msg));
    }

    public static void w(String tag, String format, Object... args) {
        w(tag, String.format(format, args));
    }

    public static void e(String tag, String msg) {
        Log.e(TAG + tag, buildMessage(msg));
    }

    public static void e(String tag, String format, Object... args) {
        e(tag, String.format(format, args));
    }

    public static void e(String tag, String msg, Throwable t) {
        Log.e(TAG + tag, buildMessage(msg), t);
    }

    private static String buildMessage(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] stackElements = t.getStackTrace();
        if (stackElements != null) {
            StringBuilder sb = new StringBuilder();
            String className = stackElements[2].getClassName();
            String methodName = stackElements[2].getMethodName();
            return sb.append("{")
                    .append(className)
                    .append("}")
                    .append(".")
                    .append(methodName)
                    .append("() ")
                    .append(msg).toString();
        } else {
            return msg;
        }
    }
}
