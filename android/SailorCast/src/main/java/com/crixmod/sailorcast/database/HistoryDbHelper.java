package com.crixmod.sailorcast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crixmod.sailorcast.model.SCAlbum;
import com.crixmod.sailorcast.model.SCAlbums;
import com.crixmod.sailorcast.model.SCVideo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fire3 on 2015/1/14.
 */
public class HistoryDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "HistoryManager";
    private static final String TABLE_HISTORY = "histories";
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_ID = "albumID";
    private static final String KEY_ALBUM_JSON = "albumJson";
    private static final String KEY_ALBUM_SITE = "albumSite";
    private static final String KEY_VIDEO_JSON = "videoJson";
    private static final String KEY_VIDEO_NO_IN_ALBUM = "videoNo";
    private static final String KEY_VIDEO_PLAY_TIME = "videoTime";
    private static final String KEY_CREATED_AT = "createdAt";

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY" + ","
                + KEY_ALBUM_ID + " TEXT" + ","
                + KEY_ALBUM_SITE + " INTEGER" + ","
                + KEY_ALBUM_JSON + " TEXT" + ","
                + KEY_CREATED_AT + " TEXT" + ","
                + KEY_VIDEO_JSON + " TEXT" + ","
                + KEY_VIDEO_NO_IN_ALBUM + " INTEGER" + ","
                + KEY_VIDEO_PLAY_TIME + " INTEGER"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        // Create tables again
        onCreate(db);
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    public void addHistory(SCAlbum album, SCVideo scVideo, int playTime) {

        History old = getHistoryById(album.getAlbumId(), album.getSite().getSiteID());
        SQLiteDatabase db = this.getWritableDatabase();

        if (old == null) {
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_ID, album.getAlbumId());
            values.put(KEY_ALBUM_SITE,album.getSite().getSiteID());
            values.put(KEY_ALBUM_JSON,album.toJson());
            values.put(KEY_CREATED_AT, getDateTime());
            values.put(KEY_VIDEO_JSON,scVideo.toJson());
            values.put(KEY_VIDEO_NO_IN_ALBUM,scVideo.getSeqInAlbum());
            values.put(KEY_VIDEO_PLAY_TIME,playTime);
            // Inserting Row
            db.insert(TABLE_HISTORY, null, values);
            db.close(); // Closing database connection
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_ID, album.getAlbumId());
            values.put(KEY_ALBUM_SITE,album.getSite().getSiteID());
            values.put(KEY_ALBUM_JSON,album.toJson());
            values.put(KEY_CREATED_AT, getDateTime());
            values.put(KEY_VIDEO_JSON,scVideo.toJson());
            values.put(KEY_VIDEO_NO_IN_ALBUM,scVideo.getSeqInAlbum());
            values.put(KEY_VIDEO_PLAY_TIME,playTime);
            // Inserting Row
            db.update(TABLE_HISTORY, values,
                    KEY_ALBUM_ID + "=? AND " + KEY_ALBUM_SITE + "=?",
                    new String[]{
                            String.valueOf(album.getAlbumId()),
                            String.valueOf(album.getSite().getSiteID())
                    });
            db.close(); // Closing database connection
        }

    }

    public History getHistoryById(String albumId, int siteID) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, new String[] {
                        KEY_ALBUM_ID,
                        KEY_ALBUM_SITE,
                        KEY_ALBUM_JSON,
                        KEY_VIDEO_JSON,
                        KEY_VIDEO_NO_IN_ALBUM,
                        KEY_VIDEO_PLAY_TIME,
                        KEY_CREATED_AT
                        },
                        KEY_ALBUM_ID + "=? AND " + KEY_ALBUM_SITE + "=?",
                new String[] { String.valueOf(albumId), String.valueOf(siteID) }, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst() == true) {
                String json = cursor.getString(2);
                SCAlbum album = SCAlbum.fromJson(json);
                json = cursor.getString(3);
                SCVideo video = SCVideo.fromJson(json);
                int videoNo = cursor.getInt(4);
                int playTime  = cursor.getInt(5);
                String createTime = cursor.getString(6);
                History history = new History(album,video,videoNo,playTime,createTime);
                cursor.close();
                db.close();
                return history;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
        }
        return null;
    }

    public void deleteAlbum(String albumID, int siteID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORY, KEY_ALBUM_ID + " = ? AND " + KEY_ALBUM_SITE  + " = ?",
                new String[] { String.valueOf(albumID), String.valueOf(siteID) });
        db.close();
    }
    public int getTotalCount() {
        String countQuery = "SELECT * FROM " + TABLE_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    public ArrayList<History> getAllHistories() {
        ArrayList<History> histories = new ArrayList<>();
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " ORDER BY datetime("+KEY_CREATED_AT+") DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SCAlbum album = SCAlbum.fromJson(cursor.getString(3));
                    String createAt = cursor.getString(4);
                    SCVideo video = SCVideo.fromJson(cursor.getString(5));
                    int videoSeq = cursor.getInt(6);
                    int playTime = cursor.getInt(7);
                    History history = new History(album,video,videoSeq,playTime,createAt);
                    histories.add(history);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return histories;
        } catch (Exception e) {
            Log.e("allHistories", "" + e);
        }
        return null;
   }

     public ArrayList<History> getHistoriesByPage(int pageNo, int pageSize) {
        ArrayList<History> histories = new ArrayList<>();
        try {
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_HISTORY
                    + " Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(pageNo*pageSize)
                    + " ORDER BY datetime("+KEY_CREATED_AT+") DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    SCAlbum album = SCAlbum.fromJson(cursor.getString(3));
                    String createAt = cursor.getString(4);
                    SCVideo video = SCVideo.fromJson(cursor.getString(5));
                    int videoSeq = cursor.getInt(6);
                    int playTime = cursor.getInt(7);
                    History history = new History(album,video,videoSeq,playTime,createAt);
                    histories.add(history);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return histories;
        } catch (Exception e) {
            Log.e("allHistories", "" + e);
        }
        return null;
   }


}
