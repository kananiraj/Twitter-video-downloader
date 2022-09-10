package com.videodownloader.twittervideoindir;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.Constants;
import com.google.firebase.perf.network.FirebasePerfUrlConnection;
import com.google.firebase.remoteconfig.RemoteConfigConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final ArrayList<String> ownedProducts = new ArrayList<>();
    BillingProcessor bp;
    private InterstitialAd GalleryActivityInter;
    private InterstitialAd OnClickButtonDown;
    private ConsentForm consentForm;
    private ConsentInformation consentInformation;
    private Dialog downloadwait;
    private InitNeeds loaderService;
    private AdView mAdView;
    private AdView mainActivityDialogBanner;
    private boolean premium = false;
    private EditText search_main;

    public static Bitmap getBitmapFromURL(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) ((URLConnection) FirebasePerfUrlConnection.instrument(new URL(str).openConnection()));
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.outHeight = 60;
            options.outWidth = 60;
            try {
                return BitmapFactory.decodeStream(inputStream);
            } catch (RuntimeException unused) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void watchYoutubeVideo(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + str));
        Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youtube.com/watch?v=" + str));
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            context.startActivity(intent2);
        }
    }

    @Override
    public void onBillingError(int i, Throwable th) {
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        ApplicationClass.setCurrentActivity(this);
        loadGDPR();
        this.loaderService = ((ApplicationClass) getApplicationContext()).loaderService;
        this.mAdView = (AdView) findViewById(R.id.AdView);
        loadMainAds();
        if (Build.VERSION.SDK_INT >= 21) {
            ((Button) findViewById(R.id.button1)).setCompoundDrawables(getResources().getDrawable(R.drawable.paste), null, null, null);
            ((Button) findViewById(R.id.button1)).setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.paste, 0, 0, 0);
            ((Button) findViewById(R.id.button2)).setCompoundDrawables(getResources().getDrawable(R.drawable.download), null, null, null);
            ((Button) findViewById(R.id.button2)).setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.download, 0, 0, 0);
            ((Button) findViewById(R.id.button4)).setCompoundDrawables(getResources().getDrawable(R.drawable.ic_menu_gallery2), null, null, null);
            ((Button) findViewById(R.id.button4)).setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_menu_gallery2, 0, 0, 0);
        }
        BillingProcessor billingProcessor = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt2n0YtZycTJQdooTstnS3W1lz0p/MYC3N+87j+mJHaNAUY0C2biR79e+WE+ySMIbLp9B1gfpSHudRWRZo+/jsbG5ahGudcZNwgIsoXdYvc5s9R6YilnV9Z3m5uIMJcjmJLhv3xIKOur6U4RgbLWxMkt1mqfzviFH2hpVpnYSba6crv9oYj15beJr8ga9Y0uJ2BZWXF3/N9zpFdpeU1RWMGjWuNrU7Yiy3gecyqqRNEF4rqigJJvSUo/UkMBbGr9umtsOJt3TqGLTzapiA4LxPPxRPHvLSOuU9tAa4wK3ePsyUp7GdpU3TqctPtpVBhMYSWIXWQHEruQAYkufIIkKFwIDAQAB", this);
        this.bp = billingProcessor;
        billingProcessor.initialize();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.mygra2));
            ((AppBarLayout) findViewById(R.id.appbar)).setOutlineProvider(null);
        }
        this.search_main = (EditText) findViewById(R.id.search_main);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.mail_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mail_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ((NavigationView) findViewById(R.id.navigator)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.downloadsOption:
                        MainActivity.this.openGalleryProperWay();
                        break;
                    case R.id.exitOption:
                        MainActivity.this.moveTaskToBack(true);
                        Process.killProcess(Process.myPid());
                        System.exit(1);
                        break;
//                    case R.id.guideOption:
//                        MainActivity.watchYoutubeVideo(MainActivity.this, "JXcuEDxnRXI");
//                        break;
                    case R.id.privacyOption:
                        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://bitbytestech.blogspot.com/2022/09/privacy-policy-of-app-name-app-name.html")));
                        break;
                    case R.id.rateOption:
                        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.videodownloader.twittervideoindir")));
                        break;
                    case R.id.shareOption:
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", MainActivity.this.getString(R.string.app_name));
                        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=com.videodownloader.twittervideoindir");
                        MainActivity mainActivity = MainActivity.this;
                        mainActivity.startActivity(Intent.createChooser(intent, mainActivity.getString(R.string.app_name)));
                        break;
                }
                return true;
            }
        });
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (action != null && action.equals("android.intent.action.SEND") && type.startsWith("text/")) {
            String stringExtra = intent.getStringExtra("android.intent.extra.TEXT");
            if (stringExtra != null) {
                ((EditText) findViewById(R.id.search_main)).setText(stringExtra);
            } else {
                new ShowDialog().show(this, getString(R.string.warning), getString(R.string.notextfound), "warning");
            }
        }
        refrestList();
        int videoCount = new Database(this).getVideoCount();
        SharedPreferences sharedPreferences = getSharedPreferences("user_status", 0);
        if (videoCount >= 1 && sharedPreferences.getString("rating_status", "none").equals("none")) {
            try {
                new GreetingsFragment().show(getSupportFragmentManager(), "Greetings");
            } catch (Exception unused) {
            }
        }
    }

    public void loadGDPR() {
        ConsentRequestParameters build = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build();
        this.consentInformation = UserMessagingPlatform.getConsentInformation(this);
        Log.d("GDPR", "loadGDPR: " + this.consentInformation.getConsentStatus());
        this.consentInformation.requestConsentInfoUpdate(this, build, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {

            @Override
            public void onConsentInfoUpdateSuccess() {
                if (MainActivity.this.consentInformation.isConsentFormAvailable()) {
                    MainActivity.this.loadForm();
                }
            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() {

            @Override
            public void onConsentInfoUpdateFailure(FormError formError) {
            }
        });
    }

    public void loadForm() {
        UserMessagingPlatform.loadConsentForm(this, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {

            @Override
            public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                MainActivity.this.consentForm = consentForm;
                if (MainActivity.this.consentInformation.getConsentStatus() == 2) {
                    consentForm.show(MainActivity.this, new ConsentForm.OnConsentFormDismissedListener() {

                        @Override
                        public void onConsentFormDismissed(FormError formError) {
                            MainActivity.this.loadForm();
                        }
                    });
                }
            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {

            @Override
            public void onConsentFormLoadFailure(FormError formError) {
            }
        });
    }

    private void openGalleryProperWay() {
        final Intent intent = new Intent(getBaseContext(), Gallery.class);
        boolean z = this.premium;
        if (!z) {
            InterstitialAd interAd = this.loaderService.getInterAd(getString(R.string.GalleryActivityInter));
            this.GalleryActivityInter = interAd;
            if (interAd != null) {
                interAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                    @Override
                    public void onAdClicked() {
                    }

                    @Override
                    public void onAdImpression() {
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        MainActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        MainActivity.this.startActivity(intent);
                    }
                });
                this.GalleryActivityInter.show(this);
                return;
            }
            startActivity(intent);
            return;
        }
        if (z) {
            intent.putExtra("premium", 1);
        } else {
            intent.putExtra("premium", 0);
        }
        startActivity(intent);
    }

    private void loadMainAds() {
        if (!this.premium) {
            this.mAdView.loadAd(this.loaderService.getBannerAd(getString(R.string.MainBottomAds)));
        } else {
            this.mAdView.setVisibility(View.GONE);
        }
        this.OnClickButtonDown = this.loaderService.getInterAd(getString(R.string.OnClickButtonDown));
        this.GalleryActivityInter = this.loaderService.getInterAd(getString(R.string.GalleryActivityInter));
    }

    @Override
    public void onResume() {
        super.onResume();
        ApplicationClass.setCurrentActivity(this);
        refrestList();
        new Permissions().checkForPermisions(this);
    }

    public void refrestList() {
        Database database = new Database(this);
        if (database.getVideoCount() < 1) {
            ((LinearLayout) findViewById(R.id.indirilenler)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.empty)).setVisibility(View.VISIBLE);
            return;
        }
        ((LinearLayout) findViewById(R.id.indirilenler)).setVisibility(View.VISIBLE);
        ((LinearLayout) findViewById(R.id.empty)).setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        DownloadedFilesAdapter downloadedFilesAdapter = null;
        try {
            downloadedFilesAdapter = new DownloadedFilesAdapter(this, database.get3Videos());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(downloadedFilesAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void pasteClick(View view) {
        ClipData clipData;
        int i;
        String str;
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            clipData = clipboardManager.getPrimaryClip();
            i = clipData.getItemCount();
        } else {
            clipData = null;
            i = 0;
        }
        if (i > 0) {
            try {
                str = clipData.getItemAt(0).getText().toString();
            } catch (Exception e) {
                new ShowDialog().show(this, getString(R.string.emptyclipboard), getString(R.string.notalink), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                e.printStackTrace();
                str = "";
            }
            if (str.toLowerCase().contains("http")) {
                this.search_main.setText(str.substring(str.indexOf("http")));
                new ShowDialog().show(this, getString(R.string.success), getString(R.string.pasted), FirebaseAnalytics.Param.SUCCESS);
                return;
            }
            new ShowDialog().show(this, getString(R.string.error), getString(R.string.cantfindtweet), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
            return;
        }
        new ShowDialog().show(this, getString(R.string.error), getString(R.string.nopastedtext), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
    }

    public void downloadClick(View view) {
        final String str;
        InterstitialAd interAd = this.loaderService.getInterAd(getString(R.string.OnClickButtonDown));
        this.OnClickButtonDown = interAd;
        if (!this.premium && interAd != null) {
            interAd.show(this);
        }
        this.downloadwait = ((AwesomeInfoDialog) ((AwesomeInfoDialog) ((AwesomeInfoDialog) ((AwesomeInfoDialog) ((AwesomeInfoDialog) new AwesomeInfoDialog(this).setTitle(R.string.loading)).setMessage(R.string.waittofind)).setColoredCircle(R.color.dialogInfoBackgroundColor)).setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)).setCancelable(false)).show();
        new ArrayList();
        String[] split = this.search_main.getText().toString().split("/");
        if (split[split.length - 1].contains("?")) {
            str = split[split.length - 1].split("\\?")[0];
        } else {
            str = split[split.length - 1];
        }
        RequestQueue newRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(1, "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php", new Response.Listener<String>() {

            public void onResponse(String str) {
                new processTheResponse().execute(str);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                HashMap hashMap = new HashMap();
                hashMap.put("id", str);
                return hashMap;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 1, 1.0f));
        newRequestQueue.add(stringRequest);
    }

    public void openGallery(View view) {
        openGalleryProperWay();
    }

    @Override
    public void onProductPurchased(String str, PurchaseInfo purchaseInfo) {
        onBillingInitialized();
    }

    @Override
    public void onBillingInitialized() {
        this.ownedProducts.clear();
        this.bp.loadOwnedPurchasesFromGoogleAsync(new BillingProcessor.IPurchasesResponseListener() {

            @Override
            public void onPurchasesError() {
            }

            @Override
            public void onPurchasesSuccess() {
                MainActivity.this.ownedProducts.addAll(MainActivity.this.bp.listOwnedSubscriptions());
                if (MainActivity.this.ownedProducts.size() > 0) {
                    MainActivity.this.premium = true;
                    ((Button) MainActivity.this.findViewById(R.id.removeAds)).setVisibility(View.GONE);
                    MainActivity.this.mAdView.setVisibility(View.GONE);
                    return;
                }
                MainActivity.this.premium = false;
            }
        });
    }

    public void removeAds(View view) {
        this.bp.subscribe(this, "premium");
    }

    @Override
    public void onDestroy() {
        Dialog dialog = this.downloadwait;
        if (dialog != null && dialog.isShowing()) {
            this.downloadwait.dismiss();
        }
        super.onDestroy();
    }

    public class processTheResponse extends AsyncTask<String, Void, String> {
        private processTheResponse() {
        }

        public void onPostExecute(String str) {
        }

        public void onPreExecute() {
        }

        public void onProgressUpdate(Void... voidArr) {
        }

        public String doInBackground(String... strArr) {
            String str = strArr[0];
            final ArrayList arrayList = new ArrayList();
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.getString(RemoteConfigConstants.ResponseFieldKey.STATE).equals(Constants.IPC_BUNDLE_KEY_SEND_ERROR)) {
                    if (jSONObject.getInt("error_code") == 1) {
                        ShowDialog showDialog = new ShowDialog();
                        MainActivity mainActivity = MainActivity.this;
                        showDialog.show(mainActivity, mainActivity.getString(R.string.error), MainActivity.this.getString(R.string.onairtweetcantdownload), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                        if (MainActivity.this.isFinishing() || MainActivity.this.downloadwait == null || !MainActivity.this.downloadwait.isShowing()) {
                            return "Executed";
                        }
                        MainActivity.this.downloadwait.dismiss();
                        return "Executed";
                    } else if (jSONObject.getInt("error_code") == 2) {
                        if (!MainActivity.this.isFinishing() && MainActivity.this.downloadwait != null && MainActivity.this.downloadwait.isShowing()) {
                            MainActivity.this.downloadwait.dismiss();
                        }
                        ShowDialog showDialog2 = new ShowDialog();
                        MainActivity mainActivity2 = MainActivity.this;
                        showDialog2.show(mainActivity2, mainActivity2.getString(R.string.error), MainActivity.this.getString(R.string.notweetfounded), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                        return "Executed";
                    } else if (jSONObject.getInt("error_code") != 3) {
                        return "Executed";
                    } else {
                        if (!MainActivity.this.isFinishing() && MainActivity.this.downloadwait != null && MainActivity.this.downloadwait.isShowing()) {
                            MainActivity.this.downloadwait.dismiss();
                        }
                        ShowDialog showDialog3 = new ShowDialog();
                        MainActivity mainActivity3 = MainActivity.this;
                        showDialog3.show(mainActivity3, mainActivity3.getString(R.string.error), MainActivity.this.getString(R.string.nomediaontweet), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                        return "Executed";
                    }
                } else if (!jSONObject.getString(RemoteConfigConstants.ResponseFieldKey.STATE).equals(FirebaseAnalytics.Param.SUCCESS)) {
                    return "Executed";
                } else {
                    JSONArray jSONArray = jSONObject.getJSONArray("videos");
                    for (int i = 0; i < jSONArray.length(); i++) {
                        Bitmap bitmapFromURL = MainActivity.getBitmapFromURL(jSONArray.getJSONObject(i).getString("thumb").replace("http", "https"));
                        String d = Double.toString(jSONArray.getJSONObject(i).getDouble("size") / 1048576.0d);
                        String substring = d.substring(0, Math.min(d.length(), 4));
                        String string = MainActivity.this.getString(R.string.image);
                        String replace = jSONArray.getJSONObject(i).getString(ImagesContract.URL).replace("http", "https");
                        if (!jSONArray.getJSONObject(i).getString(com.anjlab.android.iab.v3.Constants.RESPONSE_TYPE).equals("video")) {
                            if (jSONArray.getJSONObject(i).getString(com.anjlab.android.iab.v3.Constants.RESPONSE_TYPE).equals("gif")) {
                            }
                            arrayList.add(new VideoObject(bitmapFromURL, replace, substring + " mb", jSONArray.getJSONObject(i).getString(com.anjlab.android.iab.v3.Constants.RESPONSE_TYPE), string));
                        }
                        String[] split = jSONArray.getJSONObject(i).getString(ImagesContract.URL).split("/");
                        string = split[split.length - 2];
                        replace = jSONArray.getJSONObject(i).getString(ImagesContract.URL);
                        arrayList.add(new VideoObject(bitmapFromURL, replace, substring + " mb", jSONArray.getJSONObject(i).getString(com.anjlab.android.iab.v3.Constants.RESPONSE_TYPE), string));
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {

                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyCustomAlert1);
                            View inflate = MainActivity.this.getLayoutInflater().inflate(R.layout.download_dialog, (ViewGroup) null);
                            LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.addAccount);
                            RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv10);
                            if (!MainActivity.this.premium) {
                                MainActivity.this.mainActivityDialogBanner = (AdView) inflate.findViewById(R.id.mainActivityDialogBanner);
                                MainActivity.this.mainActivityDialogBanner.loadAd(MainActivity.this.loaderService.getBannerAd(MainActivity.this.getString(R.string.mainActivityDialogBanner)));
                            }
                            VideoObjectAdapter videoObjectAdapter = new VideoObjectAdapter(arrayList, MainActivity.this, MainActivity.this.getWindow(), MainActivity.this, MainActivity.this.premium);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recyclerView.setAdapter(videoObjectAdapter);
                            builder.setView(inflate);
                            final AlertDialog create = builder.create();
                            linearLayout.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View view) {
                                    MainActivity.this.refrestList();
                                    if (create.isShowing()) {
                                        create.dismiss();
                                    }
                                }
                            });
                            if (!MainActivity.this.isFinishing()) {
                                try {
                                    create.show();
                                } catch (Exception unused) {
                                }
                            }
                            if (MainActivity.this.downloadwait != null && MainActivity.this.downloadwait.isShowing()) {
                                MainActivity.this.downloadwait.dismiss();
                            }
                        }
                    });
                    return "Executed";
                }
            } catch (JSONException unused) {
                if (MainActivity.this.downloadwait == null || !MainActivity.this.downloadwait.isShowing()) {
                    return "Executed";
                }
                MainActivity.this.downloadwait.dismiss();
                return "Executed";
            }
        }
    }
}
