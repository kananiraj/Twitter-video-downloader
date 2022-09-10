package com.videodownloader.twittervideo;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.Constants;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.onesignal.OneSignal;

public class GreetingsFragment extends DialogFragment {
    private int rateselected = 0;

    @Override
    public void onStart() {
        super.onStart();
        try {
            getDialog().getWindow().setLayout(-1, -2);
        } catch (Exception unused) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.rating_dialog, viewGroup, false);
        getDialog().getWindow().setLayout(-1, -1);
        SmileRating smileRating = (SmileRating) inflate.findViewById(R.id.smile_rating);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, "⭐");
        smileRating.setNameForSmile(BaseRating.BAD, "⭐⭐");
        smileRating.setNameForSmile(BaseRating.OKAY, "⭐⭐⭐");
        smileRating.setNameForSmile(BaseRating.GOOD, "⭐⭐⭐⭐");
        smileRating.setNameForSmile(BaseRating.GREAT, "⭐⭐⭐⭐⭐");
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {

            @Override
            public void onSmileySelected(int i, boolean z) {
                if (i == 0) {
                    GreetingsFragment.this.rateselected = 1;
                } else if (i == 1) {
                    GreetingsFragment.this.rateselected = 2;
                } else if (i == 2) {
                    GreetingsFragment.this.rateselected = 3;
                } else if (i == 3) {
                    GreetingsFragment.this.rateselected = 4;
                } else if (i == 4) {
                    GreetingsFragment.this.rateselected = 5;
                }
            }
        });
        ((Button) inflate.findViewById(R.id.ratenow)).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                if (GreetingsFragment.this.rateselected == 5) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + GreetingsFragment.this.getActivity().getPackageName()));
                    intent.addFlags(1208483840);
                    try {
                        GreetingsFragment.this.startActivity(intent);
                    } catch (ActivityNotFoundException unused) {
                        GreetingsFragment greetingsFragment = GreetingsFragment.this;
                        greetingsFragment.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + GreetingsFragment.this.getActivity().getPackageName())));
                    }
                    SharedPreferences.Editor edit = GreetingsFragment.this.getActivity().getSharedPreferences("user_status", 0).edit();
                    edit.putString("rating_status", "dontask");
                    edit.apply();
                    OneSignal.sendTag("rating_status", "already_rated");
                    GreetingsFragment.this.dismiss();
                } else if (GreetingsFragment.this.rateselected == 0) {
                    new ShowDialog().show(GreetingsFragment.this.getContext(), GreetingsFragment.this.getString(R.string.warning), GreetingsFragment.this.getString(R.string.pleaseclickemojis), Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                } else {
                    new ShowDialog().show(GreetingsFragment.this.getContext(), GreetingsFragment.this.getString(R.string.success), GreetingsFragment.this.getString(R.string.thankyouwegot), FirebaseAnalytics.Param.SUCCESS);
                    SharedPreferences.Editor edit2 = GreetingsFragment.this.getActivity().getSharedPreferences("user_status", 0).edit();
                    edit2.putString("rating_status", "dontask");
                    edit2.apply();
                    OneSignal.sendTag("rating_status", "already_rated");
                    GreetingsFragment.this.dismiss();
                }
            }
        });
        ((Button) inflate.findViewById(R.id.remindlater)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                GreetingsFragment.this.dismiss();
            }
        });
        ((Button) inflate.findViewById(R.id.dontaskagain)).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SharedPreferences.Editor edit = GreetingsFragment.this.getActivity().getSharedPreferences("user_status", 0).edit();
                edit.putString("rating_status", "dontask");
                edit.apply();
                OneSignal.sendTag("rating_status", "dont_notificate");
                new ShowDialog().show(GreetingsFragment.this.getActivity(), GreetingsFragment.this.getActivity().getString(R.string.success), GreetingsFragment.this.getActivity().getString(R.string.wewillnotask), FirebaseAnalytics.Param.SUCCESS);
                GreetingsFragment.this.dismiss();
            }
        });
        return inflate;
    }
}
