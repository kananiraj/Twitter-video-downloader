package com.videodownloader.twittervideoindir;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;

import java.io.UnsupportedEncodingException;

public class Gallery extends AppCompatActivity {
    InitNeeds loaderService;

    @Override
    public void onResume() {
        super.onResume();
        ApplicationClass.setCurrentActivity(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_gallery);
        this.loaderService = ((ApplicationClass) getApplicationContext()).loaderService;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.mygra2));
            ((AppBarLayout) findViewById(R.id.appbar)).setOutlineProvider(null);
        }
        boolean z = false;
        if (getIntent().getIntExtra("premium", 0) == 1) {
            z = true;
        }
        if (!z) {
            ((AdView) findViewById(R.id.GalleryActivityBottomBanner)).loadAd(this.loaderService.getBannerAd(getString(R.string.GalleryActivityBottomBanner)));
        }
        refrestList();
    }

    public void refrestList() {
        Database database = new Database(this);
        if (database.getVideoCount() < 1) {
            ((LinearLayout) findViewById(R.id.indirilenler)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.empty)).setVisibility(View.VISIBLE);
            return;
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcy);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        DownloadedFilesAdapter downloadedFilesAdapter = null;
        try {
            downloadedFilesAdapter = new DownloadedFilesAdapter(this, database.getAllVideos());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(downloadedFilesAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void backClick(View view) {
        finish();
    }
}
