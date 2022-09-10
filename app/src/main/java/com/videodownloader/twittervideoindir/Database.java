package com.videodownloader.twittervideoindir;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "videos_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "downloaded_videos";
    private static final String VIDEO_BITMAP = "thumb";
    private static final String VIDEO_DATE = "date";
    private static final String VIDEO_ID = "id";
    private static final String VIDEO_PATH = "video_path";
    private static final String VIDEO_TYPE = "type";
    private final Context context;

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public Database(Context context2) {
        super(context2, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        this.context = context2;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE downloaded_videos(id INTEGER PRIMARY KEY AUTOINCREMENT,video_path TEXT,type TEXT,thumb BLOB,date DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    public void videoDelete(int i) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(i)});
        writableDatabase.close();
    }

    public void videoAdd(String str, byte[] bArr, String str2) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VIDEO_PATH, str);
        contentValues.put("type", str2);
        contentValues.put(VIDEO_BITMAP, bArr);
        writableDatabase.insert(TABLE_NAME, null, contentValues);
        writableDatabase.close();
    }

    public Bitmap getVideoBitmap(int i) {
        Bitmap bitmap;
        new HashMap();
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloaded_videos WHERE id=" + i, null);
        if (rawQuery == null || !rawQuery.moveToFirst()) {
            bitmap = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.astronaut);
        } else {
            bitmap = BitmapFactory.decodeByteArray(rawQuery.getBlob(3), 0, rawQuery.getBlob(3).length);
        }
        rawQuery.close();
        readableDatabase.close();
        return bitmap;
    }

    public ArrayList<HashMap<String, String>> getAllVideos() throws UnsupportedEncodingException {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloaded_videos", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (rawQuery.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();
                for (int i = 0; i < rawQuery.getColumnCount(); i++) {
                    if (!rawQuery.getColumnName(i).equals(VIDEO_BITMAP)) {
                        hashMap.put(rawQuery.getColumnName(i), rawQuery.getString(i));
                    }
                }
                arrayList.add(hashMap);
            } while (rawQuery.moveToNext());
        }
        readableDatabase.close();
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> get3Videos() throws UnsupportedEncodingException {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT * FROM downloaded_videos ORDER BY id DESC LIMIT 3", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (rawQuery.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();
                for (int i = 0; i < rawQuery.getColumnCount(); i++) {
                    if (!rawQuery.getColumnName(i).equals(VIDEO_BITMAP)) {
                        hashMap.put(rawQuery.getColumnName(i), rawQuery.getString(i));
                    }
                }
                arrayList.add(hashMap);
            } while (rawQuery.moveToNext());
        }
        readableDatabase.close();
        return arrayList;
    }

    public int getVideoCount() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor rawQuery = readableDatabase.rawQuery("SELECT  * FROM downloaded_videos", null);
        int count = rawQuery.getCount();
        readableDatabase.close();
        rawQuery.close();
        return count;
    }
}
