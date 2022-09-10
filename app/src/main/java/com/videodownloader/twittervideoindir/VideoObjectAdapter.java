package com.videodownloader.twittervideoindir;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.network.FirebasePerfUrlConnection;
import com.onesignal.OneSignal;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VideoObjectAdapter extends RecyclerView.Adapter<VideoObjectAdapter.ViewHolder> {
    private final Activity ac;
    private final ArrayList<VideoObject> accountModelArrayList;
    private final Context context;
    InitNeeds loaderService;
    private InterstitialAd OnClickButtonDown;
    private boolean premium;

    public VideoObjectAdapter(ArrayList<VideoObject> arrayList, Context context2, Window window, Activity activity, boolean z) {
        this.accountModelArrayList = arrayList;
        this.context = context2;
        this.ac = activity;
        this.premium = z;
        InitNeeds initNeeds = ((ApplicationClass) context2.getApplicationContext()).loaderService;
        this.loaderService = initNeeds;
        if (!z) {
            this.OnClickButtonDown = initNeeds.getInterAd(context2.getString(R.string.OnClickInsideDown));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.download_dialog_content, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.video_qualtiy.setText(this.accountModelArrayList.get(i).getQuality());
        viewHolder.video_size.setText(this.accountModelArrayList.get(i).getVideoSize());
        viewHolder.circleImageView.setImageBitmap(this.accountModelArrayList.get(i).getThumb());
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                new DownloadFile().execute(((VideoObject) VideoObjectAdapter.this.accountModelArrayList.get(i)).getVideoUrl(), ((VideoObject) VideoObjectAdapter.this.accountModelArrayList.get(i)).getType());
            }
        });
    }

    public File getPublicAlbumStorageDir(String str) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), str);
        if (!file.mkdirs()) {
            Log.e("Error", "Directory not created");
        }
        return file;
    }

    @Override
    public int getItemCount() {
        return this.accountModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView circleImageView;
        private final LinearLayout linearLayout;
        private final TextView video_qualtiy;
        private final TextView video_size;

        ViewHolder(View view) {
            super(view);
            this.circleImageView = (CircleImageView) view.findViewById(R.id.account_image);
            this.video_qualtiy = (TextView) view.findViewById(R.id.video_quality);
            this.video_size = (TextView) view.findViewById(R.id.video_size);
            this.linearLayout = (LinearLayout) view.findViewById(R.id.AccountOne);
        }
    }

    private class DownloadFile extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        private DownloadFile() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            ProgressDialog progressDialog2 = new ProgressDialog(VideoObjectAdapter.this.context);
            this.progressDialog = progressDialog2;
            progressDialog2.setProgressStyle(1);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        public String doInBackground(String... strArr) {
            byte[] bArr;
            try {
                URL url = new URL(strArr[0]);
                URLConnection uRLConnection = (URLConnection) FirebasePerfUrlConnection.instrument(url.openConnection());
                uRLConnection.connect();
                int contentLength = uRLConnection.getContentLength();
                if (contentLength < 0) {
                    contentLength = 19215984;
                }
                BufferedInputStream bufferedInputStream = new BufferedInputStream(FirebasePerfUrlConnection.openStream(url), 8192);
                int i = 1;
                String str = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US).format(new Date()) + "_" + strArr[0].substring(strArr[0].lastIndexOf(47) + 1, strArr[0].length());
                String str2 = VideoObjectAdapter.this.getPublicAlbumStorageDir("TwitterVideos") + File.separator;
                File file = new File(str2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(str2 + str);
                byte[] bArr2 = new byte[1024];
                long j = 0;
                while (true) {
                    int read = bufferedInputStream.read(bArr2);
                    if (read == -1) {
                        break;
                    }
                    j += (long) read;
                    String[] strArr2 = new String[i];
                    strArr2[0] = "" + ((int) ((100 * j) / ((long) contentLength)));
                    publishProgress(strArr2);
                    fileOutputStream.write(bArr2, 0, read);
                    bArr2 = bArr2;
                    i = 1;
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                bufferedInputStream.close();
                Database database = new Database(VideoObjectAdapter.this.context);
                if (strArr[1].equals("video") || strArr[1].equals("gif")) {
                    Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str2 + str, 3);
                    if (createVideoThumbnail == null) {
                        createVideoThumbnail = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(VideoObjectAdapter.this.context.getResources(), R.drawable.astronaut), 64, 64, false);
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    createVideoThumbnail.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream);
                    bArr = byteArrayOutputStream.toByteArray();
                } else {
                    Bitmap createScaledBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(str2 + str), 64, 64, false);
                    ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 30, byteArrayOutputStream2);
                    bArr = byteArrayOutputStream2.toByteArray();
                }
                database.videoAdd(str2 + str, bArr, strArr[1]);
                File file2 = new File(str2 + str);
                VideoObjectAdapter.this.context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", FileProvider.getUriForFile(VideoObjectAdapter.this.context, VideoObjectAdapter.this.context.getApplicationContext().getPackageName() + ".fileprovider", file2)));
                try {
                    MediaScannerConnection.scanFile(VideoObjectAdapter.this.context, new String[]{file2.toString()}, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (strArr[1].equals("image")) {
                    addImage(new File(str2 + str), str);
                } else if (strArr[1].equals("video") || strArr[1].equals("gif")) {
                    addVideo(new File(str2 + str), str);
                }
                if (database.getVideoCount() == 1) {
                    OneSignal.sendTag("user_type", "first_download");
                } else if (database.getVideoCount() == 2) {
                    OneSignal.sendTag("user_type", "multi_downloader");
                }
                return "Downloaded at: " + str2 + str;
            } catch (Exception e2) {
                e2.printStackTrace();
                return "Something went wrong";
            }
        }

        public void onProgressUpdate(String... strArr) {
            this.progressDialog.setProgress(Integer.parseInt(strArr[0]));
        }

        public void onCancelled() {
            ProgressDialog progressDialog2 = this.progressDialog;
            if (progressDialog2 != null && progressDialog2.isShowing()) {
                this.progressDialog.dismiss();
            }
            super.onCancelled();
        }

        public void onPostExecute(String str) {
            ProgressDialog progressDialog2;
            try {
                if (!VideoObjectAdapter.this.ac.isFinishing() && (progressDialog2 = this.progressDialog) != null && progressDialog2.isShowing()) {
                    this.progressDialog.dismiss();
                }
            } catch (Exception unused) {
            }
            if (!VideoObjectAdapter.this.premium) {
                VideoObjectAdapter videoObjectAdapter = VideoObjectAdapter.this;
                videoObjectAdapter.OnClickButtonDown = videoObjectAdapter.loaderService.getInterAd(VideoObjectAdapter.this.context.getString(R.string.OnClickInsideDown));
                if (VideoObjectAdapter.this.OnClickButtonDown != null) {
                    VideoObjectAdapter.this.OnClickButtonDown.show((MainActivity) VideoObjectAdapter.this.context);
                }
            }
            new ShowDialog().show(VideoObjectAdapter.this.context, VideoObjectAdapter.this.context.getString(R.string.success), VideoObjectAdapter.this.context.getString(R.string.videodownloaded), FirebaseAnalytics.Param.SUCCESS);
            ((MainActivity) VideoObjectAdapter.this.context).refrestList();
        }

        public Uri addVideo(File file, String str) {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put("title", str);
            contentValues.put("mime_type", "video/*");
            contentValues.put("_data", file.getAbsolutePath());
            return VideoObjectAdapter.this.context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        }

        public Uri addImage(File file, String str) {
            ContentValues contentValues = new ContentValues(3);
            contentValues.put("title", str);
            contentValues.put("mime_type", "image/*");
            contentValues.put("_data", file.getAbsolutePath());
            return VideoObjectAdapter.this.context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
    }
}
