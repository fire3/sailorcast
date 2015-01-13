package com.crixmod.sailorcast.database;

/**
 * Created by fire3 on 14-12-10.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryDbHelper extends SQLiteOpenHelper {
    // All Static variables
// Database Version
    //!!!!! 如果修改了数据库表格，必须修改 DATABASE_VERSION。
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "playHistoriesManager";
    // Contacts table name
    private static final String TABLE_PLAY_HISTORY = "playHistories";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_VIDEO_IN_ALBUM = "videoInAlbum";
    private static final String KEY_ALBUM_ID = "albumID";
    private static final String KEY_ALBUM_NAME = "albumName";
    private static final String KEY_VIDEO_ID = "videoID";
    private static final String KEY_VIDEO_NAME = "videoName";
    private static final String KEY_ALBUM_IMAGE_URL = "albumImageUrl";
    private static final String KEY_CATE_NAME = "cateName";
    private static final String KEY_VIDEO_SITE = "videoSite";
    private static final String KEY_CREATED_AT = "createdAt";

    private final ArrayList<PlayHistory> history_list = new ArrayList<PlayHistory>();
    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PLAY_HISTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VIDEO_IN_ALBUM + " INTEGER,"
                + KEY_ALBUM_ID + " INTEGER,"
                + KEY_ALBUM_NAME + " TEXT,"
                + KEY_VIDEO_ID + " INTEGER,"
                + KEY_VIDEO_NAME + " TEXT,"
                + KEY_ALBUM_IMAGE_URL + " TEXT,"
                + KEY_CATE_NAME + " TEXT,"
                + KEY_VIDEO_SITE + " INTEGER,"
                + KEY_CREATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAY_HISTORY);
// Create tables again
        onCreate(db);
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAY_HISTORY);
// Create tables again
        onCreate(db);
    }


   private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
// Adding new contact
    public void addPlayHistory(PlayHistory history) {

        PlayHistory old = getPlayHistoryByAlbumId(history.get_albumID());
        SQLiteDatabase db = this.getWritableDatabase();

        if(old == null) {
            ContentValues values = new ContentValues();
            values.put(KEY_VIDEO_IN_ALBUM, history.get_videoInAlbum());
            values.put(KEY_ALBUM_ID, history.get_albumID());
            values.put(KEY_ALBUM_NAME, history.get_albumName());
            values.put(KEY_VIDEO_ID, history.get_videoID());
            values.put(KEY_VIDEO_NAME, history.get_videoName());
            values.put(KEY_ALBUM_IMAGE_URL, history.get_albumImage());
            values.put(KEY_CATE_NAME, history.get_categoryName());
            values.put(KEY_VIDEO_SITE, history.get_videoSite());
            values.put(KEY_CREATED_AT, getDateTime());
// Inserting Row
            db.insert(TABLE_PLAY_HISTORY, null, values);
            db.close(); // Closing database connection
        } else
            updatePlayHistoryByID(history, old.get_id());
    }
    // Getting single contact
    PlayHistory getPlayHistoryById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAY_HISTORY, new String[] { KEY_ID,
                        KEY_VIDEO_IN_ALBUM, KEY_ALBUM_ID, KEY_ALBUM_NAME,
                        KEY_VIDEO_ID,KEY_VIDEO_NAME,KEY_ALBUM_IMAGE_URL,KEY_CATE_NAME,KEY_VIDEO_SITE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        PlayHistory contact = new PlayHistory(
                Integer.parseInt(cursor.getString(0)),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getInt(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getInt(8)
        );
// return contact
        cursor.close();
        db.close();
        return contact;
    }
        // Getting single contact
    PlayHistory getPlayHistoryByAlbumId(int albumId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAY_HISTORY, new String[] { KEY_ID,
                        KEY_VIDEO_IN_ALBUM, KEY_ALBUM_ID, KEY_ALBUM_NAME,
                        KEY_VIDEO_ID,KEY_VIDEO_NAME,KEY_ALBUM_IMAGE_URL,KEY_CATE_NAME, KEY_VIDEO_SITE}, KEY_ALBUM_ID + "=?",
                new String[] { String.valueOf(albumId) }, null, null, null, null);
        if (cursor != null) {
           if(cursor.moveToFirst() == true) {
               PlayHistory history = new PlayHistory(
                       Integer.parseInt(cursor.getString(0)),
                       cursor.getInt(1),
                       cursor.getInt(2),
                       cursor.getString(3),
                       cursor.getInt(4),
                       cursor.getString(5),
                       cursor.getString(6),
                       cursor.getString(7),
                       cursor.getInt(8)
               );
// return history
               cursor.close();
               db.close();
               return history;
           }
           else {
               cursor.close();
               db.close();
               return null;
           }
        }
        else {
            cursor.close();
            db.close();
            return null;
        }

    }


    // Getting All Contacts
    public ArrayList<PlayHistory> getPlayHistories() {
        try {
            history_list.clear();
// Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_PLAY_HISTORY + " ORDER BY datetime("+KEY_CREATED_AT+") DESC";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    PlayHistory playHistory = new PlayHistory();
                    playHistory.set_id(Integer.parseInt(cursor.getString(0)));
                    playHistory.set_videoInAlbum(cursor.getInt(1));
                    playHistory.set_albumID(cursor.getInt(2));
                    playHistory.set_albumName(cursor.getString(3));
                    playHistory.set_videoID(cursor.getInt(4));
                    playHistory.set_videoName(cursor.getString(5));
                    playHistory.set_albumImage(cursor.getString(6));
                    playHistory.set_categoryName(cursor.getString(7));
                    playHistory.set_videoSite(cursor.getInt(8));
// Adding playHistory to list
                    history_list.add(playHistory);
                } while (cursor.moveToNext());
            }
// return contact list
            cursor.close();
            db.close();
            return history_list;
        } catch (Exception e) {
// TODO: handle exception
            Log.e("all_contact", "" + e);
        }
        return history_list;
    }
     // Updating single contact
    public int updatePlayHistoryByID(PlayHistory playHistory, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_IN_ALBUM, playHistory.get_videoInAlbum());
        values.put(KEY_ALBUM_ID, playHistory.get_albumID());
        values.put(KEY_ALBUM_NAME, playHistory.get_albumName());
        values.put(KEY_VIDEO_ID, playHistory.get_videoID());
        values.put(KEY_VIDEO_NAME, playHistory.get_videoName());
        values.put(KEY_ALBUM_IMAGE_URL, playHistory.get_albumImage());
        values.put(KEY_CATE_NAME, playHistory.get_categoryName());
        values.put(KEY_VIDEO_SITE, playHistory.get_videoSite());
        values.put(KEY_CREATED_AT,getDateTime());
// updating row
        return db.update(TABLE_PLAY_HISTORY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id)});
    }

    // Updating single contact
    public int updatePlayHistory(PlayHistory playHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VIDEO_IN_ALBUM, playHistory.get_videoInAlbum());
        values.put(KEY_ALBUM_ID, playHistory.get_albumID());
        values.put(KEY_ALBUM_NAME, playHistory.get_albumName());
        values.put(KEY_VIDEO_ID, playHistory.get_videoID());
        values.put(KEY_VIDEO_NAME, playHistory.get_videoName());
        values.put(KEY_ALBUM_IMAGE_URL, playHistory.get_albumImage());
        values.put(KEY_CATE_NAME, playHistory.get_categoryName());
        values.put(KEY_VIDEO_SITE, playHistory.get_videoSite());
// updating row
        return db.update(TABLE_PLAY_HISTORY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(playHistory.get_id()) });
    }

    // Deleting single contact
    public void deletePlayHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAY_HISTORY, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    // Getting contacts Count
    public int getPlayHistoriesCount() {
        String countQuery = "SELECT * FROM " + TABLE_PLAY_HISTORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }
}
