package com.hele.hardware.analyser.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Utils {

    private static final char[] CHARACTER = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'x'};
    private static final int[] NUMBER = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    };

    @NonNull
    public static <T> T getSystemService(Context context, @NonNull String serviceName) {
        return (T) context.getSystemService(serviceName);
    }

    public static void showToastOnUiThread(final Activity activity, final String text) {
        if (activity != null)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
    }

    public static String getMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = getSystemService(context, Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        //Full Screen without NavigationBar
        //wm.getDefaultDisplay().getRealMetrics(dm);
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    public static String getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        String applicationName = null;
        String versionName = null;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            applicationName = pm.getApplicationLabel(pi.applicationInfo).toString();
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName + '\t' + versionName;
    }

    public static boolean isApkDebuggable(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        return (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private static char getRandomChar() {
        String str = "";
        int highPos;
        int lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);
    }

    public static String getRandomChinese() {
        Random random = new Random();
        int length = random.nextInt(3) + 3;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(getRandomChar());
        }
        return sb.toString();
    }

    public static String getRandomEnglish() {
        Random random = new Random();
        int length = random.nextInt(6) + 4;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTER.length);
            sb.append(CHARACTER[index]);
        }
        return sb.toString();
    }

    public static long getRandomNumber() {
        Random random = new Random();
        int length = random.nextInt(6) + 4;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(NUMBER.length);
            sb.append(NUMBER[index]);
        }
        return Long.valueOf(sb.toString());
    }
}
