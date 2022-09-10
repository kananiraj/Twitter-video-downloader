package com.videodownloader.twittervideo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FirstOpen extends AppCompatActivity {
    InitNeeds loaderService;

    @Override
    public void onResume() {
        super.onResume();
        ApplicationClass.setCurrentActivity(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_first_open);
        new Permissions().checkForPermisions(this);
        if (getSharedPreferences("first", 0).getString("first", "none").equals("done")) {
            startActivity(new Intent(this, MainActivity.class));
        }
        this.loaderService = ((ApplicationClass) getApplicationContext()).loaderService;
        ArrayList arrayList = new ArrayList();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        String[] strArr = {getString(R.string.tutorial0sentences), getString(R.string.tutorial1sentences), getString(R.string.tutorial2sentences), getString(R.string.tutorial3sentences)};
        String[] strArr2 = {getString(R.string.tutorial0head), getString(R.string.tutorial1head), getString(R.string.tutorial2head), getString(R.string.tutorial3head)};
        for (int i = 0; i < 4; i++) {
            ViewPagerModel viewPagerModel = new ViewPagerModel();
            viewPagerModel.setQuotes(strArr[i]);
            viewPagerModel.setQuote_types(strArr2[i]);
            arrayList.add(viewPagerModel);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(arrayList, this);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(viewPagerAdapter);
        ((TabLayout) findViewById(R.id.tab_main2)).setupWithViewPager(viewPager, true);
        ((LinearLayout) findViewById(R.id.skiptext)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences.Editor edit = FirstOpen.this.getSharedPreferences("first", 0).edit();
                edit.putString("first", "done");
                edit.apply();
                FirstOpen.this.startActivity(new Intent(FirstOpen.this, MainActivity.class));
            }
        });
    }

    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_ALPHA = 0.5f;
        private static final float MIN_SCALE = 0.85f;

        ZoomOutPageTransformer() {
        }

        @Override
        public void transformPage(View view, float f) {
            int width = view.getWidth();
            int height = view.getHeight();
            if (f < -1.0f) {
                view.setAlpha(0.0f);
            } else if (f <= 1.0f) {
                float max = Math.max((float) MIN_SCALE, 1.0f - Math.abs(f));
                float f2 = 1.0f - max;
                float f3 = (((float) height) * f2) / 2.0f;
                float f4 = (((float) width) * f2) / 2.0f;
                if (f < 0.0f) {
                    view.setTranslationX(f4 - (f3 / 2.0f));
                } else {
                    view.setTranslationX((-f4) + (f3 / 2.0f));
                }
                view.setScaleX(max);
                view.setScaleY(max);
                view.setAlpha((((max - MIN_SCALE) / 0.14999998f) * 0.5f) + 0.5f);
            } else {
                view.setAlpha(0.0f);
            }
        }
    }

    private class ViewPagerModel {
        private String quote_types;
        private String quotes;

        ViewPagerModel() {
        }

        public String getQuotes() {
            return this.quotes;
        }

        public void setQuotes(String str) {
            this.quotes = str;
        }

        public String getQuote_types() {
            return this.quote_types;
        }

        public void setQuote_types(String str) {
            this.quote_types = str;
        }
    }

    private class ViewPagerAdapter extends PagerAdapter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final Context context;
        private final List<ViewPagerModel> modelList;

        ViewPagerAdapter(List<ViewPagerModel> list, Context context2) {
            this.modelList = list;
            this.context = context2;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public int getCount() {
            return this.modelList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            View inflate = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.viewpager_contents1, viewGroup, false);
            viewGroup.addView(inflate);
            ((TextView) inflate.findViewById(R.id.quotes)).setText(this.modelList.get(i).getQuotes());
            ((TextView) inflate.findViewById(R.id.quote_type)).setText(this.modelList.get(i).getQuote_types());
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }
}
