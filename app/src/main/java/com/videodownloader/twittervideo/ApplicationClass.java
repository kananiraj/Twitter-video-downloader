package com.videodownloader.twittervideo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;
import com.videodownloader.twittervideo.R;

import org.json.JSONException;
import org.json.JSONObject;

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



        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(200).build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.firebase_rc_baseurl);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseRemoteConfig.fetchAndActivate();
                String ads = firebaseRemoteConfig.getString(getString(R.string.adjson));
                try {
                    JSONObject jsonObject = new JSONObject(ads);
                    String adid1=jsonObject.getString("adid1");
                    String adid2=jsonObject.getString("adid2");
                    String adid3=jsonObject.getString("adid3");
                    String adid4=jsonObject.getString("adid4");
                    String adid5=jsonObject.getString("adid5");
                    String isprokey =jsonObject.getString("isprokey");

                    SharedPreferenceManager instance = SharedPreferenceManager.getInstance(this);
                    instance.setValueInPref(SharedPreferenceManager.adid1,adid1);
                    instance.setValueInPref(SharedPreferenceManager.adid2,adid2);
                    instance.setValueInPref(SharedPreferenceManager.adid3,adid3);
                    instance.setValueInPref(SharedPreferenceManager.adid4,adid4);
                    instance.setValueInPref(SharedPreferenceManager.adid5,adid5);
                    instance.setValueInPref(SharedPreferenceManager.isprokey,isprokey);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            });

    }
}
