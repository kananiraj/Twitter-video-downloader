package com.videodownloader.twittervideoindir;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
    private static final String ONESIGNAL_APP_ID = "bf5e707f-ed72-4878-97a2-81721282385b";
    private static Activity activity;
    private static Context appContext;
    private static Context context;
    public InitNeeds loaderService;

    public static synchronized Context getAppContext() {
        Context context2;
        synchronized (ApplicationClass.class) {
            context2 = context;
        }
        return context2;
    }

    public static void setCurrentActivity(Context context2) {
        appContext = context2;
    }

    public static Activity currentActivity() {
        return activity;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        this.loaderService = new InitNeeds(context);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
