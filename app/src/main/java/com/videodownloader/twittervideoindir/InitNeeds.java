package com.videodownloader.twittervideoindir;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InitNeeds {
    private final String TAG = "Loader ";
    public Map<String, List<AdRequest>> bannerAds = new HashMap();
    public Map<String, ArrayList<InterstitialAd>> interAds = new HashMap();
    private Context context;

    InitNeeds(Context context2) {
        this.context = context2;
        init();
        adLoad();
    }

    public void init() {
        FirebaseAnalytics.getInstance(this.context);
        MobileAds.initialize(this.context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public void adLoad() {
        this.interAds.put(this.context.getString(R.string.OnClickButtonDown), new ArrayList<>());
        this.interAds.put(this.context.getString(R.string.OnClickInsideDown), new ArrayList<>());
        this.interAds.put(this.context.getString(R.string.GalleryActivityInter), new ArrayList<>());
        this.bannerAds.put(this.context.getString(R.string.MainBottomAds), new ArrayList());
        this.bannerAds.put(this.context.getString(R.string.mainActivityDialogBanner), new ArrayList());
        this.bannerAds.put(this.context.getString(R.string.GalleryActivityBottomBanner), new ArrayList());
        Iterator<Map.Entry<String, ArrayList<InterstitialAd>>> it = this.interAds.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<String, ArrayList<InterstitialAd>> next = it.next();
            String key = next.getKey();
            next.getValue();
            for (int i = 0; i < 2; i++) {
                loadInter(this.context, key);
            }
        }
        Log.d("Loader ", "adLoad: banner load 1");
        for (Map.Entry<String, List<AdRequest>> entry : this.bannerAds.entrySet()) {
            String key2 = entry.getKey();
            Log.d("Loader ", "adLoad: banner load 2 : " + key2);
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < 1; i2++) {
                Log.d("Loader ", "adLoad: banner request : " + i2);
                AdRequest build = new AdRequest.Builder().build();
                arrayList.add(build);
                Log.d("Loader ", "adLoad: banner request : " + build.toString());
            }
            this.bannerAds.put(key2, arrayList);
            Log.d("Loader ", "adLoad: banner ads count : " + this.bannerAds.get(key2).size() + "  values count = " + arrayList.size());
        }
    }

    private void loadInter(Context context2, String str) {
        AdRequest build = new AdRequest.Builder().build();
        final ArrayList<InterstitialAd> arrayList = this.interAds.get(str);
        InterstitialAd.load(context2, str, build, new InterstitialAdLoadCallback() {
            public void onAdLoaded(InterstitialAd interstitialAd) {
                arrayList.add(interstitialAd);
                Log.i("Loader ", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.d("Loader ", loadAdError.toString());
                arrayList.add(null);
            }
        });
        Log.d("Loader ", "loadInter: " + str);
        Log.d("Loader ", "loadInter: cc " + arrayList.size());
        this.interAds.put(str, arrayList);
    }

    public AdRequest getBannerAd(String str) {
        AdRequest adRequest = null;
        for (int i = 0; i < this.bannerAds.get(str).size(); i++) {
            if (this.bannerAds.get(str).get(i) != null) {
                adRequest = this.bannerAds.get(str).get(i);
            } else {

            }
        }
        return adRequest;
    }

    public InterstitialAd getInterAd(String str) {
        InterstitialAd interstitialAd = null;
        for (int i = 0; i < this.interAds.get(str).size(); i++) {
            if (this.interAds.get(str).get(i) != null) {
                interstitialAd = this.interAds.get(str).get(i);
                loadInter(this.context, str);
            }
        }
        return interstitialAd;
    }
}
