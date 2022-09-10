package com.videodownloader.twittervideoindir;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.Constants;

public class ShowDialog {
    ShowDialog() {
    }

    public void show(final Context context, final String str, final String str2, final String str3) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            public void run() {
                String str = str3;
                str.hashCode();
                char c = 65535;
                switch (str.hashCode()) {
                    case -1867169789:
                        if (str.equals(FirebaseAnalytics.Param.SUCCESS)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 96784904:
                        if (str.equals(Constants.IPC_BUNDLE_KEY_SEND_ERROR)) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1124446108:
                        if (str.equals("warning")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                int i = R.drawable.ic_close_white_24dp;
                int i2 = R.color.dialogErrorBackgroundColor;
                switch (c) {
                    case 0:
                        i2 = R.color.dialogSuccessBackgroundColor;
                        i = R.drawable.ic_check_white_24dp;
                        break;
                    case 2:
                        i2 = R.color.dialogNoticeBackgroundColor;
                        i = R.drawable.ic_info_black_24dp;
                        break;
                }
                if (Build.VERSION.SDK_INT >= 20) {
                    ((AwesomeErrorDialog) ((AwesomeErrorDialog) ((AwesomeErrorDialog) ((AwesomeErrorDialog) ((AwesomeErrorDialog) new AwesomeErrorDialog(context).setTitle(str)).setMessage(str2)).setColoredCircle(i2)).setDialogIconAndColor(i, R.color.white)).setCancelable(true)).setButtonText(context.getString(R.string.okay)).setButtonBackgroundColor(i2).setButtonText(context.getString(R.string.okay)).setErrorButtonClick(new Closure() {

                        @Override
                        public void exec() {
                        }
                    }).show();
                } else {
                    Toast.makeText(context, str2, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
