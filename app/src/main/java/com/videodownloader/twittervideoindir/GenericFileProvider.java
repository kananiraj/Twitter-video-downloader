package com.videodownloader.twittervideoindir;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class GenericFileProvider extends FileProvider {
    private static final String HUAWEI_MANUFACTURER = "Huawei";


    public static Uri getUriForFile(Context context, String str, File file) {
        if (HUAWEI_MANUFACTURER.equalsIgnoreCase(Build.MANUFACTURER) && HUAWEI_MANUFACTURER.equalsIgnoreCase(Build.MANUFACTURER) && Build.VERSION.SDK_INT <= 28) {
            try {
                return FileProvider.getUriForFile(context, str, file);
            } catch (IllegalArgumentException unused) {
                return Uri.fromFile(file);
            }
        }
        try {
            try {
                return FileProvider.getUriForFile(context, str, file);
            } catch (IllegalArgumentException unused2) {
                return Uri.fromFile(file);
            }
        } catch (IllegalArgumentException unused3) {
            return FileProvider.getUriForFile(context, str, file);
        }
    }

}
