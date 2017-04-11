package com.hele.hardware.analyser.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/11.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static CrashHandler sInstance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private SimpleDateFormat mFormatter;
    private StringBuilder mBuilder;
    private File mCrashFile;

    public static CrashHandler instance() {
        if (sInstance == null)
            synchronized (CrashHandler.class) {
                if (sInstance == null)
                    sInstance = new CrashHandler();
            }
        return sInstance;
    }

    private CrashHandler() {

    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mFormatter = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        mBuilder = new StringBuilder();
        mCrashFile = mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
    }


    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    private boolean handleException(Throwable e) {
        if (e == null) return false;
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Toast.makeText(mContext, "程序异常，即将退出", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }
        }.start();
        collectDeviceInfo();
        collectException(e);
        return true;
    }

    private void collectDeviceInfo() {
        mBuilder.delete(0, mBuilder.length());
        mBuilder.append(mFormatter.format(new Date()))
                .append('\n')
                .append("DEVICE :")
                .append('\t')
                .append(Build.BRAND)
                .append("-")
                .append(Build.MODEL)
                .append('\t')
                .append(Utils.getMetrics(mContext))
                .append('\n')
                .append(Utils.getPackageInfo(mContext))
                .append('\n');
    }

    private void collectException(Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        pw.close();
        mBuilder.append(writer.toString()).append('\n');
        HLog.e(TAG, mBuilder.toString());
        String fileName = "crashLog.log";
        FileUtils.write2file(new File(mCrashFile, fileName).getAbsolutePath(), mBuilder.toString());
    }

//    private String getAllBuild() {
//        StringBuilder sb = new StringBuilder();
//        try {
//            Field[] fields = Build.class.getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                sb.append("\n")
//                        .append(field.getName())
//                        .append(" :: ")
//                        .append(field.get(null));
//                field.setAccessible(false);
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }
}
