package com.hele.hardware.analyser.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Utils {

    @NonNull
    public static <T> T getSystemService(Context context, @NonNull String serviceName) {
        return (T) context.getSystemService(serviceName);
    }

    public static boolean checkSelfPermission(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    public static boolean checkPermissionGrantResults(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
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
}
