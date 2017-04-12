package com.hele.hardware.analyser.util;

import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/4/11.
 */

public class FileUtils {

    public static void write2file(String fileName, String text) {
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(text)) return;
        File file = new File(fileName);
        checkFileExist(file.getParentFile());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void checkFileExist(File file) {
        if (!file.exists())
            file.mkdirs();
    }
}
