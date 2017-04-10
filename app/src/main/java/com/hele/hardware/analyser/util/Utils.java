package com.hele.hardware.analyser.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/6.
 */

public class Utils {

    @NonNull
    public static <T> T getSystemService(Context context, @NonNull String serviceName) {
        return (T) context.getSystemService(serviceName);
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_DENIED;
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
}
