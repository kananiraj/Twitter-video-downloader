package com.videodownloader.twittervideoindir;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

class Permissions {
    private final String[] permissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.INTERNET"};

    Permissions() {
    }

    public void checkForPermisions(Context context) {
        boolean z = false;
        for (String str : this.permissions) {
            if (ContextCompat.checkSelfPermission(context, str) != 0) {
                z = true;
            }
        }
        if (z) {
            getPermisionRequest(context);
        }
    }

    private void getPermisionRequest(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions((Activity) context, this.permissions, 1);
        }
    }
}
