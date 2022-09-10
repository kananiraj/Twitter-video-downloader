package com.videodownloader.twittervideoindir;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anjlab.android.iab.v3.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadedFilesAdapter extends RecyclerView.Adapter<DownloadedFilesAdapter.MyViewHolder> {
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> mProductList;
    Context mcontext;

    public DownloadedFilesAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.inflater = LayoutInflater.from(context);
        this.mProductList = arrayList;
        this.mcontext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this.inflater.inflate(R.layout.main_downloaded_file, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.setData(this.mProductList.get(i), i);
    }

    @Override
    public int getItemCount() {
        return this.mProductList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView itemDelete;
        TextView itemName;
        ImageView itemShare;
        ImageView play_ic;
        RelativeLayout playlayout;
        CircleImageView thumb;

        public MyViewHolder(View view) {
            super(view);
            this.thumb = (CircleImageView) view.findViewById(R.id.thumb);
            this.playlayout = (RelativeLayout) view.findViewById(R.id.playlayout);
            this.itemName = (TextView) view.findViewById(R.id.itemName);
            this.itemShare = (ImageView) view.findViewById(R.id.itemShare);
            this.itemDelete = (ImageView) view.findViewById(R.id.itemRemove);
            this.play_ic = (ImageView) view.findViewById(R.id.play_ic);
        }

        public void onClick(View view) {
        }

        public void setData(final HashMap<String, String> hashMap, final int i) {
            final Database database = new Database(DownloadedFilesAdapter.this.mcontext);
            Context context = DownloadedFilesAdapter.this.mcontext;
            final Uri uriForFile = GenericFileProvider.getUriForFile(context, DownloadedFilesAdapter.this.mcontext.getApplicationContext().getPackageName() + ".fileprovider", new File(hashMap.get("video_path")));
            if (hashMap.get("video_path") != null) {
                if (hashMap.get(Constants.RESPONSE_TYPE).equals("image")) {
                    this.play_ic.setVisibility(View.GONE);
                    this.thumb.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {
                            if (new File((String) hashMap.get("video_path")).exists()) {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(uriForFile, "image/*");
                                DownloadedFilesAdapter.this.mcontext.startActivity(intent);
                                return;
                            }
                            new ShowDialog().show(DownloadedFilesAdapter.this.mcontext, DownloadedFilesAdapter.this.mcontext.getString(R.string.itemremovedalready), DownloadedFilesAdapter.this.mcontext.getString(R.string.itemdeletedexternal), com.google.firebase.messaging.Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                            database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                            MyViewHolder.this.removeAt(i);
                        }
                    });
                    this.itemShare.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (new File((String) hashMap.get("video_path")).exists()) {
                                Intent intent = new Intent("android.intent.action.SEND");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setType("image/*");
                                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                                intent.putExtra("android.intent.extra.SUBJECT", DownloadedFilesAdapter.this.mcontext.getText(R.string.shareimage));
                                DownloadedFilesAdapter.this.mcontext.startActivity(Intent.createChooser(intent, DownloadedFilesAdapter.this.mcontext.getText(R.string.shareimage)));
                                return;
                            }
                            new ShowDialog().show(DownloadedFilesAdapter.this.mcontext, DownloadedFilesAdapter.this.mcontext.getString(R.string.itemremovedalready), DownloadedFilesAdapter.this.mcontext.getString(R.string.itemdeletedexternal), com.google.firebase.messaging.Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                            database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                            MyViewHolder.this.removeAt(i);
                        }
                    });
                }
                if (hashMap.get(Constants.RESPONSE_TYPE).equals("video") || hashMap.get(Constants.RESPONSE_TYPE).equals("gif")) {
                    this.thumb.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (new File((String) hashMap.get("video_path")).exists()) {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(uriForFile, "video/*");
                                DownloadedFilesAdapter.this.mcontext.startActivity(intent);
                                return;
                            }
                            new ShowDialog().show(DownloadedFilesAdapter.this.mcontext, DownloadedFilesAdapter.this.mcontext.getString(R.string.itemremovedalready), DownloadedFilesAdapter.this.mcontext.getString(R.string.itemdeletedexternal), com.google.firebase.messaging.Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                            database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                            MyViewHolder.this.removeAt(i);
                        }
                    });
                    this.itemShare.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View view) {
                            if (new File((String) hashMap.get("video_path")).exists()) {
                                Intent intent = new Intent("android.intent.action.SEND");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setType("video/*");
                                intent.putExtra("android.intent.extra.STREAM", uriForFile);
                                intent.putExtra("android.intent.extra.SUBJECT", DownloadedFilesAdapter.this.mcontext.getText(R.string.sharevideo));
                                DownloadedFilesAdapter.this.mcontext.startActivity(Intent.createChooser(intent, DownloadedFilesAdapter.this.mcontext.getText(R.string.shareimage)));
                                return;
                            }
                            new ShowDialog().show(DownloadedFilesAdapter.this.mcontext, DownloadedFilesAdapter.this.mcontext.getString(R.string.itemremovedalready), DownloadedFilesAdapter.this.mcontext.getString(R.string.itemdeletedexternal), com.google.firebase.messaging.Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                            database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                            MyViewHolder.this.removeAt(i);
                        }
                    });
                }
                this.itemDelete.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        if (hashMap.get("video_path") != null) {
                            File file = new File((String) hashMap.get("video_path"));
                            if (file.exists()) {
                                file.delete();
                                database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                                hashMap.clear();
                                MyViewHolder.this.removeAt(i);
                            } else if (DownloadedFilesAdapter.this.mProductList != null && DownloadedFilesAdapter.this.mProductList.size() > 0) {
                                new ShowDialog().show(DownloadedFilesAdapter.this.mcontext, DownloadedFilesAdapter.this.mcontext.getString(R.string.itemremovedalready), DownloadedFilesAdapter.this.mcontext.getString(R.string.itemdeletedexternal), com.google.firebase.messaging.Constants.IPC_BUNDLE_KEY_SEND_ERROR);
                                database.videoDelete(Integer.parseInt((String) hashMap.get("id")));
                                MyViewHolder.this.removeAt(i);
                            }
                        } else {
                            MyViewHolder.this.removeAt(i);
                        }
                    }
                });
                this.thumb.setImageBitmap(database.getVideoBitmap(Integer.parseInt(hashMap.get("id"))));
                this.itemName.setText(hashMap.get("date"));
                return;
            }
            database.videoDelete(Integer.parseInt(hashMap.get("id")));
            removeAt(i);
        }

        public void removeAt(int i) {
            try {
                DownloadedFilesAdapter.this.mProductList.remove(i);
                DownloadedFilesAdapter.this.notifyItemRemoved(i);
                DownloadedFilesAdapter downloadedFilesAdapter = DownloadedFilesAdapter.this;
                downloadedFilesAdapter.notifyItemRangeChanged(i, downloadedFilesAdapter.mProductList.size());
            } catch (IndexOutOfBoundsException unused) {
            }
        }
    }
}
